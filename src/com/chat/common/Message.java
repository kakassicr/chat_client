package com.chat.common;

import java.io.Serializable;

public class Message implements Serializable{

	public static final int TYPE_RECEIVED = 0;
	public static final int TYPE_SENT = 1;
	private int type;
	private String sender;
	private String getter;
	private String con;
	private String sendTime;
	private int isGet;
	public int getIsGet() {
		return isGet;
	}
	public void setIsGet(int isGet) {
		this.isGet = isGet;
	}
	public Message(String con, int type) {
		this.con = con;
		this.type = type;
	}
	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getGetter() {
		return getter;
	}

	public void setGetter(String getter) {
		this.getter = getter;
	}

	public String getCon() {
		return con;
	}

	public void setCon(String con) {
		this.con = con;
	}

	public String getSendTime() {
		return sendTime;
	}

	public void setSendTime(String sendTime) {
		this.sendTime = sendTime;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getType() {
		return type;
	}
}
