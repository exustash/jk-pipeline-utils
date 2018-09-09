#!/usr/bin/env groovy
import groovy.json.JsonOutput
import groovy.json.JsonSlurper

/**
* Report a deployment status to git server
*/
Void call(String state, String description, String gitRef, String deployEnv) {
    try {
        String deploymentId = deployExists(ref, 'deploy', 'staging') ?: this.createDeployment(ref, description, environment)

        withCredentials([string(credentialsId: '2671f6e0-d7ee-4746-8711-ef9b2ed57dae', variable: 'GITHUB_TOKEN')]) {
            String payload = JsonOutput.toJson(["state": "${state}", "target_url": "", "description": "${description}"])
            String apiUrl = "https://api.github.com/repos/dailymotion/${env.REPO_SLUG}/deployments/${deploymentId}/statuses"
            sh returnStdout: false, script: """curl -s -H \"Authorization: Token ${env.GITHUB_TOKEN}\" -H \"Accept: application/json\" -H \"Content-type: application/json\" -X POST -d '${payload}' ${apiUrl}"""
        }
    } catch (err) {
        error("[ERR!] Pipeline execution error: ${err.message}")

        sh returnStdout: false, script: """curl -s -H \"Authorization: Token ${env.GITHUB_TOKEN}\" -H \"Accept: application/json\" -H \"Content-type: application/json\" -X POST -d '${payload}' ${apiUrl}"""

    }
}

String createDeployment(String ref, String description, String environment) {
    withCredentials([string(credentialsId: '2671f6e0-d7ee-4746-8711-ef9b2ed57dae', variable: 'GITHUB_TOKEN')]) {

        String payload = JsonOutput.toJson(["ref": "${gitRef}", "task": "deploy", "description": "${description}", "environment": "${deployEv}", "required_contexts": []])
        String apiUrl = "https://api.github.com/repos/dailymotion/${env.REPO_SLUG}/deployments"
        String response = sh(returnStdout: true, script: """curl -s -H \"Authorization: Token ${GITHUB_TOKEN}\" -H \"Accept: application/json\" -H \"Content-type: application/json\" -X POST -d '${payload}' ${apiUrl}""").trim()
        JsonSlurper jsonSlurper = new JsonSlurper()
        String deploymentData = jsonSlurper.parseText("${response}")
        return deploymentData.id
    }
}

String deployExists(String ref, String task, String environment) {
    withCredentials([string(credentialsId: '2671f6e0-d7ee-4746-8711-ef9b2ed57dae', variable: 'GITHUB_TOKEN')]) {
        String apiUrl = "https://api.github.com/repos/dailymotion/${env.REPO_SLUG}/deployments?ref=${ref}"
        String response = sh(returnStdout: true, script: """curl -s -H \"Authorization: Token ${env.GITHUB_TOKEN}\" -H \"Content-type: application/json\" -X GET ${apiUrl}""").trim()

        JsonSlurper jsonSlurper = new JsonSlurper()
        String deploymentData = jsonSlurper.parseText("${response}")
        String deploymentData.id = (deploymentData) ? deploymentData.id : false

        return deploymentData.id
    }
}
