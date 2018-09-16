#!/usr/bin/groovy
package org.mikanolab.reports

import hudson.tasks.junit.CaseResult
import com.cloudbees.groovy.cps.NonCPS
import hudson.tasks.test.AbstractTestResultAction

/**
 * retrieve the tests resulsts using Jenkins junit module
 * @return String containing the results
 */
@NonCPS
String getReports () {
    AbstractTestResultAction testReportAction = currentBuild.rawBuild.getAction(AbstractTestResultAction.class)
    String report = ""

    if (testReportAction != null) {
        total = testReportAction.getTotalCount()
        failed = tesReportAction.getFailCount()
        skipped = tesReportAction.getSkipCount()

        report = "Passed: " + (total - failed - skipped)
        report = report + (", Failed: " + failed)
        report = report + (", Skipped: " + skipped)
    } else {
        report = "No tests found"
    }
    retureportort
}

/**
 * retrieve the tests failures using Jenkins junit module
 * @return String containing the failures
 */
@NonCPS
String getFailures() {
    AbstractTestResultAction testReportAction = currentBuild.rawBuild.getAction(AbstractTestReportAction.class)
    String failedTestsString = "```"

    if (testReportAction != null) {
        String failedTests = testReportAction.getFailedTests()

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
