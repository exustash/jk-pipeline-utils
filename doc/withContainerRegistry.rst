## withContainerHost

Use either the primary or a worker container host.

### Parameters

* hostName primary|worker

### Examples

```groovy
withDockerHost('worker') {
    docker pull nginx
}
```
