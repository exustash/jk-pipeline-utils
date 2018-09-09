#!/usr/bin/env groovy

/** Push docker image to Docker Hub + Quay.io
 * That function needs the configuration file .alfred/docker.yaml
 *
 * @param dockerContext master|slave
 * @param imageNameFrom Source docker image name. e.g. "westeros-gbased"
 * @param imageNameTo Target docker image name. e.g. "westeros-gbased"
 * @param config Configuration mapping which allow to add some behavior
 */
import java.util.HashMap
import net.sf.json.JSONObject

import java.io.BufferedReader
import java.io.DataOutputStream

import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.Charset

import net.sf.json.JSONObject
import groovy.json.JsonSlurperClassic

import com.dailymotion.alfred.AlfredConfig

/**
 * Push an image to relevant registry
 * @param  dockerHost
 * @param  sourceImage
 * @param  targetImage
 * @param  config Branch to Image mapping
 */
Void call(String dockerContext, String sourceImage, String targetImage, Map config) {
    AlfredConfig alfred = getAlfredConfig()
    String owner = alfred.docker().getQuayioOwner()

    List imagesMigratedOnQuay = ["ldap2github", "stingray-consul", "daily-ubuntu", "nettools"]

    useDockerHost(dockerContext) {
        Boolean isImageMigratedOnQuay = imagesMigratedOnQuay.contains(targetImage.split(':')[0])

        if (isImageMigratedOnQuay) {
            println "---> Push to DockerHub disabled for image ${targetImage}"
            applyAlfredPermsOnQuay()
            pushToQuay(sourceImage, targetImage, owner)
        } else {
            println "---> Pushing image ${targetImage} to DockerHub disabled for image"
            pushToDockerHub(sourceImage, targetImage)
        }
        Boolean isRepo = isRepoOnQuay(imageNameTo.split(':')[0])
        if (isRepo) {
          applyAlfredPermsOnQuay()
          pushToQuay(imageNameFrom, imageNameTo, owner)
        } else {
          pushToQuay(imageNameFrom, imageNameTo, owner)
          applyAlfredPermsOnQuay()
        }

        if (config instanceof Map && config.size() > 0) {
            println "---> Pushing mapped image ${targetImage} to registries disabled for image"
            pushMappedImageToRegistry(config)
        } else {
            println "---> There is no config for .alfred/docker.yaml"
        }
    }
}

Void call(String sourceImage, String targetImage, Map config) {
    call("master", sourceImage, targetImage, config)
}

Void call(String sourceImage, String targetImage) {
    call("master", sourceImage, targetImage, [:])
}

Void call(String imageName, Map config) {
    call("master", imageName, imageName, config)
}

Void call(String imageName) {
    call("master", imageName, imageName, [:])
}

/**
 * Retrieve secrets from Vault
 * @param  robotName [description]
 * @return           [description]
 */
Map vaultCredentials(String robotName) {
  Map secrets = [
    [
      $class: "VaultSecret",
      path: "secret/jenkins/quay.io/robots/${robotName}",
      secretValues: [
        [$class: "VaultSecretValue", envVar: "QUAY_USER", vaultKey: "username"],
        [$class: "VaultSecretValue", envVar: "QUAY_PASSWD", vaultKey: "token"]
      ]
    ]
  ]
  return secrets
}

/**
 * Push a container image to Dockerhub registry
 * @param  sourceImage [description]
 * @param  targetImage   [description]
 * @return               [description]
 */
Void pushToDockerHub(String sourceImage, String targetImage) {
    useDockerRegistry {
        println "--> Pushing ${targetImage} to DockerHub"
        sh "docker tag ${sourceImage} dailymotion/${targetImage}"
        docker.image("dailymotion/${targetImage}").push()
    }
}

/**
 * Push a container image to Quay.io registry
 * @param  sourceImage
 * @param  targetImage
 * @param  robotName
 */
Void pushToQuay(String sourceImage, String targetImage, String robotName) {
    wrap([$class: "VaultBuildWrapper", vaultSecrets: vaultCredentials(robotName)]) {
        echo "Pushing ${targetImage} to Quay.io"
        sh 'docker login -u=\"${QUAY_USER}\" -p=\"${QUAY_PASSWD}\" quay.io'
        sh "docker tag ${sourceImage} quay.io/dailymotionadmin/${targetImage}"
        sh "docker push quay.io/dailymotionadmin/${targetImage}"
    }
}

/**
 * [pushMappedImageToRegistry description]
 * @param  config [description]
 * @return        [description]
 */
Void pushMappedImageToRegistry(Map config) {
    def branchesToImages = config.get('branches_to_images')
    if (branchesToImages instanceof Map && branchesToImages.size() > 0) {
        branchesToImages.each { branchName, imageName ->
            if (isBranch(branchName)) {
                println "--> Push images as the branch is equals to ${branchName}"
                if (isImageMigratedOnQuay) {
                    pushToQuay(sourceImage, imageName, owner)
                } else {
                    println "--> Push to DockerHub disabled for image ${targetImage}"
                    pushToDockerHub(sourceImage, imageName)
                }
            } else {
                println "--> The branch is not equals to ${branchName}"
            }
        }
        echo "Pushing ${imageNameTo} to Quay.io"
        int quayStatusCode = 0
        String quayError = ""

        quayStatusCode = sh (returnStatus:true, script:'docker login -u=\"${QUAY_USER}\" -p=\"${QUAY_PASSWD}\" quay.io > loginToQuay.txt')
        if (quayStatusCode != 0) {
          quayErrorString = parseCommandLogFile('loginToQuay.txt')
          postQuayFailEventToDatadog(imageNameTo, quayError, 'login')
        }
        cleanLogFile('loginToQuay.txt')

        sh "docker tag ${imageNameFrom} quay.io/dailymotionadmin/${imageNameTo}"

        quayStatusCode = sh (returnStatus:true, script: "docker push quay.io/dailymotionadmin/${imageNameTo} > pushToQuay.txt")
        if (quayStatusCode != 0) {
          quayErrorString = parseCommandLogFile('pushToQuay.txt')
          postQuayFailEventToDatadog(imageNameTo, quayError, 'push')
        }
        cleanLogFile('pushToQuay.txt')
    }
}

private Void cleanLogFile(String commandReturnFile) {
  sh "rm ${commandReturnFile}"
}

private String parseCommandLogFile (String commandReturnFile) {
  String errorString = ""
  List commandReturnFileLines = readFile(commandReturnFile).split("\n")
  commandReturnFileLines.each {
    if (it != null) {
      errorString = it
    }
  }
  return errorString
}

private void postQuayFailEventToDatadog(String quayRepository, String quayError, String quayAction) {
    wrap([$class: "VaultBuildWrapper", vaultSecrets: vaultDatadogCredentials()]) {
        def token = "${DATADOG_TOKEN_API}"
        def quayPayload = createPayload(quayRepository, quayError, quayAction)
        postHttpDatdog(quayPayload, token)
    }
}

private def vaultDatadogCredentials() {
  def secrets = [
    [
      $class: "VaultSecret",
      path: "secret/jenkins/datadog/dailymotion_token",
      secretValues: [
        [$class: "VaultSecretValue", envVar: "DATADOG_TOKEN_API", vaultKey: "token"]
      ]
    ]
  ]
  return secrets
}

private JSONObject createPayload(String quayRepository, String quayError, String quayAction) {
    JSONObject payload = new JSONObject()
    payload.put("event_type", "build result")
    String hostname = "ci.dm.gg"
    String buildurl = "https://ci.dm.gg"
    String job = quayAction + " to Quay"

    long timestamp = System.currentTimeMillis()/1000
    String message = ""

    StringBuilder title = new StringBuilder()
    title.append(job)
    title.append(" FAILED")
    payload.put("alert_type", "failure")
    message = quayError
    title.append(" for ").append(quayRepository)

    // Build payload
    payload.put("title", title.toString())
    payload.put("text", message)
    payload.put("date_happened", timestamp)
    payload.put("host", hostname)
    payload.put("result", "failure")
    payload.put("tags", "quay")
    payload.put("source_type_name", "jenkins")

    return payload
}

private Boolean postHttpDatdog(JSONObject payload, String apiKey) {
  String urlParameters = "?api_key=" + apiKey
  HttpURLConnection conn = null

  URL url = new URL("https://app.datadoghq.com/api/" + "v1/events" + urlParameters)
  conn = url.openConnection()
  conn.setRequestMethod("POST")
  conn.setRequestProperty("Content-Type", "application/json")
  conn.setUseCaches(false)
  conn.setDoInput(true)
  conn.setDoOutput(true)
  OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream(), "utf-8")
  wr.write(payload.toString())
  wr.close()
  conn.connect()
  def json = parseJSON(conn.content.text)
  if ("ok".equals(json.status)) {
    println "API call of type events was sent successfully!"
    println "Payload is: $payload"
    return true
  } else {
    println "API call of type events failed."
    println "Payload is: $payload"
    return false
  }
  if (conn.getResponseCode() == 403) {
    println "Hmmm, your API key may be invalid. We received a 403 error."
    return false
  } else {
    println "Client error"
    return false
  }
  if (conn != null) {
    conn.disconnect()
  }
  return true
}

private def parseJSON(json) {
    return new JsonSlurperClassic().parseText(json)
}

private def getQuayApplicationTokenFromVault() {
  def secret = [
    [
      $class: "VaultSecret",
      path: "secret/jenkins/quay.io/application/teamRights",
      secretValues: [
            [$class: "VaultSecretValue", envVar: "QUAY_APPLICATION_TOKEN", vaultKey: "token"]
      ]
    ]
  ]
  return secret
}

Boolean isRepoOnQuay(String repository) {
    String result= ""
    wrap([$class: "VaultBuildWrapper", vaultSecrets: getQuayApplicationTokenFromVault()]) {
      result = sh (returnStdout: true, script: "/usr/sbin/quaycli -f -n dailymotionadmin -t $QUAY_APPLICATION_TOKEN repo check ${repository}")
    }
    return result.toBoolean()
}
