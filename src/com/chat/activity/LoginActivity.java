package com.chat.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.chat.R;
import com.chat.db.MyDatabaseHelper;
import com.chat.net.Login;

public class LoginActivity extends Activity {
	private Button login;
	private EditText account;
	private EditText password;
	private String mAccount, mPassword;
	private MyDatabaseHelper dbHelper;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		dbHelper = new MyDatabaseHelper(LoginActivity.this, "Chat.db", null, 1);//创建数据库
		login = (Button) findViewById(R.id.login);
		login.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {		
				account = (EditText) findViewById(R.id.account);
				password = (EditText) findViewById(R.id.password);
				mAccount = account.getText().toString();
				mPassword = password.getText().toString();
				dbHelper.getWritableDatabase();
				new Login(mAccount, mPassword, new Login.SuccessCallback() {
					
					@Override
					public void onSuccess(String friendlist) {
						Intent i = new Intent(LoginActivity.this, ListActivity.class);
						ListActivity.account=mAccount;
						ListActivity.friendlist=friendlist.split(" ");
						startActivity(i);
						finish();
					}
				}, new Login.FailCallback() {
					
					@Override
					public void onFail() {
						Toast.makeText(LoginActivity.this, R.string.fail_to_login, Toast.LENGTH_LONG).show();
					}
				});
			}
		});
	}
}
