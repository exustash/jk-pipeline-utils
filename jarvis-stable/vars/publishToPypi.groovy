#!/usr/bin/env groovy

/**
 * [call description]
 * @param  packageName [description]
 * @param  indexName   [description]
 */
Void call(String packageName, String indexName) {
    if (!["prod", "sandbox"].contains(indexName)) {
        throw new GroovyRuntimeException("That ${indexName} is not supported, only 'prod' and 'sandbox'")
    }

    Map pypiConfig = pypiConfig()

    String pypiIndex = pypiConfig["sandbox_index"]
    if ("prod" == indexName) {
        pypiIndex = pypiConfig["prod_index"]
    }

    reportBuildStatus("ci/pypi/publish", "Start publishing to ${indexName} ...", "PENDING")
    try {
        useDockerHost("master") {
            useDockerRegistry {
                docker.image("dailymotion/devpi-server:latest").pull()
                withCredentials([usernamePassword(
                    credentialsId: "pypicredprod",
                    usernameVariable: "DEVPI_USER",
                    passwordVariable: "DEVPI_PASS")
                ]) {
                    withEnv(["DEVPI_PKG_NAME=${packageName}", "DEVPI_INDEX=${pypiIndex}"]) {
                        sh "make publish"
                    }
                }
            }
        }
        reportBuildStatus("ci/pypi/publish", "Published to ${indexName}", "SUCCESS")
    } catch (err) {
        reportBuildStatus("ci/pypi/publish", "Fail to publish to ${indexName}", "FAILURE")
        throw new GroovyRuntimeException("Unable to push to Pypi repository")
    }
}

/**
 * [call description]
 * @param  packageName [description]
 */
Void call(String packageName) {
    call(packageName, "prod")
}
