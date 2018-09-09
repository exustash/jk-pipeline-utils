#!/usr/bin/env groovy
/**
* Run a release pipeline task with status report
*/
Void call(String taskRunner, String taskCmd) {
//useDockerHost() {
    reportBuildStatus("release/pipeline/${taskCmd}", "Pipeline Task ${taskCmd} is runing", 'PENDING')
    println "--> Pipeline Task ${taskRunner} ${taskCmd} is runing"
    try {
        sh returnStdout: false, script: "${taskRunner} ${taskCmd}"
        reportBuildStatus("release/pipeline/${taskCmd}", "Pipeline Task ${taskCmd}  has succeeded", 'SUCCESS')
    } catch (err) {
        reportBuildStatus("release/pipeline/${taskCmd}", "Pipeline Task ${taskCmd}  has failed", 'FAILURE')
        error("[ERR!] Pipeline Task ${taskRunner} ${taskCmd} execution error: ${err.message}")
    }
//}
}
