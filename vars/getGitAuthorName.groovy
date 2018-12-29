#!/usr/bin/env groovy

/**
 * Get the name of author of the current commit
 */
String getAuthorName() {
  return sh(returnStdout: true, script: "git log -1 --pretty=format:'%an'").trim()
}
