package com.xujinliang.business;

import com.xujinliang.ipport.IpPort;
import com.xujinliang.socket.UseSocket;

public class SocketBusiness {
	static IpPort netAddress;
	static int STATE = 1;
	static String data = null;
	static String command = "";

	public static String  DoSocketBusiness(String incommand) {
		netAddress = new IpPort("10.109.16.148",0x8888);
		command = incommand;
		STATE = 1;
		new Thread(runnable).start();
		while(1 == STATE);
		return data;	
	}
	static Runnable runnable = new Runnable(){
	    @Override
	    public void run() {
	    	UseSocket  useSocket = new UseSocket(netAddress,command);
	        data =  useSocket.getData();     
	        STATE = 0;    
	    }
	};
}
