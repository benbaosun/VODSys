package com.xujinliang.decview;


import com.xujinliang.ui.R;

import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;


public class DecView {
	
	public native void Decorate();
	static {
		System.loadLibrary("DecView");
	}
	
	
	TextView textView;
	String info;
	public DecView(TextView textView,String info) {
		this.textView = textView;
		this.info = info;
	}

	public void CallFromC() {
		int color=android.graphics.Color.WHITE;
		int n= (int) (Math.random()*10);
		if(n<2) {
			color =  android.graphics.Color.GREEN; 
		} else if(n<4) {
			color =  android.graphics.Color.YELLOW;
		} else if(n<6) {
			color =  android.graphics.Color.RED;
		} else if(n<8) {
			color =  android.graphics.Color.CYAN;
		} else if(n<10) {
			color =  android.graphics.Color.LTGRAY;
		} else {
			color =  android.graphics.Color.WHITE;
		}
		textView.setText(info);
		textView.setTextColor(color);
		textView.setBackgroundResource(R.drawable.corner);
		textView.setMovementMethod(ScrollingMovementMethod.getInstance()); 

	}
}
