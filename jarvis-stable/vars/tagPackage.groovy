#!/usr/bin/env groovy

/**
 * [call description]
 * @param  version [description]
 * @return         [description]
 */
Void call(String version) {
    reportBuildStatus("release/pipeline/package/tag", "Tagging ...", "PENDING")
    try {
        withEnv(["PKG_VERSION=${version}"]) {
          sh "make tag"
        }
        reportBuildStatus("release/pipeline/package/tag", "Tagged", "SUCCESS")
    } catch (err) {
        reportBuildStatus("release/pipeline/package/tag", "Fail to Tag.", "FAILURE")
        throw new GroovyRuntimeException("Unable to tag package")
    }
}
