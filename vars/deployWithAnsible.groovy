#!/usr/bin/env groovy

/**
 * deploy using ansible playbook
 * @param deployEnv: the name of the stack to  deploy through docker-compose
 * @param serviceName:  the name of the service that will deployed to specified env
 */
Void call(String deployEnv, String serviceName, String playbookPath) {
    reportPipelineStatus("release/pipeline/service.deploy", "Pipeline Task service.deploy is runing", 'PENDING')
    try {
        println "---> Deploying ${serviceName} to ${deployEnv} #### "
        sh returnStdout: false, script: "ansible-playbook deployment.yml -e deploy_env=${env} -e service_name=${serviceName}"
        reportPipelineStatus("release/pipeline/service.deploy", "Pipeline Task service.build has succeeded", 'SUCCESS')

    } catch (err) {
       reportPipelineStatus("release/pipeline/service.deploy", "Pipeline Task service.deploy has failed", 'FAILURE')
       error("[ERR!] Pipeline execution error: ${err.message}")
    }
}

Void call(String deployEnv, String serviceName) {
  sh returnStdout: false, script: "ansible-playbook deployment.yml -e deploy_env=${env} -e service_name=${serviceName}"
}
