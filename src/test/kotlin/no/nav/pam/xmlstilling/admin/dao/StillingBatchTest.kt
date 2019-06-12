package no.nav.pam.xmlstilling.admin.dao

import kotliquery.HikariCP
import org.assertj.core.api.Assertions.assertThat
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

    val firstDate = LocalDateTime.of(2018, 1, 11, 0, 0, 0)
    val startDate = LocalDateTime.of(2018, 1, 23, 0, 0, 0)
    val endDate = LocalDateTime.of(2018, 1, 30, 0, 0, 0)
    val lastDate = LocalDateTime.of(2018, 2, 3, 0, 0, 0)
    val stillingBatch = StillingBatch(HikariCP.dataSource())

    @Test
    fun testSearchWithSearchText() {
        val stillingBatchEntries= stillingBatch.search(
                startDate,
                endDate,
                "karri"
        )
        assertThat(stillingBatchEntries.size).isEqualTo(2)
    }

    @Test
    fun testSearchWithEmptySearchText() {
        val stillingBatchEntries= stillingBatch.search(
                startDate,
                endDate,
                ""
        )
        assertThat(stillingBatchEntries.size).isEqualTo(4)
    }

    @Test
    fun testSearchWithNoMatches() {
        val stillingBatchEntries= stillingBatch.search(
                startDate,
                endDate,
                "no_matches_for_this"
        )
        assertThat(stillingBatchEntries.size).isEqualTo(0)
    }

    @Test
    fun testValues() {
        val stillingBatchEntries= stillingBatch.search(
                startDate,
                endDate,
                "nAv"
        )
        assertThat(stillingBatchEntries.size).isEqualTo(1)
        val entry = stillingBatchEntries.first()
        assertThat(entry.arbeidsgiver).isEqualTo("NAV")
        assertThat(entry.eksternBrukerRef).isEqualTo("karriereno")
    }

    @Test
    fun testSearchWithoutText() {
        arrayOf(" ", "", null).forEach {
            val stillingBatchEntries= stillingBatch.search(
                    startDate,
                    endDate,
                    it
            )
            assertThat(stillingBatchEntries.size).isEqualTo(4)
        }
    }

    @Test
    fun testGetSingle() {
        val stilling = stillingBatch.getSingle(4)
        assertThat(stilling?.stillingBatchId).isEqualTo(4)
        assertThat(stilling?.eksternBrukerRef).isEqualToIgnoringCase("karriereno")
    }

    @Test
    fun testGetSingleNull() {
        val stilling = stillingBatch.getSingle(4000)
        assertThat(stilling).isNull()
    }

    @Test
    fun testGetByReference() {
        val referanse = "222_Jernia_webcruiter"
        val stillingBatchEntries= stillingBatch.search(
                firstDate,
                lastDate,
                referanse
        )
        assertThat(stillingBatchEntries.size).isEqualTo(2)
        stillingBatchEntries.forEach {
            assertThat(it.eksternBrukerRef).isEqualTo("webcruiter")
            assertThat(it.eksternId).isEqualTo("222")
        }
    }

}
