#!/usr/bin/groovy
package com.dailymotion.alfred

/**
 * [pipeline description]
 * @return [description]
 */
AlfredPipelineConfig pipeline() {
    AlfredPipelineConfig config = new AlfredPipelineConfig()
    config.loadFromFile()
    return config
}

AlfredDockerConfig docker() {
    AlfredDockerConfig config = new AlfredDockerConfig()
    config.loadFromFile()
    return config
}

return this
