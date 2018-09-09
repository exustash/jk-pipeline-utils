## pushDockerImage

Push docker image to our docker repositories _(Docker Hub & Quay.io)_.

### Description

That function it's an abstract of the docker repositories we are using at Dailymotion.

We push our docker images to:

* Docker Hub
* Quay.io

That function required the ".alfred/docker.yaml" file.
We use that file to give permissions to the listed tribes.

```yaml
images:
  - westeros-spaces
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
docker pull dailymotion/my-remote-image:latest
```
**Quay.io**
```bash
docker pull quay.io/dailymotionadmin/my-remote-image:latest
```

### Parameters

* dockerContext: _(String|optional)_ - master|slave
* imageNameFrom: _(String)_ - e.g. my-local-image:latest
* imageNameTo: _(String)_ - e.g. my-remote-image:latest
* config: _(Map)_
  - `branches_to_images`: - Mapping branches to images

### Examples

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
            master: "staging",
            prod: "prod"
        ]
    ]
)

pushDockerImage(
    "master",
    "my-local-image:latest",
    "my-remote-image:latest",
    [
        branches_to_images: [
            master: "staging",
            prod: "prod"
        ]
    ]
)
```
