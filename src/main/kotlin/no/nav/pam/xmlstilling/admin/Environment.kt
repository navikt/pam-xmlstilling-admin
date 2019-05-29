package no.nav.pam.xmlstilling.admin

data class Environment(
        val xmlStillingDataSourceUrl: String = getEnvVar("XMLSTILLING_DB_URL"),
        val username: String = getEnvVar("XMLSTILLING_DB_USERNAME"),
        val password: String = getEnvVar("XMLSTILLING_DB_PASSWORD")

)

fun getEnvVar(varName: String, defaultValue: String? = null) =
        System.getenv(varName) ?: defaultValue ?: throw RuntimeException("Missing required variable \"$varName\"")
