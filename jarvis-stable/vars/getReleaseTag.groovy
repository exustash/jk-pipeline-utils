#!/usr/bin/env groovy

/**
* Push a release tag to github repository
* @param context
* @param version
* @return a string as release tag
*/
Void call(String deployEnv, String version) {
    try {
        getAutoTagScript()
        return sh(returnStdout: true, script: "./release_tag.sh ${deployEnv}  ${version}").trim()
    } catch (err) {
        error("[ERR!] Pipeline execution error: ${err.message}")
    }
}

Void getAutoTagScript() {
  writeFile file: 'release_tag.sh', text: libraryResource('scripts/release_tag.sh')
  sh returnStdout: false, script: 'chmod +x release_tag.sh'
}
