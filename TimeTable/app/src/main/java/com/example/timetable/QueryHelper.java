package com.example.timetable;

//SQLite query helper
public class QueryHelper {

    //Create table helper.
    public static String CreateTable(String tableName, String columnsToCreate)
    {
        return "create table " + tableName + " (" + columnsToCreate + ");";
    }

    //All these functions will have 1 space bar in-front.
    public static String PrimaryIncrementKeyPhrase(String idName){
        return " " + idName + " integer primary key autoincrement not null";
    }

    public static String PrimaryIncrementKeyPhrase_Comma(String idName)
    {
        return PrimaryIncrementKeyPhrase(idName) + ",";
    }

    public static String TextNotNull(String name)
    {
        return " " + name + " text not null";
    }

    public static String TextNotNull_Comma(String name)
    {
        return TextNotNull(name) + ",";
    }

    //ref:https://stackoverflow.com/questions/34916028/how-to-set-a-default-value-to-a-text-column-in-sqlite
    public static String TextNotNullWithDefault(String name, String _default)
    {
        return TextNotNull(name) + " default " + "'" + _default + "'";
    }

    public static String TextNotNullWithDefault_Comma(String name, String _default)
    {
        return TextNotNullWithDefault(name, _default) + ",";
    }
}
