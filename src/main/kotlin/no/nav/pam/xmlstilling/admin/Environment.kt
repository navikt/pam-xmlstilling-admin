package no.nav.pam.xmlstilling.admin

data class Environment(
        val xmlStillingDataSourceUrl: String = getEnvVar("XMLSTILLING_DB_URL"),
        val dbName: String = getEnvVar("XMLSTILLING_DB_NAME"),
        val mountPath: String = getEnvVar("VAULT_MOUNT_PATH")

)

fun getEnvVar(varName: String, defaultValue: String? = null) =
        System.getenv(varName) ?: defaultValue ?: throw RuntimeException("Missing required variable \"$varName\"")
