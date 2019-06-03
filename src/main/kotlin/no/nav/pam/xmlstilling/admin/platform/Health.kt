package no.nav.pam.xmlstilling.admin.platform

import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.response.respondText
import io.ktor.routing.Routing
import io.ktor.routing.get
import kotliquery.HikariCP
import kotliquery.queryOf
import kotliquery.sessionOf
import kotliquery.using

fun Routing.health(
        ready: () -> Boolean = { true },
        alive: () -> Boolean = { true }
) {

    fun statusFor(b: () -> Boolean) = b().let{ if(it) HttpStatusCode.OK else HttpStatusCode.InternalServerError}

    fun databaseConnectionOk(): String {
        try {using(sessionOf(HikariCP.dataSource())) {
            it.run(queryOf("select now()").asExecute)
        }} catch(e: Exception) {return "NOT OK"}
        return "OK"
    }

    get("isReady") {
        statusFor(ready).let { call.respondText("Ready: $it", status = it) }
    }

    get("isAlive") {
        statusFor(alive).let { call.respondText("Alive: $it", status = it) }
    }

    get("internal/selftest") {
        databaseConnectionOk().let {call.respondText { "Database $it"}}
    }

}

