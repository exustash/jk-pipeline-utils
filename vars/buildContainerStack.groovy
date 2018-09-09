#!/usr/bin/env groovy

/**
 * Build container images of docker-compose services
 * @param  serviceName
 * @return
 */
 Void call(String stackName) {
     postBuildStatus("release/pipeline/container-stack.build", "Pipeline Task container-stack.build is runing", 'PENDING')
     try {
         println "---> Building Docker images for services in ${stackName}"
         sh(returnStdout: false, script: "docker-compose -p ${stackName} -f docker-compose.yml build")
         postBuildStatus("release/pipeline/container-stack.build", "Pipeline Task container-stack.build has succeeded", 'SUCCESS')
     } catch (err) {
        postBuildStatus("release/pipeline/container-stack.build", "Pipeline Task container-stack.build has failed", 'FAILURE')
        error("[ERR!] Pipeline execution error: ${err.message}")
     }
 }
