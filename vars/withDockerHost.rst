## useDockerHost

Use either the primary docker host or the slave docker host.

### Parameters

* contextName _(String)_ primary|slave

### Examples

```groovy
useDockerHost {
    docker pull nginx
}

useDockerHost("primary") {
    docker pull nginx
}
```
