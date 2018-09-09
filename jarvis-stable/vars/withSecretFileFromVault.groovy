#!/usr/bin/env groovy

/**
 * [call description]
 * @param  secretPath [description]
 * @param  key        [description]
 * @param  filepath   [description]
 * @param  body       [description]
 * @return            [description]
 */
Void call(String secretPath, String key, String filepath, Closure body) {
    ArrayList creds = [
        [
            $class: 'VaultSecret',
            path: secretPath,
            secretValues: [
                [$class: 'VaultSecretValue', envVar: 'FILE_CONTENT', vaultKey: key]
            ]
        ]
    ]

    try {
        wrap([$class: 'VaultBuildWrapper', vaultSecrets: creds]) {
            writeFile file: "${filepath}", text: "${FILE_CONTENT}"
            body()
        }
    } catch (err) {
        echo "${err}"
        throw err
    } finally {
        //sh "rm ${filepath} -f"
        println (filepath)
    }
}
