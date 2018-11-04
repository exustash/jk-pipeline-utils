## buildDockerImage
Builds a docker image on a given docker host

### Description

```
buildAgentImage(String imageName, String imageTag='latest', String filePath='.')
```

### Parameters
  - imageName: the name of the image to build
  - imageTag: the tag of the version to build
  - filePath: path to the docker file of the image

### Return Values
  No return value expected

### Examples

```groovy
buildDockerImage('gradle-image', "latest.tag", '.' )
```

The above example build the image checkmate/fixtures-agent with tag latest based using fixtures/Dockerfile
## buildComposeService
Builds a docker image on a given docker host

### Description

```
buildComposeService(serviceName)
```

### Parameters
  - serviceName: the name of the image to build


### Return Values
  No return value expected

### Examples

```groovy
buildDockerImage('fixtures-agent', "latest", 'fixtures/.' )
```

The above example build the image checkmate/fixtures-agent with tag latest based using fixtures/Dockerfile
## cleanupDockerContainers
Deletes a defined list of docker containers from a given docker host

### Description
```
cleanupDockerContainers()
```

### Parameters

### Return Values**

### Examples**

```groovy
cleanupDockerContainers()
```
The above example will remove all containers  on a defined dockerhost
## cleanupDockerImages
Deletes a defined list of docker images from a given docker host

### Description


### Parameters

### Return Values

### Examples

```groovy
cleanupDockerImages()
```
The above example will remove all images  on a defined dockerhost
## cleanupDockerVolumes
Deletes a defined list of docker images from a given docker host

### Description
```
cleanupDockerVolumes()
```

### Parameters

### Return Values

### Examples
```groovy
cleanupDockerVolumes()
```

The above example will remove all volumes on a defined dockerhost
## codexSonarQube
Implements a step that launch "make prepare-sonar" and push the report to SonarQube.

### Description
The project should contain the recipe "make prepare-sonar" to work.
```
codexSonarQube()
```

### Example
```
codexSonarQube()
```
## deployService
Deploys a service a defined by an ansible cook playbook

### Description
```
deployService(String deployEnv, String serviceName, String playbookPath)
```

### Parameters
  - **deployEnv:** the name of the stack to  deploy through docker-compose
  - **serviceName:** the name of the file that defines the docker stack to deploy
  - **playbookPath:** the path to ansible playbook that define the deploy operations

### Return Values
  No values are returned.

### Example
```groovy
deployService('staging', 'essos', 'essos-playbook.yml')
```

The above example deploy the **essos** to staging env as defined by **essos-playbook.yml**
## deployWithAnsible
Deploys a given application through an ansible playbook to a specified environment

### Description
```groovy
deployWithAnsible(String deploy_env, String serviceName)
```

### Parameters
  - deployEnv: the environment to which the application will be deployment
  - serviceName: the name of the service that will deployed to specified env

### Return Values
  No values are returned

### Examples
```groovy
deployWithAnsible('uapp', 'staging')
```

The above example deploy the application **uapp** to the environment **staging**
## downloadFromFiler
Download a static asset file from a filer

### Description
```groovy
downloadFromFiler(String localPath, String remotePath)
```

### Parameters
  - localPath: the local path of the file to download
  - remotePath: the remotePath to the file to download

### Return Values
  No values returned.

### Examples
```groovy
downloadFromFiler('udger', 'uas')
```

The above example will download the content of the remote folder uas to the udger file
# gazrComplexity

Implements the "make complexity" task as specified in the specification.
https://gazr.io/

## Description

The project must contain the Makefile recipe "make complexity".

* It launchs the complexity check on the project, regardless the technology used.
* It notify github with "ci/quality/complexity"

```groovy
gazrComplexity()
```

## Parameters

* dockerContext - primary|worker _(optional)_
* body _(optional)_

## Examples

```groovy
gazrComplexity()

gazrComplexity("primary")

gazrComplexity {
    echo "hello world"
}

gazrComplexity("primary") {
    echo "hello world"
}
```
# gazrFormat

Implements the "make format" task as specified in the specification.
https://gazr.io/

## Description

The project must contain the Makefile recipe "make format".

* It launchs the format on the project, regardless the technology used.
* It notify github with "ci/quality/format"

```groovy
gazrFormat()
```

## Parameters

* dockerContext - primary|worker _(optional)_
* body _(optional)_

## Examples

```groovy
gazrFormat()

gazrFormat("primary")

gazrFormat {
    echo "hello world"
}

gazrFormat("primary") {
    echo "hello world"
}
```
# gazrStyle

Implements the "make style" task as specified in the specification.
https://gazr.io/

## Description

The project must contain the Makefile recipe "make style".

* It launchs the style check on the project, regardless the technology used.
* It notify github with "ci/quality/style"

```groovy
gazrStyle()
```

## Parameters

* dockerContext - primary|worker _(optional)_
* body _(optional)_

## Examples

```groovy
gazrStyle()

gazrStyle("primary")

gazrStyle {
    echo "hello world"
}

gazrStyle("primary") {
    echo "hello world"
}
```
# gazrTestFunctional

Implements the "make test-functional" task as specified in the specification.
https://gazr.io/

## Description

The project must contain the Makefile recipe "make test-functional".

* It launchs the function test on the project, regardless the technology used.
* It notify github with "ci/quality/test-functional"

```groovy
gazrTestFunctional()
```

## Parameters

* dockerContext - primary|worker _(optional)_
* body _(optional)_

## Examples

```groovy
gazrTestFunctional()

gazrTestFunctional("primary")

gazrTestFunctional {
    echo "hello world"
}

gazrTestFunctional("primary") {
    echo "hello world"
}
```
# gazrTestIntegration

Implements the "make test-integration" task as specified in the specification.
https://gazr.io/

## Description

The project must contain the Makefile recipe "make test-integration".

* It launchs the integration test on the project, regardless the technology used.
* It notify github with "ci/quality/test-integration"

```groovy
gazrTestIntegration()
```

## Parameters

* dockerContext - primary|worker _(optional)_
* body _(optional)_

## Examples

```groovy
gazrTestIntegration()

gazrTestIntegration("primary")

gazrTestIntegration {
    echo "hello world"
}

gazrTestIntegration("primary") {
    echo "hello world"
}
```
# gazrTest

Implements the "make test" task as specified in the specification.
https://gazr.io/

## Description

The project must contain the Makefile recipe "make test".

* It launchs the tests on the project, regardless the technology used.
* It notify github with "ci/quality/test"

```groovy
gazrTest()
```

## Parameters

* dockerContext - primary|worker _(optional)_
* body _(optional)_

## Examples

```groovy
gazrTest()

gazrTest("primary")

gazrTest {
    junit "reports/report_unit_tests.xml"
}

gazrTest("primary") {
    junit "reports/report_unit_tests.xml"
}
```
# gazrTestUnit

Implements the "make test-unit" task as specified in the specification.
https://gazr.io/

## Description

The project must contain the Makefile recipe "make test-unit".

* It launchs the unit test on the project, regardless the technology used.
* It notify github with "ci/quality/test-unit"

```groovy
gazrTestUnit()
```

## Parameters

* dockerContext - primary|worker _(optional)_
* body _(optional)_

## Examples

```groovy
gazrTestUnit()

gazrTestUnit("primary")

gazrTestUnit {
    junit "reports/report_unit_tests.xml"
}

gazrTestUnit("primary") {
    junit "reports/report_unit_tests.xml"
}
```
# getpennyworthConfig

Retrieve an "pennyworthDockerConfig" & "pennyworthPipeline" instances object.

## Description

The pennyworthConfig is just a small container which contains an "pennyworthDockerConfig" object and an "pennyworthPipelineConfig" object.

```groovy
getpennyworthConfig()
```

## Examples

```groovy
def config = getpennyworthConfig()
```

## Get pipeline object

```groovy
def config = getpennyworthConfig()
def pipeline = config.pipeline()
```

## Get docker object

```groovy
def config = getpennyworthConfig()
def docker = config.docker()
```
## getComposeService
Builds a docker image on a given docker host

### Description

```
getComposeService(composePath)
```

### Parameters
  - composePath: the name of the image to build


### Return Values
  a list of all services described in the specified docker-compose file

### Examples

```groovy
getComposeService('stack')
```
# getPackageSandboxVersion

Get sandbox version from Git tags.

## Examples

```groovy
def version = getPackageSandboxVersion()
```
# getPackageVersion

Launch task "make get-version" to retrieve the defined version of the package.

## Examples

```groovy
def packageVersion = getPackageVersion()
```
## getGitCommitHash
Implements a step that retrieves the short hash of the current commit

### Description
```
getGitCommitHash()
```

### Parameters
  No parameters required

### Return Values
  Returns a string as commit hash

### Examples
```groovy
getGitCommitHash()
```
## getGitCommitLongHash
Retrieves the long hash of the current commit

### Description
```groovy
getGitCommitLongHash()
```

### Parameters
  No parameters required

### Return Values
  Returns a string as commit long hash

### Examples
```groovy
getGitCommitLongHash()
```

The above

## getGitRepositoryURL
Retrieves the current repository URL

### Description
```
getGitRepositoryURL()
```

### Parameters
  None.

### Return Values
  Returns a string as pull request ID

### Example
```
getGitRepositoryURL()
```

The above example will return the url of the current repository url

## getGitCommitMsg
Retrieves the short hash of the current commit

### Description
```groovy
getGitCommitMsg()
```

### Parameters
  No parameters required

### Return Values
  Returns a string as commit message

### Examples
```groovy
getGitCommitMsg()
```

The above
# isBranch

Check if the current branch is equals to an arbitrary value.

## Description

Execute something is the current branch is equals to a value.

```groovy
// env.BRANCH_NAME equals to "prod" or "primary"
def isOk = isBranch(["prod", "primary"])
```

## Parameters

* branches _(String or List)_ - e.g. "prod" or ["prod", "primary"]my-local-image:latest

## Examples

```groovy
// env.BRANCH_NAME equals to "prod" or "primary"
def isOk = isBranch(["prod", "primary"])

// env.BRANCH_NAME equals to "prod"
def isOk = isBranch("prod")

// "hello world" if the branch is equas to "prod"
isBranch("prod") {
    echo "hello world"
}
```
# makeDown
Launch the "make down" makefile recipe.

## Parameters

* dockerContext - primary|worker _(optional)_
# makeDownOnFailure

Put a try/catch around a script and execute the "make down" makefile recipe if an error is thrown.

## Parameters

* dockerContext - primary|worker _(optional)_

## Examples

```groovy
makeDownOnFailure {
    echo "hello world"
}

makeDownOnFailure("primary") {
    echo "hello world"
}
```
# packageTag

Tag the package to a specific version.
Launch "make tag" with the var PKG_VERSION set the version wished.

## Parameters

* version _(String)_ - e.g. 0.0.1

## Examples

```groovy
packageTag("0.0.1")
```
## PublishTechFact
Publishes a technical facts to the Events Tool for every deployment to prod

### Description
```groovy
publishTechFacts(String releaseTag, String commitMsg, String serviceName)
```
The the following informations will be published:

*   The Commit message explaining what is being deployed
*   The release tag being currently deployed
*   The name of the person who triggered the deployment
*   The name, number and url of the deployment job and run.
*   The date and time of the deployment run.

### Usage

```groovy
publishTechFacts('prod.37.7299dc731', 'Merge pull request #1632', 'neon uapp')
```
the example above will publish technical facts board the following event: the **release prod.37.7299dc731** of **neon uapp** has been deployed to production with the message **Merge pull request #1632*
## pushDockerImage

Push docker image to our docker repositories _(Docker Hub & Quay.io)_.

## Description

That function it's an abstract of the docker repositories we are using at checkmate.


## Parameters

## Examples

```groovy
pushDockerImage("my-remote-image:latest")

```
## pushDockerImage

Push docker image to our docker repositories _(Docker Hub & Quay.io)_.

## Description

That function it's an abstract of the docker repositories we are using at checkmate.

We push our docker images to:

* Docker Hub
* Quay.io

That function required the ".pennyworth/docker.yaml" file.
We use that file to give permissions to the listed tribes.

```yaml
images:
  - nandaparbat-spaces
repositories:
  quayio:
    owners:
      - tribescale
    readers:
      - tribeinfrastructure
```

The current list of Tribes are:

* tribescale
* tribeplayer
* tribeuserproduct
* tribedata
* tribecommunitypolicy
* tribearchitecture
* tribeinfrastructure
* tribeinfrastructure
* tribedesign
* tribeproductdesign
* tribepartnerproduct
* tribecontentops
* tribeadtech

```groovy
pushDockerImage("my-local-image:latest", "my-remote-image:latest")
```

In that example:

* `my-local-image:latest` is the local image name, built on the CI.
* `my-remote-image:latest` is the remote image name, pushed on the docker repositories.

When the function is executed, the image is available on Docker Hub & Quay.io
**Docker Hub**
```bash
docker pull checkmate/my-remote-image:latest
```
**Quay.io**
```bash
docker pull quay.io/checkmateadmin/my-remote-image:latest
```

## Parameters

* dockerContext: _(String|optional)_ - primary|worker
* imageNameFrom: _(String)_ - e.g. my-local-image:latest
* imageNameTo: _(String)_ - e.g. my-remote-image:latest
* config: _(Map)_
  - `branches_to_images`: - Mapping branches to images

## Examples

```groovy
pushDockerImage("my-remote-image:latest")

pushDockerImage(
    "my-local-image:latest",
    "my-remote-image:latest"
)

pushDockerImage(
    "my-local-image:latest",
    "my-remote-image:latest",
    [
        branches_to_images: [
            primary: "staging",
            prod: "prod"
        ]
    ]
)

pushDockerImage(
    "primary",
    "my-local-image:latest",
    "my-remote-image:latest",
    [
        branches_to_images: [
            primary: "staging",
            prod: "prod"
        ]
    ]
)
```
## releaseToGithub
Pushes a given release tag to Github Releases

### Description
```groovy
releaseToGithub(releaseTag, commitMsg, repositoryName)
```

### Parameters
    - **releaseTag:** common release context are the following release, prod, staging, preprod
    - **commitMsg:** in checkmate context short commit hash are commonly used as versions
    - **repositoryName:** the name of the repository on github

### Return Values
    No return values expected

### Examples
```groovy
releaseToGithub('prod.2.23egf34', 'this is an example', 'neon')
```

The above example will create a release with the tag **prod.2.23egf34** and the artifact **prod.2.23egf34.tar.gz**
# pypiBuild

Launch task "make build" to prepare the artifact before publishing it to Pypi.

## Examples

```groovy
pypiBuild()
```
# pypiCheckVersion

Check if a specific version is already deployed on the Pypi Index.

## Parameters

* packageName _(String)_ - e.g. nandaparbat-common
* version _(String)_ - e.g. 0.0.1
* indexName _(String|optional)_ - e.g. prod|sandbox

## Examples

```groovy
// Check if the "nandaparbat-common" with 0.0.1 already exist on prod index.
pypiCheckVersion("nandaparbat-common", "0.0.1")

// Check if the "nandaparbat-common" with 0.0.1 already exist on sandbox index.
pypiCheckVersion("nandaparbat-common", "0.0.1", "sandbox")
```
# pypiConfig

Get Pypi configuration.

## Examples

```groovy
def config = pypiConfig()
```
# pypiPublish

Publish a package to the targeted index (prod or sandbox).
Use Makefile "make publish" recipe.

## Parameters

* packageName _(String)_ - e.g. nandaparbat-common
* indexName _(String|optional)_ - e.g. prod|sandbox

## Examples

```groovy
// Publish nandaparbat-common to the regular index (prod)
pypiPublish("nandaparbat-common")

// Publish nandaparbat-common to the sandbox index
pypiPublish("nandaparbat-common", "sandbox")
```
## removeDockerImage
Implements a step to Build docker image on a given docker host

### Description

```groovy
removeDockerImage(String image_name, String image_tag='latest', String file_path='.')
```

### Parameters
  - image_name: the name of the image
  - image_tag: the tag of the image

### Return Values
  No return value expected

### Examples

```groovy
removeDockerImage('fixtures-agent', "latest")
```

The above example remove the image **checkmate/fixtures-agent** with tag latest based using **fixtures/Dockerfile**
## reportTaskStatus
Reports the build statrus to the given refs on github

### Description
```groovy
reportTaskStatus(String context, String message, String state)
```

### Parameters
  -  **context**
  -  **message**
  -  **stage**

### Return Values

### Examples
```groovy
reportTaskStatus()
```

The above
## runDeployTask
Implements a step that runs a given saas task withstatus report to PR/commit and error handling

### Description
```groovy
runDeployTask(String taskCmd, String taskExecutor, String ref, String deployEnv = 'staging')
```

### Parameters
  - **taskCmd:**
  - **taskExecutor:**
  - **deployEnv:**

### Return Values

### Examples
```groovy
runDeployTask('p.deploy', 'make', 'prepprod')
```

The above example will execute the task **deploy** to the environment **preprod** using the **Make binary** as task executor.
## runReleaseTask
Runs a given pipeline task with error handling

### Description
```groovy
execPipelineTask(String taskRunner, String taskCmd)
```

This function is to be used in the context of a task executable in the pipeline through as 'sh' step.

### Parameters
  - **taskCmd:** the task to be executed
  - **taskRunner:** the binary called to execute the task

### Return Values
  No return value expected

### Examples
```groovy
execPipelineTask('t.unit ci=true', 'make')
```

The above example will execute **unit tests** using **Make binary** and report build status to GitHub
## sendJobStatusToSlack
Implements a step that send a given Status to a defined slack channel

### Description
```groovy
sendJobStatusToSlack(String buildStatus)
```

  - This is appropriate cron-like job to execute repetitive task

### Parameters
  - **buildStatus:** status of the current build to be reported to slack channel


### Return Values
  None.

### Examples
```groovy
SLACK_CHANNEL = 'release-platform'
sendJobStatusToSlack('SUCCESS')
```

The above example will Statusy in the slack channel **release-platform** that the **build is a success** and will **provide a blue ocean url** to it.
## sendPipelineNotifToSlack
Implements a step that send a given notifications to a defined slack channel

### Description
```groovy
sendPipelineStatusToSlack(String buildStatus)
```

  - This is appropriate for multibranch pipelines for CI/CD and Deployment
  - This steps needs the **SLACK_CHANNEL** Environment variable to be set.

### Parameters
  - **buildStatus:** status of the current build to be reported to slack channel


### Return Values
  None.

### Examples
```groovy
SLACK_CHANNEL = 'release-platform'
sendPipelineStatusToSlack('SUCCESS')
```

The above example will notify in the slack channel **release-platform** that the **build is a success** and will **provide a blue ocean url** to it.
# setPackageVersion

Set the version of the current pypi package to a specific value.
Launch "make set-version" with the var PKG_VERSION set the version wished.

## Parameters

* version _(String)_ - e.g. 0.0.1

## Examples

```groovy
setPackageVersion("0.0.1")
```
## setReleaseSSHkey
Sets an ssh key in the build agent by retrieving it from jenkins credentials manager

### Description
```groovy
setReleaseSSHkey()
```
This function accomplishes the following:

*   write the ssh key retrieved from Jenkins credentials to $HOME/.ssh directory
*   sets up github.com as a known host in $HOME/.ssh/knownhosts
*   create an empty configfile in $HOME/.ssh/config necessary for fabric

where $HOME is the the home dir of the user with which Jenkins connect to the worker (container)

### Parameters
  None.

### Return Values
  None.

### Examples
```groovy
HOME='/home/jenkins'
setReleaseSSHkey()
```

The above example will create do the following
  - write the jenkins ssh-key localy: **/home/jenkins/.ssh/id_rsa**
  - sets up github.com as a known host **in /home/jenkins/.ssh/knownhosts**
  - create an empty configfile in **/home/jenkins/.ssh/config**
## createReleaseTag
Creates a release tag

### Description
```
getReleaseTag(String version, String deployEnv)
```

### Parameters
  - version: the version of the application to be released
  - deployEnv: the environment to which the application will be deployment

### Return Values
  Return a string as release tag

### Examples
```groovy
getReleaseTag('eb234ab', 'staging')
```

The above example create release tag **staging.2.eb234ab**
## getReleaseVersion
Sets an ssh key in the build agent by retrieving it from jenkins credentials manager

### Description
```groovy
getReleaseVersion()
```
This function accomplishes the following:


### Parameters
  None.

### Return Values
  None.

### Examples
```groovy
getReleaseVersion()
```

The above example will create do the following
## getShortBuildName
Implements a step that create a tag based on the name and number of jenkins build

### Description
```groovy
getShortBuildName (Int numberOfDirectories = 2)
```

### Parameters
  - numberOfDdirectories

### Return Values
  Return a build tag as a string

### Examples
```groovy
getShortBuildName(int numberOfDirectories = 2)
```
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

where $HOME is the the home dir of the user with which Jenkins connect to the worker (container)

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
## stepsScaleDeployment

Deploy a service on Kubernetes Cluster, previously configured in Dragonstone.

## Description

Deploy a service on our Kubernetes Clusters.
The service have to be configured in Dragonstone.

For a specific use-case, the ".pennyworth/pipeline.yaml" file is required.

```yaml
release_name: nandaparbat
service_name: spaces
channel_slack: "#nandaparbat"
```

```groovy
stepsScaleDeployment("nandaparbat", "gbased", "d7b879d")
```

## Parameters

* releaseName _(String)_ - e.g. nandaparbat
* serviceName _(String)_ - e.g. gbased
* version _(String)_ - e.g. d7b879d
* config _(Map)_
  - `guess_env_from_branchname` - Guess environment from the branchname, default=true.
  - `branches_envs_mapping` - if "guess_env_from_branchname" is equals to true, you can configure a mapping between the branch and the targeted environment.
  - `slack_channel`
  - `env` - Environment used to deploy, if exists, the "guess_env_from_branchname" must be equals to false.
  - `skip_prod_validation` - Skip prod validation, default to false.

## Examples

```groovy
// Guess values from hub.yaml
stepsScaleDeployment()

// Guess values from hub.yaml and specific advanced configuration
stepsScaleDeployment([
    slack_channel: "#lol"
])

stepsScaleDeployment("nandaparbat", "gbased", "d7b879d")

stepsScaleDeployment("nandaparbat", "gbased", "d7b879d", [
    slack_channel: "#lol"
])
```
## uploadToS3
Upload a given static asset Upload to an aws S3 bucket

### Description
```groovy
uploadToS3(String filePath, String bucketName)
```

### Parameters
  - **filePath:** the path (including file name) to file to be uploaded
  - **bucketPame:** the path (including folders) to the S3 bucket where the file will be uploaded

### Return Values
  None.

### Examples
```groovy
uploadToS3("userAgentdb.dat", "nandaparbat/static-asset")
```
The above example will push the file **userAgentdb.dat** to the S3 bucket **nandaparbat/static-asset**
## withDockerHost

Use either the primary docker host or the worker docker host.

## Parameters

* contextName _(String)_ primary|worker

## Examples

```groovy
withDockerHost {
    docker pull nginx
}

withDockerHost("primary") {
    docker pull nginx
}
```
## useDockerRegistry

Use the docker **default** hub registry.

## Examples

```groovy
useDockerRegistry {
    docker push checkmate/my-image
}
```
## withSecretFileFromVault

Fetch a secret file from Vault and use it into the closure.
After the closure execution, the file given in parameter is deleted.

### Parameters

* **secretPath:** path in vault. e.g. secret/jenkins/scale/clusters-credentials
* **key:** vault key e.g. kubecfg
* **filepath:** file path of where the secret will be dumped

## Examples

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
