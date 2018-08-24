#!/bin/bash -e
is_tagged=''

#pull all published tags.
git fetch origin --tag

#get the current commit shot sha.
commit_sha=$2

#check if this commit has already an associated tag.
if [ ! -z $(git tag --contains ${commit_sha} | grep ${commit_sha}) ]; then
  is_tagged=$(git tag --contains ${commit_sha} | grep ${commit_sha})
fi

#get the tag of latest releases
latest_tag=$(git describe --tags $(git rev-list --tags --max-count=1))

if [ -z "$is_tagged" ]; then
  #get the counter
  IFS='.' read -r -a tag_parts <<< "$latest_tag"

  #iterate the counter
  counter="${tag_parts[1]}"
  counter_plus_one=$[$counter+1]

  #create new tag with counter's new values
  release_tag="$1.$counter_plus_one.$commit_sha"

else
  release_tag=$latest_tag
fi

echo $release_tag
