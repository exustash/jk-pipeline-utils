#!/usr/bin/env groovy

/**
 * Get the email of the author of the current commit
 */
String getAuthorEmail() {
  return sh(returnStdout: true, script: "git log -n 1 --pretty=format:'%aE'").trim()
}
