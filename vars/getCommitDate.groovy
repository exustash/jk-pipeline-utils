#!/usr/bin/env groovy

/**
 * Get the long format commit hash of the current commit
 */
String getCommitDate() {
  return sh(returnStdout: true, script: "git log -n 1 --date=format:'%y%m%d' --pretty=format:'%cd'").trim()
}
