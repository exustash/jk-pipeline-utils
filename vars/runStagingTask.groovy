#!/usr/bin/env groovy
/**
* Run a release pipeline task with status report
*/
Void call(String task, String service, String prNumber, String version) {
        reportTaskStatus("stage/task/${task}", "SaaS Pipeline Task ${task} is runing", 'PENDING')
        try {
            reportTaskStatus("stage/task/${task}", "SaaS Pipeline Task ${taskCmd}  has succeeded", 'SUCCESS')
        } catch (err) {
            reportTaskStatus("stage/task/${task}", "SaaS Pipeline Task ${taskCmd}  has failed", 'FAILURE')
            error("[ERR!] Pipeline execution error: ${err.message}")
        }
}

