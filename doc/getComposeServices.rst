## getComposeService
Builds a docker image on a given docker host

### Description

```
getComposeService(composePath)
```

### Parameters
  - composePath: the name of the image to build


### Return Values
  a list of all services described in the specified docker-compose file

### Examples

```groovy
getComposeService('stack')
```
