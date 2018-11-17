/**
* Create a git tag and push it to the git server
* @string tageName
* @string message
*/
Void call(String tagName, String message = '') {
    reportTaskStatus("delivery/task/tag.push", "Pipeline Task tag.push is runing", 'PENDING')
    try {
      sh returnStdout: false, script: "git tag -a '${tagName}' -m '${message}'"
      sh returnStdout: false, script: "git push origin refs/tags/'${tagName}'"
      reportTaskStatus("delivery/task/tag.push", "Pipeline Task tag.push  has succeeded", 'SUCCESS')

    } catch (err) {
      reportTaskStatus("delivery/task/tag.push", "Pipeline Task tag.push  has failed", 'FAILURE')
      currentBuild.result = 'FAILED'
      throw err
    }
}
