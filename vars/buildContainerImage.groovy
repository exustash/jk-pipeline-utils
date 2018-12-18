#!/usr/bin/env groovy

/**
 * Build container image
 * @param  imageName
 * @param  imageTag
 * @param  dockerFilePath
 */
Void call(String imageName, String imageTag,  dockerFilePath='.') {

    //@TODO implement kaniko centric container image build.

    reportTaskStatus("pipeline/task/image.build", "Task image.build is runing", 'PENDING')
    try {
        withDockerHost("hostName") {
                println "---> Building version ${imageTag} of container image ${repository}/${taggedImageName}"
                sh "docker build -t ${repository}/${imageName}:${imageTag} ${dockerFilePath}"
        }
        reportTaskStatus("pipeline/task/image.build", "Task image.build is runing", 'SUCCESS')
    } catch (err) {
        reportTaskStatus("pipeline/task/image.build", "Task image.build is runing", 'FAILURE')
        error("[Error!] Pipeline step BuildContainerImage execution error: ${err.message}")
    }
}
