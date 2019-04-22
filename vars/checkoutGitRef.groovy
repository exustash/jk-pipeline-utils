#!/usr/bin/env groovy

/**
 * pipeline git checkout steps that checkout a given git refs
 * @param  gitRefs    a branch name or pullrequest Id
 * @param  repository the name of the targted git repository
 */
Void call(String gitRefs, String repository) {
    println ("--> checking out specified Git Refs ${gitRefs}")
    if (gitRefs ==~ /(PR)[\S]*/) {
        checkoutPullRequest(gitRefs, repository)
    } else {
        checkoutBranch(gitRefs, repository)
    }
}

/**
 * Helper function to checkout a branch
 * @param  branch     the branch name
 * @param  repository the targted repository name
 */
Void checkoutBranch(String branch, String repository) {
    checkout([$class: 'GitSCM',
        branches: [[name: "*/${branch}"]],
        doGenerateSubmoduleConfigurations: false,
        extensions: [[$class: 'RelativeTargetDirectory', relativeTargetDir: "${repository}"]],
        userRemoteConfigs: [[credentialsId: '5d08a943-e1ff-4423-a6d5-ba852245cc2a',
        url: "git@github.com:dailymotion/${repository}.git"]]
    ])
}

/**
 * Helper function to checkout a Pull Request
 * @param  pullRequest a pullrest Id as pr-xxxx
 * @param  repository  [description]
 * @return             [description]
 */
Void checkoutPullRequest(String pullRequest, String repository) {
    breakIfGithubTokenNotSet()
    pullRequestNbr = retrievePrNumber(pullRequest)
    checkout([$class: 'GitSCM',
        branches: [[name: "FETCH_HEAD"]],
        doGenerateSubmoduleConfigurations: false,
        extensions: [
            [$class: 'LocalBranch'],
            [$class: 'RelativeTargetDirectory', relativeTargetDir: "${repository}"],
            [
                $class: 'PreBuildMerge',
                options: [
                    mergeRemote: 'origin',
                    mergeTarget: 'master'
                ]
            ]
        ],
        userRemoteConfigs: [[refspec: "+refs/pull/${pullRequestNbr}/head:refs/remotes/origin/PR-${pullRequestNbr} +refs/heads/master:refs/remotes/origin/master",
                            url: "https://${env.GITHUB_TOKEN}@github.com/dailymotion/${repository}"]]
    ])
}

/**
 *
 * @param  pullRequest pullrequestid as pr-xxxx
 */
/**
 * helper function to retrieve the digits out of a PR Id
 * @param  pullRequest pullrequest id as pr-xxxx
 * @return the digits segement of pr-xxxx as a String
 */
String retrievePrNumber(String pullRequest) {
    String prNumber = pullRequest.split('-')[1]
    return prNumber
}

/**
 * Abort execution of checkoutPullRequest if env.GITHUB_TOKEN is not set.
 */
String breakIfGithubTokenNotSet() {
    if (!env.GITHUB_TOKEN) {
        error("[ERR!] The Github Token is not set. This step cannot proceed")
    }
}
