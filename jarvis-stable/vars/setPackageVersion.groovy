#!/usr/bin/env groovy

/**
 * [call description]
 * @param  version [description]
 */
Void call(String version) {
    reportBuildStatus("ci/package/setversion", "Set version to ${version}...", "PENDING")
    try {
        withEnv(["PKG_VERSION=${version}"]) {
            sh "make set-version"
        }
        reportBuildStatus("ci/package/setversion", "Set version to ${version}", "SUCCESS")
    } catch (err) {
        reportBuildStatus("ci/package/setversion", "Fail to set version to ${version}.", "FAILURE")
        throw new GroovyRuntimeException("Unable to set the version")
    }
}
