package com.xujinliang.ui;

import com.xujinliang.business.SocketBusiness;
import com.xujinliang.switchactivity.SwitchActivity;
import com.xujinliang.userinfo.UserInfo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class RegisterView extends Activity   implements OnClickListener{
	
	EditText registerusernameedit;
	EditText registerpasswordedit;
	EditText nicknameedit;
	EditText emailedit;
	
	public UserInfo userInfo;
	//private SocketBusiness socketbusiness;
	
	String username;
	String passwd;
	String nickname;
	String email;
	Button registerbutton;
	String data = null;
	String command = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.registerview);
		
		registerusernameedit = (EditText)findViewById(R.id.registerusernameedit);
		registerpasswordedit = (EditText)findViewById(R.id.registerpasswordedit);
		nicknameedit = (EditText)findViewById(R.id.nicknameedit);
		emailedit = (EditText)findViewById(R.id.emailedit);
		registerbutton = (Button)findViewById(R.id.registerbutton);
		registerbutton.setOnClickListener(this);
		
		userInfo = (UserInfo) getApplication();

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()) {
		case R.id.registerbutton:
			username = registerusernameedit.getText().toString();
			passwd = registerpasswordedit.getText().toString();
			nickname = nicknameedit.getText().toString();
			email = emailedit.getText().toString();
			if("".equals(username)||"".equals(passwd)||"".equals(nickname)||"".equals(email)) {
				System.out.println(" ‰»Î”–ø’");
				Toast toast = Toast.makeText(getApplicationContext(),
		    			R.string.registerwarn, Toast.LENGTH_LONG);
		    	toast.setGravity(Gravity.CENTER, 0, 0);
		    	LinearLayout toastView = (LinearLayout) toast.getView();
		   		ImageView imageCodeProject = new ImageView(getApplicationContext());
		   		imageCodeProject.setImageResource(R.drawable.warn);
		   		toastView.addView(imageCodeProject, 0);
		        toast.show();					
			} else {
				command = "Register?"+username+"?"+passwd+"?"+nickname+"?"+email;		
				data = SocketBusiness.DoSocketBusiness(command);
				String[] selectURL = data.split("\\?");
				String registerstate = selectURL[0];
				if("RegisterOK".equals(registerstate)) {
					userInfo.setValue(username, passwd, nickname, email);
					SwitchActivity.DoSwitch(this, SelectView.class);
				} else {
					Toast toast = Toast.makeText(getApplicationContext(),
			    			R.string.registeragin, Toast.LENGTH_LONG);
			    	toast.setGravity(Gravity.CENTER, 0, 0);
			    	LinearLayout toastView = (LinearLayout) toast.getView();
			   		ImageView imageCodeProject = new ImageView(getApplicationContext());
			   		imageCodeProject.setImageResource(R.drawable.warn);
			   		toastView.addView(imageCodeProject, 0);
			        toast.show();		
				}	
			}
			break;
		default:
			break;
		}
		
	}
    @Override
	public boolean onKeyDown(int keyCode, KeyEvent event){
        if(keyCode == KeyEvent.KEYCODE_BACK){
	        Intent intent = new Intent();
	        intent.setClass(RegisterView.this, LoginView.class);
	        startActivity(intent);            
	        finish();
	        return true; 	
        } else {        
            return super.onKeyDown(keyCode, event); 
        } 
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		Menu fileAbout = menu.addSubMenu(0, 1, 0, R.string.about);
		MenuInflater inflaterAbout = getMenuInflater();
		inflaterAbout.inflate(R.menu.aboutmenu, fileAbout);
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case 1:
			return true;
		case R.id.help:
			new AlertDialog.Builder(this) 
			.setTitle("Help")
			.setMessage(R.string.help)
			.setPositiveButton("OK", null)
			.setNegativeButton("Cancel", null)
			.show();
			return true;
		case R.id.our:
			new AlertDialog.Builder(this) 
			.setTitle("About author")
			.setMessage(R.string.our)
			.setPositiveButton("OK", null)
			.setNegativeButton("Cancel", null)
			.show();
			return true;		
		}
		return false;
	} 

}
