#!/usr/bin/env groovy

import com.dailymotion.alfred.AlfredConfig


Void call(String imageName, String containerHost='slave') {
    String owner = getRepositoryOwner()
    String streamedImageName = streamImageName(imageName)
    String taggedImageName = tagImageName(imageName)

    useDockerHost(containerHost) {
        Boolean isRepoOnQuay = isRepoOnQuay(streamedImageName)

        if (isRepoOnQuay) {
            println "---> pulling ${taggedImageName} from Quay"
            pullFromQuay(taggedImageName, owner)

        } else {
            println "---> pulling ${taggedImageName} from DockerHub"
            pullFromDockerHub(taggedImageName)
        }
    }
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
 * Push a container image to Quay.io registry
 * @param  sourceImage
 * @param  targetImage
 * @param  robotName
 */
Void pullFromQuay(String imageName, String robotName) {
    wrap([$class: "VaultBuildWrapper", vaultSecrets: vaultCredentials(robotName)]) {
        sh 'docker login -u=\"${QUAY_USER}\" -p=\"${QUAY_PASSWD}\" quay.io'
        sh "docker pull quay.io/dailymotionadmin/${imageName}"
    }
}

/**
 * Push a container image to Dockerhub registry
 * @param  sourceImage [description]
 * @param  targetImage   [description]
 * @return               [description]
 */
Void pullFromDockerHub(String imageName) {
  wrap([$class: "VaultBuildWrapper", vaultSecrets: getDockerHubCredentialsFromVault()]) {
    sh 'docker login -u=\"${DOCKERHUB_USER}\" -p=\"${DOCKERHUB_PASSWD}\"'
    docker.image("dailymotion/${imageName}").pull()
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

private Boolean isRepoOnQuay(String repository) {
    String result= ""
    wrap([$class: "VaultBuildWrapper", vaultSecrets: getQuayApplicationTokenFromVault()]) {
      result = sh (returnStdout: true, script: "/usr/sbin/quaycli -f -n dailymotionadmin -t $QUAY_APPLICATION_TOKEN repo check ${repository}")
    }
    return result.toBoolean()
}


private String tagImageName(String imageName) {
    if(!imageName.contains(':')) {
        taggedImageName = "${imageName}:latest"
        return taggedImageName
    }

    return imageName
}

private String streamImageName(String imageName) {
    if(imageName.contains(':')) {
        streamedImageName = imageName.split(':')[0]
        return streamedImageName
    }

    return imageName
}

String getRepositoryOwner() {
    AlfredConfig alfredConfig = getAlfredConfig()
    String owner = alfredConfig.docker().getQuayioOwner()

    return owner
}
