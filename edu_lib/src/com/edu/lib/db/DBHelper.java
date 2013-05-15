package com.edu.lib.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
	private static final int VERSION = 2;
	private static final String DATABASE_NAME = "edu";

	// message 私信
	public static final String TABLE_MESSAGE = "message";
	public static final String MESSAGE_ID = "_id";// 数据库主键
	public static final String MESSAGE_THREAD_ID = "thread_id";// 属于哪个会话
	public static final String MESSAGE_TYPE = "type";// 类型 1是接收到的，2是发出的
	public static final String MESSAGE_PID = "PID";// 服务器 消息id
	public static final String MESSAGE_SEND_ID = "SendID"; // 发送人id
	public static final String MESSAGE_SEND_NAME = "SendName";// 发送人名称
	public static final String MESSAGE_RECEIVER_ID = "ReceiverID"; // 接收者 id
	public static final String MESSAGE_RECEIVER_NAME = "ReceiverName"; // 接收者名字
	public static final String MESSAGE_TITLE = "Title";// 标题
	public static final String MESSAGE_CONTENT = "Content";// 内容
	public static final String MESSAGE_SENDTIME = "SendTime";// 发送时间
	public static final String MESSAGE_PARENT_ID = "ParentID";// 上级消息id(新建的为0)

	// thread 会话 消息归档 按照人
	public static final String TABLE_THREAD = "thread";
	public static final String THREAD_ID = "_id";// 数据库主键
	public static final String THREAD_TYPE = "type";// 类型 1是接收到的，2是发出的
	public static final String THREAD_PID = "PID";// 服务器 消息id
	public static final String THREAD_SEND_ID = "SendID"; // 发送人id
	public static final String THREAD_SEND_NAME = "SendName";// 发送人名称
	public static final String THREAD_RECEIVER_ID = "ReceiverID"; // 接收者 id
	public static final String THREAD_RECEIVER_NAME = "ReceiverName"; // 接收者名字
	public static final String THREAD_TITLE = "Title";// 标题
	public static final String THREAD_CONTENT = "Content";// 内容
	public static final String THREAD_SENDTIME = "SendTime";// 发送时间
	public static final String THREAD_PARENT_ID = "ParentID";// 上级消息id(新建的为0)
	public static final String THREAD_MESSAGE_COUNT = "message_count";// integer

	// private Context context;
	private static DBHelper mInstance;

	public DBHelper(Context context) {
		super(context, DATABASE_NAME, null, VERSION);
		// this.context = context;
	}

	public DBHelper(Context context, SQLiteDatabase.CursorFactory factory) {
		super(context, DATABASE_NAME, factory, VERSION);
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
				+ " INTEGER PRIMARY KEY , "+ MESSAGE_THREAD_ID
				+ " INTEGER , " + MESSAGE_TYPE + " INTEGER, "
				+ MESSAGE_PID + " TEXT, " + MESSAGE_SEND_ID + " TEXT, "
				+ MESSAGE_SEND_NAME + " TEXT, "+ MESSAGE_RECEIVER_ID
				+ " TEXT, " + MESSAGE_RECEIVER_NAME + " TEXT, " + MESSAGE_TITLE + " TEXT,"
				+ MESSAGE_CONTENT + " TEXT," + MESSAGE_SENDTIME + " TEXT,"
				+ MESSAGE_PARENT_ID + " TEXT" + ");");
		
		db.execSQL("CREATE TABLE " + TABLE_THREAD + " (" + THREAD_ID
				+ " INTEGER PRIMARY KEY , " + THREAD_TYPE + " INTEGER, "
				+ THREAD_PID + " TEXT, " + THREAD_SEND_ID + " TEXT, "
				+ THREAD_SEND_NAME + " TEXT, " + THREAD_RECEIVER_ID
				+ " TEXT, " + THREAD_RECEIVER_NAME + " TEXT, "
				+ THREAD_TITLE + " TEXT," + THREAD_CONTENT + " TEXT,"
				+ THREAD_SENDTIME + " TEXT," + THREAD_PARENT_ID + " TEXT,"
				+ THREAD_MESSAGE_COUNT + " INTEGER " + ");");

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}
}
