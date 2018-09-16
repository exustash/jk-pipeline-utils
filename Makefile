# -*- mode: makefile -*-
#
# The files implements the release pipeline interface
# It defines a target for each task executed during the release pipeline
#

BUILD_IMAGE=mikanolab/release-gradle:latest

#This wraps the execution of yarn task into docker for local emulation of ci context
exec = docker run --rm -it -v ${PWD}:/data/release/pipelines $(BUILD_IMAGE)

ifeq ($(CI),true)
	exec =
endif

.DEFAULT_GOAL := help

#help:	##@ List available tasks on this project
help:
	@grep -E '[a-zA-Z\.\-]+:.*?@ .*$$' $(MAKEFILE_LIST)| tr -d '#'  | awk 'BEGIN {FS = ":.*?@ "}; {printf "\033[36m%-30s\033[0m %s\n", $$1, $$2}'


#code-compile: ##@ compile and install dependencies
code-compile:
	$(exec) gradle assemble

#code-lint: ##@ static analysis for code style violations
code-lint:
	$(exec) gradle check

#test-unit: ##@ run unit test suite against source
test-unit:
	$(exec) gradle test

