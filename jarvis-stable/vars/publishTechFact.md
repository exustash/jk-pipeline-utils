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
