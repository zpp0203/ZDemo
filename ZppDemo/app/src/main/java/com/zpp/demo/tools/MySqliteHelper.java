package com.zpp.demo.tools;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * 数据库操作基类
 */
public class MySqliteHelper extends SQLiteOpenHelper {
    private String TAG=MySqliteHelper.class.getName();
    public static final String TABLES_EQUIPMENT_ACTION = "equipment_action_data";// 设备操作数据库

    public static final int DATA_SAVE_DAY = 7;//保留7天数据

    private static final String DATABASE_NAME = "heyi";
    private static final int DATABASE_VERSION = 1;
    private SQLiteDatabase sb;


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
    //删除表
    private void deleteAllTable(SQLiteDatabase db) {
        db.delete(TABLES_EQUIPMENT_ACTION, null, null);

    }

    private void createTables(SQLiteDatabase db) {

        createEActionData(db, TABLES_EQUIPMENT_ACTION);
        sb=db;
    }

    private void createEActionData(SQLiteDatabase db, String table) {

        String sql = "CREATE TABLE IF NOT EXISTS "
                + table
                + " ("
                + ActionData.DATA_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ActionData.EQ_MAC + " TEXT, "
                + ActionData.EQ_NAME + " TEXT, "
                + ActionData.ACTION_TYPE + " INTEGER"
                + ")";
        db.execSQL(sql);


    }
    // 表名
    // null。数据库如果插入的数据为null，会引起数据库不稳定。为了防止崩溃，需要传入第二个参数要求的对象
    // 如果插入的数据不为null，没有必要传入第二个参数避免崩溃，所以为null
    // 插入的数据
    public void insertData(String mac,String name,int type) {
        ContentValues values = new ContentValues();
        values.put(ActionData.EQ_MAC, mac);
        values.put(ActionData.EQ_NAME,name);
        values.put(ActionData.EQ_NAME,name);
        values.put(ActionData.ACTION_TYPE,type);
        sb.insert(TABLES_EQUIPMENT_ACTION, null, values);
        Log.e(TAG,"添加："+mac);
    }
    // 表名
    // 删除条件
    // 满足删除的值
    public void deleteData(String[] macs) {
        int count = sb.delete(TABLES_EQUIPMENT_ACTION, ActionData.EQ_MAC + " = ?", macs);
        Log.e(TAG,count+"删除："+macs.toString());
    }
    // 表名
    // 修改后的数据
    // 修改条件
    // 满足修改的值
    public void updateData(String[] mac,String name,int type) {
        ContentValues values = new ContentValues();
        values.put(ActionData.EQ_NAME, name);
        values.put(ActionData.ACTION_TYPE, type);
        int count = sb
                .update(TABLES_EQUIPMENT_ACTION, values, ActionData.EQ_MAC + " = ?", mac);
        Log.e(TAG, "修改成功：" + count+"-"+mac.toString());
    }

    // 表名
    // 查询字段
    // 查询条件
    // 满足查询的值
    // 分组
    // 分组筛选关键字
    // 排序
    public void queryData(String[] macs) {

        Cursor cursor = sb.query(TABLES_EQUIPMENT_ACTION,
                new String[]{ActionData.EQ_MAC, ActionData.EQ_NAME, ActionData.ACTION_TYPE},
                ActionData.EQ_MAC + " = ?",
                macs,
                null,
                null,
                null
                        + " desc");// 注意空格！

        int name = cursor.getColumnIndex(ActionData.EQ_NAME);
        int mac = cursor.getColumnIndex(ActionData.EQ_MAC);
        int type = cursor.getColumnIndex(ActionData.ACTION_TYPE);
        while (cursor.moveToNext()) {


            Log.d(TAG, "查询：name: " + name + ", mac: " + mac);
        }

    }

    /**
     *设备操作数据表
     */
    private class ActionData {

        public static final String DATA_ID = "actionId";//mac 地址
        public static final String EQ_MAC = "deviceMac";//mac 地址
        public static final String ACTION_TYPE = "actionType";//操作类型 1.绑定 0.解绑
        public static final String EQ_NAME = "deviceName";//设备名

    }

}
