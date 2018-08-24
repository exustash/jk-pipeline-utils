#!/usr/bin/env groovy

/**
 * Build container images of docker-compose services
 * @param  serviceName
 * @return
 */
 Void call(String stackName) {
     reportBuildStatus("release/pipeline/service.build", "Pipeline Task service.build is runing", 'PENDING')
     try {
         println "---> Building ${stackName} Docker image ${stackName}"
         sh(returnStdout: false, script: "docker-compose -p ${stackName} -f docker-compose.yml build --no-cache")
         reportBuildStatus("release/pipeline/service.build", "Pipeline Task service.build has succeeded", 'SUCCESS')
     } catch (err) {
        reportBuildStatus("release/pipeline/service.build", "Pipeline Task service.build has failed", 'FAILURE')
        error("[ERR!] Pipeline execution error: ${err.message}")
     }
 }
