import java.io.BufferedWriter
import java.io.FileWriter
import java.io.IOException
import java.util.logging.Level
import java.util.logging.Logger

class SqlFileWriter(

    // Parameter Value
    private val tabName: String,
    private val from: Long,
    private val to: Long,
    private val fileNumber: Int,
    private val idInsert: Boolean,
    private val sqlStringList: MutableList<String>

): Thread() {
    private val logger = Logger.getLogger(SqlFileWriter::class.qualifiedName) // Init Value

    override fun run() {

        var bw: BufferedWriter? = null // BufferedWriter Init

        try {

            // Writer
            bw = BufferedWriter(FileWriter("${tabName}_${from}_${to}_$fileNumber.sql"))

            if (idInsert) bw.write("SET IDENTITY_INSERT $tabName ON;\n")

            for (sqlString in sqlStringList) bw.write("$sqlString\n")

            if (idInsert) bw.write("SET IDENTITY_INSERT $tabName OFF;")
            // End

        } catch (ioe: IOException) { logger.log(Level.SEVERE, ioe.toString())
        } finally { try { bw?.close() } catch (ioe: IOException) { logger.log(Level.SEVERE, ioe.toString()) } }
    }
}