#!/usr/bin/env groovy

/**
 * Get the git message of the current commit
 */
String getCommitMsg() {
  return sh(returnStdout: true, script: "giat log -n 1 --pretty=format:'%s'").trim()
}
