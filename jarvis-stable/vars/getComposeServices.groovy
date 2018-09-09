#!/usr/bin/env groovy

/**
 * [call description]
 * @param  composePath='.' [description]
 * @return                 [description]
 */
Arrays call(String composePath='.') {
    try {
        println "---> Retrieving services list from ${composePath}/docker-compose.yml"
        String fullComposePath = composePath + "/docker-compose.yml"
        servicesList = sh(returnStdout: true,  script: "docker-compose -f ${fullComposePath} config --services").split("\r?\n")
        println "---> ${servicesList}"
        return servicesList
    } catch (err) {
        error("---> Pipeline execution error: ${err.message}")
    }
    return sh(returnStdout: true,  script: """docker-compose -f ${composePath}/docker-compose.yml config --services""").split("\r?\n")
}
