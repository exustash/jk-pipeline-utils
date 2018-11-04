#!/usr/bin/env groovy

/**
 * Create a git hub release
 * @param  repositoryName        the name of the targeted repositiry
 * @param  releaseTag            the tag associated with the release
 * @param  commitMsg             the commit message used for change logs
 * @param  artifactType='tar.gz' the extention of the artifact file
 */

Void call(String repositoryName, String packageName, String releaseTag, String commitMsg, String artifactType='tar.gz') {
    reportTaskStatus("delivery/task/release.push", "Pipeline Task release.push is runing", 'PENDING')
    try {
        cleanupGHRelease(repositoryName, releaseTag)
        createGHRelease(repositoryName, releaseTag, commitMsg)
        uploadGHRelease(repositoryName, packageName, releaseTag, artifactType)
        reportTaskStatus("delivery/task/release.push", "Pipeline Task release.push  has succeeded", 'SUCCESS')

    } catch (err) {
      reportTaskStatus("delivery/task/release.push", "Pipeline Task release.push  has failed", 'FAILURE')
      error("[ERR!] Pipeline execution error: ${err.message}")
    }
}

Void cleanupGHRelease(String repositoryName, String releaseTag) {
    sh returnStdout: false, script: "github-release delete -u checkmate -r ${repositoryName} -t ${releaseTag} || true"
}

Void createGHRelease(String repositoryName, String releaseTag, String commitMsg) {
    sh returnStdout: false, script: """github-release release -u checkmate -r ${repositoryName} -t ${releaseTag} -n ${releaseTag} -d "${commitMsg}" """
}

Void uploadGHRelease(String repositoryName, String packageName, String releaseTag, String artifactType) {
    sh returnStdout: false, script: """github-release upload -u checkmate -r ${repositoryName} -t ${releaseTag} -n "${packageName}.${artifactType}" -f ${packageName}.${artifactType}"""
}
