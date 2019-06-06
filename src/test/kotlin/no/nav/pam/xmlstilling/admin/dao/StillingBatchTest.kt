package no.nav.pam.xmlstilling.admin.dao

import kotliquery.HikariCP
import kotliquery.queryOf
import kotliquery.sessionOf
import kotliquery.using
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

val createTable = """
    CREATE TABLE STILLING_BATCH (
        STILLING_BATCH_ID NUMERIC(19, 1),
        EKSTERN_BRUKER_REF VARCHAR(150),
        STILLING_XML TEXT,
        MOTTATT_DATO DATE,
        BEHANDLET_DATO DATE,
        BEHANDLET_STATUS VARCHAR(3),
        ARBEIDSGIVER VARCHAR(150),
        CONSTRAINT PK_ID PRIMARY KEY (STILLING_BATCH_ID));
""".trimIndent()

val insertStillingBatchSql = """
    INSERT INTO STILLING_BATCH (
        STILLING_BATCH_ID,
        EKSTERN_BRUKER_REF,
        STILLING_XML,
        MOTTATT_DATO,
        BEHANDLET_DATO,
        BEHANDLET_STATUS,
        ARBEIDSGIVER)
        values (?, ?, ?, ?, ?, ?, ?);
""".trimIndent()

val createDatabase = {
    using(sessionOf(HikariCP.dataSource())) {
        it.run(queryOf(createTable).asExecute)
    }
}

val loadBasicTestData = {
    using(sessionOf(HikariCP.dataSource())) { session ->
        session.run(queryOf(insertStillingBatchSql, 1, "aditro", "<xml>1</xml>", "2018-01-11", null, "0", "Coop Nordland").asUpdate)
        session.run(queryOf(insertStillingBatchSql, 2, "webcruiter", "<xml>2</xml>", "2018-01-23", null, "0", "Evje og Hornnes kommune").asUpdate)
        session.run(queryOf(insertStillingBatchSql, 3, "webcruiter", "<xml>3</xml>", "2018-01-23", null, "0", "UIO").asUpdate)
        session.run(queryOf(insertStillingBatchSql, 4, "karriereno", "<xml>4</xml>", "2018-01-23", null, "0", "NAV").asUpdate)
        session.run(queryOf(insertStillingBatchSql, 5, "webcruiter", "<xml>5</xml>", "2018-01-30", null, "-1", "Krydder og karri").asUpdate)
        session.run(queryOf(insertStillingBatchSql, 6, "webcruiter", "<xml>6</xml>", "2018-01-31", null, "0", "Buddy").asUpdate)
    }
}

class StillingBatchTest {

    companion object {
        @BeforeAll
        @JvmStatic
        fun setupMemDb() {
            HikariCP.default("jdbc:h2:mem:test", "user", "pass")
            createDatabase()
            loadBasicTestData()
        }
    }

    val startDate = LocalDateTime.of(2018, 1, 23, 0, 0, 0)
    val endDate = LocalDateTime.of(2018, 1, 30, 0, 0, 0)
    val stillingBatch = StillingBatch(HikariCP.dataSource())

    @Test
    fun testSearchWithSearchText() {
        val stillingBatchEntries= stillingBatch.search(
                startDate,
                endDate,
                "karri"
        )
        Assertions.assertThat(stillingBatchEntries.size).isEqualTo(2)
    }

    @Test
    fun testSearchWithEmptySearchText() {
        val stillingBatchEntries= stillingBatch.search(
                startDate,
                endDate,
                ""
        )
        Assertions.assertThat(stillingBatchEntries.size).isEqualTo(4)
    }

    @Test
    fun testSearchWithNoMatches() {
        val stillingBatchEntries= stillingBatch.search(
                startDate,
                endDate,
                "no_matches_for_this"
        )
        Assertions.assertThat(stillingBatchEntries.size).isEqualTo(0)
    }

    @Test
    fun testValues() {
        val stillingBatchEntries= stillingBatch.search(
                startDate,
                endDate,
                "nAv"
        )
        Assertions.assertThat(stillingBatchEntries.size).isEqualTo(1)
        val entry = stillingBatchEntries.get(0)
        Assertions.assertThat(entry.stillingXml).isEqualTo("<xml>4</xml>")
        Assertions.assertThat(entry.arbeidsgiver).isEqualTo("NAV")
        Assertions.assertThat(entry.eksternBrukerRef).isEqualTo("karriereno")
    }

    @Test
    fun testSearchWithoutText() {
        arrayOf(" ", "", null).forEach {
            val stillingBatchEntries= stillingBatch.search(
                    startDate,
                    endDate,
                    it
            )
            Assertions.assertThat(stillingBatchEntries.size).isEqualTo(4)
        }
    }
}
