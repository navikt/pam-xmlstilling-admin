package no.nav.pam.xmlstilling.admin.dao

import kotliquery.*
import kotliquery.action.ListResultQueryAction
import mu.KotlinLogging
import java.time.LocalDate
import java.time.LocalDateTime

private val searchStillingBatchSql = """
    select *
    from stilling_batch
    where mottatt_dato >= ? and mottatt_dato <= ?
    and (lower(ekstern_bruker_ref) like ? or lower(arbeidsgiver) like ?)
""".trimIndent()

class StillingBatch (
        private val searchQuery: String = searchStillingBatchSql
) {
    private val log = KotlinLogging.logger {}

    data class StillingBatchEntry (
          val stillingBatchId: Int,
          val eksternBrukerRef: String,
          val stillingXml: String,
          val mottattDato: LocalDateTime,
          val behandletDato: LocalDate?,
          val behandletStatus: String?,
          val arbeidsgiver: String?
    )

    val toStillingBatchEntry: (Row) -> StillingBatchEntry = {
        StillingBatchEntry(
                stillingBatchId = it.int("stilling_batch_id"),
                eksternBrukerRef = it.string("ekstern_bruker_ref"),
                stillingXml = it.string("stilling_xml"),
                mottattDato = it.localDateTime("mottatt_dato"),
                behandletDato = it.localDateOrNull("behandlet_dato"),
                behandletStatus = it.stringOrNull("behandlet_status"),
                arbeidsgiver = it.stringOrNull("arbeidsgiver")
        )
    }

    fun getQuery(mottattFom: LocalDateTime, mottattTom: LocalDateTime, searchstring: String): ListResultQueryAction<StillingBatchEntry> {
        return queryOf(searchQuery, mottattFom, mottattTom, searchstring, searchstring).map(toStillingBatchEntry).asList
    }

    fun search(mottattFom: LocalDateTime, mottattTom: LocalDateTime, searchstring: String): List<StillingBatchEntry> {
        return using(sessionOf(HikariCP.dataSource())) {
            return@using it.run(getQuery(mottattFom, mottattTom, "%" + searchstring.toLowerCase() + "%"))
        }
    }
}