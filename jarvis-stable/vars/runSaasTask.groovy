#!/usr/bin/env groovy
/**
* Run a release pipeline task with status report
*/
Void call(String task, String releaseName, String service, String prNumber, String version) {
    //useDockerHost() {
        reportBuildStatus("saas/task/${task}", "SaaS Pipeline Task ${task} is runing", 'PENDING')
        try {
            sh returnStdout: false, script: "docker run -i --rm dailymotion/scale-deployment staging -t ${task} -r ${releaseName} -pr ${prNumber} -s ${service} -v ${version}"
            reportBuildStatus("saas/task/${task}", "SaaS Pipeline Task ${taskCmd}  has succeeded", 'SUCCESS')
        } catch (err) {
            reportBuildStatus("saas/task/${task}", "SaaS Pipeline Task ${taskCmd}  has failed", 'FAILURE')
            error("[ERR!] Pipeline execution error: ${err.message}")
        }
    //}
}

Void call(String releaseName, String service, String prNumber, String version) {
    call('create', releaseName, service, prNumber, version)

}
