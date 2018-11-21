#!/usr/bin/env groovy

/**
 * Get the current branch name
 */
String call() {
  String currentBranch = env.BRANCH_NAME
  if (!currentBranch) {
    currentRaw = sh(script: 'git name-rev --name-only HEAD', returnStdout: true).trim()
    currentBranch = currentRaw.split('remotes/origin/')[1];
  }
  return currentBranch
}
