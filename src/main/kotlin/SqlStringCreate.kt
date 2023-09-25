class SqlStringCreate(tbName: String, colNameList: MutableSet<String>,
                      colValueLists: MutableList<MutableList<Any?>>): Thread() {

    // Parameter Value
    private val tabName: String
    private val colNameList: MutableSet<String>
    private val colValueLists: MutableList<MutableList<Any?>>

    val sqlStringList = mutableListOf<String>() // Data Value

    init {
        this.tabName = tbName
        this.colNameList = colNameList
        this.colValueLists = colValueLists
    }

    override fun run() {
        for (colValueList in colValueLists) {
            val sqlString = StringBuilder("INSERT INTO $tabName(${colNameList.elementAt(0)}")
            for (i in 1 ..< colNameList.size) sqlString.append(", ${colNameList.elementAt(i)}")

            if (colValueList[0] == null) sqlString.append(") VALUES(NULL")
            else sqlString.append(") VALUES('${colValueList[0]}'")
            for (i in 1 ..< colValueList.size)
                if (colValueList[i] == null) sqlString.append(", NULL") else sqlString.append(", '${colValueList[i]}'")

            sqlStringList.add(sqlString.append(");").toString())
        }

        // DateTime
        for (i in 0 ..< sqlStringList.size) sqlStringList[i] = sqlStringList[i].replace(
            "[0-9]{5}-[0-9]{2}-[0-9]{2} [0-9]{2}:[0-9]{2}:[0-9]{2}.[0-9]{1,3}".toRegex(),
            "1900-01-01 00:00:00.0")
        // End

    }
}