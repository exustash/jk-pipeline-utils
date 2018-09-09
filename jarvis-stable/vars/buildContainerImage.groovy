#!/usr/bin/env groovy

/**
 * Build container image on designated container host
 * @param  imageTag
 * @param  containerHost='slave'
 */
Void call(String imageTag, String containerHost='slave') {

    String namespace = 'quay.io/dailymotionadmin'

    def alfredConfig = getAlfredConfig()

    reportBuildStatus("release/pipeline/container-image.build", "Task container-image.build is runing", 'PENDING')
    try {
            useDockerHost("${containerHost}") {
                    for (image in alfredConfig.docker().getImages()) {
                    println "---> Building version ${imageTag} of docker image ${namespace}/${image.name}"
                    docker.build("${namespace}/${image.name}:${imageTag}", "${image.build_args}")
                }
            }
        reportBuildStatus("release/pipeline/container-image.build", "Task container-image.build is runing", 'SUCCESS')
    } catch (err) {
        reportBuildStatus("release/pipeline/container-image.build", "Task container-image.build is runing", 'FAILURE')
        error("[ERR!] Pipeline execution error: ${err.message}")
    }
}
