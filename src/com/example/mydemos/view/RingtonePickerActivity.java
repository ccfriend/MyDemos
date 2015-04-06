/*
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.mydemos.view;


import java.io.IOException;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.media.AudioManager.OnAudioFocusChangeListener;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.SectionIndexer;
import android.widget.SimpleCursorAdapter;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;
import android.provider.Settings;
import android.media.Ringtone;
import java.lang.reflect.Method;
import java.util.ArrayList;

import com.example.mydemos.R;

import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
/*ZTE_AUDIO_LIYANG 2014/06/30 fix bug for ringtone when remove sdcard start */
import android.text.TextUtils;
import android.content.Context;
/*ZTE_AUDIO_LIYANG 2014/06/30 fix bug for ringtone when remove sdcard end */
public class RingtonePickerActivity extends Activity implements TabHost.TabContentFactory, OnClickListener, OnItemClickListener 
{
	private Button okBtn;
	private Button cancelBtn;
	//private Button silentBtn;
	private Cursor toneCur;
	private Uri mSelectedUri;
	private MediaPlayer mMediaPlayer;
	private long mSelectedId = -1;
	private ListView listView;
	private Uri BaseUri;
	boolean mListShown;
	private RingtoneManager mRingtoneManager;
	private AudioManager audioManager;
	private OnAudioFocusChangeListener mAudioFocusListener;
	private int toneType;
	private int toneActivityType;
	private TabListAdapter adapter;
	// add by chengcheng
    int mCurPos;
    private ViewPager mViewPager;
    private TabsPageAdapter mTabsAdapter;
	static final int MY_QUERY_TOKEN = 42;
	static final int SYSTEM_TONE = 100;
	private static final int MUSIC_TONE = 111;
	private static final int RECORD_TONE = 112;
	private int tabName;
	private static final int RINGTONE_TYPE = RingtoneManager.TYPE_RINGTONE;
	private static final int ALARM_TYPE = RingtoneManager.TYPE_ALARM;
	private static final int NOTIFICATION_TYPE = RingtoneManager.TYPE_NOTIFICATION;
    private static final String SAVE_CLICKED_POS = "clicked_pos";

	private String where = null;
	private boolean silentItemChecked;
	private final static long SILENT_ID= 11111111;
	private View silentView;
	
	private boolean defaultItemChecked;
	private final static long DEFAULT_ID= 22222222;
	private View defaultView;
	//! by duwenhua
	private boolean mHasDefaultItem;
    private Uri mUriForDefaultItem;
	private boolean mHasSilentItem;
	private Uri mExistingUri;
	private boolean mIsDrmValid = false;
    /** The number of static items in the list. */
    private int mStaticItemCount;
	
    private static final int ID_COLUMN_INDEX = 0;
    private static final int TITLE_COLUMN_INDEX = 1;
    private static final int URI_COLUMN_INDEX = 2;
		
	
	public static boolean isAvailableToCheckRingtone()
    {
    	
    	try
		{
			Class RingtoneManagerClass = Class.forName("android.media.RingtoneManager");
			if (RingtoneManagerClass == null)
				return false;
			
			Method CheckRingtoneMethod = RingtoneManagerClass.getMethod("checkRingtone", Context.class, Uri.class, int.class);
			if (CheckRingtoneMethod == null)
					return false;
			return true;
			
		} catch (Exception e)
		{
			e.printStackTrace();
		}
    	return false;
    }
    
    public static Ringtone checkRingtone_reflect(final Context context, Uri ringtoneUri,int streamType)
    {
    	try
		{
			Class RingtoneManagerClass = Class.forName("android.media.RingtoneManager");
			if (RingtoneManagerClass == null)
				return null;
			
			Method CheckRingtoneMethod = RingtoneManagerClass.getMethod("checkRingtone", Context.class, Uri.class, int.class);
			if (CheckRingtoneMethod == null)
					return null;
			return (Ringtone)(CheckRingtoneMethod.invoke(RingtoneManagerClass, context, ringtoneUri, streamType));
			
		} catch (Exception e)
		{
			e.printStackTrace();
		}
    	return null;
    }
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.ringtone_picker);
		CharSequence sysTitle = (CharSequence) getResources().getText(R.string.sys_tone);
		CharSequence MusicTitle = (CharSequence) getResources().getText(R.string.sd_music);
		CharSequence RecordTitle = (CharSequence) getResources().getText(R.string.record_tone);
		Intent intent = getIntent();
		toneType = intent.getIntExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, -1);
		
		Log.e("duwenhua","ringPick get toneType:" + toneType);
		
		//! by duwenhua
		//if(toneType == RingtoneManager.TYPE_RINGTONE_SECOND)
		//    toneType = RINGTONE_TYPE;
			
		//! by duwenhua
        mHasDefaultItem = intent.getBooleanExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_DEFAULT,true);
        mUriForDefaultItem = intent.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI);
        //Log.i("lys", "mUriForDefaultItem == " + mUriForDefaultItem);
        if (savedInstanceState != null) {
            mSelectedId = savedInstanceState.getLong(SAVE_CLICKED_POS, -1);
        }
        //Log.i("lys", "mUriForDefaultItem 1== " + mUriForDefaultItem+", mSelectedId =="+mSelectedId);		
        mHasSilentItem = intent.getBooleanExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_SILENT, true);		
		mExistingUri = intent.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI);	
		//if(mUriForDefaultItem != null)
		//{
		//    mSelectedId = ContentUris.parseId(mUriForDefaultItem);
		//}
		
		//Log.i("lys", "RingtonePickerActivity.java onCreate mExistingUri == " + mExistingUri);	
		//String action = getIntent().getAction();
		//Log.i("lys", "PT Intent action == " + action);
		
		if(toneType==NOTIFICATION_TYPE)
		{
			mUriForDefaultItem = Settings.System.DEFAULT_NOTIFICATION_URI;
		}
        else if (toneType==RINGTONE_TYPE)
	    {
			mUriForDefaultItem = Settings.System.DEFAULT_RINGTONE_URI;
		}
		else if (toneType==ALARM_TYPE)	
		{
			mUriForDefaultItem = Settings.System.DEFAULT_ALARM_ALERT_URI;     
		}
		
		if(isAvailableToCheckRingtone() == true)
		{
			Ringtone ringtone_DefaultItem = checkRingtone_reflect(this,mUriForDefaultItem,toneType);
			if (ringtone_DefaultItem != null && ringtone_DefaultItem.getUri() != null)	
			{
				mUriForDefaultItem = ringtone_DefaultItem.getUri();
				Log.i("lys", "RingtoneManager.getRingtone  mUriForDefaultItem== " + mUriForDefaultItem);	
			}
			else
			{
				//mUriForDefaultItem = 'content/medial/';
/*ZTE_AUDIO_LIYANG 2014/06/30 fix bug for ringtone when remove sdcard start */				
                        String originalRingtone = Settings.System.getString(getApplicationContext().getContentResolver(), "ringtone_original");
                        if (originalRingtone != null && !TextUtils.isEmpty(originalRingtone)) {                        
                            mUriForDefaultItem =  Uri.parse(originalRingtone);
                            Log.e("liyang", "select riongtone error  ,change to originalRingtone == " +mExistingUri );	 
                        }
/*ZTE_AUDIO_LIYANG 2014/06/30 fix bug for ringtone when remove sdcard end */						 
				Log.i("lys", "mUriForDefaultItem set as default mUriForDefaultItem== 222" );	
			}
			
			if (mExistingUri != null)
			{
				Ringtone ringtone = checkRingtone_reflect(this,mExistingUri,toneType);
				if (ringtone != null && ringtone.getUri()!= null)	
				{
					//mUriForDefaultItem = ringtone.getUri(); 
					mExistingUri = ringtone.getUri();
					Log.i("lys", "RingtoneManager.getRingtone  mExistingUri== " + mExistingUri);	
				}
				else
				{
					mExistingUri = mUriForDefaultItem;
					Log.i("lys", "mExistingUri set as default mExistingUri== " + mExistingUri);	
				}
			}
		}
				
				
		boolean includeDrm = intent.getBooleanExtra(RingtoneManager.EXTRA_RINGTONE_INCLUDE_DRM,false);
		Log.e("lys","includeDrm =="+includeDrm);
		mRingtoneManager = new RingtoneManager(this);
        mRingtoneManager.setIncludeDrm(includeDrm);
		if (toneType != -1) {
		    mRingtoneManager.setType(toneType);
		}
        
        setVolumeControlStream(mRingtoneManager.inferStreamType());
	//	toneActivityType = mRingtoneManager.getActivityType();
		if(toneType==ALARM_TYPE){
			sysTitle = (CharSequence) getResources().getText(
					R.string.alarm_tone);
		}else if(toneType==NOTIFICATION_TYPE){
			sysTitle = (CharSequence) getResources().getText(
					R.string.notification_tone);
		}
		
		
		listView = new ListView(this);
		listView.setOnItemClickListener(this);
		//listView.setBackgroundColor(#ff5a5a5a);
		listView.setFastScrollEnabled(true);
		//listView.setFastScrollAlwaysVisible(true);
		listView.setEmptyView(findViewById(android.R.id.empty));
		//mHasSilentItem = intent.getBooleanExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_SILENT, true);

		//mHasDefaultItem = true; //temp
		if (mHasDefaultItem)
		{
			//chengcheng
			addDefaultStaticItem(listView,com.android.internal.R.string.ringtone_default);
			

		}
		
		setDefaultRingtone();
		
		if(mHasSilentItem){
			// chengcheng
			addSilendStaticItem(listView,com.android.internal.R.string.ringtone_silent);
		}
		audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		mAudioFocusListener = new OnAudioFocusChangeListener() {
			public void onAudioFocusChange(int focusChange) {

			}
		};
		
		okBtn = (Button) findViewById(R.id.ok);
		okBtn.setOnClickListener(this);
		cancelBtn = (Button) findViewById(R.id.cancel);
		cancelBtn.setOnClickListener(this);
		

                
		TabHost mTabHost = (TabHost) findViewById(android.R.id.tabhost);
		mTabHost.setup();
		mTabHost.addTab(mTabHost.newTabSpec("tab_system").setIndicator(sysTitle,
				null).setContent(this));
		mTabHost.addTab(mTabHost.newTabSpec("tab_music").setIndicator(MusicTitle,
				null).setContent(this));
		mTabHost.addTab(mTabHost.newTabSpec("tab_record").setIndicator(RecordTitle,
				null).setContent(this));
		mTabHost.setCurrentTab(0);
		mTabHost.setOnTabChangedListener(new OnTabChangeListener() {
			@Override
			public void onTabChanged(String tabId) {
				// stopMediaPlayer();
				createTabContent(tabId);
			}
		});
	}

	private void setDefaultRingtone() {
		final Uri musicDefault = MediaStore.Audio.Media.INTERNAL_CONTENT_URI;
		final String musicTitle = MediaStore.Audio.AudioColumns.TITLE;
		final String musicId = MediaStore.Audio.Media._ID;
		final String musicName = MediaStore.Audio.AudioColumns.DISPLAY_NAME;
		
    	// Get a ContentResovler, 
    	// so that we can read the music information form SD card.
    	ContentResolver contentResolver = getContentResolver();
    	Uri uri = null;
    	String ringtone = "AA_Preo_ringtone.ogg";
    	Cursor cursor = contentResolver.query(
    			musicDefault,
    			new String[]{musicId,musicName}, 
    			musicName + " LIKE ?",
    			new String[]{ringtone}, 
    			null);
    	// If cursor has a recode, we support that we find the music.
    	if (cursor.moveToNext()) {
    		// Get the music id.
    		int position = cursor.getInt(cursor.getColumnIndex(musicId));
    		uri = Uri.withAppendedPath(musicDefault, "/" + position);
    		Log.d("test", uri.toString() );
    	}
    	// Finish our query work.
    	cursor.close();
    	
    	if( uri != null ) {
    		ContentValues values = new ContentValues();
    		values.put(MediaStore.Audio.AudioColumns.ALBUM_ARTIST, "first");
    		contentResolver.update(uri, values, null, null);
    	}
    	
//        defaultView = getLayoutInflater().inflate(
//        		R.layout.tab_picker_item, listView, false);
//        TextView textView = (TextView) defaultView.findViewById(R.id.title);
//		textView.setText(musicName);
//        listView.addHeaderView(defaultView, null, true);
//        mStaticItemCount++;
        
    }
	
	private void setViewPager() {
		// need cancel these feature in onCreate
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		
      final ActionBar bar = getActionBar();
      bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
      
      
      mViewPager = (ViewPager) findViewById(R.id.pager);		
		//using TabsPageAdapter
		mTabsAdapter = new TabsPageAdapter(this,mViewPager);
      
      mViewPager.setAdapter(mTabsAdapter);
      mViewPager.setOnPageChangeListener(mTabsAdapter);
		
      ActionBar actionBar = this.getActionBar();
      Tab tab1 = actionBar.newTab();
      mTabsAdapter.addTab(tab1, TabContentFragment.class, null);
      Tab tab2 = actionBar.newTab();
      mTabsAdapter.addTab(tab2, TabContentFragment.class, null);
	}
	
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(SAVE_CLICKED_POS, mSelectedId);
    }
	
	private Cursor getCursor(int tabName, String where)
	{
	    Log.e("zxw","getCursor");
		Log.e("zxw","getCursortabName" + tabName);
		switch(tabName)
		{
			case SYSTEM_TONE:
				Cursor cur = getContentResolver().query(
						MediaStore.Audio.Media.INTERNAL_CONTENT_URI,null,where,
						null,  
						MediaStore.Audio.Media.ALBUM_ARTIST + " DESC,"
						+ MediaStore.Audio.Media.TITLE+" COLLATE LOCALIZED ASC");
			    //cur.			
				//		MediaStore.Audio.Media.ALBUM_ARTIST);
				return cur;
				//+ MediaStore.Audio.Media.TITLE+" COLLATE LOCALIZED ASC");
			case MUSIC_TONE:
			case RECORD_TONE:
			{
				//final String status = Environment.getExternalStorageState();
				//Log.e("zxw","getCursorExstatus" + status);
				//if (status.equals(Environment.MEDIA_MOUNTED) ||
				//status.equals(Environment.MEDIA_MOUNTED_READ_ONLY))
				//{
					return getContentResolver()
							.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,null,
							where, null,
							MediaStore.Audio.Media.TITLE+" COLLATE LOCALIZED ASC");
				//}
				//else
				//{
				//	return null;
				//}							
			}
			default:
				return null;
		}
	}
    private int getRingtonePosition(Uri ringtoneUri, int tabName, String were) {
        
        if (ringtoneUri == null) return -1;
        
        final Cursor cursor = getCursor(tabName, were);
        final int cursorCount = cursor.getCount();
        final String musicId = MediaStore.Audio.Media._ID;
        
        if (!cursor.moveToFirst()) {
			Log.e("zxw","getRingtonePosition 1");
            return -1;
        }
        
        // Only create Uri objects when the actual URI changes
        Uri BaseUri = null;
        Uri currentUri = null;
        String previousUriString = null;
		if(tabName == SYSTEM_TONE)
		{
			BaseUri = MediaStore.Audio.Media.INTERNAL_CONTENT_URI;
		}
		else
		{
			BaseUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
		}
		
        for (int i = 0; i < cursorCount; i++) {
        	
        	int position = cursor.getInt(cursor.getColumnIndex(musicId));

			currentUri = ContentUris.withAppendedId(BaseUri, position);
        	
	
        	//if( position == mSelectedId) {
			if (ringtoneUri.equals(currentUri)) {
        		Log.e("zxw","getRingtonePosition i" + i);
        		cursor.close();
            	return i;
        	}
        	
//            String uriString = cursor.getString(URI_COLUMN_INDEX);
//            if (currentUri == null || !uriString.equals(previousUriString)) {
//                currentUri = Uri.parse(uriString);
//            }            
//            if (ringtoneUri.equals(ContentUris.withAppendedId(currentUri, cursor
//                    .getLong(ID_COLUMN_INDEX)))) {
//                Log.e("zxw","getRingtonePosition i" + i);
//                return i;
//            }
            
            cursor.move(1);
            
//            previousUriString = uriString;
        }
        cursor.close();
        Log.e("zxw","getRingtonePosition 2" );
        return -1;
    }

	private int  addSilendStaticItem(ListView listView, int textResId) {

	        silentView = getLayoutInflater().inflate(
	        		R.layout.tab_picker_item, listView, false);
	        TextView textView = (TextView) silentView.findViewById(R.id.title);
			textView.setText(textResId);
	        listView.addHeaderView(silentView, null, true);
            mStaticItemCount++;
	        return listView.getHeaderViewsCount() - 1;
	    }
		
	private int  addDefaultStaticItem(ListView listView, int textResId) {

	        defaultView = getLayoutInflater().inflate(
	        		R.layout.tab_picker_item, listView, false);
	        TextView textView = (TextView) defaultView.findViewById(R.id.title);
			textView.setText(textResId);
	        listView.addHeaderView(defaultView, null, true);
            mStaticItemCount++;
	        return listView.getHeaderViewsCount() - 1;
	    }
	
	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		Log.e("lys","PT onRestart called");
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		Log.e("lys","PT onStart called next doQuery");
		//doQuery(false, null);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.e("lys","PT onResume called");
	}
	
	@Override
	protected void onUserLeaveHint() {
		// TODO Auto-generated method stub
		super.onUserLeaveHint();
		stopMediaPlayer();
		//finish();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		Log.e("lys","PT onStop called");
		stopMediaPlayer();
		audioManager.abandonAudioFocus(mAudioFocusListener);
	}

	@Override
	public View createTabContent(String tag) 
	{
		Log.i("lys", "createTabContent tag == " + tag);
		if(tag.equals("tab_system")) 
		{
			tabName = SYSTEM_TONE;
			if(toneType == ALARM_TYPE)
			{ 
				where =MediaStore.Audio.Media.IS_ALARM; 
			}
			else if(toneType==RINGTONE_TYPE)
			{ 
				where =MediaStore.Audio.Media.IS_RINGTONE; 
			}
			else if(toneType==NOTIFICATION_TYPE)
			{ 
				where =MediaStore.Audio.Media.IS_NOTIFICATION;
			}	 
		} 
		else if(tag.equals("tab_music")) 
		{
			tabName = MUSIC_TONE;
		} 
		else 
		{
			tabName = RECORD_TONE;
		}
		
		Log.e("lys","tabContent where == "+where);
		
		//String project[] = {" DISTINCT title ","_id","_data",MediaStore.Audio.Media.ALBUM,"track","year","is_music","is_ringtone","is_alarm","is_notification"};
		
		switch(tabName) 
		{
			case SYSTEM_TONE:
			    toneCur = getCursor(tabName, where);
				//toneCur = getContentResolver().query(
				//		MediaStore.Audio.Media.INTERNAL_CONTENT_URI,null,where,
				//		null, MediaStore.Audio.Media.TITLE+" COLLATE LOCALIZED ASC");

				//BaseUri = MediaStore.Audio.Media.INTERNAL_CONTENT_URI;
				break;
			case MUSIC_TONE:
            {
				where = "mime_type != 'audio/amr'" + " AND " + MediaStore.Audio.Media.TITLE + " != 'hangout_ringtone' AND " + 
				    MediaStore.Audio.Media.TITLE + " != 'Join Hangout' AND " + MediaStore.Audio.Media.TITLE + " != 'hangout_dingtone' ";
			    toneCur = getCursor(tabName, where);					
				//final String status = Environment.getExternalStorageState();
				//if (status.equals(Environment.MEDIA_MOUNTED) ||
                //    status.equals(Environment.MEDIA_MOUNTED_READ_ONLY))
				//{
				//    toneCur = getContentResolver()
				//		    .query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,null,
				//				    where, null,
				//				    MediaStore.Audio.Media.TITLE+" COLLATE LOCALIZED ASC");
				//}
				//else
				//{
				//    toneCur = null;
				//}

				//BaseUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
				break;
            }
			case RECORD_TONE:
            {
				//new String[] { "recordings" }
				//where = MediaStore.Audio.Media.ALBUM +  " = 'Records' ";
				where = "mime_type = 'audio/amr'";
			    toneCur = getCursor(tabName, where);
				//final String status = Environment.getExternalStorageState();
				//if (status.equals(Environment.MEDIA_MOUNTED) ||
                //    status.equals(Environment.MEDIA_MOUNTED_READ_ONLY))
				//{
				//    toneCur = getContentResolver().query(
				//		    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,null, where,
				//		    null,
				//		    MediaStore.Audio.Media.DATE_ADDED+" COLLATE LOCALIZED ASC");
				//}
				//else
				//{
				//    toneCur = null;
				//}
				//BaseUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
				break;
            }
			default:
				return null;
		}
		Log.i("lys", "toneCur == " + toneCur);
		if(toneCur == null) 
		{
			Log.e("lys", "createTabContent toneCur== null");
			//listView.setEmptyView(findViewById(android.R.id.empty));
		    listView.setAdapter(null);
			listView.invalidate();
			return listView;
		}
		
		if (mHasDefaultItem)
		{
		    if (RingtoneManager.isDefault(mExistingUri))
			{
		        defaultItemChecked = true;
				silentItemChecked= false;
				mSelectedId = DEFAULT_ID;
				//mSelectedUri = mUriForDefaultItem; 
		    }
		}
		if (mHasSilentItem)
		{
		    if (mExistingUri == null)
		    {
		        silentItemChecked = true;
				defaultItemChecked = false;
			    mSelectedId = SILENT_ID;
			    //mSelectedUri = null;				
		    }
		}
		
		if ((mSelectedId != DEFAULT_ID) && (mSelectedId != SILENT_ID))
		{
			silentItemChecked = false;
			defaultItemChecked = false;
			if (mExistingUri != null) {
			    Log.e("mExistingUri===",mExistingUri.toString());
			    mSelectedId = ContentUris.parseId(mExistingUri);
			}
			//mSelectedUri = mExistingUri;
		}
		adapter = new TabListAdapter(this, R.layout.tab_picker_item, toneCur,new String[] {}, new int[] {});
		listView.setAdapter(adapter);
		listView.invalidate();
		
		
		int index = getRingtonePosition(mExistingUri,tabName,where);
		if(mHasSilentItem) {
			index += 1;
		} 
		if(mHasDefaultItem) {
			index += 1;
		}
		listView.setSelection(index);
		return listView;
	}

	void stopMediaPlayer() {
		Log.e("lys","stopMediaPlayer()");
		if (mMediaPlayer != null) {
			mMediaPlayer.stop();
			mMediaPlayer.release();
			mMediaPlayer = null;
		}
	}

	public void onClick(View v) {
		Log.e("lys","onClick called");
		if(toneType ==-1){
			finish();
		}
		Intent resultIntent = new Intent();
		ContentResolver resolver = getContentResolver();
		switch (v.getId()) 
		{
			case R.id.ok:
				//if(mSelectedId >= 0 && mSelectedId != SILENT_ID && mSelectedId != DEFAULT_ID) 
				//{
				//	Log.e("lys","onClick mSelectedId == "+mSelectedId);
				//	ContentValues values = new ContentValues(2);
				//	if(toneActivityType == ALARM_TYPE)
				//	{ 
				//		values.put(MediaStore.Audio.Media.IS_ALARM, "1");
				//	}
				//	else if(toneActivityType==NOTIFICATION_TYPE)
				//	{ 
				//		values.put(MediaStore.Audio.Media.IS_NOTIFICATION, "1");
				//	}	
				//	else//! if(toneType==RINGTONE_TYPE) by duwenhua
				//	{ 
				//		values.put(MediaStore.Audio.Media.IS_RINGTONE, "1");
				//	}

					Log.e("lys","onClick values == "+mSelectedUri);
	            //    resolver.update(mSelectedUri, values, null, null);
				//	resultIntent.putExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI, mSelectedUri);
				//	setResult(RESULT_OK, resultIntent);
					//Toast.makeText(this, "mSelectedUri==" + mSelectedUri,
					//		Toast.LENGTH_LONG).show();
				//	finish();
				//}
				//else if(mSelectedId == SILENT_ID) 
				//{
					//resultIntent.putExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI, mSelectedUri);
					//setResult(RESULT_OK, resultIntent);
					//Toast.makeText(this, "mSelectedUri==" + mSelectedUri,
					//		Toast.LENGTH_LONG).show();
					//finish();
				//}
				if (mSelectedId == SILENT_ID)
				{
				    mSelectedUri = null;
				} else if(mSelectedId == DEFAULT_ID)
				{
				    mSelectedUri = mUriForDefaultItem;
				} else {
				Log.e("lys","onClick values mSelectedId == "+mSelectedId);


					//wuqingliang modify begin20130307 for the first time entery the ringtonePickerActivity, and the ringtone is in EXTERNAL_CONTENT_URI
					//user click ok button in TAP ringtone.
					if(BaseUri != MediaStore.Audio.Media.INTERNAL_CONTENT_URI && BaseUri != MediaStore.Audio.Media.EXTERNAL_CONTENT_URI)
						{
							mSelectedUri = mExistingUri;
						}
						else
						{
							mSelectedUri = ContentUris.withAppendedId(BaseUri, mSelectedId);
						}
					//wuqingliang modify end
				    
				}
				resultIntent.putExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI, mSelectedUri);
				
				RingtoneManager.setActualDefaultRingtoneUri(this, RingtoneManager.TYPE_RINGTONE, mSelectedUri);
				
					setResult(RESULT_OK, resultIntent);
					//Toast.makeText(this, "mSelectedUri==" + mSelectedUri,
					//		Toast.LENGTH_LONG).show();
					finish();
				break;
	
			case R.id.cancel:
				setResult(RESULT_CANCELED);
				stopMediaPlayer();
				finish();
				break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position,
			long id) {
		Log.e("lys","onItemClick position =="+position+"id"+id);
		if(id == -1){
		    if (mHasSilentItem)
			{

				if ((mHasDefaultItem && (position == 1)) || !mHasDefaultItem)
				{
			        Log.e("lys","onItemClick silentItemChecked == true");
			        //toneCur.moveToPosition(position);
			        mSelectedId = SILENT_ID;
			        mSelectedUri = null;
			        silentItemChecked = true;
					defaultItemChecked = false;
			        stopMediaPlayer();
			        listView.invalidateViews();
			        return;
				} else if (mHasDefaultItem && (position == 0))
				{
			        Log.e("lys","onItemClick defaultItemChecked == true");
			        //toneCur.moveToPosition(position);
			        mSelectedId = DEFAULT_ID;
			        mSelectedUri = mUriForDefaultItem;
			        defaultItemChecked = true;
					silentItemChecked = false;
				}
		    } else {
			    if (mHasDefaultItem)
				{
			        Log.e("lys","onItemClick defaultItemChecked == true");
			        //toneCur.moveToPosition(position);
			        mSelectedId = DEFAULT_ID;
			        mSelectedUri = mUriForDefaultItem;
			        defaultItemChecked = true;
					silentItemChecked = false;
				}
			}

		}else {
		    if(mHasSilentItem){
			    silentItemChecked = false;
			    position--;
		    }
			
			if (mHasDefaultItem){
			    defaultItemChecked = false;
			    position--;
		    }
		}
		if (id != -1)
		{
		toneCur.moveToPosition(position);
		Log.e("lys", "onItemClick position == " + position + "id ==" + id);
		long newId = toneCur.getLong(toneCur.getColumnIndex(MediaStore.Audio.Media._ID));
		mSelectedId = newId;
		Log.e("lys", " onItemClick mSelectedId==" + mSelectedId);
		
		//wuqingliang modify begin20130307
			if(tabName == SYSTEM_TONE)
				{
					BaseUri = MediaStore.Audio.Media.INTERNAL_CONTENT_URI;
					Log.e("lys", "BaseUri = MediaStore.Audio.Media.INTERNAL_CONTENT_URI;111111");
				}
				else
				{
					BaseUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
					Log.e("lys", "BaseUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;1111111");
				}
			//wuqingliang modify end
		mSelectedUri = ContentUris.withAppendedId(BaseUri, newId);
		}
		listView.invalidateViews();
		audioManager.requestAudioFocus(mAudioFocusListener,
				AudioManager.STREAM_MUSIC,
				AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
		stopMediaPlayer();
		mMediaPlayer = new MediaPlayer();
		try {
		    if(toneType==ALARM_TYPE)
			    mMediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
            else 			
			    mMediaPlayer.setAudioStreamType(AudioManager.STREAM_RING);
			mMediaPlayer.setDataSource(RingtonePickerActivity.this, mSelectedUri);
			mMediaPlayer.prepare();
			mMediaPlayer.start();

		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	class TabListAdapter extends SimpleCursorAdapter implements SectionIndexer {

		private int mTitleIdx;
		private RadioButton radio;
		private RadioButton radioSilent;
		private RadioButton radioDefault;
		private int mIdIdx;
		private RingtoneAlphabetIndexer mIndexer;
		//private boolean mLoading = true;

		public TabListAdapter(Context context, int layout, Cursor c,
				String[] from, int[] to) {
			super(context, layout, c, from, to);
			mTitleIdx = c.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE);
			mIdIdx = c.getColumnIndex(MediaStore.Audio.Media._ID);
			mIndexer = new RingtoneAlphabetIndexer(c, mTitleIdx,
					getResources().getString(R.string.fast_scroll_alphabet));
		}

		@Override
		public void bindView(View view, Context context, Cursor cursor) {
			// TODO Auto-generated method stub
			super.bindView(view, context, cursor);
			TextView tv = (TextView) view.findViewById(R.id.title);
			String name = cursor.getString(mTitleIdx);
			tv.setText(name);
			final long id = cursor.getLong(mIdIdx);
			if(mHasSilentItem){
				radioSilent = (RadioButton) silentView.findViewById(R.id.radio);
				if(silentItemChecked){
					radioSilent.setChecked(true);
				}else {
					radioSilent.setChecked(false);
				}
			}
			if (mHasDefaultItem) {

			    radioDefault = (RadioButton) defaultView.findViewById(R.id.radio);
				if (defaultItemChecked) {
				    radioDefault.setChecked(true);
				} else {
				    radioDefault.setChecked(false);
				}
			}
			Log.e("lys", "bindView id== " + id + ";mSelectedId=="+ mSelectedId);
			//wuqingliang modify begin20130307
			if(id == mSelectedId)
			{
				if(tabName == SYSTEM_TONE)
				{
					BaseUri = MediaStore.Audio.Media.INTERNAL_CONTENT_URI;
					Log.e("lys", "BaseUri = MediaStore.Audio.Media.INTERNAL_CONTENT_URI;");
				}
				else
				{
					BaseUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
					Log.e("lys", "BaseUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;");
				}
			}
			//wuqingliang modify end

			radio = (RadioButton) view.findViewById(R.id.radio);
			radio.setChecked(id == mSelectedId);
		}

		@Override
		public View newView(Context context, Cursor cursor, ViewGroup parent) {
			// TODO Auto-generated method stub
			View v = super.newView(context, cursor, parent);
			return v;
		}
		
		
		@Override
		public int getPositionForSection(int section) {
			// TODO Auto-generated method stub
			Log.e("lys", "PT getPositionForSection()");
			Cursor cursor = getCursor();
			if (cursor == null) {
				// No cursor, the section doesn't exist so just return 0
				return 0;
			}
			Log.e("lys","getPositionForSection mIndexer == null"+(mIndexer==null));
			if(!isFinishing()){
				return mIndexer.getPositionForSection(section);
			}else {
				finish();
				return 0;
			}
		}

		@Override
		public int getSectionForPosition(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public Object[] getSections() {
			// TODO Auto-generated method stub
			if (mIndexer != null) {
				return mIndexer.getSections();
			}
			return null;
		}

	}
	
    class TabsPageAdapter extends FragmentPagerAdapter implements ViewPager.OnPageChangeListener,
    		ActionBar.TabListener {
    	
    	final class TabInfo {
    		private final Bundle args;
    		private final Class cls;
    		
    		TabInfo (Class cls, Bundle args) {
    			this.args = args;
    			this.cls = cls;
    		}
    	}
    	
    	private int mCurPos;
    	private Context mCtx;
    	private ViewPager mViewPager;
    	private ActionBar mActionBar;
    	private final ArrayList<TabInfo> mTabs = new ArrayList<TabInfo>();
    	
        public TabsPageAdapter(Activity aty, ViewPager viewPager) {
        	super(aty.getFragmentManager());
        	mCtx = aty;
        	mActionBar = aty.getActionBar();
        	mViewPager = viewPager;
        }

        @Override
        public Fragment getItem(int position) {

        	// get the fragment
        	TabInfo tabInfo = mTabs.get(position);
        	return Fragment.instantiate(mCtx, tabInfo.cls.getName(), tabInfo.args);  
        }

        public void addTab(Tab tab, Class cls, Bundle args) {
        	TabInfo tabInfo = new TabInfo(cls,args);
        	tab.setTag(tabInfo);
        	mTabs.add(tabInfo);
        	tab.setTabListener(this);
        	mActionBar.addTab(tab);
        	notifyDataSetChanged();
        }
        
        @Override
        public int getCount() {
            return mTabs.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {

        	return mTabs.get(position).args.getString("title");  
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            mCurPos = position;
            
            //update actionbar state
            final ActionBar actionBar = getActionBar();  
            actionBar.setSelectedNavigationItem(position); 
        }

        public int getCurrentPage() {
            return mCurPos;
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            if (state == ViewPager.SCROLL_STATE_IDLE) {
                //updateCurrentTab(mCurPos);
            }
        }

		@Override
		public void onTabReselected(Tab arg0, FragmentTransaction arg1) {
			// TODO Auto-generated method stub			
		}

		@Override
		public void onTabSelected(Tab arg0, FragmentTransaction arg1) {
			// TODO Auto-generated method stub
			mViewPager.setCurrentItem(mCurPos);
		}

		@Override
		public void onTabUnselected(Tab arg0, FragmentTransaction arg1) {
			// TODO Auto-generated method stub
			
		}
    }	
	
    
    
    
    
    public static class TabContentFragment extends ListFragment {
    	
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);         
        }

        @Override public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
    		
            String [] array = new String[]{"C", "C++", "Java","C", "C++", "Java","C", "C++", "Java"
            		,"C", "C++", "Java","C", "C++", "Java","C", "C++", "Java",
            		"C", "C++", "Java","C", "C++", "Java","C", "C++", "Java"};  
    		setListAdapter(new ArrayAdapter<String>(getActivity(),   
    			android.R.layout.simple_list_item_1, array));  
    		
            // Give some text to display if there is no data.  In a real
            // application this would come from a resource.
            setEmptyText("No applications");

            // We have a menu item to show in action bar.
            setHasOptionsMenu(true);

            // Create an empty adapter we will use to display the loaded data.
            //setListAdapter(mAdapter);

            // Start out with a progress indicator.
            //setListShown(false);

        }
    	
		@Override
		public void onListItemClick(ListView l, View v, int position, long id) {
			// TODO Auto-generated method stub
			super.onListItemClick(l, v, position, id);
		}

		@Override
		public void setListShown(boolean shown) {
			// TODO Auto-generated method stub
			super.setListShown(shown);
		}

		private String mTabName;

        //public TabContentFragment(String text) {
        //	mTabName = text;
        //}

        public String getText() {
            return mTabName;
        }
    }
    
}
