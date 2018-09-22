## sendPipelineNotifToSlack
Implements a step that send a given notifications to a defined slack channel

### Description  
```groovy
sendPipelineStatusToSlack(String buildStatus)
```

  - This is appropriate for multibranch pipelines for CI/CD and Deployment
  - This steps needs the **SLACK_CHANNEL** Environment variable to be set.

### Parameters
  - **buildStatus:** status of the current build to be reported to slack channel
  - **context:** explianing the context of execution of the pipeline


### Return Values
  None.

### Examples
```groovy
SLACK_CHANNEL = 'release-platform'
sendPipelineStatusToSlack('SUCCESS')
```

The above example will notify in the slack channel **release-platform** that the **build is a success** and will **provide a blue ocean url** to it.
