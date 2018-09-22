## setSSHCredentials
Sets an ssh key in the build agent by retrieving it from jenkins credentials manager

### Description  
```groovy
setSSHCredentials()
```
This function accomplishes the following:

*   write the ssh key retrieved from Jenkins credentials to $HOME/.ssh directory
*   sets up github.com as a known host in $HOME/.ssh/knownhosts
*   create an empty configfile in $HOME/.ssh/config necessary for fabric

where $HOME is the the home dir of the user with which Jenkins connect to the slave (container)

### Parameters
  None.

### Return Values
  None.

### Examples
```groovy
HOME='/home/jenkins'
setSSHCredentials()
```

The above example will create do the following
  - write the jenkins ssh-key localy: **/home/jenkins/.ssh/id_rsa**
  - sets up github.com as a known host **in /home/jenkins/.ssh/knownhosts**
  - create an empty configfile in **/home/jenkins/.ssh/config**
