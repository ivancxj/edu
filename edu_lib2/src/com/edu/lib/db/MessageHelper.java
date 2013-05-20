//package com.edu.lib.db;
//
//import java.util.ArrayList;
//
//import android.content.ContentValues;
//import android.content.Context;
//import android.database.Cursor;
//import android.database.sqlite.SQLiteDatabase;
//
//import com.edu.lib.bean.Message;
//import com.edu.lib.util.LogUtils;
//
//public class MessageHelper {
//	
//	public void insert(Context context,Message message){
//		if(message == null) return;
//		ArrayList<Message> messages = new ArrayList<Message>();
//		messages.add(message);
//		insert(context,messages);
//	}
//
//	public void insert(Context context, ArrayList<Message> messages) {
//		DBHelper db = DBHelper.getInstance(context, null);
//		SQLiteDatabase write = db.getWritableDatabase();
//		
//		Cursor cursor = null;
//		
//		String whereClause = DBHelper.MESSAGE_PID +" = ? ";
//		
//		if(messages == null || messages.size() == 0){
//			write.close();
//			db.close();
//			return;
//		}
//		
//		int length = messages.size();
//		for(int i = 0;i<length;i++){
//			Message message = messages.get(i);
//			LogUtils.D("insert message.ParentID = "+message.ParentID +" message.PID="+message.PID);
//			if ("0".equals(message.ParentID)) {// 表示是新创建的
//				message.ParentID = message.PID;
//			}
//			LogUtils.D("replace message.ParentID = "+message.ParentID +" message.PID="+message.PID);
//			cursor = write.rawQuery("select * from "
//					+ DBHelper.TABLE_MESSAGE + " where " + whereClause,
//					new String[] { message.PID});
//			
//			if(cursor != null && cursor.getCount() >0){
//				//  一个消息的pid 是唯一的
//				continue;
//			}
//
//			ContentValues values = new ContentValues();
//			values.put(DBHelper.MESSAGE_TYPE, message.type);
//			
//			values.put(DBHelper.MESSAGE_PID, message.PID);
//			values.put(DBHelper.MESSAGE_SEND_ID, message.SendID);
//			values.put(DBHelper.MESSAGE_SEND_NAME, message.SendName);
//			values.put(DBHelper.MESSAGE_TITLE, message.Title);
//			values.put(DBHelper.MESSAGE_CONTENT, message.Content);
//			values.put(DBHelper.MESSAGE_SENDTIME, message.SendTime);
//			values.put(DBHelper.MESSAGE_PARENT_ID, message.ParentID);
//
//			write.insert(DBHelper.TABLE_MESSAGE, null, values);
//		}
//		
//
//		write.close();
//		db.close();
//	}
//
//	public ArrayList<Message> getMessages(Context context,Message message) {
//		ArrayList<Message> messages = null;
//		if (message == null)
//			return null;
//		LogUtils.D("getMessages message.ParentID = "+message.ParentID +" message.PID="+message.PID);
//		if ("0".equals(message.ParentID)){
//			LogUtils.D("message.ParentID = 0");
//			messages = new ArrayList<Message>();
//			messages.add(message);
//			return messages;
//		}
//
//		DBHelper db = DBHelper.getInstance(context, null);
//		SQLiteDatabase read = db.getReadableDatabase();
//
//		Cursor cursor = read.rawQuery("select * from " + DBHelper.TABLE_MESSAGE
//				+ " where " + DBHelper.MESSAGE_PARENT_ID + " = ? ",new String[]{message.ParentID});
//		if(cursor != null && cursor.getCount() > 0){
//			messages = new ArrayList<Message>();
//			while (cursor.moveToNext()) {
//				Message m= new Message();
//				m.type = cursor.getInt(cursor
//						.getColumnIndex(DBHelper.MESSAGE_TYPE));
//				m.PID = cursor.getString(cursor
//						.getColumnIndex(DBHelper.MESSAGE_PID));
//				m.SendID = cursor.getString(cursor
//						.getColumnIndex(DBHelper.MESSAGE_SEND_ID));
//				m.SendName = cursor.getString(cursor
//						.getColumnIndex(DBHelper.MESSAGE_SEND_NAME));
//				m.Title = cursor.getString(cursor
//						.getColumnIndex(DBHelper.MESSAGE_TITLE));
//				m.Content = cursor.getString(cursor
//						.getColumnIndex(DBHelper.MESSAGE_CONTENT));
//				m.SendTime = cursor.getString(cursor
//						.getColumnIndex(DBHelper.MESSAGE_SENDTIME));
//				m.ParentID = cursor.getString(cursor
//						.getColumnIndex(DBHelper.MESSAGE_PARENT_ID));
//				messages.add(m);
//			}
//			cursor.close();
//		}
//		
//		read.close();
//		db.close();
//		
//		return messages;
//	}
//
//}
