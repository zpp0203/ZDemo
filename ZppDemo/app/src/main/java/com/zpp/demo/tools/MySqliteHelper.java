package com.zpp.demo.tools;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 数据库操作基类
 */
public class MySqliteHelper extends SQLiteOpenHelper {

    public static final String TABLES_NAME_HISTORY_DATA = "history_data";// 温湿度记录表
    public static final String TABLES_NAME_HISTORY_BLOB_DATA = "history_blob_data";// 温湿度记录表

    public static final int DATA_SAVE_DAY = 7;//保留7天数据

    private static final String DATABASE_NAME = "heyi";
    private static final int DATABASE_VERSION = 1;

    public MySqliteHelper(Context context) {
        // calls the super constructor, requesting the default cursor factory.
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        createTables(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        deleteAllTable(db);
        createTables(db);

    }

    private void deleteAllTable(SQLiteDatabase db) {
        db.delete(TABLES_NAME_HISTORY_DATA, null, null);

    }

    private void createTables(SQLiteDatabase db) {

//        createHistoryData(db, TABLES_NAME_HISTORY_DATA);
        createHistoryBlobData(db, TABLES_NAME_HISTORY_BLOB_DATA);

    }

    private void createHistoryData(SQLiteDatabase db, String table) {

        String sql = "CREATE TABLE IF NOT EXISTS "
                + table
                + " ("
                + HistoryData.DATA_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + HistoryData.DEVICE_SN + " TEXT, "
                + HistoryData.DEVICE_MAC + " TEXT, "
                + HistoryData.DATA_TIME + " TEXT, "
                + HistoryData.DATA_DATE + " INTEGER, "
                + HistoryData.DATA_HOUR + " INTEGER, "//
                + HistoryData.DATA_TEMPERATURE + " INTEGER, "//
                + HistoryData.DATA_HUMIDITY + " INTEGER, "//
                + HistoryData.DATA_STATUS + " INTEGER "//
                + ")";
        db.execSQL(sql);


    }

    /**
     * 直接存blob格式
     * @param db
     * @param table
     */
    private void createHistoryBlobData(SQLiteDatabase db, String table) {

        String blobSQL = "CREATE TABLE IF NOT EXISTS "
                + table
                + " ("
                + HistoryData.DATA_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + HistoryData.DEVICE_MAC + " TEXT, "
                + HistoryData.DATA_BYTE + " BLOB, "//
                + HistoryData.DATA_DATE_HOUR + " INTEGER, "//
                + HistoryData.DATA_STATUS + " INTEGER "//
                + ")";
        db.execSQL(blobSQL);

    }


    /**
     * 历史数据表，只保留三天
     */
    public class HistoryData {

        public static final String DATA_ID = "dataId";
        public static final String DEVICE_SN = "deviceSn";//序列号
        public static final String DEVICE_MAC = "deviceMac";//mac 地址
        public static final String DATA_TIME = "receiveTime";//接收时间
        public static final String DATA_DATE = "day";//日期
        public static final String DATA_HOUR = "hour";//钟点
        public static final String DATA_TEMPERATURE = "temperature";//温度值
        public static final String DATA_HUMIDITY = "humidity";//湿度值
        public static final String DATA_STATUS = "status";//状态： -1数据未初始化，0同步蓝牙数据 ，1数据同步服务器
        public static final String DATA_BYTE = "data";//湿度值
        public static final String DATA_DATE_HOUR = "dayHour";//湿度值

    }
    /*删掉三天前的数据，*/

}
