#!/usr/bin/env groovy

/**
 * Build container image on designated container host
 * @param  imageTag
 * @param  containerHost='slave'
 */
Void call() {
    def alfredConfig = getAlfredConfig()
    reportBuildStatus("release/pipeline/container-image.lint", "Task container-image.lint is running", 'PENDING')
    try {
            for (image in alfredConfig.docker().getImages()) {
                println "---> Linting container image dailymotionadmin/${image.name}"
                sh returnStdout: false, script: "dockerfilelint ${image.path}/Dockerfile"
            }
        reportBuildStatus("release/pipeline/container-image.lint", "Task container-image.lint has succeded", 'SUCCESS')
    } catch (err) {
        reportBuildStatus("release/pipeline/container-image.lint", "Task container-image.lint has failed", 'FAILURE')
        error("[ERR!] Pipeline execution error: ${err.message}")
    }
}
