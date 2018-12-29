## buildDockerImage
Builds a docker image given a specific name, tagg an dockerfile path.

### Description

```
buildDockerImage(String imageName, String imageTag, String dockerFilePath='.')
```

### Parameters
  - imageName: the name of the image to build
  - imageTag: the tag of the version to build
  - dockerFilePath: path to the docker file of the image

### Return Values
  No return value expected

### Examples

```groovy
buildDockerImage('acme-app', "eh123nb", 'acme-docker/.' )
```

The above example build the image checkmate/acme-app with tag eh123nb using a dockerfile  located acme-docker/.
