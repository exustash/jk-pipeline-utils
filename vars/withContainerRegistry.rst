## withContainerHost

Use either the primary host or the worker host.

### Parameters

* contextName _(String)_ primary|worker

### Examples

```groovy
withDockerHost {
    docker pull nginx
}

useDockerHost("primary") {
    docker pull nginx
}
```
