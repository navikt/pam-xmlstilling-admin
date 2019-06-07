package no.nav.pam.xmlstilling.admin.dao

import kotliquery.HikariCP
import kotliquery.queryOf
import kotliquery.sessionOf
import kotliquery.using
import java.io.InputStreamReader

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

val loadTestData = {
    using(sessionOf(HikariCP.dataSource())) { session ->
        session.run(queryOf(insertStillingBatchSql, 1, "aditro", Leverandor.ADITRO.xml(), "2018-01-11", null, "0", "Coop Nordland").asUpdate)
        session.run(queryOf(insertStillingBatchSql, 2, "webcruiter", Leverandor.WEBCRUITER.xml(), "2018-01-23", null, "0", "Evje og Hornnes kommune").asUpdate)
        session.run(queryOf(insertStillingBatchSql, 3, "hrmanager", Leverandor.HRMANAGER.xml(), "2018-01-23", null, "0", "UIO").asUpdate)
        session.run(queryOf(insertStillingBatchSql, 4, "karriereno", Leverandor.KARRIERENO.xml(), "2018-01-23", null, "0", "NAV").asUpdate)
        session.run(queryOf(insertStillingBatchSql, 5, "webcruiter", Leverandor.WEBCRUITER.xml(), "2018-01-30", null, "-1", "Krydder og karri").asUpdate)
        session.run(queryOf(insertStillingBatchSql, 6, "globesoft", Leverandor.GLOBESOFT.xml(), "2018-01-31", null, "0", "Buddy").asUpdate)
        session.run(queryOf(insertStillingBatchSql, 7, "stepstone", Leverandor.STEPSTONE.xml(), "2018-02-02", null, "0", "Buddy").asUpdate)
        session.run(queryOf(insertStillingBatchSql, 8, "webcruiter", Leverandor.WEBCRUITER_UPDATE.xml(), "2018-02-03", null, "0", "Buddy").asUpdate)

    }
}

private object XmlTestFiles {

    private var FILE_CONTENT: Map<Leverandor, String>

    init {
        FILE_CONTENT =
            Leverandor.values()
                    .map { it to InputStreamReader(this.javaClass.getResourceAsStream("/xml/example-" + it.name.toLowerCase() + ".xml")).readText() }
                    .toMap()
    }

    fun get(leverandor: Leverandor): String {
        return FILE_CONTENT.get(leverandor).orEmpty()
    }
}

enum class Leverandor {
    GLOBESOFT,
    HRMANAGER,
    JOBBNORGE,
    KARRIERENO,
    ADITRO,
    STEPSTONE,
    WEBCRUITER,
    WEBCRUITER_UPDATE,;

    fun xml(): String = XmlTestFiles.get(this)
}