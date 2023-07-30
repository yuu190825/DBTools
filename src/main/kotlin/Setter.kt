import java.util.*

// Main_Config
private const val DEFAULT_RECORD: Short = 5000

class Setter {

    // Set_Value
    var mode: Byte = 0
    var dbName = ""
    var record: Short = 0
    var tabName = mutableListOf<String>()
    var total = 0

    fun getTransferSet() {
        val scan = Scanner(System.`in`)

        print("Transfer Mode (1)ToFile (2)ToDB: ")
        mode = try { scan.nextLine().toByte() } catch (nfe: NumberFormatException) { 0 }

        if (mode.toInt() == 2) {
            print("DB Name: ")
            dbName = scan.nextLine()
        }

        println("Record: ")
        record = try { scan.nextLine().toShort() } catch (nfe: NumberFormatException) { DEFAULT_RECORD }
        if (record < 0) record = DEFAULT_RECORD

        print("Table Name: ")
        tabName = scan.nextLine().split(",").toMutableList()

        print("Total Row: ")
        total = try { scan.nextLine().toInt() } catch (nfe: NumberFormatException) { 0 }

        scan.close()
    }

    fun getCompareSet() {
        val scan = Scanner(System.`in`)

        print("Table Name: ")
        tabName = scan.nextLine().split(",").toMutableList()

        print("Total Row: ")
        total = try { scan.nextLine().toInt() } catch (nfe: NumberFormatException) { 0 }

        scan.close()
    }
}