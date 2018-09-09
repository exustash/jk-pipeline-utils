#!/usr/bin/env groovy

/**
 * Retrieve a pypi config map
 * @return Map configuration
 */
Map call() {
    return [
        prod_index: "https://pypi.dm.gg/dm/prod",
        sandbox_index: "https://pypi.dm.gg/dm/sandbox"
    ]
}
