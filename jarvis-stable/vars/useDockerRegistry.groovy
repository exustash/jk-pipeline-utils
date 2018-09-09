#!/usr/bin/env groovy

/**
 * [call description]
 * @param  body [description]
 * @return      [description]
 */
Void call(Closure body) {
    Map creds = dockerCredentials()
    docker.withRegistry(creds["registryUrl"], creds["jenkinsCred"]) {
        body()
    }
}

/**
 * [dockerCredentials description]
 * @return [description]
 */
Map dockerCredentials() {
  withCredentials([usernamePassword(
      credentialsId: 'dockerHub',
      usernameVariable: 'USERNAME',
      passwordVariable: 'PASSWORD')
  ]) {
    return [
      jenkinsCred: "dockerHub",
      registryUrl: "https://index.docker.io/v1/"
    ]
  }
}
