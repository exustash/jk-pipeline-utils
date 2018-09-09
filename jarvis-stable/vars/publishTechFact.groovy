#!/usr/bin/env groovy

/**
* Publishes a technical facts to the Events Tool for every deployment to prod
* @param releaseTag
* @param commitMsg
* @param serviceName
*/
Void call(String releaseTag, String commitMsg, String serviceName) {
    try {
        println '---> Publishing Events to Technical Facts'
        String buildUser = wrap([$class: 'BuildUser']) {
          return env.BUILD_USER
        }

        buildUser = buildUser ?: 'Release Maester'

        sh returnStdout: true, script: """
          DATE=`echo \$(date '+%Y-%m-%dT%H:%M:%S%z')`
          curl -i -X --insecure POST --data-urlencode 'data={"title": "[${serviceName}] ['Deploy'] ['Prod']: ${releaseTag} - ${commitMsg} - Build #${env.BUILD_NUMBER}","type":"release","datetime": "'\$DATE'","author": "${buildUser}","children": [{"title": "Link to build", "description": "${commitMsg}"}]}' https://events.dailymotion.com/api/document
        """
    } catch (err) {
        error("[ERR!] Pipeline execution error: ${err.message}")
    }
}
