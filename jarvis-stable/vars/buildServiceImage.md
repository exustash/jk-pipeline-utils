## buildComposeService
Builds a docker-compose service images as described on a given docker-compose.yml file

### Description

```
buildComposeService(serviceName)
```

### Parameters
  - serviceName: the name of the image to build


### Return Values
  No return value expected

### Examples

```groovy
buildDockerImage('fixtures-agent', "latest", 'fixtures/.' )
```

The above example build the image dailymotion/fixtures-agent with tag latest based using fixtures/Dockerfile
