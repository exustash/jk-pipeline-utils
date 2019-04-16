#!/usr/bin/env groovy

/**
 * Rsync download files from remote location
 * @param localPath
 * @param remotePath
 */
def call(String localPath, String remotePath, String remoteHost) {
  sh returnStdout: false, script: "mkdir -p /tmp/${localPath}"
  sh returnStdout: false, script: "rm -rf /tmp/${localPath}/*"
  sh returnStdout: false, script: "rsync --exclude .git -a ${remoteHost}::${remotePath}/ /tmp/${localPath}/"
}
