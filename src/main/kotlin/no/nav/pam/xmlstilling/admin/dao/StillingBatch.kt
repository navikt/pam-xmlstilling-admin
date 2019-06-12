package no.nav.pam.xmlstilling.admin.dao

import kotliquery.*
import kotliquery.action.ListResultQueryAction
import mu.KotlinLogging
import java.time.LocalDate
import java.time.LocalDateTime
import javax.sql.DataSource

private val selectById = """
    select *
    from stilling_batch
    where stilling_batch_id = ?
""".trimIndent()

private val searchWithDatesOnlySql = """
    select *
    from stilling_batch
    where mottatt_dato >= ? and mottatt_dato <= ?
""".trimIndent()

private val searchWithTextSql = """
    select *
    from stilling_batch
    where mottatt_dato >= ? and mottatt_dato <= ?
    and (lower(ekstern_bruker_ref) like ? or lower(arbeidsgiver) like ?)
""".trimIndent()

private val searchReferenceSql = """
    select *
    from stilling_batch
    where mottatt_dato >= ? and mottatt_dato <= ?
    and lower(ekstern_id) = ? and lower(ekstern_bruker_ref) = ?
""".trimIndent()

class StillingBatch (
        private val ds: DataSource,
        private val searchWithDatesOnlyQuery: String = searchWithDatesOnlySql,
        private val searchWithTextQuery: String = searchWithTextSql,
        private val searchWithReferenceQuery: String = searchReferenceSql
) {
    private val log = KotlinLogging.logger {}

    data class StillingBatchEntry (
          val stillingBatchId: Int,
          val eksternBrukerRef: String,
          val stillingXml: String,
          val mottattDato: LocalDateTime,
          val behandletDato: LocalDate?,
          val behandletStatus: String?,
          val arbeidsgiver: String?,
          val eksternId: String?
    )

    val toStillingBatchEntry: (Row) -> StillingBatchEntry = {
        StillingBatchEntry(
                stillingBatchId = it.int("stilling_batch_id"),
                eksternBrukerRef = it.string("ekstern_bruker_ref"),
                stillingXml = it.string("stilling_xml"),
                mottattDato = it.localDateTime("mottatt_dato"),
                behandletDato = it.localDateOrNull("behandlet_dato"),
                behandletStatus = it.stringOrNull("behandlet_status"),
                arbeidsgiver = it.stringOrNull("arbeidsgiver"),
                eksternId = it.stringOrNull("ekstern_id")
        )
    }

    private fun getQuery(mottattFom: LocalDateTime, mottattTom: LocalDateTime, searchstring: String?): Query {
        if (searchstring.isNullOrBlank()) {
            return queryOf(searchWithDatesOnlyQuery, mottattFom, mottattTom)
        }
        val reference = searchstring.split('_')
        if (reference.size > 2) {
            return queryOf(searchWithReferenceQuery, mottattFom, mottattTom, reference.first(), reference.last())
        }
        val likeSearchstring = "%" + searchstring + "%"
        return queryOf(searchWithTextQuery, mottattFom, mottattTom, likeSearchstring, likeSearchstring)
    }

    fun search(mottattFom: LocalDateTime, mottattTom: LocalDateTime, searchstring: String?): List<StillingBatchEntry> {
        return using(sessionOf(ds)) {
            return@using it.run(
                    getQuery(mottattFom, mottattTom, searchstring?.toLowerCase())
                            .map(toStillingBatchEntry)
                            .asList
            )
        }
    }

    fun getSingle(id: Int): StillingBatchEntry? {
        return using(sessionOf(ds)) {
            return@using it.run(
            queryOf(selectById, id)
                    .map(toStillingBatchEntry)
                    .asSingle
            )
        }
    }
}