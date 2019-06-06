package no.nav.pam.xmlstilling.admin

import no.nav.pam.xmlstilling.admin.dao.StillingBatch

val testEnvironment = Environment(
        xmlStillingDataSourceUrl = "jdbc:h2:mem:test;",
        username = "user",
        password = "pass"
)

fun main(args: Array<String>) {
    //val ds = Bootstrap.initializeDatabase(testEnvironment)
    val ds = Bootstrap.initializeDatabase(Environment())

    Bootstrap.start(webApplication(batch = StillingBatch(ds)))
}
