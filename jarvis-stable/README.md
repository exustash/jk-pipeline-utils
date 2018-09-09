# Release Pipeline Library
A shared library containing Jenkins pipeline steps and utilities to implement the DRY and KISS concept into Jenkins pipelines.

## Overview
The shared library contains a wide range (in scope) of functions accessible as steps to run Jenkins pipelines in accordance with best practice.

The best practice pipelines are based on the general concepts of Jenkins 2.0 **Declarative Pipelines** and aim to provide a way to write pipeline that easily maintainable.

You can use the functions provided by this library to create project-specific complex pipelines while preserving the readability and simplicity.


## Usage
The best practices is to explicitly call the library at the top of your pipeline (Declarative or Scripted) in order to always have the version that
is relevant to the stages and steps running in your pipelines.

This is no different than installing a dependency in a software project you always control the version of the dependencies integrated in your project.

It is thus strongly recommended that you explicitly call for a specific version of this library in your project. Otherwise a function changes can break your pipeline any given day.

### Example

```groovy
#!/usr/bin/env groovy
@Library(value="jarvis@beta", changelog=false) _
```

## Documentation
All available function in this library are documented [here](doc/functions.md)

Every function is referenced this way:
 - **Name:** the  by which the function will be called as pipeline step
 - **Short Overview:** a short definition explaining what the function does
 - **Description:** an detailed function signature with some explanation when necessary
 - **Parameters:** a detailed description of parameters of the function when needed
 - **Return Values:** a detailed description of the return values of the function when needed
 - **Example:** an example of the function usage with explanation if relevant.


## Contribution
Please take a moment to review these guidelines in order to make the contribution process easy and effective for all engineers involved.


### Feature requests
Feature requests are welcome. But take a moment to find out whether your request fits with the scope and aims of this library or if the feature you wish to request hasn't been already implemented completely or partially and provide as much detail and context as possible.

### Pull requests
Good pull requests - patches, improvements, new features - are a fantastic help. They should remain focused in scope and **avoid duplication** of features and functionalities.

Before embarking on coding for any significant pull request please discuss with everyone involved in this project and adhere to the following Prerequisites and conventions.

**PR Prerequisite questions:**
- Does the feature (function) already exist in the library?
- If the feature (function) exists, should it be improved or fixed?
- If improvement or fixes are needed, do i preserve or break compatibility?


**Coding conventions** used throughout a project (indentation, accurate comments, etc.) and any other requirements...
[http://groovy-lang.org/style-guide.html](http://groovy-lang.org/style-guide.html)

**Commit message conventions** used throughout the project based on the angular project conventions. Respect of this convention helps with the change logs associated with each releases of the library.
[https://github.com/angular/angular.js/blob/master/DEVELOPERS.md#commits](https://github.com/angular/angular.js/blob/master/DEVELOPERS.md#commits)

**Documentation Connvention** (cf documentation section above) used throughout the project, each function should have a documentation file in markdown, that are automatically merged   [here](doc/functions.md)


**Commit Message:**
> type(scope): subject

> doc(readme.md): adds a readme documment to the project

**PR Title:**

> Ticket-Number: PR Short Description

> DEVSYS-XXXX: my fantastic PR

**PR Description:**

> Introductory line
  - Item 1
  - Item 2

## Testing
** Comming Soon! **

Before we install the required tools for groovy testing you can test your new and shiny function by calling it from the jenkisnfile.

Add your function in existing stage if the scope is relevant if your function is related to a git operation (including github)
you should add your function call to the Git stage.

Do not forget to add some additional script to check if your function worked and if your function created arftifacts on remote services or systems, please do not forget to cleanup after yourself.

external script for testing purpose go into the scripts folder
