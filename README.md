# Release Pipeline Library
This library provides setps for common jenkins pipeline logic

## Overview
Ths shared library contains a wide range of functionalities accessible as pipeline steps. You can use the functions provided by this library to create complex pipelines while preserving readability and simplicity.


## Usage


### Example

```groovy
#!/usr/bin/env groovy
@Library(value="pennyworth@master", changelog=false) _
```

## Documentation
All available function in this library are documented [here](doc/functions.md)

Every function is referenced this way:
 - **Signature:** function signature of the setp
 - **Parameters:** the parameters if any that are required
 - **Return Value:** the value returned by the setp if any
 - **Example:** an example of the setp usage in a pipeline

