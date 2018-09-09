#!/usr/bin/env groovy

/**
 * Launch `alfred project secrets import` and imports all needed secrets
 */
def call() {
    alfredAuthenticate()
    sh "alfred project secrets import"
}
