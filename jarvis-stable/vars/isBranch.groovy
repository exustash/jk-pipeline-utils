#!/usr/bin/env groovy

/**
 * [call description]
 * @param  branchNames [description]
 * @param  body        [description]
 * @return             [description]
 */
Void call(List branchNames, Closure body) {
    Boolean isBranch = call(branchNames)

    if (isBranch) {
        body()
    }
}

/**
 * [call description]
 * @param  branchNames [description]
 * @return             [description]
 */
Boolean call(List branchNames) {
    //@TODO: there is a better way to write and avoid params reassigment
    if ((branchNames instanceof List)) {
        branchNameList = branchNames
    } else {
         branchNameList = [branchNames]
    }

    for (String branchName in branchNameList) {
        if (branchName == env.BRANCH_NAME) {
            return true
        }
    }
    return false
}
