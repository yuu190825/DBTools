class SqlStringCreate(tbName: String, clNameList: MutableList<String>,
                      clValueLists: MutableList<MutableList<Any?>>): Thread() {

    // Parameter Value
    private val tabName: String
    private val colNameList: MutableList<String>
    private val colValueLists: MutableList<MutableList<Any?>>

    val sqlStringList = mutableListOf<String>() // Data Value

    init {
        this.tabName = tbName
        this.colNameList = clNameList
        this.colValueLists = clValueLists
    }

    override fun run() {
        for (clValueList in colValueLists) {
            val sqlString = StringBuilder("INSERT INTO $tabName(${colNameList[0]}")
            for (i in 1 ..< colNameList.size) sqlString.append(", ${colNameList[i]}")

            if (clValueList[0] == null) sqlString.append(") VALUES(NULL")
            else sqlString.append(") VALUES('${clValueList[0]}'")
            for (i in 1 ..< clValueList.size)
                if (clValueList[i] == null) sqlString.append(", NULL") else sqlString.append(", '${clValueList[i]}'")

            sqlStringList.add(sqlString.append(");").toString())
        }

        // DateTime
        for (sqlString in sqlStringList) sqlString.replace(
            "[0-9]{5}-[0-9]{2}-[0-9]{2} [0-9]{2}:[0-9]{2}:[0-9]{2}.[0-9]{1,3}".toRegex(),
            "1900-01-01 00:00:00.0")
        // End

    }
}