package com.chat.activity;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.chat.R;
import com.chat.common.Message;
import com.chat.db.MyDatabaseHelper;
import com.chat.net.SendMessage;
import com.chat.service.ShowMessage;

public class ChatActivity extends Activity implements OnClickListener {
	private ListView msgListView;
	private EditText inputText;
	private Button send;
	private Button back;
    private MsgAdapter adapter;
	private String friend;
	private TextView account2;
	private List<Message> msgList;
	private MyDatabaseHelper dbHelper;
	private SQLiteDatabase db;
	private ContentValues contentValues;
	private AlarmReceiverInActivity alarmReceiverInActivity=null;
	
	public class AlarmReceiverInActivity extends BroadcastReceiver {
		ArrayList<String> msgArray=new ArrayList<String>();
		String friend;
		@Override
		public void onReceive(Context context, Intent intent) {
			Log.d("Query", "AlarmReceiverInActivity.onReceive");
			friend=intent.getStringExtra("friend");
			msgArray=intent.getStringArrayListExtra("msgArray");
			if (msgArray.size()>0) {
			//显示内容
			for(int i=0;i<msgArray.size();i++){
				Message msgForShow= new Message(msgArray.get(i), Message.TYPE_RECEIVED);
				msgList.add(msgForShow);
			}
			adapter.notifyDataSetChanged(); // 当有新消息时，刷新ListView中的显示
			msgListView.setSelection(msgList.size()); // 将ListView定位到最后一行
			}
			Log.d("Query", "AlarmReceiverInActivity.msgArray:"+msgArray.size());
			msgArray.clear();
			Intent i = new Intent(context, ShowMessage.class);
			i.putExtra("account", ListActivity.account);
			i.putExtra("friend", friend);
			context.startService(i);
		}

	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		friend = getIntent().getStringExtra("friend");
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.msg);
		account2 = (TextView) this.findViewById(R.id.account2);
		account2.setText(friend);// 标题显示好友名字
		msgList = new ArrayList<Message>();
		adapter = new MsgAdapter(ChatActivity.this, R.layout.msg_item, msgList);
		inputText = (EditText) findViewById(R.id.input_text);
		send = (Button) findViewById(R.id.send);
		back = (Button) findViewById(R.id.back);
		back.setOnClickListener(this);
		msgListView = (ListView) findViewById(R.id.msg_list_view);
		msgListView.setAdapter(adapter);
		dbHelper = new MyDatabaseHelper(this, "Chat.db", null, 1);
		db = dbHelper.getWritableDatabase();
		initMsgs(); // 初始化消息数据
		msgListView.setSelection(msgList.size()); // 将ListView定位到最后一行	
		Log.d("ChatActivity", "------------1------------");
		Intent intent = new Intent(ChatActivity.this, ShowMessage.class);//启动后台服务从数据库得到新消息
		intent.putExtra("account", ListActivity.account);
		intent.putExtra("friend", friend);
		startService(intent);	
		//注册广播接收器
		alarmReceiverInActivity=new AlarmReceiverInActivity();
		IntentFilter filter=new IntentFilter();
		filter.addAction("com.chat.activity.ShowMessageService");
		registerReceiver(alarmReceiverInActivity, filter);
		Log.d("Query", "onCreatefriend:"+friend);
//		Intent bindIntent = new Intent(this, ShowMessage.class);
//		bindService(bindIntent, connection, BIND_AUTO_CREATE); // 绑定服务
		send.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				send();//显示内容并发送内容至服务器保存
			}
		});
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unregisterReceiver(alarmReceiverInActivity);
	}

	private void initMsgs() {
		String sql = "(sender=? and getter=?) or (sender=? and getter=?)";
		String[] paras={ListActivity.account,friend,friend,ListActivity.account};
		Cursor cursor = db.query("Messages", null, sql, paras, null, null, null);
		if (cursor.moveToFirst()) {
			do {
				// 遍历Cursor对象，取出数据并打印
				String sender = cursor.getString(cursor
						.getColumnIndex("sender"));
				String getter = cursor.getString(cursor
						.getColumnIndex("getter"));
				String content = cursor.getString(cursor
						.getColumnIndex("content"));
				String sendTimer = cursor.getString(cursor
						.getColumnIndex("sendTimer"));
				Message msg;
				if (sender.equals(ListActivity.account)) {
					msg = new Message(content, Message.TYPE_SENT);
				} else {
					msg = new Message(content, Message.TYPE_RECEIVED);
				}
				msg.setSender(sender);
				msg.setGetter(getter);
				msg.setSendTime(sendTimer);
				msgList.add(msg);
			} while (cursor.moveToNext());
		}
		cursor.close();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
//			Intent bindIntent = new Intent(this, QueryMessage.class);
//			bindService(bindIntent, connection, BIND_AUTO_CREATE); // 绑定服务
//			Intent intent = new Intent(this, QueryMessage.class);//启动后台服务从数据库得到新消息
//			intent.putExtra("account", ListActivity.account);
//			intent.putExtra("friend", ListActivity.account);
//			unbindService(connection); // 解绑服务
			Intent stopIntent = new Intent(this, ShowMessage.class);//停止服务
			stopService(stopIntent);
			Intent intentActivity = new Intent(this, ListActivity.class);
			startActivity(intentActivity);
			finish();
			break;
		default:
			break;
		}
	}

	public void send() {
		try {
			String content = inputText.getText().toString();

			if (!"".equals(content)) {
				Message msg = new Message(content, Message.TYPE_SENT);
				msg.setSender(ListActivity.account);
				msg.setGetter(friend);
				String sendtime=new Timestamp(System.currentTimeMillis()).toString();
				msg.setSendTime(sendtime);
				insetMessage(msg);// 写入本地数据库
				msgList.add(msg);
				Log.d("ChatActivity", "------------2------------");
//				Log.d("ChatActivity", oos.toString());
				adapter.notifyDataSetChanged(); // 当有新消息时，刷新ListView中的显示
				msgListView.setSelection(msgList.size()); // 将ListView定位到最后一行
				inputText.setText("");
				new SendMessage(ListActivity.account, friend, content, sendtime, new SendMessage.SuccessCallback() {
					@Override
					public void onSuccess() {
						
					}
				}, new SendMessage.FailCallback() {
					@Override
					public void onFail() {
						Toast.makeText(ChatActivity.this, R.string.fail_to_send_message, Toast.LENGTH_LONG).show();
					}
				});
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void insetMessage(Message msg) {
		contentValues = new ContentValues();
		contentValues.put("sender", msg.getSender());
		contentValues.put("getter", msg.getGetter());
		contentValues.put("content", msg.getCon());
		contentValues.put("sendTimer", msg.getSendTime());
		contentValues.put("isGet", msg.getIsGet());
		db.insert("Messages", null, contentValues); // 插入数据
		contentValues.clear();
	}
	
	
}