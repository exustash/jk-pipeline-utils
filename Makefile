# -*- mode: makefile -*-
#
# The files implements the release pipeline interface
# It defines a target for each task executed during the release pipeline
#

BUILD_IMAGE=mikanolab/guardians-gradle:latest

#This wraps the execution of yarn task into docker for local emulation of ci context
run = docker run --rm -it -v ${PWD}:/workspaces $(BUILD_IMAGE)

ifeq ($(CI),true)
	run =
endif

.DEFAULT_GOAL := tasks.list

#tasks.list:	##@ List available tasks on this project
tasks.list:
	@grep -E '[a-zA-Z\.\-]+:.*?@ .*$$' $(MAKEFILE_LIST)| sort | tr -d '#'  | awk 'BEGIN {FS = ":.*?@ "}; {printf "\033[36m%-30s\033[0m %s\n", $$1, $$2}'

#deps.install: ##@ compile and/or install dependencies
deps.install:
	$(run) gradle assemble

#static.lint: ##@ static analysis for code style violations
static.lint:
	$(run) gradle check

#test.unit: ##@ run unit test suite against source
test.unit:
	$(run) gradle test
