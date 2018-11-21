/**
* Retrieve the current pull request number
*/
Integer getPullRequestNumber() {
    env.CHANGE_ID != null && env.CHANGE_ID.length() > 0
    return env.CHANGE_ID
}
