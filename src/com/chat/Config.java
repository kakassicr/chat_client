package com.chat;

public class Config {
	public static final String CHARSET = "utf-8";
//	public static final String SERVER_URL = "http://169.254.25.129:8080/ChatServer/actionser";
	public static final String SERVER_URL = "http://192.168.0.104:8080/ChatServer/actionser";
	
	public static final String KEY_ACTION = "action";
	public static final String KEY_ACCOUNT = "account";
	public static final String KEY_FRIEND = "friend";
	public static final String KEY_PASSWORD = "password";
	public static final String KEY_SENDER = "sender";
	public static final String KEY_GETTER = "getter";
	public static final String KEY_CONTENT = "content";
	public static final String KEY_SENDTIME = "sendtime";
	public static final String KEY_STATUS = "status";
	public static final String KEY_MESSAGES = "Messages";	
	
	public static final int RESULT_STATUS_SUCCESS = 1;
	public static final int RESULT_STATUS_FAIL = 2;
	
	public static final String ACTION_LOGIN = "login";
	public static final String ACTION_SENDMESSAGE = "sendmessage";
	public static final String ACTION_GETMESSAGE = "getmessage";

}

