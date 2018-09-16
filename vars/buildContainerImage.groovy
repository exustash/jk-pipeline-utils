#!/usr/bin/env groovy

/**
 * Build container image on designated container host
 * @param  imageTag
 * @param  containerHost='slave'
 */
Void call(String namespace, String imageName, String imageTag) {

    reportBuildStatus("release/pipeline/container-image.build", "Task container-image.build is runing", 'PENDING')
    try {
            withDockerHost("${containerHost}") {
                    println "---> Building version ${imageTag} of containr image ${repository}/${image.name}"
                    sh "docker build -t ${repository}/${image.name}:${imageTag} ${image.path}"
                }
            }
        reportBuildStatus("release/pipeline/container-image.build", "Task container-image.build is runing", 'SUCCESS')
    } catch (err) {
        reportBuildStatus("release/pipeline/container-image.build", "Task container-image.build is runing", 'FAILURE')
        error("[ERR!] Pipeline step BuildContainerImage execution error: ${err.message}")
    }
}


