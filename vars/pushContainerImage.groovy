#!/usr/bin/env groovy

import com.checkmate.alfred.AlfredConfig

/**
 * Push an image to relevant registry
 * @param  dockerHost
 * @param  sourceImage
 * @param  targetImage
 * @param  config Branch to Image mapping
 */
Void call(String imageTag, String containerHost='slave') {
    String owner = getRepositoryOwner()
    List images = getContainerImages()
    List imagesMigratedToQuay = getImagesMigratedToQuay()

    reportBuildStatus("release/pipeline/container-image.push", "Task container-image.push is running", 'PENDING')
    try {
        useDockerHost(containerHost) {
            for (image in images) {
                Boolean isImageMigratedToQuay = isImageMigratedToQuay(image.name, imagesMigratedToQuay)
                Boolean isRepoOnQuay = isRepoOnQuay(image.name)

                if (isImageMigratedToQuay) {
                    println "---> Push to DockerHub disabled for image ${image.name}"
                    runPushToQuayWithPerms(image.name, imageTag, owner, isRepoOnQuay)

                } else {
                    println "---> Pushing image ${image.name} to DockerHub"
                    pushToDockerHub("${image.name}:${imageTag}")
                    runPushToQuayWithPerms(image.name, imageTag, owner, isRepoOnQuay)
                }
            }
        }
        reportBuildStatus("release/pipeline/container-image.push", "Task container-image.push has succeded", 'SUCCESS')
    } catch (err) {
        reportBuildStatus("release/pipeline/container-image.push", "Task container-image.push has failed", 'FAILURE')
        error("[ERR!] Pipeline step pushContainerImage execution error: ${err.message}")
    }
}

String getRepositoryOwner() {
    AlfredConfig alfredConfig = getAlfredConfig()
    String owner = alfredConfig.docker().getQuayioOwner()

    return owner
}

List getContainerImages() {
    AlfredConfig alfredConfig = getAlfredConfig()
    List images = alfredConfig.docker().getImages()

    return images
}

List getImagesMigratedToQuay() {
     List imagesMigratedOnQuay = ["ldap2github", "stingray-consul", "daily-ubuntu", "nettools",
        "lanterns-base", "lanterns-gradle", "lanterns-golang", "lanterns-python", "lanterns-java",
        "lanterns-plumber", "lanterns-westeros", "lanterns-nodejs", "lanterns-sdk", "gotham-jenkins",
        "gotham-sonar", "gotham-postgres"
     ]

     return imagesMigratedOnQuay
}

/**
 * Retrieve secrets from Vault
 * @param  robotName [description]
 * @return           [description]
 */
Map vaultCredentials(String robotName) {
  List secrets = [
    [
      $class: "VaultSecret",
      path: "secret/jenkins/quay.io/robots/${robotName}",
      secretValues: [
        [$class: "VaultSecretValue", envVar: "QUAY_USER", vaultKey: "username"],
        [$class: "VaultSecretValue", envVar: "QUAY_PASSWD", vaultKey: "token"]
      ]
    ]
  ]
  return secrets
}

/**
 * Push a container image to Dockerhub registry
 * @param  sourceImage [description]
 * @param  targetImage   [description]
 * @return               [description]
 */
Void pushToDockerHub(String imageName) {
  wrap([$class: "VaultBuildWrapper", vaultSecrets: getDockerHubCredentialsFromVault()]) {
    echo "Pushing ${imageName} to DockerHub"
    sh 'docker login -u=\"${DOCKERHUB_USER}\" -p=\"${DOCKERHUB_PASSWD}\"'
    streamedImageName = streamImageName(imageName)
    sh "docker push checkmate/${streamedImageName}"

  }
}

def getDockerHubCredentialsFromVault() {
  def secrets = [
    [
      $class: "VaultSecret",
      path: "secret/jenkins/docker.io/dockerHub",
      secretValues: [
        [$class: "VaultSecretValue", envVar: "DOCKERHUB_USER", vaultKey: "username"],
        [$class: "VaultSecretValue", envVar: "DOCKERHUB_PASSWD", vaultKey: "password"]
      ]
    ]
  ]
  return secrets
}

/**
 * Push a container image to Quay.io registry
 * @param  sourceImage
 * @param  targetImage
 * @param  robotName
 */
Void pushToQuay(String imageName, String robotName) {
    wrap([$class: "VaultBuildWrapper", vaultSecrets: vaultCredentials(robotName)]) {
        println "Pushing ${imageName} to Quay.io"
        sh 'docker login -u=\"${QUAY_USER}\" -p=\"${QUAY_PASSWD}\" quay.io'
        streamedImageName = streamImageName(imageName)
        sh "docker push quay.io/checkmateadmin/${streamedImageName}"
    }
}

private def getQuayApplicationTokenFromVault() {
  def secret = [
    [
      $class: "VaultSecret",
      path: "secret/jenkins/quay.io/application/teamRights",
      secretValues: [
            [$class: "VaultSecretValue", envVar: "QUAY_APPLICATION_TOKEN", vaultKey: "token"]
      ]
    ]
  ]
  return secret
}

private Boolean isRepoOnQuay(String repository) {
    String result= ""
    wrap([$class: "VaultBuildWrapper", vaultSecrets: getQuayApplicationTokenFromVault()]) {
      result = sh (returnStdout: true, script: "/usr/sbin/quaycli -f -n checkmateadmin -t $QUAY_APPLICATION_TOKEN repo check ${repository}")
    }
    return result.toBoolean()
}

private Boolean isImageMigratedToQuay(String imageName, List imagesMigratedToQuay) {
    return imagesMigratedToQuay.contains(imageName)
}

Void createAndPushToQuay(String sourceImage, String version,  String owner) {
    pushToQuay("${sourceImage}:${version}", owner)
    applyAlfredPermsOnQuay(sourceImage)
}

private String streamImageName(String imageName) {
    if (imageName.contains(':')) {
        streamedImageName = imageName.split(':')[0]
        return streamedImageName
    }

    return imageName
}

private Void runPushToQuayWithPerms(String imageName, String imageTag, String owner, Boolean isRepoOnQuay) {
    if (isRepoOnQuay) {
        applyAlfredPermsOnQuay(imageName)
        pushToQuay("${imageName}:${imageTag}", owner)
    } else {
        pushToQuay("${imageName}:${imageTag}", owner)
        applyAlfredPermsOnQuay(imageName)
    }
}
