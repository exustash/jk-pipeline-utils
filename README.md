# Release Pipeline Library
jk-shared-lib is [Jenkins Shared Library](https://jenkins.io/doc/book/pipeline/shared-libraries/) which contains a wide range of functionalities accessible as reusable Jenkins pipeline steps, which pipeline code reduce redundacy accross project and make sure Jenkisnfiles are as DRY as possible. You can use the functions provided by this library to create complex pipelines while preserving readability and simplicity of your Jekinsfile.


## Usage

In order to load shared libraries, the pipeline described in the Jenkinsfile is required to use the @Library annotation, followed by the name of the library and the version/branch to be loaded (optional)

```groovy
#!/usr/bin/env groovy
@Library(value="jk-shared-lib@master", changelog=false) _
@Library(value="jk-shared-lib", changelog=false) _
@Library(value="jk-shared-lib@1.3", changelog=false) _
```

## Documentation
All available function in this library are documented [here](doc/stepsdoc.rst)

Every function is referenced this way:
 - **Signature:** function signature of the setp
 - **Parameters:** the parameters if any that are required
 - **Return Value:** the value returned by the setp if any
 - **Example:** an example of the setp usage in a pipeline

