#!/usr/bin/env groovy

/**
 * Build container images of docker-compose services
 * @param  serviceName
 * @return
 */
 Void call(String stackName) {
     try {
         println "---> Building Docker images for services in ${stackName}"
         sh(returnStdout: false, script: "docker-compose -p ${stackName} -f docker-compose.yml build")
          } catch (err) {
        error("[ERR!] Pipeline execution error: ${err.message}")
     }
 }
