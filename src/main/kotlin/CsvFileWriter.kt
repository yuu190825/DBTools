import java.io.BufferedWriter
import java.io.FileWriter
import java.io.IOException
import java.util.logging.Level
import java.util.logging.Logger

class CsvFileWriter(tabName: String, notInDBA: MutableList<MutableList<Any?>>, addInDBA: MutableList<MutableList<Any?>>,
                    xorInDBA: MutableList<MutableList<Any?>>) {

    // Parameter Value
    private val tabName: String
    private val notInDBA: MutableList<MutableList<Any?>>
    private val addInDBA: MutableList<MutableList<Any?>>
    private val xorInDBA: MutableList<MutableList<Any?>>

    // Init Value
    private val logger: Logger

    init {
        this.tabName = tabName
        this.notInDBA = notInDBA
        this.addInDBA = addInDBA
        this.xorInDBA = xorInDBA

        logger = Logger.getLogger(CsvFileWriter::class.qualifiedName)
    }

    fun go() {

        // BufferedWriter Init
        var bw: BufferedWriter? = null

        try {

            // Writer
            bw = BufferedWriter(FileWriter("$tabName.csv"))

            bw.write("Not in test2:\n")
            for (i in 0 ..< notInDBA.size) {
                bw.write(notInDBA[i][0].toString())
                for (j in 1..< notInDBA.size) bw.write(", ${notInDBA[i][j]}")
                bw.newLine()
            }

            bw.write("\nAdd in test2:\n")
            for (i in 0 ..< addInDBA.size) {
                bw.write(addInDBA[i][0].toString())
                for (j in 1..< addInDBA.size) bw.write(", ${addInDBA[i][j]}")
                bw.newLine()
            }

            bw.write("\nNot equal in test2:\n")
            for (i in 0 ..< xorInDBA.size) {
                bw.write(xorInDBA[i][0].toString())
                for (j in 1..< xorInDBA.size) bw.write(", ${xorInDBA[i][j]}")
                bw.newLine()
            }
            // End

        } catch (ioe: IOException) { logger.log(Level.SEVERE, ioe.toString())
        } finally { try { bw?.close() } catch (ioe: IOException) { logger.log(Level.SEVERE, ioe.toString()) } }
    }
}