#!/usr/bin/env groovy

/**
 * [call description]
 * @param  serviceName       [description]
 * @return                   [description]
 */
 Void call(String stackName, override='') {
     reportBuildStatus("release/pipeline/stack.build", "Pipeline Task stack.build is runing", 'PENDING')
     try {
         println "---> Building ${serviceName} Docker image ${serviceName}"
         sh(returnStdout: false, script: "docker-compose -f docker-compose.yml ${override} run")
         reportBuildStatus("release/pipeline/stack.build", "Pipeline Task stack.build has succeeded", 'SUCCESS')
     } catch (err) {
        reportBuildStatus("release/pipeline/stack.build", "Pipeline Task stack.build has failed", 'FAILURE')
        error("[ERR!] Pipeline execution error: ${err.message}")
     }
 }
