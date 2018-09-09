#!/usr/bin/env groovy

/**
 * [call description]
 */
Void call() {
    reportBuildStatus("ci/pypi/building", "Start Building ...", "PENDING")
    try {
        useDockerHost("master") {
            sh "make build"
        }
        reportBuildStatus("ci/pypi/building", "Built", "SUCCESS")
    } catch (err) {
        reportBuildStatus("ci/pypi/building", "Fail to build", "FAILURE")
        throw new GroovyRuntimeException("Fail to build the Python package")
    }
}
