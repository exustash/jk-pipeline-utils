#!/usr/bin/env groovy

/**
* Upload static assets to AWS S3
* @param filePath
* @paramc bucketName
*/
Void call(String filePath, String bucketName) {
    try {
        withCredentials([[$class: 'AmazonWebServicesCredentialsBinding',
          credentialsId: 's3-uploader',
          accessKeyVariable: 'AWS_ACCESS_KEY_ID',
          secretKeyVariable: 'AWS_SECRET_ACCESS_KEY'
          ]]) {
            sh returnStdout: false, script: """aws s3 cp \"/tmp/${env.LOCAL_PATH}/${filePath}\" s3://\"${bucketName}\"/${filePath}"""
        }
    } catch (err) {
        error("[Err!] Pipeline execution error: ${err.message}")
    }
}
