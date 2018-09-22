## releaseToGithub
Pushes a given release tag to Github Releases

### Description  
```groovy
releaseToGithub(releaseTag, commitMsg, repositoryName)
```

### Parameters
    - **releaseTag:** common release context are the following release, prod, staging, preprod
    - **commitMsg:** in checkmate context short commit hash are commonly used as versions
    - **repositoryName:** the name of the repository on github

### Return Values
    No return values expected

### Examples
```groovy
releaseToGithub('prod.2.23egf34', 'this is an example', 'neon')
```

The above example will create a release with the tag **prod.2.23egf34** and the artifact **prod.2.23egf34.tar.gz**
