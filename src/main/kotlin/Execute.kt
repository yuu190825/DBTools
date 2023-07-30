import java.util.logging.Level
import java.util.logging.Logger

// Main Config
private const val DEFAULT_MS_DB_NAME = "test"

// SQL Config from SQL Server
private const val MS_DB_URL_A = "jdbc:sqlserver://0.0.0.0:1433;encrypt=false;databaseName=test2"
private const val MS_DB_URL_B = "jdbc:sqlserver://0.0.0.0:1433;encrypt=false;databaseName=test"

class Execute(fromDBType: Byte, toDBType: Byte, func: Byte, record: Short, tabName: String, from: Long, to: Long) {

    // Parameter Value
    private val fromDBType: Byte
    private val toDBType: Byte
    private val func: Byte
    private val record: Short
    private val tabName: String
    private val from: Long
    private val to: Long

    // Init Value
    private val logger: Logger

    init {
        this.fromDBType = fromDBType
        this.toDBType = toDBType
        this.func = func
        this.record = record
        this.tabName = tabName
        this.from = from
        this.to = to

        logger = Logger.getLogger(Execute::class.qualifiedName)

        println("$tabName from $from to $to")
    }

    fun doTransfer(mode: Byte, dbName: String) {

        // Data Value
        val sqlStringPackages = mutableListOf<MutableList<String>>()
        val sqlStringCreateList = mutableListOf<SqlStringCreate>()
        val insertIntoList = mutableListOf<InsertInto>()
        val sqlFileWriterList = mutableListOf<SqlFileWriter>()

        // Select
        val selectA = Select(fromDBType, func, 1, record, MS_DB_URL_A, tabName, from, to)
        
        selectA.start()

        try { selectA.join() } catch (ie: InterruptedException) { logger.log(Level.SEVERE, ie.toString()) }
        // End

        // SqlStringCreate
        print("Running SqlStringCreate...")
        for (clValuePackage in selectA.colValuePackages)
            sqlStringCreateList.add(SqlStringCreate(tabName, selectA.colNameList, clValuePackage))

        for (sqlStringCreateThread in sqlStringCreateList) sqlStringCreateThread.start()

        println("...")
        for (sqlStringCreateThread in sqlStringCreateList) {
            try { sqlStringCreateThread.join() } catch (ie: InterruptedException) {
                logger.log(Level.SEVERE, ie.toString())
            }

            sqlStringPackages.add(sqlStringCreateThread.sqlStringList)
        }
        // End

        if (mode.toInt() == 2) {

            // InsertInto
            print("Running InsertInto...")
            for (sqlStringListPackage in sqlStringPackages)
                if (dbName.isNotEmpty()) insertIntoList.add(InsertInto(dbName, sqlStringListPackage))
                else insertIntoList.add(InsertInto(DEFAULT_MS_DB_NAME, sqlStringListPackage))

            for (insertIntoThread in insertIntoList) insertIntoThread.start()

            println("...")
            for (insertIntoThread in insertIntoList) {
                try { insertIntoThread.join() } catch (ie: InterruptedException) {
                    logger.log(Level.SEVERE, ie.toString())
                }
            }
            // Ene

        } else {

            // SqlFileWriter
            print("Running SqlFileWriter...")
            for (i in 0 ..< sqlStringPackages.size)
                sqlFileWriterList.add(SqlFileWriter(tabName, from, to, i + 1,sqlStringPackages[i]))

            for (sqlFileWriterThread in sqlFileWriterList) sqlFileWriterThread.start()

            println("...")
            for (sqlFileWriterThread in sqlFileWriterList) {
                try { sqlFileWriterThread.join() } catch (ie: InterruptedException) {
                    logger.log(Level.SEVERE, ie.toString())
                }
            }
            // End

        }
    }

    fun doCompare() {

        // Select
        val selectA = Select(fromDBType, func, 1, record, MS_DB_URL_A, tabName, from, to)
        val selectB = Select(toDBType, func, 2, record, MS_DB_URL_B, tabName, from, to)

        selectA.start()
        selectB.start()

        try {
            selectA.join()
            selectB.join()
        } catch (ie: InterruptedException) { logger.log(Level.SEVERE, ie.toString()) }
        // End

        // Compare
        val compare = Compare(selectA.colValueListsA, selectB.colValueListsB)
        compare.go()
        // End

        // Writer
        CsvFileWriter(tabName, compare.notInDBA, compare.addInDBA, compare.xorInDBA).go()
        // End

    }
}