#!/usr/bin/env groovy

Void call(String imageName, String imageTag, String sourceDir, String targetDir) {
  reportBuildStatus("release/pipeline/container.extract", "Task container.extract is running", 'PENDING')
  try {
    println "---> Extract source code from running container"
    docker.image("dailymotion/${imageName}:${imageTag}").withRun() { c ->
      sh "docker export --output=\"/tmp/container_dump.tar\" ${c.id}"
      sh "tar -xvf /tmp/container_dump.tar usr/${sourceDir}"
      sh "rm -rf ${targetDir}"
      sh "mv usr/${sourceDir} ${targetDir}"
      sh "rm -rf usr/"
    }

    reportBuildStatus("release/pipeline/container.extract", "Task container.extract has succeeded", 'SUCCESS')
  } catch (err) {
    reportBuildStatus("release/pipeline/container.extract", "Task container.extract has failed", 'FAILURE')
    error("[ERR!] Pipeline execution error: ${err.message}")
  }

}
