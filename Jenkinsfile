#!/usr/bin/env groovy
@Library(value="jarvis@stable", changelog=false) _

pipeline {

  agent {
    label 'gradle-agent'
  }

  environment {
    SERVICE_NAME = 'jarvis'
    SLACK_CHANNEL = 'release-platform'
    CI = 'true'
    GITHUB_TOKEN=credentials('2671f6e0-d7ee-4746-8711-ef9b2ed57dae')
  }

  options {
    timestamps()
  }

  stages {
    stage('Build'){
      steps {
        runPipelineTask('make', 'code.compile')
      }
    }

    stage('Tests'){
      steps {
          parallel(
            "code linting": {
                runPipelineTask('make', 'code.lint')
            },
            "unit testing": {
                runPipelineTask('make', 'unit.test')
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
                branch 'beta'
            }
        }
        steps {
            script {
                releaseLog = git.getCommitMsg()
                releaseTag = getReleaseVersion()
                packageName = "${env.SERVICE_NAME}."+git.getCurrentBranch()
                runPipelineTask('make', "release.assemble package_name=${packageName}")
                pushReleaseToGH("${env.SERVICE_NAME}", packageName, releaseTag, releaseLog)
            }
        }
        post {
            success {
                sendPipelineStatusToSlack('SUCCESS', 'Release published on repo')
            }
            failure {
                sendPipelineStatusToSlack('FAILURE', 'Release published on repo')
            }
        }
    }
  }
}
