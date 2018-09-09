#!/usr/bin/env groovy

/**
 * from "make get-standbox-version"
 * @return Sandbox version e.g. 0.0.1-23ef45 retrieved
 */
String call() {
    return sh(script: "make get-sandbox-version | tail -n 1", returnStdout: true).trim()
}
