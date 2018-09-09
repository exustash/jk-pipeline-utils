## buildDockerImage
Builds a docker image on a given docker host

### Description

```
buildAgentImage(String imageName, String imageTag='latest', String dockerFilePath='.')
```

### Parameters
  - imageName: the name of the image to build
  - imageTag: the tag of the version to build
  - dockerFilePath: path to the docker file of the image

### Return Values
  No return value expected

### Examples

```groovy
buildDockerImage('acme-app', "20180425-155123.eh123nb", 'acme-docker/.' )
```

The above example build the image dailymotion/acme-app with tag 20180425-155123.eh123nb using a dockerfile  located acme-docker/.
