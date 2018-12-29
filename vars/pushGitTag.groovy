/**
* Create a git tag and push it to the git server
* @string tageName
* @string message
*/
Void call(String tagName, String message = '') {
    try {
      sh returnStdout: false, script: "git tag -a '${tagName}' -m '${message}'"
      sh returnStdout: false, script: "git push origin refs/tags/'${tagName}'"
    } catch (err) {
      currentBuild.result = 'FAILED'
      throw err
    }
}
