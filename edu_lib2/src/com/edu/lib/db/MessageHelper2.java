package com.edu.lib.db;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.edu.lib.bean.Message;
import com.edu.lib.util.LogUtils;

public class MessageHelper2 {
	// 本机登陆者都是 MESSAGE_RECEIVER_ID 这个字段
	public long insert(Context context, Message message) {
		if (message == null)
			return -1;
		ArrayList<Message> messages = new ArrayList<Message>();
		messages.add(message);
		return insert(context, messages);
	}

	public long insert(Context context, ArrayList<Message> messages) {
		long thread_id = -1;
		if (messages == null || messages.size() == 0) {
			return thread_id;
		}

		DBHelper db = DBHelper.getInstance(context, null);
		SQLiteDatabase write = db.getWritableDatabase();

		Cursor cursor = null;
		String whereClause = DBHelper.THREAD_SEND_ID + " = ? and "
				+ DBHelper.MESSAGE_RECEIVER_ID + "= ?";
		int length = messages.size();
		for (int i = 0; i < length; i++) {
			Message message = messages.get(i);
			// LogUtils.D("insert message.ParentID = "+message.ParentID
			// +" message.PID="+message.PID);
			// if ("0".equals(message.ParentID)) {// 表示是新创建的
			// message.ParentID = message.PID;
			// }
			LogUtils.D(" message.SendID = " + message.SendID
					+ " message.ReceiverID=" + message.ReceiverID);
			if (message.type == 1) {// 接收者
				cursor = write.rawQuery("select * from "
						+ DBHelper.TABLE_THREAD + " where " + whereClause,
						new String[] { message.SendID, message.ReceiverID });
			} else {// 发送者
				cursor = write.rawQuery("select * from "
						+ DBHelper.TABLE_THREAD + " where " + whereClause,
						new String[] { message.ReceiverID, message.SendID });
			}

			ContentValues values = new ContentValues();
			values.put(DBHelper.THREAD_TYPE, message.type);
			values.put(DBHelper.THREAD_PID, message.PID);
			// 这样做目的是 为了方便会话统计
			if (message.type == 1) {
				values.put(DBHelper.THREAD_SEND_ID, message.SendID);
				values.put(DBHelper.THREAD_SEND_NAME, message.SendName);
				values.put(DBHelper.THREAD_RECEIVER_ID, message.ReceiverID);
				values.put(DBHelper.THREAD_RECEIVER_NAME, message.ReceiverName);
			} else {// type = 2
				values.put(DBHelper.THREAD_SEND_ID, message.ReceiverID);
				values.put(DBHelper.THREAD_SEND_NAME, message.ReceiverName);
				values.put(DBHelper.THREAD_RECEIVER_ID, message.SendID);
				values.put(DBHelper.THREAD_RECEIVER_NAME, message.SendName);
			}
			values.put(DBHelper.THREAD_TITLE, message.Title);
			values.put(DBHelper.THREAD_CONTENT, message.Content);
			values.put(DBHelper.THREAD_SENDTIME, message.SendTime);
			values.put(DBHelper.THREAD_PARENT_ID, message.ParentID);

			if (cursor != null && cursor.getCount() > 0) {// 已经有会话
				cursor.moveToFirst();
				int message_count = cursor.getInt(cursor
						.getColumnIndex(DBHelper.THREAD_MESSAGE_COUNT));
				thread_id = cursor.getInt(cursor
						.getColumnIndex(DBHelper.THREAD_ID));

				values.put(DBHelper.THREAD_MESSAGE_COUNT, message_count + 1);
				if (message.type == 1) {
					write.update(DBHelper.TABLE_THREAD, values, whereClause,
							new String[] { message.SendID, message.ReceiverID });
				} else {
					write.update(DBHelper.TABLE_THREAD, values, whereClause,
							new String[] { message.ReceiverID, message.SendID });
				}
				LogUtils.D("update threads count =" + (message_count + 1));
			} else {
				values.put(DBHelper.THREAD_MESSAGE_COUNT, 1);
				thread_id = write.insert(DBHelper.TABLE_THREAD, null, values);
				LogUtils.D("insert threads");
			}

			// update insert 成功
			if (thread_id != -1) {
				values = new ContentValues();
				values.put(DBHelper.MESSAGE_THREAD_ID, thread_id);
				values.put(DBHelper.MESSAGE_TYPE, message.type);
				values.put(DBHelper.MESSAGE_PID, message.PID);
				values.put(DBHelper.MESSAGE_SEND_ID, message.SendID);
				values.put(DBHelper.MESSAGE_SEND_NAME, message.SendName);
				values.put(DBHelper.MESSAGE_RECEIVER_ID, message.ReceiverID);
				values.put(DBHelper.MESSAGE_RECEIVER_NAME, message.ReceiverName);
				values.put(DBHelper.MESSAGE_TITLE, message.Title);
				values.put(DBHelper.MESSAGE_CONTENT, message.Content);
				values.put(DBHelper.MESSAGE_SENDTIME, message.SendTime);
				values.put(DBHelper.MESSAGE_PARENT_ID, message.ParentID);

				write.insert(DBHelper.TABLE_MESSAGE, null, values);
				LogUtils.D("insert message");
			}

			if (cursor != null)
				cursor.close();
		}

		write.close();
		db.close();

		return thread_id;
	}

	// 获取本人的全部会话列表
	public ArrayList<Message> getThread(Context context, String ReceiverID) {
		ArrayList<Message> messages = null;
		DBHelper db = DBHelper.getInstance(context, null);
		SQLiteDatabase read = db.getReadableDatabase();
		// 时间排序
		Cursor cursor = read.rawQuery("select * from " + DBHelper.TABLE_THREAD
				+ " where " + DBHelper.THREAD_RECEIVER_ID + " = ? ",
				new String[] { ReceiverID });

		if (cursor != null && cursor.getCount() > 0) {
			messages = new ArrayList<Message>();
			LogUtils.D("获取本地会话");
			while (cursor.moveToNext()) {
				Message message = new Message();

				message.thread_id = cursor.getInt(cursor
						.getColumnIndex(DBHelper.THREAD_ID));
				message.type = cursor.getInt(cursor
						.getColumnIndex(DBHelper.THREAD_TYPE));
				message.PID = cursor.getString(cursor
						.getColumnIndex(DBHelper.THREAD_PID));
				message.SendID = cursor.getString(cursor
						.getColumnIndex(DBHelper.THREAD_SEND_ID));
				message.SendName = cursor.getString(cursor
						.getColumnIndex(DBHelper.THREAD_SEND_NAME));
				message.ReceiverID = cursor.getString(cursor
						.getColumnIndex(DBHelper.THREAD_RECEIVER_ID));
				message.ReceiverName = cursor.getString(cursor
						.getColumnIndex(DBHelper.THREAD_RECEIVER_NAME));
				message.Title = cursor.getString(cursor
						.getColumnIndex(DBHelper.THREAD_TITLE));
				message.Content = cursor.getString(cursor
						.getColumnIndex(DBHelper.THREAD_CONTENT));
				message.SendTime = cursor.getLong(cursor
						.getColumnIndex(DBHelper.THREAD_SENDTIME));
				message.ParentID = cursor.getString(cursor
						.getColumnIndex(DBHelper.THREAD_PARENT_ID));

				messages.add(message);
			}
			cursor.close();
		} else {
			LogUtils.D("本地没有会话");
		}

		read.close();
		db.close();
		return messages;
	}

	public ArrayList<Message> getMessages(Context context, long thread_id) {
		ArrayList<Message> messages = null;
		if (thread_id <= 0)// 不合法数据库 id
			return null;
		LogUtils.D("getMessages thread_id = " + thread_id);

		DBHelper db = DBHelper.getInstance(context, null);
		SQLiteDatabase read = db.getReadableDatabase();

		String sql = "select * from " + DBHelper.TABLE_MESSAGE + " where "
				+ DBHelper.MESSAGE_THREAD_ID + " = ? " + "order by "
				+ DBHelper.MESSAGE_SENDTIME + " asc";
		LogUtils.D("sql="+sql);
		Cursor cursor = read.rawQuery(sql, new String[] { thread_id + "" });
		if (cursor != null && cursor.getCount() > 0) {
			messages = new ArrayList<Message>();
			LogUtils.D("获取本地消息列表");
			while (cursor.moveToNext()) {
				Message m = new Message();
				m.message_thread_id = cursor.getInt(cursor
						.getColumnIndex(DBHelper.MESSAGE_THREAD_ID));
				m.type = cursor.getInt(cursor
						.getColumnIndex(DBHelper.MESSAGE_TYPE));
				m.PID = cursor.getString(cursor
						.getColumnIndex(DBHelper.MESSAGE_PID));
				m.SendID = cursor.getString(cursor
						.getColumnIndex(DBHelper.MESSAGE_SEND_ID));
				m.SendName = cursor.getString(cursor
						.getColumnIndex(DBHelper.MESSAGE_SEND_NAME));
				m.ReceiverID = cursor.getString(cursor
						.getColumnIndex(DBHelper.MESSAGE_RECEIVER_ID));
				m.ReceiverName = cursor.getString(cursor
						.getColumnIndex(DBHelper.MESSAGE_RECEIVER_NAME));
				m.Title = cursor.getString(cursor
						.getColumnIndex(DBHelper.MESSAGE_TITLE));
				m.Content = cursor.getString(cursor
						.getColumnIndex(DBHelper.MESSAGE_CONTENT));
				m.SendTime = cursor.getLong(cursor
						.getColumnIndex(DBHelper.MESSAGE_SENDTIME));
				m.ParentID = cursor.getString(cursor
						.getColumnIndex(DBHelper.MESSAGE_PARENT_ID));
				messages.add(m);
			}
			cursor.close();
		} else {
			LogUtils.D("获取本地消息列表 thread_id = " + thread_id + " null");
		}

		read.close();
		db.close();

		return messages;
	}

	public void deleteThreadById(Context context, long thread_id) {
		if (thread_id < 1)
			return;
		DBHelper db = DBHelper.getInstance(context, null);
		SQLiteDatabase write = db.getWritableDatabase();
		write.delete(DBHelper.TABLE_MESSAGE,
				DBHelper.MESSAGE_THREAD_ID + "= ?", new String[] { thread_id
						+ "" });
		write.delete(DBHelper.TABLE_THREAD, DBHelper.THREAD_ID + "= ?",
				new String[] { thread_id + "" });
		write.close();
		db.close();
	}
}
