#!/usr/bin/env groovy

/**
 * Delete docker image on current host
 * @param imageName
 * @param imageTag
 */
Void call(String imageName, String imageTag='latest') {
    try {
        println "---> removing image ${imageName}/${imageTag}"
        sh returnStdout: false, script: "docker rmi dailymotion/${imageName}:${imageTag}"
        sh returnStdout: false, script: "docker rmi dailymotion/${imageName}:latest"
    } catch (err) {
        error("[ERR!] Pipeline execution error: ${err.message}")
    }
 }
