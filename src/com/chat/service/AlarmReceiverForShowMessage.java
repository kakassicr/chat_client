package com.chat.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.chat.activity.ListActivity;

public class AlarmReceiverForShowMessage extends BroadcastReceiver {
	String friend;
	@Override
	public void onReceive(Context context, Intent intent) {
		Log.d("Query", "AlarmReceiverForShowMessage.onReceive");
		friend=intent.getStringExtra("friend");
		Log.d("Query", "AlarmReceiverForShowMessage.friend:"+friend);
		Intent i = new Intent(context, ShowMessage.class);
		i.putExtra("account", ListActivity.account);
		i.putExtra("friend", friend);
		context.startService(i);
	}

}
