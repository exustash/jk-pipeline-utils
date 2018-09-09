## sendJobStatusToSlack
Implements a step that send a given Status to a defined slack channel

### Description  
```groovy
sendJobStatusToSlack(String buildStatus, String context='none')
```

  - This is appropriate cron-like job to execute repetitive task

### Parameters
  - **buildStatus:** status of the current build to be reported to slack channel
  - **context:** explianing the context of execution of the job


### Return Values
  None.

### Examples
```groovy
SLACK_CHANNEL = 'release-platform'
sendJobStatusToSlack('SUCCESS', 'Cleaning up master and slave host')
```

The above example will Statusy in the slack channel **release-platform** that the **build is a success** and will **provide a blue ocean url** to it.
