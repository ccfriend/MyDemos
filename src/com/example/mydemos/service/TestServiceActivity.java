package com.example.mydemos.service;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mydemos.R;

public class TestServiceActivity extends Activity {

	private static String TAG = "MyServiceTest";
		
    Button mKillButton;
    TextView mCallbackText;

    private boolean mIsBound;

	protected Object mBoundService;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		Log.e(TAG,"onCreate");
		
		super.onCreate(savedInstanceState);

        setContentView(R.layout.service_test);

        // Watch for button clicks.
        Button button = (Button)findViewById(R.id.start);
        button.setOnClickListener(mStartListener);
        
        button = (Button)findViewById(R.id.stop);
        button.setOnClickListener(mStopListener);
        
        button = (Button)findViewById(R.id.bind);
        button.setOnClickListener(mBindListener);
        
        button = (Button)findViewById(R.id.unbind);
        button.setOnClickListener(mUnbindListener);
        
        mKillButton = (Button)findViewById(R.id.kill);
        mKillButton.setOnClickListener(mKillListener);
        mKillButton.setEnabled(false);
        
        mCallbackText = (TextView)findViewById(R.id.callback);
        mCallbackText.setText("Not attached.");		
	}
    
    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            // This is called when the connection with the service has been
            // established, giving us the service object we can use to
            // interact with the service.  We are communicating with our
            // service through an IDL interface, so get a client-side
            // representation of that from the raw service object.
        	Log.e(TAG,"onServiceConnected");
        	
            mBoundService = ((MyLocalService.LocalBinder)service).getService();
            mCallbackText.setText("Attached.");

            // Tell the user about this for our demo.
            Toast.makeText(TestServiceActivity.this, "local_service_connected",
                    Toast.LENGTH_SHORT).show();
        }

        public void onServiceDisconnected(ComponentName className) {
            // This is called when the connection with the service has been
            // unexpectedly disconnected -- that is, its process crashed.
            // Because it is running in our same process, we should never
            // see this happen.
        	Log.e(TAG,"onServiceDisconnected");
            mBoundService = null;
            Toast.makeText(TestServiceActivity.this, "local_service_disconnected",
                    Toast.LENGTH_SHORT).show();
        }
    };	
	
    private OnClickListener mStartListener = new OnClickListener() {
        public void onClick(View v) {
            // Make sure the service is started.  It will continue running
            // until someone calls stopService().  The Intent we use to find
            // the service explicitly specifies our service component, because
            // we want it running in our own process and don't want other
            // applications to replace it.
            startService(new Intent(TestServiceActivity.this,
                    MyLocalService.class));
        }
    };

    private OnClickListener mStopListener = new OnClickListener() {
        public void onClick(View v) {
            // Cancel a previous call to startService().  Note that the
            // service will not actually stop at this point if there are
            // still bound clients.
            stopService(new Intent(TestServiceActivity.this,
            		MyLocalService.class));
        }
    };	
	
    private OnClickListener mBindListener = new OnClickListener() {
        public void onClick(View v) {
        	
        	doBindService();
        	
        }
    };

    private OnClickListener mUnbindListener = new OnClickListener() {
        public void onClick(View v) {
        	
        	doUnbindService();
        }
    };

    void doBindService() {
        // Establish a connection with the service.  We use an explicit
        // class name because we want a specific service implementation that
        // we know will be running in our own process (and thus won't be
        // supporting component replacement by other applications).
        bindService(new Intent(this, 
               MyLocalService.class), mConnection, Context.BIND_AUTO_CREATE);
        mIsBound = true;        
        mCallbackText.setText("Binding.");
    }
    
    void doUnbindService() {
        if (mIsBound) {
            // Detach our existing connection.
            unbindService(mConnection);
            mIsBound = false;
            mKillButton.setEnabled(false);           
            mCallbackText.setText("Unbinding.");            
        }
    }
        
    private OnClickListener mKillListener = new OnClickListener() {
        public void onClick(View v) {
            // To kill the process hosting our service, we need to know its
            // PID.  Conveniently our service has a call that will return
            // to us that information.
        }
    };
    
}
