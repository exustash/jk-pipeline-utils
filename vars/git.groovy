#!/usr/bin/env groovy


/**
 * Get the short format commit hash of the current commit
 */
String getCommitHash() {
  return sh(returnStdout: true, script: "git log -n 1 --pretty=format:'%h'").trim()
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
