def call(Closure body) {
  String dockerConfigFilepath = "${env.HOME}/.docker/config.json"
  println "dockerConfigFilepath is $dockerConfigFilepath"
  withSecretFileFromVault(
          "secret/jenkins/docker/clientAuth",
          "secret",
          dockerConfigFilepath
   ) {
        body()
    }
}
