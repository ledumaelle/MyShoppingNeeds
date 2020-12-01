package com.example.ledumaelle.myshoppingneeds.contract;

public class ArticleContract {

    public static final String TABLE_NAME = "articles";

    public static final String COL_ID = " id ";
    public static final String COL_NAME = " name ";
    public static final String COL_PRICE = " price ";
    public static final String COL_DESCRIPTION = " description ";
    public static final String COL_RATE = " rate ";
    public static final String COL_URL = " url ";
    public static final String COL_STATE = " state ";
    public static final String COL_NAME_FILE = " name_file ";

    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME
            +" ( " +
            COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"+
            COL_NAME + " TEXT,"+
            COL_PRICE + " REAL,"+
            COL_DESCRIPTION + " TEXT,"+
            COL_RATE + " REAL,"+
            COL_URL + " TEXT,"+
            COL_STATE + " INTEGER,"+
            COL_NAME_FILE + " TEXT"+
            ");";

    public static final String DROP_TABLE = "DROP TABLE " + TABLE_NAME;
}
