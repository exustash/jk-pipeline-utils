#!/usr/bin/env groovy

/**
 * Create a version string based on scheme provided as param
 * @param versionning scheme
 * @return the version number as a string
 */
String call(String environment='prod') {
    println '---> construct and return a release version'
    switch(versionType) {
        case 'stage':
            version = "stage.${git.getCommitHash()}"
        break
        case 'dev':
            version = "dev.${git.getCommitHash()}"
        break
        default:
            version = git.getCommitHash()
        break
    }

    return version
}

String getVersionFromBuild() {
    String commitDate = git.getCommitDate()
    String commitHash = git.getCommitHash()

    return "${commitDate}.${env.BUILD_NUMBER}.${commitHash}"
}

String getSemanticVersion() {

}
