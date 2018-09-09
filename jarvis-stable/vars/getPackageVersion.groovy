#!/usr/bin/env groovy

/**
 * @return The package version retrieved from "make get-version"
 */
String call() {
    return sh(script: "make get-version | tail -n 1", returnStdout: true).trim()
}
