## useDockerHost

Use either the primary docker host or the worker docker host.

### Parameters

* hostName _(String)_ primary|worker

### Examples

```groovy
useDockerHost {
    docker pull nginx
}

useDockerHost("primary") {
    docker pull nginx
}
```
