## useDockerHost

Use either the master docker host or the slave docker host.

### Parameters

* contextName _(String)_ master|slave

### Examples

```groovy
useDockerHost {
    docker pull nginx
}

useDockerHost("master") {
    docker pull nginx
}
```
