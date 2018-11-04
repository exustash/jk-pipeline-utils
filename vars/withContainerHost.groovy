#!/usr/bin/env groovy

/**
 * [call description]
 * @param  hostName [description]
 * @param  body        [description]
 */
Void call(String hostName, Closure body) {
    //change tcp adresses to the one of your liking
    Map containerHosts = [
        worker_one: "tcp://worker_one.container.host.io:4243",
        primary_two: "tcp://worker_two.container.host.io:4243"
    ]

    if (hostName != "worker_one" && hostName != "worker_two") {
        throw new GroovyRuntimeException("${hostName} not supported, choose either primary or worker")
    }

    println "Use ${hostName} host"

    docker.withServer(dockerHosts[hostName]) {
        body()
    }
}
