
#!/usr/bin/env groovy

/**
* Run a release pipeline task with status report
* @return the git repository url
*/
def call() {
  sh "git config --get remote.origin.url > .git/remote-url"
  return readFile(".git/remote-url").trim()
}
