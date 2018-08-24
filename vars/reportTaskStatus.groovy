#!/usr/bin/env groovy

/**
 * Report a build status to github
 *
 * @param  context the context of the report
 * @param  message the message explaining the status
 * @param  state   the status reported
 */
Void call (String context, String message, String state) {
    if (env.JOB_BASE_NAME.startsWith("PR-")) {
        step([
            $class: "GitHubCommitStatusSetter",
            reposSource: [$class: "ManuallyEnteredRepositorySource", url: git.getRepositoryURL()],
            contextSource: [$class: "ManuallyEnteredCommitContextSource", context: context],
            errorHandlers: [[$class: "ChangingBuildStatusErrorHandler", result: "UNSTABLE"]],
            statusBackrefSource: [$class: "ManuallyEnteredBackrefSource", backref: "${env.RUN_DISPLAY_URL}"],
            statusResultSource: [$class: "ConditionalStatusResultSource", results: [[$class: "AnyBuildResult", message: message, state: state]] ]
        ]);
        return
    }
    step([
        $class: "GitHubCommitStatusSetter",
        reposSource: [$class: "ManuallyEnteredRepositorySource", url: git.getRepositoryURL()],
        contextSource: [$class: "ManuallyEnteredCommitContextSource", context: context],
        commitShaSource: [$class: "ManuallyEnteredShaSource", sha: git.getCommitLongHash()],
        errorHandlers: [[$class: "ChangingBuildStatusErrorHandler", result: "UNSTABLE"]],
        statusBackrefSource: [$class: "ManuallyEnteredBackrefSource", backref: "${env.RUN_DISPLAY_URL}"],
        statusResultSource: [$class: "ConditionalStatusResultSource", results: [[$class: "AnyBuildResult", message: message, state: state]] ]
    ]);
}
