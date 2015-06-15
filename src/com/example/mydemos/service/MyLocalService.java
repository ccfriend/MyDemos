package com.example.mydemos.service;

import android.app.Notification;
import android.app.Notification.Builder;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.example.mydemos.R;

public class MyLocalService extends Service {

	private static String TAG = "MyService";
	
    /**
     * Class for clients to access.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with
     * IPC.
     */
    public class LocalBinder extends Binder {
        MyLocalService getService() {
            return MyLocalService.this;
        }
    }
    
    // This is the object that receives interactions from clients. 
    // See RemoteService for a more complete example.
    private final IBinder mBinder = new LocalBinder();
    
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		
        // Select the interface to return.  If your service only implements
        // a single interface, you can just return it here without checking
        // the Intent.
		Log.e(TAG,"onBind, intent = " + arg0.toString());
		
        return mBinder;
	}

    
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		Log.e(TAG,"onCreate");
		this.showNotification();
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.e(TAG,"onDestroy");
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		Log.e(TAG,"onStartCommand");
        // We want this service to continue running until it is explicitly
        // stopped, so return sticky.
        return START_STICKY;
		//return super.onStartCommand(intent, flags, startId);
	}


	@Override
	public void onTaskRemoved(Intent rootIntent) {
		// TODO Auto-generated method stub
		super.onTaskRemoved(rootIntent);
	}


	@Override
	public void onRebind(Intent intent) {
		// TODO Auto-generated method stub
		super.onRebind(intent);
		Log.e(TAG,"onRebind, intent = " + intent.toString());
	}


	@Override
	public boolean onUnbind(Intent intent) {
		// TODO Auto-generated method stub
		Log.e(TAG,"onUnbind, intent = " + intent.toString());
		return super.onUnbind(intent);
	}

    /**
     * Show a notification while this service is running.
     */
    private void showNotification() {
 
    	//before API 11
    	/*
    	// In this sample, we'll use the same text for the ticker and the expanded notification
        CharSequence text = getText(R.string.local_service_started);

        // Set the icon, scrolling text and timestamp
        Notification notification = new Notification(R.drawable.stat_sample, text,
                System.currentTimeMillis());

        // The PendingIntent to launch our activity if the user selects this notification
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, LocalServiceActivities.Controller.class), 0);

        // Set the info for the views that show in the notification panel.
        notification.setLatestEventInfo(this, getText(R.string.local_service_label),
                       text, contentIntent);

        // Send the notification.
        mNM.notify(NOTIFICATION, notification);
        */
        
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);  
        Builder builder = new Notification.Builder(MyLocalService.this);  
        PendingIntent contentIndent = PendingIntent.getActivity(MyLocalService.this, 0, 
        		new Intent(MyLocalService.this,TestServiceActivity.class), 
        		PendingIntent.FLAG_UPDATE_CURRENT);  
        builder.setContentIntent(contentIndent)
        	   .setSmallIcon(R.drawable.ic_launcher)//设置状态栏里面的图标（小图标） 　　　　　　　　　　　　　　　　　　　　.setLargeIcon(BitmapFactory.decodeResource(res, R.drawable.i5))//下拉下拉列表里面的图标（大图标） 　　　　　　　.setTicker("this is bitch!") //设置状态栏的显示的信息  
               .setWhen(System.currentTimeMillis())//设置时间发生时间  
               .setAutoCancel(true)//设置可以清除  
               .setContentTitle("local_service_label")//设置下拉列表里的标题  
               .setContentText("local_service_label");//设置上下文内容  
        Notification notification = builder.build();  
        //加i是为了显示多条Notification  
        notificationManager.notify(0,notification);  

    }	
}
