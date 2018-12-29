/**
* Set git user configuration
* @param userEmail
* @param userName
*/
Void call(String userName, String userEmail) {
  sh(returnStdout: false, script: 'git config --global user.email "' + userEmail + '"')
  sh(returnStdout: false, script: 'git config --global user.name "' + userName + '"')
}
