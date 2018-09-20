#!/usr/bin/env groovy

/**
* Set ssh key for git and remote server operation
*/

Void call() {
    try {
        if (isCredentailsSet() == false) {
            withCredentials([sshUserPrivateKey(
            credentialsId: 'jenkins-credetials-id',
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

Boolean isCredentailsSet() {
    Boolean jenkinsSSHExists = fileExists '/home/jenkins/.ssh/id_rsa'
    Boolean rootSSHExists = fileExists '/home/root/.ssh/id_rsa'
    return ((jenkinsSSHExists == true) || (rootSSHExists == true)) ? true : false
}
