package com.dailymotion.alfred

import groovy.transform.Field

/* Configuration retrieved from .alfred/pipeline.yaml
 *
 * File format:
 * -------------------------------------------
 * release_name: westeros
 * service_name: spaces
 * channel_slack: #westeros [optional]
 * deployment: [optional]
 *   branches_envs_mapping:
 *     master: staging
 *     prod: prod
 * -------------------------------------------
 */

@Field
Map config = null

Void loadFromFile() {
    if (!fileExists('.alfred')) {
        throw new GroovyRuntimeException("'.alfred' directory doesn't exist.")
    }
    if (!fileExists(".alfred/pipeline.yaml")) {
        throw new GroovyRuntimeException("Create a .alfred/pipeline.yaml file.")
    }
    config = readYaml file: '.alfred/pipeline.yaml'

    if (!config || !(config instanceof java.util.Map)) {
        throw new GroovyRuntimeException("Wrong format for '.alfred/pipeline.yaml'. Provide keys at root level.")
    }

    validateConfiguration()
}

Map getConfig() {
    return config
}

Void validateConfiguration() {
    if (config.containsKey("release_name")
        && !(config["release_name"] instanceof String)) {
        throw new GroovyRuntimeException("'release_name' must be a String")
    }

    if (config.containsKey("service_name")
        && !(config["service_name"] instanceof String)) {
        throw new GroovyRuntimeException("'service_name' must be a String")
    }

    if (config.containsKey("channel_slack")
        && !(config["channel_slack"] instanceof String)) {
        throw new GroovyRuntimeException("'channel_slack' must be a String")
    }

    if (config.containsKey("deployment")) {
        if (!(config["deployment"] instanceof java.util.Map)) {
            throw new GroovyRuntimeException("'deployment' must be a map")
        }

        Map deployment = config["deployment"]
        if (deployment.containsKey("branches_envs_mapping")
            && !(deployment["branches_envs_mapping"] instanceof java.util.Map)) {
            throw new GroovyRuntimeException("'deployment.branches_envs_mapping' must be a map")
        }
    }
}

String getServiceName() {
    return config.get("service_name")
}

String getReleaseName() {
   return config.get("release_name")
}

String getChannelSlack() {
    return config.get("channel_slack")
}

String getDeployment() {
    return config.get("deployment")
}

/**
 * [getDeploymentBranchesEnvsMapping description]
 * @return [description]
 */
Map getDeploymentBranchesEnvsMapping() {
    Map deployment = getDeployment()

    if (deployment instanceof java.util.Map && deployment.containsKey("branches_envs_mapping")) {
        return deployment["branches_envs_mapping"]
    }

    return null
}

return this
