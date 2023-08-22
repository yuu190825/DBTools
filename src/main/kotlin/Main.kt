import java.awt.event.ActionEvent
import java.awt.event.ActionListener

// Main Config
private const val DEFAULT_RECORD: Short = 5000
private const val MAX: Long = 500000

private val window = Window() // Window Init

fun main() {
    StartButton().init()
    window.print()
}

private class StartButton {
    fun init() { window.btnStart.addActionListener(Click()) }

    private inner class Click: ActionListener { override fun actionPerformed(e: ActionEvent?) { Start().start() } }
}

private class Start {
    fun start() {

        // Data Value
        val colValueListsA = mutableListOf<MutableList<Any?>>()
        val colValueListsB = mutableListOf<MutableList<Any?>>()

        window.start()

        val record = try { window.record.text.toShort() } catch (nfe: NumberFormatException) { DEFAULT_RECORD }
        val total = try { window.total.text.toLong() } catch (nfe: NumberFormatException) { 0 }

        if (window.tabNameList.isNotEmpty()) {
            for (tabName in window.tabNameList) {
                var from: Long = if (window.fromDbType.toInt() == 3) 0 else 1
                var to: Long = MAX

                while (from <= total) {
                    val execute = Execute(window.fromDbType, window.fromDbUrl.text, window.fromDbName.text,
                        window.fromDbUser.text, String(window.fromDbPass.password), window.toDbType,
                        window.toDbUrl.text, window.toDbName.text, window.toDbUser.text,
                        String(window.toDbPass.password), window.func, window.idInsert, record, tabName, from, to)

                    execute.start(window.mode)

                    if (window.func.toInt() == 2) {
                        colValueListsA.addAll(execute.colValueListsA)
                        colValueListsB.addAll(execute.colValueListsB)
                    }

                    from += MAX
                    if (window.fromDbType.toInt() != 3) to += MAX
                }

                if (window.func.toInt() == 2) {

                    // Compare
                    val compare = Compare(colValueListsA, colValueListsB)
                    compare.start()
                    // End

                    // Writer
                    CsvFileWriter(window.fromDbName.text, tabName, compare.notInDBA, compare.addInDBA, compare.xorInDBA)
                        .start()
                    // End

                }
            }
        }

        println("ok")
        window.end()
    }
}