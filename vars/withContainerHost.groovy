#!/usr/bin/env groovy

/**
 * [call description]
 * @param  contextName [description]
 * @param  body        [description]
 * @return             [description]
 */
Void call(String contextName, Closure body) {
    //change tcp adresses to the one of your liking
    Map dockerHosts = [
        executor: "tcp://executor.checkmate.io:4243",
        orchestrator: "tcp://orchestrator.checkmate.io:4243"
    ]

    if (contextName != "worker" && contextName != "orchestrator") {
        throw new GroovyRuntimeException("${contextName} not supported, choose either 'orchestrator' or 'executor'")
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
    call("executor", body)
}
