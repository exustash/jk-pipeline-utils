/**
 * retrieve the url of the repositiry associated with current build
 * @return an url string
 */
String call() {
  sh "git config --get remote.origin.url > .git/remote-url"
  return readFile(".git/remote-url").trim()
}
