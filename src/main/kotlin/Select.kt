import java.sql.*
import java.util.logging.Level
import java.util.logging.Logger

class Select(dbType: Byte, dbUrl: String, dbSid: String, dbName: String, dbUser: String, dbPass: String, func: Byte,
             step: Byte, record: Short, tabName: String, from: Long, to: Long): Thread() {

    // Parameter Value
    private val dbType: Byte
    private val dbUrl: String
    private val dbSid: String
    private val dbName: String
    private val dbUser: String
    private val dbPass: String
    private val func: Byte
    private val step: Byte
    private val record: Short
    private val tabName: String
    private val from: Long
    private val to: Long

    // Init Value
    private val dbConfig = DbConfig()
    private val sqlQuery = SqlQuery()
    private val logger = Logger.getLogger(Select::class.qualifiedName)

    // Data Value
    val colNameList = mutableSetOf<String>()
    private val toDeleteColNameList = mutableSetOf<String>()
    var colValueListsA = mutableListOf<MutableList<Any?>>()
    val colValueListsB = mutableListOf<MutableList<Any?>>()
    val colValuePackages = mutableListOf<MutableList<MutableList<Any?>>>()

    init {
        this.dbType = dbType
        this.dbUrl = dbUrl
        this.dbSid = dbSid
        this.dbName = dbName
        this.dbUser = dbUser
        this.dbPass = dbPass
        this.func = func
        this.step = step
        this.record = record
        this.tabName = tabName
        this.from = from
        this.to = to
    }

    override fun run() {

        // SQL Init
        var conn: Connection? = null
        var stmt: Statement? = null
        var rs: ResultSet? = null

        try {
            Class.forName(dbConfig.getJdbcDriver(dbType))
            conn = if (dbType.toInt() == 1)
                DriverManager.getConnection(dbConfig.getDbUrl(dbType, dbUrl, dbSid), dbUser, dbPass)
            else DriverManager.getConnection(dbConfig.getDbUrl(dbType, dbUrl, dbName), dbUser, dbPass)

            stmt = conn.createStatement()

            // SELECT COLUMN_NAME FROM TABLE
            println("Getting COLUMN_NAME...")

            rs = stmt.executeQuery(sqlQuery.getSelectColumnNameQuery(dbType, dbName, tabName))

            while (rs.next()) colNameList.add(rs.getString("COLUMN_NAME"))
            // End

            // Check DataType
            println("Checking DataType...")

            rs = stmt.executeQuery(sqlQuery.getSelectOneQuery(dbType, tabName))

            while (rs.next()) {
                val metadata = rs.metaData
                for (i in 1 .. metadata.columnCount) if (metadata.getColumnTypeName(i).equals("BLOB") ||
                    metadata.getColumnTypeName(i).equals("timestamp") ||
                    metadata.getColumnTypeName(i).equals("varbinary"))
                    toDeleteColNameList.add(metadata.getColumnName(i))
            }

            for (colName in toDeleteColNameList) colNameList.remove(colName)
            // End

            // SELECT * FROM TABLE
            println("Getting COLUMN_VALUE...")

            rs = stmt.executeQuery(
                sqlQuery.getSelectAllQuery(dbType, tabName, colNameList.elementAt(0), from, to))

            while (rs.next()) {
                val colValueList = mutableListOf<Any?>()
                for (colName in colNameList) {
                    if (func.toInt() == 1) {
                        try { colValueList.add(rs.getTimestamp(colName)) }
                        catch (e: Exception) {
                            colValueList.add(rs.getString(colName).replace("'", "''")) } // '
                    } else {
                        if (rs.getObject(colName) == null) colValueList.add("NULL")
                        else {
                            try { colValueList.add(rs.getTimestamp(colName)) }
                            catch (e: Exception) { colValueList.add(rs.getString(colName)) }
                        }
                    }
                }
                if (func.toInt() == 1) colValueListsA.add(colValueList)
                else { if (step.toInt() == 1) colValueListsA.add(colValueList) else colValueListsB.add(colValueList) }
                if (func.toInt() == 1 && colValueListsA.size.toShort() == record) {
                    colValuePackages.add(colValueListsA)
                    colValueListsA = mutableListOf()
                }
            }
            if (func.toInt() == 1 && colValueListsA.isNotEmpty()) colValuePackages.add(colValueListsA)
            // End

        } catch (e: Exception) { logger.log(Level.SEVERE, e.toString())
        } finally {
            try {
                rs?.close()
                stmt?.close()
                conn?.close()
            } catch (sqe: SQLException) { logger.log(Level.SEVERE, sqe.toString()) }
        }
    }
}