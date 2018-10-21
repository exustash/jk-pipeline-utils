#!/usr/bin/env groovy

/**
* Set ssh key for git and remote server operation
*/

Void call(String secretId) {
    try {
        if (isCredentailsSet() == false) {
            withCredentials([sshUserPrivateKey(
            credentialsId: "${ssh_key_id}",
            keyFileVariable: 'RELEASE_KEY',
            usernameVariable: 'RELEASE_KEY_USR',
            )]) {
                sh returnStdout: false, script: 'mkdir -p $HOME/.ssh/'
                sh returnStdout: false, script: 'cp $RELEASE_KEY $HOME/.ssh/id_rsa'
                sh returnStdout: false, script: 'touch $HOME/.ssh/known_hosts'
                sh returnStdout: false, script: 'touch $HOME/.ssh/config'
                sh returnStdout: false, script: 'chown -R $USER:$USER $HOME/.ssh'
                sh returnStdout: false, script: 'ssh-keyscan github.com >> $HOME/.ssh/known_hosts'
            }
        }
    } catch (err) {
        error("[Error!] Pipeline execution error: ${err.message}")
    }
}

Boolean isSSHkeySet() {
    Boolean jenkinsSSHExists = fileExists '/home/jenkins/.ssh/id_rsa'
    return (jenkinsSSHExists == true) ? true : false
}
