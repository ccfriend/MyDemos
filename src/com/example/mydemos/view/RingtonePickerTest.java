package com.example.mydemos.view;

import android.app.ListActivity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;


public class RingtonePickerTest extends ListActivity {

	public static final String INTENT_MYPICKER = "android.intent.action.MYPICKER";
	
	public static final int REQUEST_CODE_ATTACH_AUDIO = 1;
	
	private int mRingtoneType = -1;
	
    private static final String[] mStrings = new String[] {
        "testRingtone()",	// 0
        "testAlarm()",
    };    

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Use an existing ListAdapter that will map an array
        // of strings to TextViews
        setListAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, mStrings));
        getListView().setTextFilterEnabled(true);
        
    }
    
    protected void onPrepareRingtonePickerIntent(Intent ringtonePickerIntent) {
    	
    	//ringtonetPickerIntent.
    
    }
    
    @Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		
		switch(position) {
		
		case 0:
			testRingtone();
			break;
		
		}
		
	}
    
    
    public void testRingtone() {
        //Uri uri = Uri.parse(NORMAL_URL);
    	Intent ringtonePickerIntent = new Intent(INTENT_MYPICKER);
        
    	mRingtoneType = RingtoneManager.TYPE_RINGTONE; 
    	
    	
		final Uri musicDefault = MediaStore.Audio.Media.INTERNAL_CONTENT_URI;
		final String musicTitle = MediaStore.Audio.AudioColumns.TITLE;
		final String musicId = MediaStore.Audio.Media._ID;
		
		Uri uri = null;
		
    	// Get a ContentResovler, 
    	// so that we can read the music information form SD card.
    	ContentResolver contentResolver = getContentResolver();
    	String musicName = "The party";
    	Cursor cursor = contentResolver.query(
    			musicDefault,
    			new String[]{musicId,musicTitle}, 
    			musicTitle + " LIKE ?",
    			new String[]{musicName}, 
    			null);
    	// If cursor has a recode, we support that we find the music.
    	if (cursor.moveToNext()) {
    		// Get the music id.
    		int position = cursor.getInt(cursor.getColumnIndex(musicId));
    		String position1 = cursor.getString(cursor.getColumnIndex(musicTitle));
    		uri = Uri.withAppendedPath(musicDefault, "/" + position);
    		Log.d("test", uri.toString() );
    	}
    	// Finish our query work.
    	cursor.close();
    	
    	uri = RingtoneManager.getActualDefaultRingtoneUri(this, getRingtoneType());
    	//ringtonePickerIntent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_DEFAULT, true);
    	ringtonePickerIntent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_SILENT, false);
    	ringtonePickerIntent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_RINGTONE);
    	ringtonePickerIntent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI,uri);
    	startActivityForResult(ringtonePickerIntent,REQUEST_CODE_ATTACH_AUDIO);
    }

    public int getRingtoneType() {
    	return mRingtoneType;
    	
    }
    
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		
		if( resultCode == REQUEST_CODE_ATTACH_AUDIO) {
			
			if( data != null) {
				Uri uri = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
				
				RingtoneManager.setActualDefaultRingtoneUri(this, getRingtoneType(), uri);
			}
			
		}
		
		
	}
    
    
}