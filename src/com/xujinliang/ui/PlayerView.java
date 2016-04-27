package com.xujinliang.ui;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import com.xujinliang.business.SocketBusiness;
import com.xujinliang.decview.DecView;
import com.xujinliang.switchactivity.SwitchActivity;
import com.xujinliang.userinfo.UserInfo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnInfoListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.MediaPlayer.OnSeekCompleteListener;
import android.media.MediaPlayer.OnVideoSizeChangedListener;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView; 

public class PlayerView extends Activity implements 
OnCompletionListener,OnErrorListener,OnInfoListener,
OnPreparedListener,OnBufferingUpdateListener,OnSeekCompleteListener,
OnVideoSizeChangedListener,SurfaceHolder.Callback,
MediaController.MediaPlayerControl{
	
	MediaController controller;
	Display currentDisplay;
	SurfaceView surfaceView;
	SurfaceHolder surfaceHolder;
	MediaPlayer mediaPlayer;
	
	public UserInfo userInfo;
	private TextView userInfoPlayerText;
	private TextView videoInfoPlayerText;
	
	int videoWidth = 0;
	int videoHeight = 0;
	
	boolean readyToPlay = false;
	String filePath;
	String infostate;
	String videoInfo = "";
	String data = null;
	String command = "";
	
	public final static String LOGTAG = "MyVideoPlayer";
	
	
	private Timer timer = new Timer();
	private TimerTask task = new TimerTask() {
    	@Override
    	public void run() {
    		Message message = new Message();
    	    message.what = 200;
    	    handler.sendMessage(message);
    	}
    };

	final  Handler handler = new Handler() {  
        public void handleMessage(Message msg) {   
            switch (msg.what) {   
             	  
                 default:
                	DecView decview = new DecView(videoInfoPlayerText,videoInfo);
             		decview.Decorate();

                	super.handleMessage(msg);
               	  break;
            }   
            super.handleMessage(msg);   
       }   
	};



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        
    	
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.playerview);

        userInfoPlayerText = (TextView)findViewById(R.id.userInfoPlayerText);
        videoInfoPlayerText = (TextView)findViewById(R.id.videoInfoPlayerText);
        videoInfoPlayerText.setVisibility(0x00000004);
        surfaceView = (SurfaceView) this.findViewById(R.id.SurfaceView);
        surfaceHolder = surfaceView.getHolder();
        
        surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        
        mediaPlayer = new MediaPlayer();

        mediaPlayer.setOnCompletionListener(this);
        mediaPlayer.setOnErrorListener(this);
        mediaPlayer.setOnInfoListener(this);
        mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.setOnSeekCompleteListener(this);
        mediaPlayer.setOnVideoSizeChangedListener(this);    
        mediaPlayer.setOnBufferingUpdateListener(this);
        
        userInfo = (UserInfo) getApplication();
        userInfoPlayerText.setText("           当前用户：  "+userInfo.getName());
        userInfoPlayerText.setBackgroundColor(android.graphics.Color.GRAY);
      
        filePath = SwitchActivity.DoGetString(this, "keyUri");
		command = "VideoInfo?"+filePath;
		data = SocketBusiness.DoSocketBusiness(command);
		System.out.println("data:"+data);//data contains nothing
		String[] selectURL = data.split("\\?");
		System.out.println(data);
		infostate = selectURL[0];
		if("InfoOK".equals(infostate)) {
			String name = selectURL[1] ;
			String author = selectURL[2];
			String desc = selectURL[3];
			String URL = selectURL[4];
			videoInfo ="Name:《"+name+"》\n";
			videoInfo += "Author:"+author+"\n";
			videoInfo += "URL:"+URL+"\n";
			videoInfo += "Description:"+desc+"\n";	
		} else {
			System.out.println("InfoFalse");
		}
  
        try { 
        	Log.v(LOGTAG,filePath);
        	mediaPlayer.setDataSource(filePath);    
        } catch (IllegalArgumentException e) {
        	Log.v(LOGTAG,"setDataSource:ARGUMENT ERROR");
        	pause();
        } catch (IllegalStateException e) {
        	Log.v(LOGTAG,"setDataSource:STATE ERROR");
        	pause();
        } catch (IOException e) {
        	Log.v(LOGTAG,"setDataSource:IO ERROR");
        	pause();
        }
        currentDisplay = getWindowManager().getDefaultDisplay();
        controller = new MediaController(this);
    }
    
    public void surfaceCreated(SurfaceHolder holder){
    	Log.v(LOGTAG,"surfaceCreated Called");  	
    	mediaPlayer.setDisplay(holder); 	
    	try {
    		mediaPlayer.setScreenOnWhilePlaying(true);
    		mediaPlayer.prepareAsync();
    	} catch(IllegalStateException e) {
    		Log.v(LOGTAG,"surfaceCreated:STATE ERROR");
        	pause();
    	}
    }  
    public void surfaceChanged(SurfaceHolder holder,int format,int width,int height){
    	Log.v(LOGTAG,"surfaceChanged Called");
    }
    @Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		Log.v(LOGTAG,"onsurfaceDestroyed Called");
	}
    public void onCompletion(MediaPlayer mp) {
    	Log.v(LOGTAG,"onCompletion Called");
    	
    	Toast toast = Toast.makeText(getApplicationContext(),
    			(String)this.getString(R.string.toTheEnd), Toast.LENGTH_LONG);
    		   toast.setGravity(Gravity.CENTER, 0, 0);
    		   LinearLayout toastView = (LinearLayout) toast.getView();
    		   ImageView imageCodeProject = new ImageView(getApplicationContext());
    		   imageCodeProject.setImageResource(R.drawable.over);
    		   toastView.addView(imageCodeProject, 0);
        toast.show();
    	Intent intent = new Intent();
    	intent.setClass(PlayerView.this, SelectView.class);
    	startActivity(intent);
        mediaPlayer.stop();
        mediaPlayer.release(); 
        timer.cancel();
    	
    }
    public boolean onError(MediaPlayer mp,int whatError,int extra) {
    	Log.v(LOGTAG,"onError Called");
    	if(whatError == MediaPlayer.MEDIA_ERROR_SERVER_DIED) {
    		Log.v(LOGTAG,"onError:SERVER DIE ERROR");
    	} else if (whatError == MediaPlayer.MEDIA_ERROR_UNKNOWN) {
    		Log.v(LOGTAG,"onError:UNKNOWN ERROR");
    	}
    	return false;
    }
    public boolean onInfo(MediaPlayer mp,int whatInfo,int extra) {
    	if (whatInfo == MediaPlayer.MEDIA_INFO_BAD_INTERLEAVING) {
    		Log.v(LOGTAG,"Media Info,Media Info Bad Interleaving "+extra);
    	} else if(whatInfo == MediaPlayer.MEDIA_INFO_NOT_SEEKABLE) {
    		Log.v(LOGTAG,"Media Info,Media Info Not Seekable "+extra);
    	} else if(whatInfo == MediaPlayer.MEDIA_INFO_UNKNOWN) {
    		Log.v(LOGTAG,"Media Info,Media Info Unknown "+extra);
    	} else if(whatInfo == MediaPlayer.MEDIA_INFO_VIDEO_TRACK_LAGGING) {
    		Log.v(LOGTAG,"Media Info,Media Info Video Track Lagging "+extra);
    	} else if(whatInfo == MediaPlayer.MEDIA_INFO_METADATA_UPDATE) {
    		Log.v(LOGTAG,"Media Info,Media Info Metadata Update "+extra);
    	}
    	return false;
    }
    public void onPrepared(MediaPlayer player){
    	Log.v(LOGTAG,"onPrepared Called");
    	
    	videoWidth = player.getVideoWidth();
    	videoHeight = player.getVideoHeight();
    	
    	Log.v(LOGTAG,"Width:"+videoWidth);
    	Log.v(LOGTAG,"Height:"+videoHeight);
    	
    	if(videoWidth >currentDisplay.getWidth()||videoHeight >currentDisplay.getHeight()){
    		float heightRatio = (float)videoHeight/(float)currentDisplay.getHeight();
    		float widthRatio = (float)videoWidth/(float)currentDisplay.getWidth();
    		
    		if(heightRatio >1 || widthRatio >1){
    			if(heightRatio>widthRatio){
    				videoHeight=(int)Math.ceil((float)videoHeight/(float)heightRatio);
        			videoWidth=(int)Math.ceil((float)videoWidth/(float)heightRatio);
    			}else{
    				videoHeight=(int)Math.ceil((float)videoHeight/(float)widthRatio);
        			videoWidth=(int)Math.ceil((float)videoWidth/(float)widthRatio);
    			}  			
    		}
    	}
    	
    	surfaceView.setLayoutParams(new LinearLayout.LayoutParams(videoWidth, videoHeight));
    	controller.setMediaPlayer(this);
        controller.setAnchorView(this.findViewById(R.id.rtspQosMainView));
        controller.setEnabled(true);
        controller.show();
        player.setScreenOnWhilePlaying(true);
        
        player.start();
    }
    public void onSeekComplete(MediaPlayer mp){
    	Log.v(LOGTAG,"onSeekComplete Called");
    }
    
    public void onVideoSizeChanged(MediaPlayer mp,int width,int height){
    	mp.pause();
    
    	Log.v(LOGTAG,"onVideoSizeChanged Called");
    	
    	videoWidth = mp.getVideoWidth();
    	videoHeight = mp.getVideoHeight();
    	
    	Log.v(LOGTAG,"Width:"+videoWidth);
    	Log.v(LOGTAG,"Height:"+videoHeight);
    	
    	if(videoWidth >currentDisplay.getWidth()||videoHeight >currentDisplay.getHeight()){
    		
    		float heightRatio = (float)videoHeight/(float)currentDisplay.getHeight();
    		float widthRatio = (float)videoWidth/(float)currentDisplay.getWidth();
    		
    		if(heightRatio >1 || widthRatio >1){
    			if(heightRatio>widthRatio){
    				videoHeight=(int)Math.ceil((float)videoHeight/(float)heightRatio);
        			videoWidth=(int)Math.ceil((float)videoWidth/(float)heightRatio);
    			}else{
    				videoHeight=(int)Math.ceil((float)videoHeight/(float)widthRatio);
        			videoWidth=(int)Math.ceil((float)videoWidth/(float)widthRatio);
    			}			
    		}
    	}
    	surfaceView.setLayoutParams(new LinearLayout.LayoutParams(videoWidth, videoHeight));
    	mp.start();
    }

    public void onBufferingUpdate(MediaPlayer mp,int bufferedPercent) {
    	Log.v(LOGTAG,"MediaPlayer Buffering:"+bufferedPercent+"%");
    }
    
    public boolean canPause(){
    	return true;
    }
    public boolean canSeekBackward(){
    	return true;
    }
    public boolean canSeekForward(){
    	return true;
    }
    public int getBufferPercentage(){
    	return 0;
    }
    public int getCurrentPosition(){
    	return mediaPlayer.getCurrentPosition();
    }
    public int getDuration(){
    	return mediaPlayer.getDuration();
    }
    public boolean isPlaying(){
    	return mediaPlayer.isPlaying();
    }
    public void pause(){
    	
    	if(mediaPlayer.isPlaying()){
    		mediaPlayer.pause();
    	}
    }
    public void seekTo(int pos){
    	mediaPlayer.seekTo(pos);
    }
    public void start(){
    	mediaPlayer.start();
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent ev){
    	if(controller.isShowing()){
    		controller.hide();
    	} else {	
    		controller.show();   			
    	}
    	return false;
    }

	@Override
	public int getAudioSessionId() {
		// TODO Auto-generated method stub
		return 0;
	}
    @Override
	public boolean onKeyDown(int keyCode, KeyEvent event){
        if(keyCode == KeyEvent.KEYCODE_BACK){
        	if(videoInfoPlayerText.isShown()) {
        		videoInfoPlayerText.setVisibility(0x00000004);//set invisible
				
				return true;
			} else {
				mediaPlayer.stop();
				mediaPlayer.release();
				SwitchActivity.DoSwitch(PlayerView.this, SelectView.class,filePath);
				return true; 
			}
        } else {        
            return super.onKeyDown(keyCode, event); 
        } 
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, 2, 0, R.string.info);
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
		case 2:
			videoInfoPlayerText.setVisibility(0x00000000);
	        timer.schedule(task, 100,2000);
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
