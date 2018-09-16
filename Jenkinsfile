#!/usr/bin/env groovy
@Library(value="pennyworth@master", changelog=false) _

pipeline {

  agent {
    label 'gradle-executor'
  }

  environment {
    CI = 'true'
    LIBRARY_NAME = "pennyworth"
    SLACK_CHANNEL = "release-train"
  }

  options {
    timestamps()
  }

  stages {
    stage('Build'){
      steps {
        execPipelineTask('make', 'install-deps')
      }
    }

    stage('Tests'){
      steps {
          parallel(
            "code linting": {
                execPipelineTask('make', 'lint-code')
            },
            "unit testing": {
                execPipelineTask('make', 'test-unit')
            }
          )
      }
      post {
          success {
              sendPipelineStatusToSlack('SUCCESS', 'Build and Tests')
          }
          failure {
              sendPipelineStatusToSlack('FAILURE', 'Build and Tests')
          }
      }
    }

    stage('Release'){
        when {
            anyOf {
                branch 'master'
            }
        }
        steps {
            script {
                releaseLog = git.getCommitMsg()
                releaseTag = getReleaseVersion()
                pushGitTag(releaseTag, releaseLog)
            }
        }
        post {
            success {
                sendNotifToSlack('SUCCESS', 'Release published on repo')
            }
            failure {
                sendNotifToSlack('FAILURE', 'Release published on repo')
            }
        }
    }
  }
}
