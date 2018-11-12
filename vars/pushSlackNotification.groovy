#!/usr/bin/env groovy
import org.mikanolab.reports.Testing

/**
 * Send notifications based on build status string
 *
 * @parm buildStatus
 */
Void call(String buildStatus, String context='none') {
    try {
        String colorCode = defineColorCode(buildStatus)
        String notification = buildNotification(buildStatus, context)
        String channel = defineSlackChannel()

        slackSend(color: colorCode, message: notification, channel: channel)
    } catch (err) {
        error("[ERR!] Pipeline execution error: ${err.message}")
    }
}

/**
 * Build a notification string given the build status
 *
 * @param  buildStatus the status of the current build
 * @return notification to send as a string
 */
String buildNotification(String buildStatus, String context) {
    String upperCaseBuildName = getShortBuildName().toUpperCase()
    String capitalizedBuildStatus = buildStatus.toLowerCase().capitalize()
    String extBuildStatus = "*" + upperCaseBuildName + ", Build #${env.BUILD_NUMBER}:* <${env.RUN_DISPLAY_URL}|${capitalizedBuildStatus}>"
    String notifContext = "*Context:* " + context
    String authorName = getJobAuthorName()
    String branchName = getRefsName()
    String buildChanges = "*Changes:* " + git.getCommitMsg()
    String testSummary = "*Test Results:* " + getTestSummary()

    if (isStageDeploy()) {
        extBuildStatus = "*" + upperCaseBuildName + ", ${env.STAGE_NAME} #${env.BUILD_NUMBER}:* <${env.RUN_DISPLAY_URL}|${capitalizedBuildStatus}>"
        String version = "*Version:* " + git.getCommitHash()
        String environment = "*Environment:* " + getDeployEnv()
        return "${extBuildStatus}\n${version}\n${environment}\n${authorName}\n${buildChanges}"

    } else if (isStageRelease()) {
        extBuildStatus = "*" + upperCaseBuildName + ", ${env.STAGE_NAME} #${env.BUILD_NUMBER}:* <${env.RUN_DISPLAY_URL}|${capitalizedBuildStatus}>"
        String version = "*Version:* " + git.getCommitHash()
        return "${extBuildStatus}\n${version}\n${authorName}\n${buildChanges}"
    }

    return "${extBuildStatus}\n${notifContext}\n${authorName}\n${branchName}\n${buildChanges}\n${testSummary}"
}

/**
 * Defines the slack channels to send notif to
 *
 * @return the name of channel as string
 */
String defineSlackChannel() {
    String channel = "#release-platform"

    if (env.SLACK_CHANNEL) {
        channel = "#${env.SLACK_CHANNEL}"
    }

    return channel
}

/**
 * Gets the name of the user who triggered the job
 *
 * @return a string w/ the name of author
 */
String getJobAuthorName() {
    wrap([$class: 'BuildUser']) {
        if (env.BUILD_USER) {
            return "*Started by:* " + env.BUILD_USER
        }
        return "*Changes by:* " + git.getAuthorName()
    }
}

/**
 * Defines the color code of the notification given build status
 *
 * @param  buildStatus the status of current build
 * @return return a color code string
 */
String defineColorCode(String buildStatus) {
    switch (buildStatus.toUpperCase()) {
        case 'SUCCESS':
            colorCode = '#268D11'
        break
        case 'FAILURE':
            colorCode = '#C31818'
        break
        case ['PENDING', 'IN PROGRESS']:
            colorCode = '#D47804'
        break
        default:
            colorCode = '#B09C9C'
        break
    }

    return colorCode
}

/**
 * Gets The Id of the PR or Branch
 *
 * @return a string w/ a Branch or PR name
 */
String getRefsName() {
    if (env.BRANCH_NAME ==~ /^PR-\d+$/) {
        prNumber = git.getPullRequestNumber()
        return "*Pull Request:* <${env.CHANGE_URL}|${prNumber}>"
    }

    return "*Branch:* ${env.GIT_BRANCH}"
}

/**
 * Get the test results summary
 *
 * @return a test summary string
 */
String getTestSummary() {
    Testing testing = new Testing()
    return testing.getSummary()
}

/**
 * Checks if the stage is a deploy stage
 *
 * @return a boolean
 */

String isStageDeploy() {
    String stageName = env.STAGE_NAME.toLowerCase()
    return (stageName ==~ /^(deploy|deployment)[\s\S]*/) ? true : false
}

/**
 *
 * @return [description]
 */
String isStageRelease() {
    String stageName = env.STAGE_NAME.toLowerCase()
    return (stageName ==~ /^(package|release)[\s\S]*/) ? true : false
}

/**
 *
 * @return [description]
 */
String getDeployEnv() {
    String environment
    String repo = 'origin'

    if (params.DEPLOY_TO) {
        environment = params.DEPLOY_TO
        return environment
    }

    switch (env.GIT_BRANCH) {
        case 'prod':
            environment = "production"
        break
        case 'preprod':
            environment = "pre-production"
        case 'primary':
            environment = "stage"
        break
        case 'stage':
            environment = "stage"
        break
        default:
            environment = 'N/A'
        break
    }

    return environment
}
