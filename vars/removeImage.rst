## removeDockerImage
Implements a step to Build docker image on a given docker host

### Description

```groovy
removeDockerImage(String image_name, String image_tag='latest', String file_path='.')
```

### Parameters
  - image_name: the name of the image
  - image_tag: the tag of the image

### Return Values
  No return value expected

### Examples

```groovy
removeDockerImage('fixtures-agent', "latest")
```

The above example remove the image **checkmate/fixtures-agent** with tag latest based using **fixtures/Dockerfile**
