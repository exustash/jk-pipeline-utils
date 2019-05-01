#!/usr/bin/env groovy
import java.net.URLDecoder

/**
 * Create a jenkins based build tag
 * @param numberOfDirectories
 */
 def call(int numberOfDirectories = 2) {
  // Jenkins double encode '/' character in URL
  def decodedJobUrl = URLDecoder.decode(env.JOB_URL, "UTF-8")
  decodedJobUrl = URLDecoder.decode(decodedJobUrl, "UTF-8")
  parts = decodedJobUrl.replaceAll("/", "").split('job')

  if(parts.size() > 1) {
    numberOfDirectories = Math.min(numberOfDirectories, parts.size())
    parts = parts.takeRight(numberOfDirectories)
  }

  return parts.join('-')
}
