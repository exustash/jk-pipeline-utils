#!/usr/bin/env groovy

/**
 * Create a jenkins based build tag
 * @param numberOfDirectories
 */
 def call(int numberOfDirectories = 2) {
  // Jenkins double encode '/' character in URL
  def decodedJobUrl = URLDecoder.decode(env.JOB_URL, "UTF-8")
  decodedJobUrl = URLDecoder.decode(decodedJobUrl, "UTF-8")
  parts = decodedJobUrl.replaceAll("/", "").split('job')

  if (parts.size() > 1) {
    numberOfDirs = Math.min(numberOfDirectories, parts.size())
    parts = parts.takeRight(numberOfDirs)
  }

  return parts.join('-')
}
