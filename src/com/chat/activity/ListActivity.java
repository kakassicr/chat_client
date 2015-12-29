package com.chat.activity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.chat.R;
import com.chat.service.QueryMessage;

public class ListActivity extends Activity implements OnClickListener{
	public static String account;
	public static String[] friendlist;//好友列表
	private Button back;
	private TextView account2;//标题中自己的用户名
	ContentValues contentValues;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = new Intent(this, QueryMessage.class);//启动后台服务从数据库得到新消息
		intent.putExtra("account", ListActivity.account);
		startService(intent);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.friendlist);
		back=(Button) findViewById(R.id.back);//返回LoginActivity
		back.setOnClickListener(this);
		account2=(TextView) findViewById(R.id.account2);//标题中自己的用户名
		account2.setText(account);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(
				ListActivity.this, android.R.layout.simple_list_item_1, friendlist);
		ListView listView = (ListView) findViewById(R.id.list_view);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int index,
					long arg3) {
				Intent stopIntent = new Intent(ListActivity.this, QueryMessage.class);//停止服务
				stopService(stopIntent);
					Intent intentActivity = new Intent(ListActivity.this,ChatActivity.class);
					intentActivity.putExtra("friend", friendlist[index]);
					startActivity(intentActivity);
					finish();
			}
		});
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			Intent stopIntent = new Intent(this, QueryMessage.class);//停止服务
			stopService(stopIntent);
			Intent intent = new Intent(this, LoginActivity.class);
			startActivity(intent);
			finish();
			break;
		default:
			break;
		}
	}
}
