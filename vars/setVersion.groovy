#!/usr/bin/env groovy

/**
 * Create a version string based on scheme pattern type provided as string param
 * @param versionType versionning scheme
 * @return the version number as a string
 */
String call(String versionType='build') {
    println "---> construct and return a release version based the ${versionType} schema"
    switch (versionType) {
        case 'semver':
            version = generateReleaseTag()
            break
        case 'build':
            version = getBuildVer()
            break
        case 'commit':
            version = getGitCommitHash()
            break
        default:
            version = getGitCommitHash()
            break
    }

    return version
}

String getBuildVer() {
    String commitDate = sh(returnStdout: true, script: "git log -n 1 --date=format:'%y%m%d' --pretty=format:'%cd'").trim()
    String commitHash = getGitCommitHash()
    return "${commitDate}.${env.BUILD_NUMBER}.${commitHash}"
}

String generateReleaseTag() {
    getAutoTagScript()
    return sh(returnStdout: true, script: "./release bump_version").trim()
}

Void getAutoTagScript() {
    writeFile file: 'release', text: libraryResource('scripts/release.util.sh')
    sh returnStdout: false, script: 'chmod +x release'
}
