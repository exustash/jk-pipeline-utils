#!/usr/bin/env groovy

/**
 * Deploy a docker stack
 * @param stackName: the name of the stack to  deploy through docker-compose
 * @param filePath: the name of the file that defines the docker stack to deploy
 */
 Void call(String rmiType='local') {
    try {
        println "---> Stoping local container stack ####"
        sh returnStdout: false, script: "docker-compose down --volumes --rmi=${rmiType}"
    } catch (err) {
        error("[ERR!] Job execution error: ${err.message}")
    }
}
