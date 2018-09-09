#!/usr/bin/env groovy

String call() {
    currentBranch = git.getCurrentBranch()

    if (currentBranch ==~ /(prod|stable)/ ) {
        return 'latest'
    }

    return 'staging'
}
