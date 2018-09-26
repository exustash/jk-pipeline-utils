#!/usr/bin/env groovy

/**
 * [call description]
 * @param  hostName [description]
 * @param  body        [description]
 */
Void call(String hostName, Closure body) {
    //change tcp adresses to the one of your liking
    Map containerHosts = [
        worker: "tcp://worker.container.host.io:4243",
        primary: "tcp://primary.container.host.io:4243"
    ]

    if (hostName != "worker" && hostName != "primaryd") {
        throw new GroovyRuntimeException("${hostName} not supported, choose either primary or worker")
    }

    println "Use ${hostName} host"

    docker.withServer(dockerHosts[hostName]) {
        body()
    }
}

/**
 * [call description]
 * @param  body [description]
 * @return      [description]
 */
Void call(Closure body) {
    call("executor", body)
}
