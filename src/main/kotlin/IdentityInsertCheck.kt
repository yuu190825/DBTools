import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException
import java.sql.Statement
import java.util.logging.Level
import java.util.logging.Logger

class IdentityInsertCheck(dbUrl: String, dbName: String, dbUser: String, dbPass: String,
                          tabNameList: MutableSet<String>) {

    // Parameter Value
    private val dbUrl: String
    private val dbName: String
    private val dbUser: String
    private val dbPass: String
    private val tabNameListIn: MutableSet<String>

    // Init Value
    private val dbConfig = DbConfig()
    private val sqlQuery = SqlQuery()
    private val logger = Logger.getLogger(Select::class.qualifiedName)

    // Data Value
    val tabNameListOut: MutableSet<String>

    init {
        this.dbUrl = dbUrl
        this.dbName = dbName
        this.dbUser = dbUser
        this.dbPass = dbPass
        this.tabNameListIn = tabNameList

        tabNameListOut = tabNameList
    }

    fun start() {

        // SQL Init
        var conn: Connection? = null
        var stmt: Statement? = null

        try {
            Class.forName(dbConfig.getJdbcDriver(2))
            conn = DriverManager.getConnection(dbConfig.getDbUrl(2, dbUrl, dbName), dbUser, dbPass)
            stmt = conn?.createStatement()
        } catch (e: Exception) { logger.log(Level.SEVERE, e.toString()) }

        for (tabName in tabNameListIn) {

            // Check IDENTITY_INSERT
            try { stmt?.executeUpdate(sqlQuery.checkIdentityInsert(tabName)) }
            catch (sqe: SQLException) {
                logger.log(Level.INFO, sqe.toString())
                tabNameListOut.remove(tabName)
            }
            // End

        }

        try {
            stmt?.close()
            conn?.close()
        } catch (sqe: SQLException) { logger.log(Level.SEVERE, sqe.toString()) }
    }
}