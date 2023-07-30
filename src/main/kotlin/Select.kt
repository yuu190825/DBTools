import java.sql.*
import java.util.logging.Level
import java.util.logging.Logger

// SQL Config from Oracle DB
private const val ORACLE_JDBC_DRIVER = "oracle.jdbc.driver.OracleDriver"
private const val ORACLE_DB_URL = "jdbc:oracle:thin:@0.0.0.0:1521:test"
private const val ORACLE_USER = "test"
private const val ORACLE_PASS = "test"

// SQL Config from SQL Server
private const val MS_JDBC_DRIVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver"
private const val MS_USER = "test"
private const val MS_PASS = "test"

class Select(dbType: Byte, func: Byte, type: Byte, record: Short, dbName: String, tabName: String, from: Long, to: Long):Thread() {

    // Parameter Value
    private val dbType: Byte
    private val func: Byte
    private val type: Byte
    private val record: Short
    private val dbName: String
    private val tabName: String
    private val from: Long
    private val to: Long

    // Init Value
    private val logger: Logger

    // Data Value
    val colNameList = mutableListOf<String>()
    private val toDeleteColNameList = mutableListOf<String>()
    var colValueListsA = mutableListOf<MutableList<Any?>>()
    val colValueListsB = mutableListOf<MutableList<Any?>>()
    val colValuePackages = mutableListOf<MutableList<MutableList<Any?>>>()

    init {
        this.dbType = dbType
        this.func = func
        this.type = type
        this.record = record
        this.dbName = dbName
        this.tabName = tabName
        this.from = from
        this.to = to

        logger = Logger.getLogger(Select::class.qualifiedName)
    }

    override fun run() {

        // SQL Init
        var conn: Connection? = null
        var stmt: Statement? = null
        var rs: ResultSet? = null

        try {
            conn = if (func.toInt() == 1) {
                Class.forName(ORACLE_JDBC_DRIVER)
                DriverManager.getConnection(ORACLE_DB_URL, ORACLE_USER, ORACLE_PASS)
            } else {
                Class.forName(MS_JDBC_DRIVER)
                DriverManager.getConnection(dbName, MS_USER, MS_PASS)
            }

            stmt = conn.createStatement()

            // SELECT COLUMN_NAME FROM TABLE
            println("Getting COLUMN_NAME...")

            rs = stmt.executeQuery(SqlQuery().getSelectColumnNameQuery(dbType, tabName))

            while (rs.next()) colNameList.add(rs.getString("COLUMN_NAME"))
            // End

            // Check DataType
            println("Checking DataType...")

            rs = stmt.executeQuery(SqlQuery().getSelectOneQuery(dbType, tabName))

            for (i in 1 .. rs.metaData.columnCount)
                if (rs.metaData.getColumnTypeName(i).equals("BLOB") ||
                    rs.metaData.getColumnTypeName(i).equals("CLOB"))
                    toDeleteColNameList.add(rs.metaData.getColumnName(i))

            for (colName in toDeleteColNameList) colNameList.remove(colName)
            // End

            // SELECT * FROM TABLE
            println("Getting COLUMN_VALUE...")

            rs = stmt.executeQuery(SqlQuery().getSelectAllQuery(dbType, tabName, colNameList[0], from, to))

            while (rs.next()) {
                val colValueList = mutableListOf<Any?>()
                for (colName in colNameList) {
                    if (func.toInt() == 1) {
                        try { colValueList.add(rs.getTimestamp(colName))
                        } catch (e: Exception) {

                            // '
                            colValueList.add(rs.getString(colName).replace("'", "''"))
                            // End

                        }
                    } else {
                        if (rs.getObject(colName) == null) colValueList.add("NULL")
                        else {
                            try { colValueList.add(rs.getTimestamp(colName))
                            } catch (e: Exception) { colValueList.add(rs.getString(colName)) }
                        }
                    }
                }
                if (func.toInt() == 1) colValueListsA.add(colValueList)
                else { if (type.toInt() == 1) colValueListsA.add(colValueList) else colValueListsB.add(colValueList) }
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