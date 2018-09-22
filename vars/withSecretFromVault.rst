## withSecretFileFromVault

Fetch a secret file from Vault and use it into the closure.
After the closure execution, the file given in parameter is deleted.

### Parameters

* **secretPath:** path in vault. e.g. secret/jenkins/scale/clusters-credentials
* **key:** vault key e.g. kubecfg
* **filepath:** file path of where the secret will be dumped

### Examples

```groovy
def secretFilePath = "path/to/secret-file"
withSecretFileFromVault(
    "secret/jenkins/scale/clusters-credentials",
    "kubecfg",
    secretFilePath
) {
    sh "docker -v ${secretFilePath}:/file/path myimage"
}
```
