#!/usr/bin/env groovy

/**
 * Create a version string based on scheme provided as param
 * @param versionning scheme
 * @return the version number as a string
 */
String call(String versionType='hash') {
    println '---> construct and return a release version'
    switch(versionType) {
        case 'semver':
            version = '1.0.0'
        break
        case 'build':
            version = getBuildVer()
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
