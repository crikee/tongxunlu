package com.gsf;


public class SmsMsg {
	
	private String 	body;
	private String 	date;
	private int 	bg;
	private int     fouth ;
	
	public SmsMsg(String body, String date) {
		this.body = body;
		this.date = date;
	}
	
	public String getBody() {
		return body;
	}
	
	public void setBody(String body) {
		this.body = body;
	}
	
	public String getDate() {
		return date;
	}
	
	public void setDate(String date) {
		this.date = date;
	}
	
	public int getBg() {
		return bg;
	}
	
	public void setBg(int bg) {
		this.bg = bg;
	}
	
	
}
