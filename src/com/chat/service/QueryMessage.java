package com.chat.service;

import java.util.List;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;

import com.chat.common.Message;
import com.chat.db.MyDatabaseHelper;
import com.chat.net.GetMessage;

public class QueryMessage extends Service {
	private MyDatabaseHelper dbHelper;
	private SQLiteDatabase db;
	private ContentValues contentValues;
	private Message msg;
	private AlarmManager manager;
	private PendingIntent pi;
	private String mAccount;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		mAccount=intent.getStringExtra("account");
		Log.d("Query", "mAccount:"+mAccount);
		new GetMessage(mAccount, new GetMessage.SuccessCallback() {
			@Override
			public void onSuccess(List<Message> Messages) {
				dbHelper = new MyDatabaseHelper(QueryMessage.this, "Chat.db", null, 1);
				db = dbHelper.getWritableDatabase();
				contentValues = new ContentValues();
				for(int i=0;i<Messages.size();i++){
					msg=Messages.get(i);
					contentValues.put("sender", msg.getSender());
					contentValues.put("getter", msg.getGetter());
					contentValues.put("content", msg.getCon());
					contentValues.put("sendTimer", msg.getSendTime());
					contentValues.put("isGet", 0);
					db.insert("Messages", null, contentValues); // 插入数据
					contentValues.clear();
				}
			}
		},new GetMessage.FailCallback() {
			
			@Override
			public void onFail() {
				
			}
		});
		manager = (AlarmManager) getSystemService(ALARM_SERVICE);
		int Time = 5 * 1000; // 这是5秒的毫秒数
		long triggerAtTime = SystemClock.elapsedRealtime() + Time;
		Intent i = new Intent(QueryMessage.this, AlarmReceiver.class);
		pi = PendingIntent.getBroadcast(QueryMessage.this, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
		manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);	
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		manager.cancel(pi);
		Log.d("Query", "QueryMessage.onDestroy");
		super.onDestroy();
	}
	
}
