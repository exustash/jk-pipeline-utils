## createReleaseTag
Creates a release tag

### Description  
```
getReleaseTag(String version, String deployEnv)
```

### Parameters
  - version: the version of the application to be released
  - deployEnv: the environment to which the application will be deployment

### Return Values
  Return a string as release tag

### Examples
```groovy
getReleaseTag('eb234ab', 'staging')
```

The above example create release tag **staging.2.eb234ab**
