## pypiPublish

Publish a package to the targeted index (prod or sandbox).
Use Makefile "make publish" recipe.

### Parameters

* packageName _(String)_ - e.g. westeros-common
* indexName _(String|optional)_ - e.g. prod|sandbox

### Examples

```groovy
// Publish westeros-common to the regular index (prod)
pypiPublish("westeros-common")

// Publish westeros-common to the sandbox index
pypiPublish("westeros-common", "sandbox")
```
