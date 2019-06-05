package no.nav.pam.xmlstilling.admin

import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.ContentNegotiation
import io.ktor.gson.gson
import io.ktor.response.respond
import io.ktor.routing.get
import io.ktor.routing.routing
import io.ktor.server.engine.ApplicationEngine
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import kotliquery.HikariCP
import mu.KotlinLogging
import no.nav.pam.xmlstilling.admin.Bootstrap.start
import no.nav.pam.xmlstilling.admin.dao.StillingBatch
import no.nav.pam.xmlstilling.admin.platform.health
import java.time.LocalDateTime

fun main(args: Array<String>) {

    Bootstrap.initializeDatabase(Environment())
    start(webApplication())

}

fun webApplication(port: Int = 9024, batch: StillingBatch = StillingBatch()): ApplicationEngine {
    return embeddedServer(Netty, port) {
        install(ContentNegotiation) {
            gson {
                setPrettyPrinting()
            }
        }
        routing {
            health()
            get("search/{from}/{to}/{searchtext?}") {
                call.respond(batch.search(
                        LocalDateTime.parse(call.parameters["from"] + "T00:00:00"),
                        LocalDateTime.parse(call.parameters["to"] + "T23:59:59"),
                        call.parameters["searchtext"]
                        ))
            }
        }
    }
}

object Bootstrap {

    private val log = KotlinLogging.logger { }

    fun initializeDatabase(env: Environment) {
        log.debug("Initializing database connection pool")
        HikariCP.default(env.xmlStillingDataSourceUrl, env.username, env.password)
    }

    fun start(webApplication: ApplicationEngine) {
        log.debug("Starting weg application")
        webApplication.start(wait = true)
    }
}