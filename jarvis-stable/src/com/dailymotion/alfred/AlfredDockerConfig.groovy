#!/usr/bin/groovy
package com.dailymotion.alfred

import groovy.transform.Field

/* Configuration retrieved from .alfred/docker.yaml
 *
 * File format:
 * -------------------------------------------
 * images:
 *   - westeros-spaces
 *   - westeros-spaces-two
 * repositories:
 *   quayio:
 *     owners:
 *       - tribe-scale
 *       - tribe-infrastructure
 *     readers:
 *       - tribe-user-product
 * -------------------------------------------
 *
 * Either a more advanced format for images
 * -------------------------------------------
 * images:
 *   - name: westeros-spaces
 *     build_args: spaces/
 * repositories:
 *   quayio:
 *     owners:
 *       - tribe-scale
 *       - tribe-infrastructure
 *     readers:
 *       - tribe-user-product
 * -------------------------------------------
 */

@Field
def config = null

def loadFromFile() {
    if (!fileExists('.alfred')) {
        throw new GroovyRuntimeException("'.alfred' directory doesn't exist.")
    }
    if (!fileExists(".alfred/docker.yaml")) {
        throw new GroovyRuntimeException("Please create a .alfred/docker.yaml file.")
    }
    config = readYaml file: '.alfred/docker.yaml'

    if (!config || !(config instanceof java.util.Map)) {
        throw new GroovyRuntimeException("'.alfred/docker.yaml' doesn't fit the format. Please provide keys at root level.")
    }

    validateConfiguration()
    config["images"] = config["images"].collect(this.&castImageToMap)
}

def getConfig() {
    return config
}

def validateConfiguration() {
    if (!(config instanceof java.util.Map) ||
        !config.containsKey("images") || !config.containsKey("repositories")) {
        throw new GroovyRuntimeException("'images' & 'repositories' keys are mandatory")
    }
    def images = config["images"]
    if (!(images instanceof java.util.List)) {
        throw new GroovyRuntimeException("'images' must be a list")
    }
    if (images.size() < 1) {
        throw new GroovyRuntimeException("'images' can't be empty")
    }

    for (image in images) {
        if (!(image instanceof java.util.Map) && !(image instanceof String)) {
            throw new GroovyRuntimeException("'image' MUST BE a String or Map")
        }

        if (image instanceof java.util.Map && !image.containsKey("name")) {
            throw new GroovyRuntimeException("'image' MUST CONTAINS at least the name property")
        }
    }

    def repositories = config["repositories"]
    if (!(repositories instanceof java.util.Map) || !repositories.containsKey("quayio") ||
        !(repositories["quayio"] instanceof java.util.Map)) {
        throw new GroovyRuntimeException("'repositories' is mandatory and must be a map and must contains 'quayio' key.")
    }
    def quayio = repositories["quayio"]
    if (!quayio.containsKey("owners") || !(quayio["owners"] instanceof java.util.List)) {
        throw new GroovyRuntimeException("'repositories.quayio.owners' is mandatory and must be a list")
    }

    if (quayio.containsKey("readers") && !(quayio["readers"] instanceof java.util.List) && quayio["readers"].size() < 1) {
        throw new GroovyRuntimeException("'repositories.quayio.readers' must be a list and can't be empty")
    }
    def owners = quayio["owners"]
    if (owners.size() < 1) {
        throw new GroovyRuntimeException("'repositories.quayio.owners' can't be empty")
    }
}

/**
 * Transform string to map or return map with that details
 * [
 *   name: westeros-spaces,
 *   build_args: spaces/
 * ]
 *
 * @param img should be a String of a Map
 * @return Map
 */
def castImageToMap(img) {
    if (img instanceof String) {
        return [
            name: img,
            build_args: null
        ]
    }
    return [build_args: null] + img
}

def getQuayioPermissionsByImages() {
    def result = []
    config["images"].each { img ->
        def quayio = config["repositories"]["quayio"]
        quayio["owners"].each { owner ->
            result += [
                image_name: img["name"],
                team: owner,
                role: "owner"
            ]
        }
        if (quayio.containsKey('readers')) {
            quayio["readers"].each { reader ->
                result += [
                    image_name: img["name"],
                    team: reader,
                    role: "reader"
                ]
            }
        }
    }

    return result
}

def getQuayioPermissionsByImage(String imageName) {
    def imagePermissions = []
    def quayio = config["repositories"]["quayio"]
    quayio["owners"].each { owner ->
        imagePermissions += [
            team: owner,
            role: "owner"
        ]
    }
    if (quayio.containsKey('readers')) {
        quayio["readers"].each { reader ->
            imagePermissions += [
                team: reader,
                role: "reader"
            ]
        }
    }

    return imagePermissions
}

def getQuayioOwners() {
    return config["repositories"]["quayio"]["owners"]
}

def getQuayioOwner() {
    return config["repositories"]["quayio"]["owners"][0]
}

def getImageName() {
    return config["images"][0]["name"]
}

def getImage() {
    return config["images"][0]
}

def getImages() {
    return config["images"]
}

return this
