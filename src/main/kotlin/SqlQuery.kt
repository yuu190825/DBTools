// SQL Query in Oracle DB(1)
private const val SELECT_COLUMN_NAME_IN_ORACLE_DB = "SELECT COLUMN_NAME FROM ALL_TAB_COLUMNS WHERE TABLE_NAME ="
private const val ORDER_BY_COLUMN_ID_IN_ORACLE_DB = "ORDER BY COLUMN_ID"
private const val SELECT_ONE_IN_ORACLE_DB_1 = "SELECT * FROM"
private const val SELECT_ONE_IN_ORACLE_DB_2 = "LIMIT 1"
private const val SELECT_ALL_IN_ORACLE_DB = "SELECT * FROM (SELECT ROWNUM AS NUM, * FROM"
private const val SELECT_ALL_FROM_IN_ORACLE_DB = "WHERE NUM >="
private const val SELECT_ALL_TO_IN_ORACLE_DB = "AND NUM <="

// SQL Query in SQL Server(2)
private const val SELECT_COLUMN_NAME_IN_SQL_SERVER = "SELECT COLUMN_NAME FROM INFORMATION_SCHEMA.COLUMNS " +
        "WHERE TABLE_NAME ="
private const val ORDER_BY_COLUMN_ID_IN_SQL_SERVER = "ORDER BY ORDINAL_POSITION"
private const val SELECT_ONE_IN_SQL_SERVER = "SELECT TOP 1 * FROM"
private const val SELECT_ALL_ORDER_BY_IN_SQL_SERVER = "SELECT * FROM (SELECT ROW_NUMBER() OVER(ORDER BY"
private const val SELECT_ALL_IN_SQL_SERVER = "AS NUM,* FROM"
private const val SELECT_ALL_FROM_IN_SQL_SERVER = "A WHERE NUM >="
private const val SELECT_ALL_TO_IN_SQL_SERVER = "AND NUM <="

class SqlQuery {
    fun getSelectColumnNameQuery(type: Byte, tabName: String): String {
        return if (type.toInt() == 1) "$SELECT_COLUMN_NAME_IN_ORACLE_DB $tabName $ORDER_BY_COLUMN_ID_IN_ORACLE_DB"
        else "$SELECT_COLUMN_NAME_IN_SQL_SERVER $tabName $ORDER_BY_COLUMN_ID_IN_SQL_SERVER"
    }

    fun getSelectOneQuery(type: Byte, tabName: String): String {
        return if (type.toInt() == 1) "$SELECT_ONE_IN_ORACLE_DB_1 $tabName $SELECT_ONE_IN_ORACLE_DB_2"
        else "$SELECT_ONE_IN_SQL_SERVER $tabName"
    }

    fun getSelectAllQuery(type: Byte, tabName: String, id: String, from: Long, to: Long): String {
        return if (type.toInt() == 1) "$SELECT_ALL_IN_ORACLE_DB $tabName) $SELECT_ALL_FROM_IN_ORACLE_DB $from " +
                "$SELECT_ALL_TO_IN_ORACLE_DB $to"
        else "$SELECT_ALL_ORDER_BY_IN_SQL_SERVER $id) $SELECT_ALL_IN_SQL_SERVER $tabName) " +
                "$SELECT_ALL_FROM_IN_SQL_SERVER $from $SELECT_ALL_TO_IN_SQL_SERVER $to"
    }
}