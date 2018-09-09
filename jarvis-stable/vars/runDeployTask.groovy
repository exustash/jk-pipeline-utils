#!/usr/bin/env groovy
/**
* Run a deploy task with status report to the correponding PR.
* @param taskCmd
* @param taskExecutor
* @param deployEnv
*/
Void call(String taskCmd, String taskExecutor, String ref, String deployEnv = 'staging') {
    reportDeployStatus('pending', "Deployment to ${deployEnv} is running.", ref, environment)
    try {
        sh returnStdout: false, script: "${taskExecutor} ${taskCmd}"
        reportDeployStatus('success', "Deployment to ${deployEnv} finished successfully.", ref, environment)

    } catch (err) {
        reportDeployStatus('failure', "Deployment to ${deployEnv} finished unsuccessfully.", ref, environment)
        error("[ERR!] Pipeline execution error: ${err.message}")
    }
}
