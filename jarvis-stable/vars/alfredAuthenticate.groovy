#!/usr/bin/env groovy

/**
 * Authenticate Alfred on vault
 */
def call() {
    wrap([$class: "VaultBuildWrapper", vaultSecrets: vaultCredentials()]) {
        echo "Authenticate Alfred on vault"
        sh "alfred auth --role-id ${ALFRED_ROLE_ID} --secret-id ${ALFRED_SECRET_ID}"
    }
}

def vaultCredentials() {
  def secrets = [
    [
      $class: "VaultSecret",
      path: "secret/dev/alfred/vault",
      secretValues: [
        [$class: "VaultSecretValue", envVar: "ALFRED_ROLE_ID", vaultKey: "role_id"],
        [$class: "VaultSecretValue", envVar: "ALFRED_SECRET_ID", vaultKey: "secret_id"]
      ]
    ]
  ]
  return secrets
}
