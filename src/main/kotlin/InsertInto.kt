import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException
import java.sql.Statement
import java.util.logging.Level
import java.util.logging.Logger

// SQL_Config from SQL Server
private const val MS_JDBC_DRIVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver"
private const val MS_DB_URL = "jdbc:sqlserver://0.0.0.0:1433;encrypt=false;databaseName="
private const val MS_USER = "test"
private const val MS_PASS = "test"

class InsertInto(dbName: String, sqlStringList: MutableList<String>): Thread() {

    // Parameter_Value
    private val dbName: String
    private val sqlStringList: MutableList<String>

    // Init_Value
    private val logger: Logger

    init {
        this.dbName = dbName
        this.sqlStringList = sqlStringList

        logger = Logger.getLogger(InsertInto::class.qualifiedName)
    }

    override fun run() {

        // SQL_Init
        var conn: Connection? = null
        var stmt: Statement? = null

        try {
            Class.forName(MS_JDBC_DRIVER)
            conn = DriverManager.getConnection(MS_DB_URL + dbName, MS_USER, MS_PASS)
            stmt = conn?.createStatement()
        } catch (e: Exception) { logger.log(Level.SEVERE, e.toString()) }

        for (sqlString in sqlStringList) {
            try {

                // INSERT INTO
                stmt?.executeUpdate(sqlString)
                // End

            } catch (sqe: SQLException) { logger.log(Level.SEVERE, "$sqe\n$sqlString") }
        }

        try {
            stmt?.close()
            conn?.close()
        } catch (sqe: SQLException) { logger.log(Level.SEVERE, sqe.toString()) }
    }
}