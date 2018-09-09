#!/usr/bin/env groovy

/**
 * Retrieve secrets from Vault
 * @param  robotName [description]
 * @return           [description]
 */
Map call(String secretPath) {
  List secrets = [
    [
      $class: "VaultSecret",
      path: "secret/${secretPath}",
      secretValues: [
        [$class: "VaultSecretValue", envVar: "SECRET_USER", vaultKey: "username"],
        [$class: "VaultSecretValue", envVar: "SECRET_PASSWD", vaultKey: "token"]
      ]
    ]
  ]
  return secrets
}
