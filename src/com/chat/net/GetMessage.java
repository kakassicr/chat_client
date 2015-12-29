package com.chat.net;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.chat.Config;
import com.chat.common.Message;

public class GetMessage {
	public GetMessage(String mAccount, final SuccessCallback successCallback,final FailCallback failCallback) {
		new NetConnection(Config.SERVER_URL,HttpMethod.POST,new NetConnection.SuccessCallback() {
		@Override
		public void onSuccess(String result) {
			try {
				JSONObject jsonObject = new JSONObject(result);
				switch (jsonObject.getInt(Config.KEY_STATUS)) {
				case Config.RESULT_STATUS_SUCCESS:
					if (successCallback != null) {
						List<Message> Messages = new ArrayList<Message>();
						JSONArray messagesJsonArray = jsonObject.getJSONArray(Config.KEY_MESSAGES);
						JSONObject messageObj;
						Message msg;
						for (int i = 0; i < messagesJsonArray.length(); i++) {
							messageObj = messagesJsonArray.getJSONObject(i);
							msg = new Message(messageObj.getString(Config.KEY_CONTENT),Message.TYPE_RECEIVED);
							msg.setSender(messageObj.getString(Config.KEY_SENDER));
							msg.setGetter(messageObj.getString(Config.KEY_GETTER));
							msg.setSendTime(messageObj.getString(Config.KEY_SENDTIME));
							Messages.add(msg);
						}

						successCallback.onSuccess(Messages);
					}

					break;
				default:
					if (failCallback != null) {
						failCallback.onFail();
					}
					break;
				}

			} catch (JSONException e) {
				e.printStackTrace();
				if (failCallback != null) {
					failCallback.onFail();
				}
			}
		}
	}, new NetConnection.FailCallback() {

		@Override
		public void onFail() {
			if (failCallback != null) {
				failCallback.onFail();
			}
		}
	}, Config.KEY_ACTION, Config.ACTION_GETMESSAGE,
	Config.KEY_ACCOUNT, mAccount);
	}
	
	
	public GetMessage(String mAccount,String friend, final SuccessCallback successCallback,final FailCallback failCallback) {
		new NetConnection(Config.SERVER_URL,HttpMethod.POST,new NetConnection.SuccessCallback() {
		@Override
		public void onSuccess(String result) {
			try {
				JSONObject jsonObject = new JSONObject(result);
				switch (jsonObject.getInt(Config.KEY_STATUS)) {
				case Config.RESULT_STATUS_SUCCESS:
					if (successCallback != null) {
						List<Message> Messages = new ArrayList<Message>();
						JSONArray messagesJsonArray = jsonObject.getJSONArray(Config.KEY_MESSAGES);
						JSONObject messageObj;
						Message msg;
						for (int i = 0; i < messagesJsonArray.length(); i++) {
							messageObj = messagesJsonArray.getJSONObject(i);
							msg = new Message(messageObj.getString(Config.KEY_CONTENT),Message.TYPE_RECEIVED);
							msg.setSender(messageObj.getString(Config.KEY_SENDER));
							msg.setGetter(messageObj.getString(Config.KEY_GETTER));
							msg.setSendTime(messageObj.getString(Config.KEY_SENDTIME));
							Messages.add(msg);
						}

						successCallback.onSuccess(Messages);
					}

					break;
				default:
					if (failCallback != null) {
						failCallback.onFail();
					}
					break;
				}

			} catch (JSONException e) {
				e.printStackTrace();
				if (failCallback != null) {
					failCallback.onFail();
				}
			}
		}
	}, new NetConnection.FailCallback() {

		@Override
		public void onFail() {
			if (failCallback != null) {
				failCallback.onFail();
			}
		}
	}, Config.KEY_ACTION, Config.ACTION_GETMESSAGE,
	Config.KEY_ACCOUNT, mAccount);
	}
	public static interface SuccessCallback {
		void onSuccess(List<Message> Messages);
	}

	public static interface FailCallback {
		void onFail();
	}
}
