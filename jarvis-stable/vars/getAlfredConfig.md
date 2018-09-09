## getAlfredConfig

Retrieve an "AlfredDockerConfig" & "AlfredPipeline" instances object.

### Description

The AlfredConfig is just a small container which contains an "AlfredDockerConfig" object and an "AlfredPipelineConfig" object.

```groovy
getAlfredConfig()
```

### Examples

```groovy
def config = getAlfredConfig()
```

### Get pipeline object

```groovy
def config = getAlfredConfig()
def pipeline = config.pipeline()
```

### Get docker object

```groovy
def config = getAlfredConfig()
def docker = config.docker()
```
