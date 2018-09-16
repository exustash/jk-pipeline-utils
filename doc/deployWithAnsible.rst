## deployWithAnsible
Deploys a given application through an ansible playbook to a specified environment

### Description  
```groovy
deployWithAnsible(String deploy_env, String serviceName)
```

### Parameters
  - deployEnv: the environment to which the application will be deployment
  - serviceName: the name of the service that will deployed to specified env

### Return Values
  No values are returned

### Examples
```groovy
deployWithAnsible('uapp', 'staging')
```

The above example deploy the application **uapp** to the environment **staging**
