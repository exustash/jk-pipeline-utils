#!/usr/bin/env groovy

import com.dailymotion.alfred.AlfredConfig

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

    useDockerHost(containerHost) {
        for (image in images) {
            Boolean isImageMigratedToQuay = isImageMigratedToQuay(image.name, imagesMigratedToQuay)

            if (isImageMigratedToQuay) {
                println "---> Push to DockerHub disabled for image ${image.name}"
                applyAlfredPermsOnQuay(image.name)
                pushToQuay("${image.name}:${imageTag}", owner)
            } else {
                println "---> Pushing image ${image.name} to DockerHub"
                pushToDockerHub("${image.name}:${imageTag}")

                Boolean isRepoOnQuay = isRepoOnQuay(image.name)

                if (isRepoOnQuay) {
                    applyAlfredPermsOnQuay(image.name)
                    pushToQuay("${image.name}:${imageTag}", owner)
                } else {
                    createAndPushToQuay(image.name, imageTag, owner)
                }
            }
        }
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
     List imagesMigratedOnQuay = ["ldap2github", "stingray-consul", "daily-ubuntu", "nettools"]

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
    sh "docker tag quay.io/dailymotionadmin/${imageName} dailymotion/${imageName}"
    addDefaultTag(imageName, "dailymotion")
    docker.image("dailymotion/${imageName}").push()
    streamedImageName = streamImageName(imageName)
    docker.image("dailymotion/${streamedImageName}:${getImageDefaultTag()}").push()
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
        addDefaultTag(imageName, "quay.io/dailymotionadmin")
        sh "docker push quay.io/dailymotionadmin/${imageName}"
        streamedImageName = streamImageName(imageName)
        sh "docker push quay.io/dailymotionadmin/${streamedImageName}:${getImageDefaultTag()}"
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
      result = sh (returnStdout: true, script: "/usr/sbin/quaycli -f -n dailymotionadmin -t $QUAY_APPLICATION_TOKEN repo check ${repository}")
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
    if(imageName.contains(':')) {
        streamedImageName = imageName.split(':')[0]
        return streamedImageName
    }

    return imageName
}

private Void addDefaultTag(String imageName, String repository) {
    streamedImageName = streamImageName(imageName)
    defaultTag = getImageDefaultTag()
    sh "docker tag ${repository}/${imageName} ${repository}/${streamedImageName}:${defaultTag}"
}
