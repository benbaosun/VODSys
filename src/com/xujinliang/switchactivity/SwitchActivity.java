package com.xujinliang.switchactivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class SwitchActivity {
	
	public static void  DoSwitch(Activity myContext,Class<?> myCls,String string) {
		myContext.finish();
		Intent intent = new Intent();
	    intent.setClass(myContext,myCls );
	    Bundle bundle = new Bundle();
	    bundle.putString("keyUri", string);
	    intent.putExtras(bundle);
	    myContext.startActivity(intent);
		
	}
	public static void  DoSwitch(Activity myContext,Class<?> myCls) {
		myContext.finish();
		Intent intent = new Intent();
	    intent.setClass(myContext,myCls );
	    myContext.startActivity(intent);
		
	}
	public static String DoGetString(Activity myContext,String uri) {    
		Bundle bundle =myContext.getIntent().getExtras();
		String string;
        if(bundle!=null){
            string = bundle.getString(uri);
        } else {
        	string = null;
        }
		return string;
	}
}
