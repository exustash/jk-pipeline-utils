#!/usr/bin/env groovy

Void call(String imageName, String containerHost='worker') {
    String streamedImageName = streamImageName(imageName)
    String taggedImageName = tagImageName(imageName)

    withDockerHost(containerHost) {
        println "---> pulling ${taggedImageName} from DockerHub"
        pullFromDockerHub(taggedImageName)

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
      path: "secret/jenkins/dockerhub",
      secretValues: [
        [$class: "VaultSecretValue", envVar: "HUB_USER", vaultKey: "username"],
        [$class: "VaultSecretValue", envVar: "HUB_PASSWD", vaultKey: "token"]
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
Void pullFromDockerHub(String imageName) {
  wrap([$class: "VaultBuildWrapper", vaultSecrets: getDockerHubCredentialsFromVault()]) {
    sh 'docker login -u=\"${DOCKERHUB_USER}\" -p=\"${DOCKERHUB_PASSWD}\"'
    sh "docker pull  checkmate/${imageName}"
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

private String tagImageName(String imageName) {
    if (!imageName.contains(':')) {
        taggedImageName = "${imageName}:latest"
        return taggedImageName
    }

    return imageName
}

private String streamImageName(String imageName) {
    if (imageName.contains(':')) {
        streamedImageName = imageName.split(':')[0]
        return streamedImageName
    }

    return imageName
}
