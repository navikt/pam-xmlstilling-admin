package no.nav.pam.xmlstilling.admin.dao

import kotliquery.HikariCP
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class StillingBatchTest {

    companion object {
        @BeforeAll
        @JvmStatic
        fun setupMemDb() {
            HikariCP.default("jdbc:h2:mem:test", "user", "pass")
            createDatabase()
            loadTestData()
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

    @Test
    fun testGetSingle() {
        val stilling = stillingBatch.getSingle(4)
        Assertions.assertThat(stilling?.stillingBatchId).isEqualTo(4)
        Assertions.assertThat(stilling?.eksternBrukerRef).isEqualToIgnoringCase("karriereno")
    }

    @Test
    fun testGetSingleNull() {
        val stilling = stillingBatch.getSingle(4000)
        Assertions.assertThat(stilling).isNull()
    }

}
