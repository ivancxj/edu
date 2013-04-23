package com.edu.lib.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
	private static final String DATABASE_NAME = "edu";

	// message 私信
	public static final String TABLE_MESSAGE = "message";
	public static final String MESSAGE_ID = "_id";// 数据库主键
	public static final String MESSAGE_TYPE = "type";// 类型 1是接收到的，2是发出的

	public static final String MESSAGE_PID = "PID";// 服务器 消息id
	public static final String MESSAGE_SEND_ID = "SendID"; // 发送人id
	public static final String MESSAGE_SEND_NAME = "SendName";// 发送人名称
	public static final String MESSAGE_TITLE = "Title";// 标题
	public static final String MESSAGE_CONTENT = "Content";// 内容
	public static final String MESSAGE_SENDTIME = "SendTime";// 发送时间
	public static final String MESSAGE_PARENT_ID = "ParentID";// 上级消息id(新建的为0)

	// private Context context;
	private static DBHelper mInstance;

	public DBHelper(Context context) {
		super(context, DATABASE_NAME, null, 1);
		// this.context = context;
	}

	public DBHelper(Context context, SQLiteDatabase.CursorFactory factory) {
		super(context, DATABASE_NAME, factory, 1);
		// this.context = context;
	}

	public static DBHelper getInstance(Context context,
			SQLiteDatabase.CursorFactory factory) {
		if (mInstance == null) {
			mInstance = new DBHelper(context, factory);
		}
		return mInstance;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// message 私信
		db.execSQL("CREATE TABLE " + TABLE_MESSAGE + " (" + MESSAGE_ID
				+ " INTEGER PRIMARY KEY , " + MESSAGE_TYPE + " INTEGER, "
				+ MESSAGE_PID + " TEXT, " + MESSAGE_SEND_ID + " TEXT, "
				+ MESSAGE_SEND_NAME + " TEXT, " + MESSAGE_TITLE + " TEXT,"
				+ MESSAGE_CONTENT + " TEXT," + MESSAGE_SENDTIME + " TEXT,"
				+ MESSAGE_PARENT_ID + " TEXT" + ");");

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

}
