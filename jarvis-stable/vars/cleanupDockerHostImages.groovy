#!/usr/bin/env groovy

/**
 * Cleanup all docker images on current host
 */
Void call() {
    def excludeImages = (String[])["'jenkins'", "'sonar'", "'postgres'"]
    def grepCommand = " -e " + excludeImages.join(' -e ')

    try {
        println("---> Cleanup Docker images")
        sh returnStdout: false, script: 'docker images --quiet --filter=dangling=true | xargs --no-run-if-empty docker rmi -f  || true'
        sh returnStdout: false, script: "docker images | grep 'hours ago'  | grep -v $grepCommand | awk '{print \$3}' | xargs docker rmi -f || true"
        sh returnStdout: false, script: "docker images | grep 'days ago'   | grep -v $grepCommand | awk '{print \$3}' | xargs docker rmi -f || true"
        sh returnStdout: false, script: "docker images | grep 'weeks ago'  | grep -v $grepCommand | awk '{print \$3}' | xargs docker rmi -f || true"
        sh returnStdout: false, script: "docker images | grep 'months ago' | grep -v $grepCommand | awk '{print \$3}' | xargs docker rmi -f || true"
    } catch (err) {
        error("[ERR!] Job execution error: ${err.message}")
    }
}
