## useDockerHost

Use either the primary docker host or the worker docker host.

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
