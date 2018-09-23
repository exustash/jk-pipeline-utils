# Release Pipeline Library
This library provides setps for common jenkins pipeline logic

## Overview
Ths shared library contains a wide range of functionalities accessible as pipeline steps. You can use the functions provided by this library to create complex pipelines while preserving readability and simplicity.


## Usage


### Example

```groovy
#!/usr/bin/env groovy
@Library(value="pennyworth@primary", changelog=false) _
```

## Documentation
All available function in this library are documented [here](doc/functions.md)

Every function is referenced this way:
 - **Name:** the  by which the function will be called as pipeline step
 - **Description:** an detailed function signature with some explanation when necessary
 - **Parameters:** a detailed description of parameters of the function when needed
 - **Return Values:** a detailed description of the return values of the function when needed
 - **Usage:** an example of the function usage with explanation if relevant.


## Contribution
[here](CONTRIBUTING.md)
