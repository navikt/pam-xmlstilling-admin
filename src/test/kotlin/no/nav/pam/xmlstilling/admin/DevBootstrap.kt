package no.nav.pam.xmlstilling.admin

val testEnvironment = Environment(
        xmlStillingDataSourceUrl = "jdbc:h2:mem:test;",
        username = "user",
        password = "pass"
)

fun main(args: Array<String>) {


    Bootstrap.initializeDatabase(testEnvironment)

    Bootstrap.start(webApplication())

}
