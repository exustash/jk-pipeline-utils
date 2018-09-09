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
 * Get the long format commit hash of the current commit
 */
String getCommitDate() {
  return sh(returnStdout: true, script: "git log -n 1 --date=format:'%Y%m%d-%H%M%S' --pretty=format:'%cd'").trim()
}

/**
 * Get the short format commit hash of the current commit
 */
String getCommitHash() {
  return sh(returnStdout: true, script: "git log -n 1 --pretty=format:'%h'").trim()
}

/**
 * Get the long format commit hash of the current commit
 */
String getCommitLongHash() {
  return sh(returnStdout: true, script: "git log -n 1 --pretty=format:'%H'").trim()
}

/**
 * Get the git message of the current commit
 */
String getCommitMsg() {
  return sh(returnStdout: true, script: "git log -n 1 --pretty=format:'%s'").trim()
}

/**
 * Get the current branch name
 */
String getCurrentBranch() {
  String currentBranch = env.BRANCH_NAME
  if (!currentBranch) {
    currentRaw = sh(script: 'git name-rev --name-only HEAD', returnStdout: true).trim()
    currentBranch = currentRaw.split('remotes/origin/')[1];
  }
  return currentBranch
}

/**
 * retrieve the url of the repositiry associated with current build
 * @return an url string
 */
String getRepositoryURL() {
  sh "git config --get remote.origin.url > .git/remote-url"
  return readFile(".git/remote-url").trim()
}

/**
* Retrieve the current pull request number
*/
Integer getPullRequestNumber() {
    env.CHANGE_ID != null && env.CHANGE_ID.length() > 0
    return env.CHANGE_ID
}

/**
* Create a git tag and push it to the git server
* @string tageName
* @string message
*/
Void pushTag(String tagName, String message = '') {
    reportBuildStatus("release/pipeline/tag.push", "Pipeline Task tag.push is runing", 'PENDING')
    try {
      sh returnStdout: false, script: "git tag -a '${tagName}' -m '${message}'"
      sh returnStdout: false, script: "git push origin refs/tags/'${tagName}'"
      reportBuildStatus("release/pipeline/tag.push", "Pipeline Task tag.push  has succeeded", 'SUCCESS')

    } catch (err) {
      reportBuildStatus("release/pipeline/tag.push", "Pipeline Task tag.push  has failed", 'FAILURE')
      currentBuild.result = 'FAILED'
      throw err
    }
}

/**
* Set git user configuration
* @param userEmail
* @param userName
*/
Void setUser(String userName, String userEmail) {
  sh(returnStdout: false, script: 'git config --global user.email "' + userEmail + '"')
  sh(returnStdout: false, script: 'git config --global user.name "' + userName + '"')
}
