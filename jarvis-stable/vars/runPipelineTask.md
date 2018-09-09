## runReleaseTask
Runs a given pipeline task with error handling

### Description  
```groovy
runPipelineTask(String taskRunner, String taskCmd)
```

This function is to be used in the context of a task executable in the pipeline through as 'sh' step.

### Parameters
  - **taskCmd:** the task to be executed
  - **taskRunner:** the binary called to execute the task

### Return Values
  No return value expected

### Examples
```groovy
runPipelineTask('t.unit ci=true', 'make')
```

The above example will execute **unit tests** using **Make binary** and report build status to GitHub
in this example uploadToS3 is a common step that you can use if you need to upload  a backup, an archive, etc... to a AWS S3 bucket. 
