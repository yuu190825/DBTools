import java.io.BufferedWriter
import java.io.FileWriter
import java.io.IOException
import java.util.logging.Level
import java.util.logging.Logger

class CsvFileWriter(dbName: String, tabName: String, notInDBA: MutableList<MutableList<Any?>>,
                    addInDBA: MutableList<MutableList<Any?>>, xorInDBA: MutableList<MutableList<Any?>>) {

    // Parameter Value
    private val dbName: String
    private val tabName: String
    private val notInDBA: MutableList<MutableList<Any?>>
    private val addInDBA: MutableList<MutableList<Any?>>
    private val xorInDBA: MutableList<MutableList<Any?>>

    private val logger = Logger.getLogger(CsvFileWriter::class.qualifiedName) // Init Value

    init {
        this.dbName = dbName
        this.tabName = tabName
        this.notInDBA = notInDBA
        this.addInDBA = addInDBA
        this.xorInDBA = xorInDBA
    }

    fun start() {

        var bw: BufferedWriter? = null // BufferedWriter Init

        try {

            // Writer
            bw = BufferedWriter(FileWriter("$tabName.csv"))

            bw.write("Not in $dbName:\n")
            for (i in 0 ..< notInDBA.size) {
                bw.write(notInDBA[i][0].toString())
                for (j in 1..< notInDBA[i].size) bw.write(", ${notInDBA[i][j].toString()}")
                bw.newLine()
            }

            bw.write("\nAdd in $dbName:\n")
            for (i in 0 ..< addInDBA.size) {
                bw.write(addInDBA[i][0].toString())
                for (j in 1..< addInDBA[i].size) bw.write(", ${addInDBA[i][j].toString()}")
                bw.newLine()
            }

            bw.write("\nNot equal in $dbName:\n")
            for (i in 0 ..< xorInDBA.size) {
                bw.write(xorInDBA[i][0].toString())
                for (j in 1..< xorInDBA[i].size) bw.write(", ${xorInDBA[i][j].toString()}")
                bw.newLine()
            }
            // End

        } catch (ioe: IOException) { logger.log(Level.SEVERE, ioe.toString())
        } finally { try { bw?.close() } catch (ioe: IOException) { logger.log(Level.SEVERE, ioe.toString()) } }
    }
}