package com.xujinliang.socket;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

import com.xujinliang.ipport.IpPort;

public class UseSocket {

	IpPort netAddress;
	String command = "";
	public UseSocket(IpPort netAddress,String command) {
		this.netAddress = netAddress;
		this.command = command;
	}
 
	public String getData() {
		String address = netAddress.getIP();
		int port = netAddress.getPort();
		String allItem="";
		int length;
		byte[] b = new byte[1024];
        Socket client = new Socket();
        InetSocketAddress isa = new InetSocketAddress(address, port);    
        try {
        	client.connect(isa, 10000);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("failed to connect to the server");
		}
            
		try {
			BufferedOutputStream out = new BufferedOutputStream(client
					.getOutputStream());
			BufferedInputStream in = new BufferedInputStream(client
					.getInputStream());
	        out.write(command.getBytes());
	        out.flush();

            while ((length = in.read(b)) > 0)//
            {
            	allItem += new String(b, 0, length);
            }
            int pos = allItem.indexOf("$");

            allItem = allItem.substring(0, pos);
  
	        out.close();
	        out = null;
            in.close();
            in = null;
	        client.close();
	        client = null;
	            
		} catch (IOException e) {
			System.out.println("failed at the I/O stream");
		}		
		return allItem;
	}



}
