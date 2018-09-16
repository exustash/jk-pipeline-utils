#!/usr/bin/env groovy

/**
* Run a release pipeline task with or without status report
*/
Void call(String taskRunner, String taskCmd, Boolean report=false) {
    String task = "${taskRunner} ${taskCmd}"
    switch (report) {
        case false:
            runTaskWithoutReport(task)
        break
        case true:
            runTaskWithReport(task, taskCmd)
        break
    }
}

Void runTaskWithReport(String task, String taskCmd) {
    reportBuildStatus("release/pipeline/${taskCmd}", "Pipeline Task ${taskCmd} is runing", 'PENDING')
    println "--> Pipeline Task ${task} is runing"
    try {
        sh returnStdout: false, script: "${task}"
        reportBuildStatus("release/pipeline/${taskCmd}", "Pipeline Task ${taskCmd}  has succeeded", 'SUCCESS')
    } catch (err) {
        reportBuildStatus("release/pipeline/${taskCmd}", "Pipeline Task ${taskCmd}  has failed", 'FAILURE')
        error("[ERR!] Pipeline Task ${task} execution error: ${err.message}")
    }
}

Void runTaskWithoutReport(String task) {
    try {
        println "--> Pipeline Task ${task} is runing"
        sh returnStdout: false, script: "${task}"
    } catch (err) {
        error("[ERR!] Pipeline execution error: ${err.message}")
    }
}
