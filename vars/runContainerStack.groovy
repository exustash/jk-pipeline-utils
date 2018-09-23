#!/usr/bin/env groovy

/**
 * [call description]
 * @param  serviceName       [description]
 * @return                   [description]
 */
 Void call(String stackName, override='') {
     try {
         println "---> Running ${stackName}"
         sh(returnStdout: false, script: "docker-compose -f docker-compose.yml ${override} run")
     } catch (err) {
        error("[ERR!] Pipeline execution error: ${err.message}")
     }
 }
