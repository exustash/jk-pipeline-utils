#!/usr/bin/env groovy


/**
 * Get the email of the author of the current commit
 */
String getAuthorEmail() {
  return sh(returnStdout: true, script: "git log -n 1 --pretty=format:'%aE'").trim()
}

/**
 * Get the name of author of the current commit
 */
String getAuthorName() {
  return sh(returnStdout: true, script: "git log -1 --pretty=format:'%an'").trim()
}

/**

/**
 * Get the long format commit hash of the current commit
 */
String getCommitDate() {
  return sh(returnStdout: true, script: "git log -n 1 --date=format:'%y%m%d' --pretty=format:'%cd'").trim()
}
