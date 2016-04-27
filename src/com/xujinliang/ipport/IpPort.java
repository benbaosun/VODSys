package com.xujinliang.ipport;

public class IpPort {
	String myIP;
	int myPort;

	public  IpPort(String myIP,int myPort) {
		this.myIP = myIP;
		this.myPort = myPort;
		
	}
	public String getIP() {
		return myIP;
	}
	public int getPort() {
		return myPort;
	}

}
