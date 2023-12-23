import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException
import java.sql.Statement
import java.util.logging.Level
import java.util.logging.Logger

class InsertInto(

    // Parameter Value
    private val dbType: Byte,
    private val dbUrl: String,
    private val dbName: String,
    private val dbUser: String,
    private val dbPass: String,
    private val sqlStringList: MutableList<String>

): Thread() {

    // Init Value
    private val dbConfig = DbConfig()
    private val logger = Logger.getLogger(InsertInto::class.qualifiedName)

    override fun run() {

        // SQL Init
        var conn: Connection? = null
        var stmt: Statement? = null

        try {
            Class.forName(dbConfig.getJdbcDriver(dbType))
            conn = DriverManager.getConnection(dbConfig.getDbUrl(dbType, dbUrl, dbName), dbUser, dbPass)
            stmt = conn?.createStatement()
        } catch (e: Exception) { logger.log(Level.SEVERE, e.toString()) }

        for (sqlString in sqlStringList) {
            try { stmt?.executeUpdate(sqlString) } // INSERT INTO
            catch (sqe: SQLException) { logger.log(Level.WARNING, "$sqe\n$sqlString\n") }
        }

        try {
            stmt?.close()
            conn?.close()
        } catch (sqe: SQLException) { logger.log(Level.SEVERE, sqe.toString()) }
    }
}