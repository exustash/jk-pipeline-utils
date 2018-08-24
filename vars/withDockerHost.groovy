#!/usr/bin/env groovy

/**
 * [call description]
 * @param  contextName [description]
 * @param  body        [description]
 * @return             [description]
 */
Void call(String contextName, Closure body) {
    Map dockerHosts = [
        slave: "tcp://releaseslave-01.ear.thxcheckmate.com:4243",
        master: "tcp://inspect-02.ear.thxcheckmate.com:4243"
    ]

    if (contextName != "slave" && contextName != "master") {
        throw new GroovyRuntimeException("${contextName} not supported, choose either 'master' or 'slave'")
    }

    println "Use ${contextName} host"

    docker.withServer(dockerHosts[contextName]) {
        body()
    }
}

/**
 * [call description]
 * @param  body [description]
 * @return      [description]
 */
Void call(Closure body) {
    call("slave", body)
}
