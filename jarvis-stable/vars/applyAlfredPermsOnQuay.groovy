/** applyAlfredPermsOnQuay
 *
 * That function needs the configuration file .alfred/docker.yaml
 *
 */
import groovy.json.JsonSlurper
import java.util.regex.Pattern
import com.dailymotion.alfred.AlfredConfig

Void call(String imageName=null) {
    AlfredConfig alfred = getAlfredConfig()
    String prefix = 'dailymotionadmin+'
    List alfredRepositories = []
    List repos = []
     if(imageName == null) {
        List quayPermissionsByImages = alfred.docker().getQuayioPermissionsByImages()

        // Apply rights defined in Alfred configuration file
        quayPermissionsByImages.each { it ->
        if (it.role == "owner") {
            execQuayCliActionOnRights(it.image_name, prefix + it.team, "putrobot", "owner")
            alfredRepositories << ["image_name": it.image_name, "user": prefix + it.team, "role": "admin"]
            execQuayCliActionOnRights(it.image_name, prefix + it.team + "pull", "putrobot", "read")
            alfredRepositories << [ "image_name": it.image_name, "user": prefix + it.team + "pull", "role": "read"]
            execQuayCliActionOnRights(it.image_name, it.team, "put", "read")
            alfredRepositories << ["image_name": it.image_name, "user": it.team, "role": "read"]
        } else if (it.role == "reader") {
            execQuayCliActionOnRights(it.image_name, prefix + it.team + "pull", "putrobot", "read")
            alfredRepositories << ["image_name": it.image_name, "user": prefix + it.team + "pull", "role": "read"]
            execQuayCliActionOnRights(it.image_name, it.team, "put", "read")
            alfredRepositories << ["image_name": it.image_name, "user": it.team, "role": "read"]
        } else {
            error "[putQuayRepoRights] The role $it.role does not exist - aborting putQuayRepoRights"
        }
        repos << it.image_name
        }

    } else {
        def quayImagePerms = alfred.docker().getQuayioPermissionsByImage(imageName)
        quayImagePerms.each { quayImagePerm ->
        if (quayImagePerm.role == "owner") {
            execQuayCliActionOnRights(imageName, prefix + quayImagePerm.team, "putrobot", "owner")
            alfredRepositories << ["image_name": imageName, "user": prefix + quayImagePerm.team, "role": "admin"]
            execQuayCliActionOnRights(imageName, prefix + quayImagePerm.team + "pull", "putrobot", "read")
            alfredRepositories << [ "image_name": imageName, "user": prefix + quayImagePerm.team + "pull", "role": "read"]
            execQuayCliActionOnRights(imageName, quayImagePerm.team, "put", "read")
            alfredRepositories << ["image_name": imageName, "user": quayImagePerm.team, "role": "read"]
        } else if (quayImagePerm.role == "reader") {
            execQuayCliActionOnRights(imageName, prefix + quayImagePerm.team + "pull", "putrobot", "read")
            alfredRepositories << ["image_name": imageName, "user": prefix + quayImagePerm.team + "pull", "role": "read"]
            execQuayCliActionOnRights(imageName, quayImagePerm.team, "put", "read")
            alfredRepositories << ["image_name": imageName, "user": quayImagePerm.team, "role": "read"]
        } else {
            error ("[ERR!][putQuayRepoRights] The role $quayImagePerms.role does not exist - aborting putQuayRepoRights")
        }
        repos << imageName
        }
     }

    // Get rights for each repository from Quay.io
    List quayRepositories = []
    quayRepositories = getQuayReposRights(repos, quayRepositories)

    // Do not touch the special robot puller
    List whiteList = []
    repos.unique().each { repo ->
        whiteList << [ "image_name": repo, "user": prefix + "puller", "role": "read" ]
    }

    List diff = quayRepositories - alfredRepositories
    diff  = diff - whiteList
    Pattern reg = ~/^dailymotionadmin\+/
    if (diff.size() != 0) {
        Boolean isAdmin = false
        isAdmin = removeAdminRights(diff, isAdmin, reg, quayPermissionsByImages)
        if (isAdmin) {
            quayRepositories = []
            diff = []
            quayRepositories = getQuayReposRights(repos, quayRepositories)
            diff = quayRepositories - alfredRepositories
        }
    removeReadRights(diff, reg)
    }
}

/**
 * [removeReadRights description]
 * @param diff
 * @param reg
 */
private Void removeReadRights(List diff, Pattern reg) {
    diff.each { it ->
        if (it.user =~ reg) {
            execQuayCliActionOnRights(it.image_name, it.user, "delrobot", "")
        } else {
            execQuayCliActionOnRights(it.image_name, it.user, "del", "")
        }
    }
}

/**
 * [removeAdminRights description]
 * @param  diff
 * @param  isAdmin
 * @param  reg
 * @param  quayPermissionsByImages
 * @return Boolean
 */
private boolean removeAdminRights(List diff, boolean isAdmin, Pattern reg, List quayPermissionsByImages) {
    diff.each { it ->
        Boolean isReader = false
        Boolean isAdminVar = isAdmin
        if (it.role == "admin") {
            isAdminVar = true
            String tribe = it.user - reg
            quayPermissionsByImages.find { right ->
                if (right.image_name == it.image_name) {
                    if (right.team == tribe) {
                        isReader = true
                        return isReader
                    }
                }
            }
            if (isReader) {
                // Then this tribe has read rights on the repo, just remove the admin right for the robot
                execQuayCliActionOnRights(it.image_name, it.user, "delrobot", "")
            } else {
                // All the tribe rights must be removed (tribe read, tribe robot pull read and tribe robot admin)
                execQuayCliActionOnRights(it.image_name, it.user,          "delrobot", "")
                execQuayCliActionOnRights(it.image_name, it.user + "pull", "delrobot", "")
                execQuayCliActionOnRights(it.image_name, it.user - reg,    "del", "")
            }
        }
    }
    return isAdminVar
}

/**
 * [getQuayReposRights description]
 * @param  repos
 * @param  quayRepositories
 * @return List
 */
private List getQuayReposRights(List repos, List quayRepositories) {
    repos.unique().each { it ->
        String result = execQuayCliActionOnRights(it, "", "get", "")
        JsonSlurper jsonSlurper = new JsonSlurper()
        jsonSlurper.parseText(result).each { item ->
            quayRepositories << ["image_name": it, "user": item.Name, "role": item.Perm]
        }
    }
    return quayRepositories
}

/**
 * [getQuayApplicationTokenFromVault description]
 * @return [description]
 */
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

/**
 * [execQuayCliActionOnRights description]
 * @param  repository
 * @param  team
 * @param  action
 * @param  rights
 * @return String
 */
private String execQuayCliActionOnRights(String repository, String team, String action, String rights) {
    wrap([$class: "VaultBuildWrapper", vaultSecrets: getQuayApplicationTokenFromVault()]) {
        result = sh (returnStdout: true, script: "/usr/sbin/quaycli -f -n dailymotionadmin -t $QUAY_APPLICATION_TOKEN perm ${action} ${repository} ${team} ${rights}")
    }
    return result
}
