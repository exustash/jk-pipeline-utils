#!/usr/bin/env groovy

/**
 * download static assets from filer
 * @param localPath
 * @param remotePath
 */
Void call(String localPath, String remotePath) {
    try {
        println "---> Download from ${remotePath} to ${localPath} ####"
        sh returnStdout: false, script: "mkdir -p /tmp/${localPath}"
        sh returnStdout: false, script: "rm -rf /tmp/${localPath}/*"
        sh returnStdout: false, script: "rsync --exclude .git -a install.dm.gg::${remotePath}/ /tmp/${localPath}/"
    } catch (err) {
        error("[ERR!] Pipeline execution error: ${err.message}")
    }
}
