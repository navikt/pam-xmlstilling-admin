package no.nav.pam.xmlstilling.admin

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
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
import mu.KotlinLogging
import no.nav.pam.xmlstilling.admin.Bootstrap.start
import no.nav.pam.xmlstilling.admin.dao.StillingBatch
import no.nav.pam.xmlstilling.admin.platform.health
import java.time.LocalDateTime
import javax.sql.DataSource

fun main(args: Array<String>) {
    val ds = Bootstrap.initializeDatabase(Environment())
    start(webApplication(batch = StillingBatch(ds)))
}

fun webApplication(port: Int = 9024, batch: StillingBatch): ApplicationEngine {
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

    fun initializeDatabase(env: Environment): DataSource {
        log.debug("Initializing database connection pool")
        val config = HikariConfig()
        config.jdbcUrl = env.xmlStillingDataSourceUrl
        config.username = env.username
        config.password = env.password
        config.maximumPoolSize = 5
        config.minimumIdle = 1
        return HikariDataSource(config)
    }

    fun start(webApplication: ApplicationEngine) {
        log.debug("Starting weg application")
        webApplication.start(wait = true)
    }
}