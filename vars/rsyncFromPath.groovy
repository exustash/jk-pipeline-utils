#!/usr/bin/env groovy

/**
 * download static assets from filer
 * @param localPath
 * @param remotePath
 */
Void call(String remoteHost, String localPath, String remotePath) {
    try {
        println "---> Downloading from ${remoteHost}::${remotePath} to ${localPath} ####"
        sh returnStdout: false, script: "mkdir -p /tmp/${localPath}"
        sh returnStdout: false, script: "rm -rf /tmp/${localPath}/*"
        sh returnStdout: false, script: "rsync --exclude .git -a ${remoteHost}::${remotePath}/ /tmp/${localPath}/"
    } catch (err) {
        error("[ERR!] Pipeline execution error: ${err.message}")
    }
}
