import java.util.*

// Main Config
private const val MAX: Long = 500000

fun main() {

    // Set Value
    val fromDBType: Byte
    val toDBType: Byte
    val func: Byte

    // Init Value
    val setter = Setter()

    // Get Function Set
    val scan = Scanner(System.`in`)

    print("From (1)Oracle DB (2)SQL Server: ")
    fromDBType = try { scan.nextLine().toByte() } catch (nfe: NumberFormatException) { 1 }

    print("To (1)Oracle DB (2)SQL Server: ")
    toDBType = try { scan.nextLine().toByte() } catch (nfe: NumberFormatException) { 1 }

    print("Function Mode (1)Transfer (2)Compare: ")
    func = try { scan.nextLine().toByte() } catch (nfe: NumberFormatException) { 0 }

    scan.close()
    // End

    if (func.toInt() == 1) {
        setter.getTransferSet()
        if (setter.tabName[0].isNotEmpty()) {
            for (tabName in setter.tabName) {
                var from: Long = 1
                var to: Long = MAX

                while (from <= setter.total) {
                    Execute(fromDBType, toDBType, func, setter.record, tabName, from, to).doTransfer(setter.mode,
                        setter.dbName)

                    from += MAX
                    to += MAX
                }
            }
        }
    } else {
        setter.getCompareSet()
        if (setter.tabName[0].isNotEmpty()) {
            for (tbName in setter.tabName) {
                var from: Long = 1
                var to: Long = MAX

                while (from <= setter.total) {
                    Execute(fromDBType, toDBType, func, setter.record, tbName, from, to).doCompare()

                    from += MAX
                    to += MAX
                }
            }
        }
    }

    println("ok")
}