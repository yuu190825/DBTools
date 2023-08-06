import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException
import java.sql.Statement
import java.util.logging.Level
import java.util.logging.Logger

class InsertInto(dbType: Byte, dbUrl: String, dbName: String, dbUser: String, dbPass: String,
                 sqlStringList: MutableList<String>): Thread() {

    // Parameter Value
    private val dbType: Byte
    private val dbUrl: String
    private val dbName: String
    private val dbUser: String
    private val dbPass: String
    private val sqlStringList: MutableList<String>

    // Init Value
    private val dbConfig = DbConfig()
    private val logger = Logger.getLogger(InsertInto::class.qualifiedName)

    init {
        this.dbType = dbType
        this.dbUrl = dbUrl
        this.dbName = dbName
        this.dbUser = dbUser
        this.dbPass = dbPass
        this.sqlStringList = sqlStringList
    }

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