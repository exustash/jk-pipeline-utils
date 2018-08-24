# -*- mode: makefile -*-
#
# The files implements the release pipeline interface
# It defines a target for each task executed during the release pipeline
# This file shall always be found at the root of the neon source code
#
#proxy values for npm or yarn

BUILD_IMAGE=quay.io/checkmateadmin/lanterns-gradle:latest

#This wraps the execution of yarn task into docker for local emulation of ci context
wrapper = docker run --user root --rm -it -v ${PWD}:/data/release/pipelines $(BUILD_IMAGE)

ifeq ($(CI),true)
	wrapper =
endif

.DEFAULT_GOAL := tasks.list

tasks.list:	##@ List available tasks on this project
	@grep -E '^[a-zA-Z\.\-]+:.*?##@ .*$$' $(MAKEFILE_LIST) | awk 'BEGIN {FS = ":.*?##@ "}; {printf "\033[36m%-30s\033[0m %s\n", $$1, $$2}'



#target naming structure. object_noun.verbe_applied
#examples:
# unit.test: unit is name of the object, test is the verb applied on object unit
# release.fingerprint: release is the object, fingerprint is the verb


#installing all dependencies and compiling project
code.compile: ##@ compile and install dependencies
	$(wrapper) ./gradlew assemble --stacktrace

#checkstyles analysis
code.lint: ##@ static analysis for code style violations
	$(wrapper) ./gradlew check

#unit testing
unit.test: ##@ run unit test suite against source
	$(wrapper) ./gradlew test

release.package: ##@ create a versionned release package
	$(wrapper) tar -czvf $(package_name).tar.gz src vars resources doc

doc.update: ##@ update documentation
	doc/aggregate-doc.sh || true
	@echo "<sup> `date --utc`: `git show HEAD | head -n1 | sed -e 's/commit //g'` ($(context) - desktop) </sup>" >> doc/jarvis-api.md

doc.publish: ##@ publish updated documentation
	git add doc/jarvis-api.md
	git commit -am 'update docucmentation'
	git push origin HEAD:master
