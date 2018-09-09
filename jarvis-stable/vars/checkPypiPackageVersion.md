## pypiCheckVersion

Check if a specific version is already deployed on the Pypi Index.

### Parameters

* packageName _(String)_ - e.g. westeros-common
* version _(String)_ - e.g. 0.0.1
* indexName _(String|optional)_ - e.g. prod|sandbox

### Examples

```groovy
// Check if the "westeros-common" with 0.0.1 already exist on prod index.
pypiCheckVersion("westeros-common", "0.0.1")

// Check if the "westeros-common" with 0.0.1 already exist on sandbox index.
pypiCheckVersion("westeros-common", "0.0.1", "sandbox")
```
