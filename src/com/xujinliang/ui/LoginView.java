package com.xujinliang.ui;



import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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

import com.xujinliang.switchactivity.SwitchActivity;
import com.xujinliang.userinfo.UserInfo;
import com.xujinliang.business.*;

public class LoginView extends Activity  implements OnClickListener{
	
	EditText username_edit;
	EditText password_edit;
	Button login_button;
	Button to_register;
	
	public UserInfo userInfo;
	String username;
	String password;
	String loginstate;
	String data = null;
	String command = "";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.loginview);
		
		username_edit = (EditText)findViewById(R.id.username_edit);
		password_edit = (EditText)findViewById(R.id.password_edit);
		login_button =  (Button)findViewById(R.id.login_button);
		to_register = (Button)findViewById(R.id.to_register);
		login_button.setOnClickListener(this);
		to_register.setOnClickListener(this);
		userInfo = (UserInfo) getApplication();
		
		
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()) {
			case R.id.login_button:
				username = username_edit.getText().toString();
				password = password_edit.getText().toString();
				if("".equals(username)||"".equals(password)) {
					Toast toast = Toast.makeText(getApplicationContext(),
			    			R.string.nameAndpasswd, Toast.LENGTH_LONG);
			    	toast.setGravity(Gravity.CENTER, 0, 0);
			    	LinearLayout toastView = (LinearLayout) toast.getView();
			   		ImageView imageCodeProject = new ImageView(getApplicationContext());
			   		imageCodeProject.setImageResource(R.drawable.warn);
			   		toastView.addView(imageCodeProject, 0);
			        toast.show();			
				} else {
					command = "Login?"+username+"?"+password;
					data = SocketBusiness.DoSocketBusiness(command);
					String[] selectURL = data.split("\\?");
					loginstate = selectURL[0];
					if("LoginOK".equals(loginstate)) {
						userInfo.setValue(selectURL[1], selectURL[2], selectURL[3], selectURL[4]);
						SwitchActivity.DoSwitch(this, SelectView.class);
						
					} else {
						Toast toast = Toast.makeText(getApplicationContext(),
				    			R.string.warnToRegister, Toast.LENGTH_LONG);
				    	toast.setGravity(Gravity.CENTER, 0, 0);
				    	LinearLayout toastView = (LinearLayout) toast.getView();
				   		ImageView imageCodeProject = new ImageView(getApplicationContext());
				   		imageCodeProject.setImageResource(R.drawable.warn);
				   		toastView.addView(imageCodeProject, 0);
				        toast.show();						
					}	
				}
				
				
				break;
			case R.id.to_register:
				LoginView.this.finish();
				Intent intent = new Intent();
			    intent.setClass(this,RegisterView.class );
			    startActivity(intent);		
				break;
			default:
				break;
		}
		
	}
    @Override
	public boolean onKeyDown(int keyCode, KeyEvent event){

    	if(keyCode == KeyEvent.KEYCODE_BACK){
            new AlertDialog.Builder(this) 
                .setIcon(R.drawable.exit_yes_no_icon) 
                .setTitle(R.string.exit_yes_no_title) 
                .setMessage(R.string.exit_yes_no_confirm) 
                .setNegativeButton(R.string.exit_yes_no_no, new DialogInterface.OnClickListener() { 
                    @Override 
                    public void onClick(DialogInterface dialog, int which) {
                    } 
                    }) 
                .setPositiveButton(R.string.exit_yes_no_yes, new DialogInterface.OnClickListener()
                { 
                    public void onClick(DialogInterface dialog, int whichButton) {                	
                    	finish();
                    } 
                }).show(); 
          
            return true; 
        } else  {        
            return super.onKeyDown(keyCode,event); 
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
