#!/usr/bin/env groovy

/**
 * [call description]
 * @param  packageName [description]
 * @param  version     [description]
 * @param  indexName   [description]
 * @return             [description]
 */
Boolean call(String packageName, String version, String indexName) {
    Map pypiConfig = pypiConfig()

    String pypiIndex = pypiConfig["sandbox_index"]
    if ("prod" == indexName) {
        pypiIndex = pypiConfig["prod_index"]
    }

    Integer exists = "1"
    try {
        reportBuildStatus("ci/pypi/checkversion", "Checking version on ${pypiIndex}...", "PENDING")
        useDockerHost("master") {
            useDockerRegistry {
                docker.image("dailymotion/devpi-server:latest").pull()
                withCredentials([usernamePassword(credentialsId: "pypicredprod",
                                                usernameVariable: "DEVPI_USER",
                                                passwordVariable: "DEVPI_PASS")]) {
                    withEnv(["DEVPI_PKG_NAME=${packageName}",
                             "DEVPI_INDEX=${pypiIndex}",
                             "DEVPI_PKG_VERSION=${version}"]) {
                        exists = sh(script: "make exists |tail -n 1", returnStdout: true).trim()
                    }
                }
            }
        }
    } catch (err) {
        reportBuildStatus("ci/pypi/checkversion", "Version ${version} already exists on ${pypiIndex}", "FAILURE")
        throw new GroovyRuntimeException("Error encountered to check the version (Pypi)")
    }
    reportBuildStatus("ci/pypi/checkversion", "Version ${version} is okay on ${pypiIndex}", "SUCCESS")
    return (exists == "0") ? false : true
}

/**
 * [call description]
 * @param  packageName [description]
 * @param  version     [description]
 * @return             [description]
 */
Boolean call(String packageName, String version) {
    return call(packageName, version, "prod")
}
