## withContainerHost

Use either the primary host or the worker host.

### Parameters

* registryId _(String)_ primary|worker

### Examples

```groovy
withDockerHost {
    docker pull nginx
}
```
