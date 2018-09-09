#!/usr/bin/env groovy

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
    String upperCaseBuildName = getJobName()
    String capitalizedBuildStatus = buildStatus.toLowerCase().capitalize()
    String extBuildStatus = "*" + upperCaseBuildName + ", Build #${env.BUILD_NUMBER}:* <${env.RUN_DISPLAY_URL}|${capitalizedBuildStatus}>"
    String notifContext = "*Context:* " + context
    String authorName = getJobAuthorName()

    return "${extBuildStatus}\n${authorName}\n${notifContext}"
}

String getJobName() {
    return env.JOB_BASE_NAME.toUpperCase()
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

        return "*Started by:* " + 'Release-Master'
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
