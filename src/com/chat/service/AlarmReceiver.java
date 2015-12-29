package com.chat.service;

import com.chat.activity.ListActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class AlarmReceiver extends BroadcastReceiver {
	String friend;
	@Override
	public void onReceive(Context context, Intent intent) {
		Intent i = new Intent(context, QueryMessage.class);
		i.putExtra("account", ListActivity.account);
		context.startService(i);
	}

}
