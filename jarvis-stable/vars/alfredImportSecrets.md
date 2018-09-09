## alfredImportSecrets

Import project's secrets described in the directory ./alfred/secrets/* _(DotEnv & File)_

### Description

Alfred is a project compagnon, mainly used by our developers to manage security issues. The function `alfredImportSecrets` executes the command `alfred project import secrets`, and it ensures that alfred is authenticated on vault before the execution of the import.

```
alfredImportSecrets()
```

### Return Values
  No return value expected

### Examples

```groovy
alfredImportSecrets()
```
