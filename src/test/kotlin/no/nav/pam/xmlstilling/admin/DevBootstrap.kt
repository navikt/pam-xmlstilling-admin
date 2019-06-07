package no.nav.pam.xmlstilling.admin

import kotliquery.HikariCP
import no.nav.pam.xmlstilling.admin.dao.StillingBatch
import no.nav.pam.xmlstilling.admin.dao.createDatabase
import no.nav.pam.xmlstilling.admin.dao.loadTestData
import javax.sql.DataSource

fun initializeDatabase(): DataSource {
    HikariCP.default("jdbc:h2:mem:test;", "user", "pass")
    createDatabase()
    loadTestData()
    return HikariCP.dataSource()
}

fun main(args: Array<String>) {
    Bootstrap.start(webApplication(batch = StillingBatch(initializeDatabase())))
}
