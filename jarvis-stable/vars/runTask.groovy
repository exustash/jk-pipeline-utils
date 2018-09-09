#!/usr/bin/env groovy
/**
* Run a release pipeline task with status report
*/
Void call(String taskRunner, String taskCmd) {
    try {
        println "--> Pipeline Task ${taskRunner} ${taskCmd} is runing"
        sh returnStdout: false, script: "${taskRunner} ${taskCmd}"
    } catch (err) {
        error("[ERR!] Pipeline execution error: ${err.message}")
    }
}
