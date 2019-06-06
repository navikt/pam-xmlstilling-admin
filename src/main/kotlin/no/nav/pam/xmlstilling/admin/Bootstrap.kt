package no.nav.pam.xmlstilling.admin

import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializer
import com.zaxxer.hikari.HikariConfig
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
import no.nav.vault.jdbc.hikaricp.HikariCPVaultUtil
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
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
                registerTypeAdapter(LocalDateTime::class.java, JsonSerializer<LocalDateTime> { localDateTime, _, _ ->
                    JsonPrimitive(DateTimeFormatter.ISO_INSTANT.format(localDateTime.atOffset(ZoneOffset.UTC).toInstant()))
                })
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
        config.maximumPoolSize = 5
        config.minimumIdle = 1
        return HikariCPVaultUtil.createHikariDataSourceWithVaultIntegration(
                config,
                env.mountPath,
                String.format("%s-readonly", env.dbName)
        )
    }

    fun start(webApplication: ApplicationEngine) {
        log.debug("Starting weg application")
        webApplication.start(wait = true)
    }
}