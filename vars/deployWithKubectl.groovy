#!/usr/bin/env groovy

/**
 * For a given release name, service name and version. We deploy the
 * related application on our Kubernetes clusters.
 *
 * Default behavior -> stepsScaleDeployment() used .alfred/pipeline.yaml
 * to setup default parameters.
 *
 * @param releaseName Release name e.g. westeros
 * @param serviceName Service name e.g. gbased
 * @param version e.g. 12ef34
 * @param config Configuration mapping to enable/disable some features
 */
def call(String releaseName, String serviceName, String version, Map configMap) {
    config = configMap ?: [:]

    try {
        reportBuildStatus('release/pipeline/deploy', 'Start deploying to kubernetes', 'PENDING')
        def guessEnvFromBranchname = config.get('guess_env_from_branchname', true)
        def branchesEnvsMapping = config.get('branches_envs_mapping', [
            master: "staging",
            prod: "prod"
        ])
        def availableEnvs = ["prod", "preprod", "staging", "playground"]
        def slackChannel = config.get('slack_channel')

        def currentEnv

        if (!guessEnvFromBranchname && !config.containsKey('env')) {
            throw new GroovyRuntimeException("'env' key have to be specified if 'guess_env_from_branchname' is equals to false.")
        }

        if (config.containsKey('env')) {
            echo "Guess env from config"
            if (!availableEnvs.contains(config.get('env'))) {
                throw new GroovyRuntimeException("We don't support ${config.get('env')} environment.")
            }
            currentEnv = config.get('env')
        } else {
            echo "Guess env from branch name '${env.BRANCH_NAME}'"
            if (!branchesEnvsMapping.containsKey(env.BRANCH_NAME)) {
                throw new GroovyRuntimeException("There is no mapping for ${env.BRANCH_NAME}")
            }
            currentEnv = branchesEnvsMapping.get(env.BRANCH_NAME)
        }

        echo "Start deployment to ${currentEnv}"

        def skipProdValidation = config.get("skip_prod_validation", false)
        if (currentEnv == "prod" && !skipProdValidation) {
            if (slackChannel) {
                slackSend(
                    color: "warning",
                    channel: slackChannel,
                    message: "*${serviceName}* _(version ${version})_ is waiting an action from you to proceed with the deployment (*${currentEnv}* environment)."
                )
            }

            input "Ready to go?"
        }

        useDockerHost("slave") {
            useDockerRegistry {
                deployImageName = 'checkmate/scale-deployment:latest'
                docker.image("${deployImageName}").pull()
                def contexts = Eval.me(sh(script: "docker run -i --rm ${deployImageName} contexts -e ${currentEnv} -r ${releaseName} -s ${serviceName}", returnStdout: true).trim())
                def targets = [:]
                def pwd = sh(script: 'pwd', returnStdout: true).trim()
                String kubeConfigFilepath = "${pwd}/.kubecfg-${env.BUILD_TAG}"
                String serviceAccountFilePath = "${pwd}/sa_deploy_gke.json.${env.BUILD_TAG}"
                String kubecfgPath = '/scale/config/.kubecfg'
                String deployGkePath = '/.sa_deploy_gke.json'

                wrap([$class: "VaultBuildWrapper", vaultSecrets: secretsCredentials()]) {
                    withSecretFileFromVault(
                        "secret/jenkins/scale/clusters-credentials",
                        "kubecfg",
                        kubeConfigFilepath
                    ) {
                        withSecretFileFromVault(
                            "secret/prod/gcp/dm-scale/serviceaccounts/deploy-gke",
                            "json",
                            serviceAccountFilePath
                        ) {
                            for (context in contexts) {
                                def contextName = context
                                targets["${contextName}"] = {
                                  sh "docker run -i --rm -v ${kubeConfigFilepath}:${kubecfgPath} -v ${serviceAccountFilePath}:${deployGkePath} ${deployImageName} deploy -e ${currentEnv} -r ${releaseName} -c ${contextName} -s ${serviceName} -t ${version}"
                                }
                            }
                            parallel targets
                        }
                    }

                }

            }
        }
        reportBuildStatus('release/kubernetes/deploy', 'Deployed to kubernetes', 'SUCCESS')
    } catch (err) {
        reportBuildStatus('release/kubernetes/deploy', 'Fail to deploy on kubernetes', 'FAILURE')
        throw err
    }
}

def call(String releaseName, String serviceName, String version) {
    call(releaseName, serviceName, version, [:])
}

def call(Map configmap) {
    def alfred = getAlfredConfig()
    def pipeline = alfred.pipeline()

    config = configMap ?: [:]

    if (!config.containsKey("slack_channel") && pipeline.getChannelSlack()) {
        config << [slack_channel: pipeline.getChannelSlack()]
    }

    if (!config.containsKey("branches_envs_mapping") && pipeline.getDeploymentBranchesEnvsMapping()) {
        config << [branches_envs_mapping: pipeline.getDeploymentBranchesEnvsMapping()]
    }

    call(
        pipeline.getReleaseName(),
        pipeline.getServiceName(),
        getGitCommitHash(),
        config
    )
}

def call() {
    call([:])
}

def secretsCredentials() {
  def secrets = [
    [
      $class: "VaultSecret",
      path: "secret/prod/jenkins/scale-deployment-approle",
      secretValues: [
        [$class: "VaultSecretValue", envVar: "SECRETVAULT_ROLEID", vaultKey: "role_id"],
        [$class: "VaultSecretValue", envVar: "SECRETVAULT_SECRETID", vaultKey: "secret_id"]
      ]
    ]
  ]
  return secrets
}
