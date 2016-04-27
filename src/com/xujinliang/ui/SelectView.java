package com.xujinliang.ui;

import com.xujinliang.business.SocketBusiness;
import com.xujinliang.switchactivity.SwitchActivity;
import com.xujinliang.userinfo.UserInfo;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class SelectView extends Activity implements OnClickListener {

	public UserInfo userInfo;
	private ListView listView;
	private Button toPlay;
	private TextView userInfoSelectText;
	String uri=null;
	String [] nameList = null;
	String data = null;
	String command = "";
    String[] songArray;
    String selectedStr;    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.selectview);

		listView = (ListView) findViewById(R.id.listview);
		toPlay=(Button)findViewById(R.id.toplay);
		userInfoSelectText = (TextView)findViewById(R.id.userInfoSelectText);
		toPlay.setOnClickListener(this);
		selectedStr = (String)this.getString(R.string.selected);	
		
		
		command = "AllSong";//getNameList,URL
		data = SocketBusiness.DoSocketBusiness(command);
		nameList = data.split("\\?");	
		ArrayAdapter<String> arrAdapter = new ArrayAdapter<String>(this
				,android.R.layout.simple_list_item_single_choice,nameList);
		listView.setAdapter(arrAdapter);
		listView.setOnItemClickListener(new OnItemClickedListener());
		toPlay.setOnClickListener(this);
		
		userInfo = (UserInfo) getApplication();
		userInfoSelectText.setText("           当前用户：  "+userInfo.getName());	
	}
	class OnItemClickedListener implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> parent, View v, int position,
				long id) {
			// TODO Auto-generated method stub
			String songName=nameList[position];
			command = "URL?"+songName;
			data = SocketBusiness.DoSocketBusiness(command);
			String[] selectURL = data.split("\\?");
			uri = selectURL[0];
	
	    	Toast toast = Toast.makeText(getApplicationContext(),
	    			"<"+songName+">"+selectedStr, Toast.LENGTH_LONG);
	    	toast.setGravity(Gravity.CENTER, 0, 0);
	    	toast.show();
		}		
	}
	@Override
	public void onClick(View v) {
		String backUri;
		backUri = SwitchActivity.DoGetString(this, "keyUri");
        if(uri==null){
        	if(backUri!=null){
        		uri=backUri;
        	} else {
        		command = "URL?"+songArray[0];
        		data = SocketBusiness.DoSocketBusiness(command);
    			String[] selectURL = data.split("\\?");
    			uri = selectURL[0];
        	}
        }
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.toplay:
			SwitchActivity.DoSwitch(SelectView.this,PlayerView.class,uri);
		    break;
		    default:
		    	break;
		}	
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		Menu file = menu.addSubMenu(0, 1, 0, R.string.about);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.aboutmenu, file);
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
    @Override
	public boolean onKeyDown(int keyCode, KeyEvent event){
        if(keyCode == KeyEvent.KEYCODE_BACK){      	
        	userInfo.setValue("", "", "", "");
        	SwitchActivity.DoSwitch(SelectView.this, LoginView.class); 	
	        return true; 	
        } else {        
            return super.onKeyDown(keyCode, event); 
        } 
	}
}
