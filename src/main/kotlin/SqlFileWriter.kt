import java.io.BufferedWriter
import java.io.FileWriter
import java.io.IOException
import java.util.logging.Level
import java.util.logging.Logger

class SqlFileWriter(tbName: String, from: Long, to: Long, fileNumber: Int,
                    sqlStringList: MutableList<String>): Thread() {

    // Parameter Value
    private val tabName: String
    private val from: Long
    private val to: Long
    private val fileNumber: Int
    private val sqlStringList: MutableList<String>

    // Init Value
    private val logger: Logger

    init {
        this.tabName = tbName
        this.from = from
        this.to = to
        this.fileNumber = fileNumber
        this.sqlStringList = sqlStringList

        logger = Logger.getLogger(SqlFileWriter::class.qualifiedName)
    }

    override fun run() {

        // BufferedWriter Init
        var bw: BufferedWriter? = null

        try {

            // Writer
            bw = BufferedWriter(FileWriter("${tabName}_${from}_${to}_$fileNumber.sql"))

            for (sqlString in sqlStringList) bw.write("$sqlString\n")
            // End

        } catch (ioe: IOException) { logger.log(Level.SEVERE, ioe.toString())
        } finally { try { bw?.close() } catch (ioe: IOException) { logger.log(Level.SEVERE, ioe.toString()) } }
    }
}