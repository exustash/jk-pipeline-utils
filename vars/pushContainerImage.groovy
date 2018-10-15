#!/usr/bin/env groovy

/**
 * Push an image to relevant registry
 * @param  dockerHost
 * @param  sourceImage
 * @param  targetImage
 * @param  config Branch to Image mapping
 */
Void call(String imageTag, String containerHost='worker') {

    reportPipelineStatus("release/pipeline/container-image.push", "Task container-image.push is running", 'PENDING')
    try {
        withDockerHost(containerHost) {

        }
        reportPipelineStatus("release/pipeline/container-image.push", "Task container-image.push has succeded", 'SUCCESS')
    } catch (err) {
        reportPipelineStatus("release/pipeline/container-image.push", "Task container-image.push has failed", 'FAILURE')
        error("[Err!] Pipeline step pushContainerImage execution error: ${err.message}")
    }
}
