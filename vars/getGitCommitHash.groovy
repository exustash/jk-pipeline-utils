#!/usr/bin/env groovy

/**
 * Get the short format commit hash of the current commit
 */
String call() {
  return sh(returnStdout: true, script: "git log -n 1 --pretty=format:'%h'").trim()
}
