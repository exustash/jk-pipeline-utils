## withSecretFileFromVault

Fetch a secret from Vault server, write the secret into a file.
After usage within the closure the file is deleted.

### Parameters

* **secretPath:** path in vault. e.g. secret/file/path
* **key:** vault key e.g. kubecfg
* **filepath:** file path of where the secret will be dumped

### Examples

```groovy
def secretFilePath = "secret/path/file"
withSecretFileFromVault(
    "secret/path/file",
    "sshconfig",
    secretFilePath
) {
    sh "..."
}
```
