package com.chat.net;

import com.chat.Config;

public class Login {

	public Login(String account,String password,final SuccessCallback successCallback,final FailCallback failCallback) {
		new NetConnection(Config.SERVER_URL, HttpMethod.POST, new NetConnection.SuccessCallback() {
			
			@Override
			public void onSuccess(String result) {
						if (successCallback!=null&(!result.equals(Config.RESULT_STATUS_FAIL))) {
							successCallback.onSuccess(result);
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
		}, Config.KEY_ACTION,Config.ACTION_LOGIN,
		Config.KEY_ACCOUNT,account,
		Config.KEY_PASSWORD,password);
	}
	
	public static interface SuccessCallback{
		void onSuccess(String friendlist);
	}
	
	public static interface FailCallback{
		void onFail();
	}
}
