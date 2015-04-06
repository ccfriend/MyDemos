package com.example.mydemos.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteCallbackList;

public class MyRemoteService extends Service {

	private static String TAG = "MyService";
	
    /**
     * This is a list of callbacks that have been registered with the
     * service.  Note that this is package scoped (instead of private) so
     * that it can be accessed more efficiently from inner classes.
     */
    final RemoteCallbackList<IMyRemoteServiceCallback> mCallbacks
            = new RemoteCallbackList<IMyRemoteServiceCallback>();
    	
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		
        // Select the interface to return.  If your service only implements
        // a single interface, you can just return it here without checking
        // the Intent.
        if (IMyRemoteService.class.getName().equals(arg0.getAction())) {
            return mBinder;
        }
        
		return null;
	}

    /**
     * The IRemoteInterface is defined through IDL
     */
    private final IMyRemoteService.Stub mBinder = new IMyRemoteService.Stub() {
        public void registerCallback(IMyRemoteServiceCallback cb) {
            if (cb != null) mCallbacks.register(cb);
        }
        public void unregisterCallback(IMyRemoteServiceCallback cb) {
            if (cb != null) mCallbacks.unregister(cb);
        }
    };

    
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		return super.onStartCommand(intent, flags, startId);
	}

	
}
