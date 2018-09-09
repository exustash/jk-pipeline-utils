## isBranch

Check if the current branch is equals to an arbitrary value.

### Description

Execute something is the current branch is equals to a value.

```groovy
// env.BRANCH_NAME equals to "prod" or "master"
def isOk = isBranch(["prod", "master"])
```

### Parameters

* branches _(String or List)_ - e.g. "prod" or ["prod", "master"]my-local-image:latest

### Examples

```groovy
// env.BRANCH_NAME equals to "prod" or "master"
def isOk = isBranch(["prod", "master"])

// env.BRANCH_NAME equals to "prod"
def isOk = isBranch("prod")

// "hello world" if the branch is equas to "prod"
isBranch("prod") {
    echo "hello world"
}
```
