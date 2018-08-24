#!/usr/bin/groovy
package com.checkmate.summaries

import hudson.tasks.junit.CaseResult
import com.cloudbees.groovy.cps.NonCPS
import hudson.tasks.test.AbstractTestResultAction

/**
 * retrieve the tests resulsts using Jenkins junit module
 * @return String containing the results
 */
@NonCPS
String getSummary () {
    AbstractTestResultAction testResultAction = currentBuild.rawBuild.getAction(AbstractTestResultAction.class)
    String summary = ""

    if (testResultAction != null) {
        total = testResultAction.getTotalCount()
        failed = testResultAction.getFailCount()
        skipped = testResultAction.getSkipCount()

        summary = "Passed: " + (total - failed - skipped)
        summary = summary + (", Failed: " + failed)
        summary = summary + (", Skipped: " + skipped)
    } else {
        summary = "No tests found"
    }
    return summary
}

/**
 * retrieve the tests failures using Jenkins junit module
 * @return String containing the failures
 */
@NonCPS
String getFailures() {
    AbstractTestResultAction testResultAction = currentBuild.rawBuild.getAction(AbstractTestResultAction.class)
    String failedTestsString = "```"

    if (testResultAction != null) {
        String failedTests = testResultAction.getFailedTests()

        if (failedTests.size() > 9) {
            failedTests = failedTests.subList(0, 8)
        }

        for (CaseResult cr : failedTests) {
            failedTestsString = failedTestsString + "${cr.getFullDisplayName()}:\n${cr.getErrorDetails()}\n\n"
        }
        failedTestsString = failedTestsString + "```"
    }
    return failedTestsString
}

return this
