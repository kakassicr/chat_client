package com.chat.net;

import com.chat.Config;

public class SendMessage {
	public SendMessage(String sender,String getter,String content,String sendtime,final SuccessCallback successCallback,final FailCallback failCallback) {
		new NetConnection(Config.SERVER_URL, HttpMethod.POST, new NetConnection.SuccessCallback() {
			
			@Override
			public void onSuccess(String result) {

						if (successCallback!=null&(result.equals(Config.RESULT_STATUS_SUCCESS))) {
							successCallback.onSuccess();
						}else if(successCallback!=null&result.equals(Config.RESULT_STATUS_FAIL)){
							failCallback.onFail();
						}
						
		}
			}, new NetConnection.FailCallback() {
			
			@Override
			public void onFail() {
				if (failCallback!=null) {
					failCallback.onFail();
				}
			}
		}, Config.KEY_ACTION,Config.ACTION_SENDMESSAGE,
		Config.KEY_SENDER,sender,
		Config.KEY_GETTER,getter,
		Config.KEY_CONTENT,content,
		Config.KEY_SENDTIME,sendtime);
	}
	
	public static interface SuccessCallback{
		void onSuccess();
	}
	
	public static interface FailCallback{
		void onFail();
	}
}
