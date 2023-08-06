import java.util.logging.Level
import java.util.logging.Logger

class Execute(fromDbType: Byte, fromDbUrl: String, fromDbName: String, fromDbUser: String, fromDbPass: String,
              toDbType: Byte, toDbUrl: String, toDbName: String, toDbUser: String, toDbPass: String, func: Byte,
              record: Short, tabName: String, from: Long, to: Long) {

    // Parameter Value
    private val fromDbType: Byte
    private val fromDbUrl: String
    private val fromDbName: String
    private val fromDbUser: String
    private val fromDbPass: String
    private val toDbType: Byte
    private val toDbUrl: String
    private val toDbName: String
    private val toDbUser: String
    private val toDbPass: String
    private val func: Byte
    private val record: Short
    private val tabName: String
    private val from: Long
    private val to: Long

    private val logger = Logger.getLogger(Execute::class.qualifiedName) // Init Value

    // Data Value
    val colValueListsA = mutableListOf<MutableList<Any?>>()
    val colValueListsB = mutableListOf<MutableList<Any?>>()

    init {
        this.fromDbType = fromDbType
        this.fromDbUrl = fromDbUrl
        this.fromDbName = fromDbName
        this.fromDbUser = fromDbUser
        this.fromDbPass = fromDbPass
        this.toDbType = toDbType
        this.toDbUrl = toDbUrl
        this.toDbName = toDbName
        this.toDbUser = toDbUser
        this.toDbPass = toDbPass
        this.func = func
        this.record = record
        this.tabName = tabName
        this.from = from
        this.to = to

        println("$tabName from $from to $to")
    }

    fun start(mode: Byte) {

        // Data Value
        val sqlStringPackages = mutableListOf<MutableList<String>>()
        val sqlStringCreateList = mutableListOf<SqlStringCreate>()
        val insertIntoList = mutableListOf<InsertInto>()
        val sqlFileWriterList = mutableListOf<SqlFileWriter>()

        // Select
        val selectA = Select(fromDbType, fromDbUrl, fromDbName, fromDbUser, fromDbPass, func, 1, record, tabName,
            from, to)
        val selectB = Select(toDbType, toDbUrl, toDbName, toDbUser, toDbPass, func, 2, record, tabName, from, to)
        
        selectA.start()
        if (func.toInt() == 2) selectB.start()

        if (func.toInt() == 1) try { selectA.join() } catch (ie: InterruptedException) {
            logger.log(Level.SEVERE, ie.toString()) }
        else {
            try {
                selectA.join()
                selectB.join()
            } catch (ie: InterruptedException) { logger.log(Level.SEVERE, ie.toString()) }
        }
        // End

        if (func.toInt() == 1) {

            // SqlStringCreate
            print("Running SqlStringCreate...")
            for (clValuePackage in selectA.colValuePackages)
                sqlStringCreateList.add(SqlStringCreate(tabName, selectA.colNameList, clValuePackage))

            for (sqlStringCreateThread in sqlStringCreateList) sqlStringCreateThread.start()

            println("...")
            for (sqlStringCreateThread in sqlStringCreateList) {
                try { sqlStringCreateThread.join() }
                catch (ie: InterruptedException) { logger.log(Level.SEVERE, ie.toString()) }

                sqlStringPackages.add(sqlStringCreateThread.sqlStringList)
            }
            // End

            if (mode.toInt() == 2) {

                // InsertInto
                print("Running InsertInto...")
                for (sqlStringListPackage in sqlStringPackages)
                    insertIntoList.add(InsertInto(toDbType, toDbUrl, toDbName, toDbUser, toDbPass,
                        sqlStringListPackage))

                for (insertIntoThread in insertIntoList) insertIntoThread.start()

                println("...")
                for (insertIntoThread in insertIntoList) {
                    try { insertIntoThread.join() }
                    catch (ie: InterruptedException) { logger.log(Level.SEVERE, ie.toString()) }
                }
                // Ene

            } else {

                // SqlFileWriter
                print("Running SqlFileWriter...")
                for (i in 0..<sqlStringPackages.size)
                    sqlFileWriterList.add(SqlFileWriter(tabName, from, to, i + 1, sqlStringPackages[i]))

                for (sqlFileWriterThread in sqlFileWriterList) sqlFileWriterThread.start()

                println("...")
                for (sqlFileWriterThread in sqlFileWriterList) {
                    try { sqlFileWriterThread.join() }
                    catch (ie: InterruptedException) { logger.log(Level.SEVERE, ie.toString()) }
                }
                // End

            }
        } else {
            colValueListsA.addAll(selectA.colValueListsA)
            colValueListsB.addAll(selectB.colValueListsB)
        }
    }
}