package com.example.mydemos.app;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import com.example.mydemos.R;


import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.ListActivity;
import android.app.SearchManager;
import android.content.ComponentName;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.ComponentInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.AlarmClock;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;


public class AvailableIntentsTest extends ListActivity {
    private static final String NORMAL_URL = "http://www.google.com/";
    private static final String SECURE_URL = "https://www.google.com/";

    private static final String[] mStrings = new String[] {
        "testViewNormalUrl()",	// 0
        "testViewSecureUrl()",
        "testWebSearchNormalUrl()",
        "testWebSearchSecureUrl()",
        "testWebSearchPlainText()",
        "testCallPhoneNumber()",	// 5
        "testDialPhoneNumber()",
        "testDialVoicemail()",
        "testCamera()",		// 8
        "testSettings()",
        "testCalendarAddAppointment()",
        "testContactsCallLogs()",	//11
        "testMusicPlayback()",
        "testAlarmClock()",
        "testOpenDocumentAny()",
        "testOpenDocumentImage()",
        "testCreateDocument()",
        "testGetContentAny()", //17
        "testGetContentImage()",
        "testRingtonePicker()",
        "testViewDownloads()",
        "testMyDialerIntent()",	//21
        "testMyContactIntent()",
        "testMyMmsIntent()",
        "testMyWebIntent()",
        "testMyCameraIntent()",
        "testAllLaucherIntent()"
    };    

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Use an existing ListAdapter that will map an array
        // of strings to TextViews
        setListAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, mStrings));
        getListView().setTextFilterEnabled(true);
        
        
        
        Intent addShortCut = new Intent(  
        		"com.android.launcher.action.INSTALL_SHORTCUT");  
        String title = getResources().getString(R.string.app_name);  
        Parcelable icon = Intent.ShortcutIconResource.fromContext(  
        		this, R.drawable.ic_launcher);  
        Intent intent = new Intent(this, AvailableIntentsTest.class);  
        addShortCut.putExtra(Intent.EXTRA_SHORTCUT_NAME, title);  
        addShortCut.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, icon);    
        addShortCut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, intent);  
        sendBroadcast(addShortCut);  

    }
    
    @Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		
		switch(position) {
		case 0:
			testViewNormalUrl();
			break;
			
		case 1:
			testViewSecureUrl();
			break;
			
		case 2:
			testWebSearchNormalUrl();
			break;
			
		case 5:
			testCallPhoneNumber();
			break;

		case 6:
			testDialPhoneNumber();
			break;

			
		case 8:
			testCamera();
			break;
			
		case 11:
			testContactsCallLogs();
			break;
			
		case 12:
			testMusicPlayback();
			break;			
			
		case 17:
			testGetContentAny();
			break;
			
		case 19:
			testRingtonePicker();
			break;

		case 21:
			testMyDialerIntent();
			break;
		
		case 22:	
	        testMyContactIntent();
	        break;
	        
		case 23:
	        testMyMmsIntent();
	        break;
	        
		case 24:			
	        testMyWebIntent();
	        break;
	
		case 25:
			testMyCameraIntent();
			break;
			
		case 26:
			testAllLaucherIntent();
			break;
			
		default:
			break;
		}		
		
	}
    
    /**
     * find the proper activities through
     * @param intent,
     * then set the exact componentName  
     * */
//    private ComponentName setNewComponent(ComponentName cn, Intent intent) {
//    	
//        final Intent launchIntent = new Intent(Intent.ACTION_MAIN, null);
//        launchIntent.addCategory(Intent.CATEGORY_LAUNCHER);
//        
//		PackageManager packageManager = this.getPackageManager();
//		final List<ResolveInfo> launcheResolveInfoList = packageManager.queryIntentActivities(launchIntent, 0);
//				
//        String pkg = cn.getPackageName();
//        String cls = cn.getClassName();
//        List<ResolveInfo> resolveInfoList = packageManager.queryIntentActivities(intent, 0);
//        boolean found = false;
//        if(resolveInfoList != null) {
//            for(ResolveInfo rv : resolveInfoList) {
//            	found = false;
//            	
//                //system app
//                if((rv.activityInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) {
//                	for(ResolveInfo lauchRv : launcheResolveInfoList){
//                		if(lauchRv.equals(rv)) {
//                			found = true;
//                			break;
//                		}
//                	}
//                	
//                	if(!found)  continue; 
//                	
//                    if(rv.resolvePackageName != null) {
//                        pkg = rv.resolvePackageName;
//                        cls = rv.activityInfo.name;
//                    } else {
//                        if(rv.activityInfo != null) {
//                            pkg = rv.activityInfo.packageName;
//                            cls = rv.activityInfo.name;
//                        }
//                    }
//                    break;
//                }
//            }
//        }
//        
//        ComponentName reComp = new ComponentName(pkg,cls);
//        if(reComp.equals(cn)) {
//        	if(cn.getPackageName().contains("camera")) {
//        		for(ResolveInfo lauchRv : launcheResolveInfoList){
//        			if(((lauchRv.activityInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0)
//        					&& lauchRv.activityInfo.name.contains("camera")) {
//        		        String newPkg = cn.getPackageName();
//        		        String newCls = cn.getClassName();
//        		        newPkg = lauchRv.activityInfo.packageName;
//        		        newCls = lauchRv.activityInfo.name;
//        		        break;
//        			}
//        		}
//        	}
//        }
//        return reComp;
//    }

//    private ComponentName setNewComponent(ComponentName cn, Intent intent) {
//
//        String pkg = cn.getPackageName();
//        String cls = cn.getClassName();
//		
//
////        if(cn.getPackageName().contains("camera")) {
////            final Intent launchIntent = new Intent(Intent.ACTION_MAIN, null);
////            launchIntent.addCategory(Intent.CATEGORY_LAUNCHER);
////            
////    		PackageManager packageManager = this.getPackageManager();
////    		final List<ResolveInfo> launchResolveInfoList = packageManager.queryIntentActivities(launchIntent, 0);
////    		
////    		for(ResolveInfo lauchRv : launchResolveInfoList) {
////    			if(((lauchRv.activityInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0)
////    					&& lauchRv.activityInfo.name.contains("camera")) {
////                    pkg = lauchRv.activityInfo.packageName;
////                    cls = lauchRv.activityInfo.name;
////                    break;
////    			}
////    		}
////    		
////    		return new ComponentName(pkg,cls);
////        }
//        
//        PackageManager packageManager = this.getPackageManager();               
//        List<ResolveInfo> resolveInfoList = packageManager.queryIntentActivities(intent, 0);
//        
//        if(resolveInfoList != null) {
//            for(ResolveInfo rv : resolveInfoList) {
//                
//                //system app
//                if((rv.activityInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) {
//                    if(rv.activityInfo != null) {
//                        pkg = rv.activityInfo.packageName;
//                        
//                        // we are in activity-alias
//                        if( rv.activityInfo.targetActivity != null) {
//                            Intent launchIntent = new Intent(Intent.ACTION_MAIN, null);
//                            launchIntent.addCategory(Intent.CATEGORY_LAUNCHER);
//                            
//                    		List<ResolveInfo> launchResolveInfoList = packageManager.queryIntentActivities(launchIntent, 0);
//
//                    	     if( launchResolveInfoList != null) {
//                    	    	 for(ResolveInfo launchRv : launchResolveInfoList) {
//                    	    		if( launchRv.activityInfo.name.equals(rv.activityInfo.name)) {
//                    	    			cls = launchRv.activityInfo.name;
//                    	    		    break;
//                    	    		} 
//                    	    	 }
//                    	     }
//                    	    	
//                        } else {
//                        	cls = rv.activityInfo.name;
//                        }
//                                                
//                        //cls = rv.activityInfo.targetActivity==null? rv.activityInfo.name :rv.activityInfo.targetActivity;
//                    }
//                    break;
//                }
//            }
//        }
//
//        return new ComponentName(pkg,cls);
//    }    
//    private ComponentName findRealPackage(String packageName, String className) {
//
//        String[] hotseat = {"dialer","contact", "mms","browser","camera"};
//        
//        ComponentName newComp = new ComponentName(packageName,className);
//        Intent intent = new Intent();
//        
//       // first check the original package
//       if(packageName.contains("dialer")) {               
//            intent.setAction(Intent.ACTION_DIAL);                
//       }
//       
//       // first check the original package
//       if(packageName.contains("contact")) {
//            //method 1:
////          Intent intent = new Intent(Intent.ACTION_VIEW);
////          intent.setType("vnd.android.cursor.dir/contact");
//           
//           //method 2:
//           intent.setAction(Intent.ACTION_MAIN);
//           intent.addCategory(Intent.CATEGORY_LAUNCHER);
//           intent.addCategory(Intent.CATEGORY_APP_CONTACTS); 
//       }       
//       
//       // first check the original package
//       if(packageName.contains("mms")) {
//           
//           //method 1: 
//           intent.setAction(Intent.ACTION_MAIN);
//           intent.setType("vnd.android.cursor.dir/mms");
//           
//           //method 2:
////         Intent intent = new Intent(Intent.ACTION_MAIN, null);
////         intent.addCategory(Intent.CATEGORY_LAUNCHER);
////         intent.addCategory(Intent.CATEGORY_APP_MESSAGING);
//       }
//       
//       // first check the original package
//       if(packageName.contains("browser")) {
//           
//           //method 1:
////         Uri uri = Uri.parse(NORMAL_URL);
////         Intent intent = new Intent(Intent.ACTION_WEB_SEARCH,uri);
//           
//           //method 2:
//           intent.setAction(Intent.ACTION_MAIN);
//           intent.addCategory(Intent.CATEGORY_LAUNCHER);
//           intent.addCategory(Intent.CATEGORY_APP_BROWSER);                
//       }       
//       
//       if(packageName.contains("camera")) {               
//           intent.setAction(MediaStore.INTENT_ACTION_STILL_IMAGE_CAMERA);
//    	   //intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
//       }       
//    
//       newComp = setNewComponent(newComp,intent);
//       return newComp;
//    }

    List<ResolveInfo> getMatchingList(Intent intent) {
    	PackageManager packageManager = this.getPackageManager();
    	List<ResolveInfo> resolveInfoList = packageManager.queryIntentActivities(intent, 0);
	    if( resolveInfoList != null) {
	    	for( ResolveInfo rv : resolveInfoList) {
	    		if((rv.activityInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
	    			resolveInfoList.remove(rv);
	    		}	    		
	    	}
	    }    	
    	return resolveInfoList;
    }
    
    boolean matchingActivity(ResolveInfo rv, List<ResolveInfo> resolveInfoList) {
    
    	for(ResolveInfo reinfo : resolveInfoList) {
    		if( reinfo.activityInfo.name.equals(rv.activityInfo.name) )
    			return true;
    	}
    	
    	return false;
    }
 
    
    private ComponentName findRealPackage(String packageName, String className) {
    	
	    String pkg = packageName;
	    String cls = className;
    	
	    //step 1: find launcher activity
        final Intent launchIntent = new Intent(Intent.ACTION_MAIN, null);
        launchIntent.addCategory(Intent.CATEGORY_LAUNCHER);        
		PackageManager packageManager = this.getPackageManager();
		List<ResolveInfo> launchResolveInfoList = packageManager.queryIntentActivities(launchIntent, 0);
		
		//filter system packages
	    if( launchResolveInfoList != null) {
	    	Iterator <ResolveInfo> it = launchResolveInfoList.iterator();  
	    	while(it.hasNext())  
	    	{
	    		ResolveInfo rv = it.next();
	    		if((rv.activityInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) { 	    	      
	    	        it.remove();  
	    	    }  
	    	}
	    }
	    
	    //step 2: find proper activity by intent fitler
		//find the activities by intent
		Intent intent = new Intent();
		List<ResolveInfo> appResolveInfoList = new ArrayList<ResolveInfo>();
		
		// first check the original package
	    if(packageName.contains("dialer")) {               
	    	intent.setAction(Intent.ACTION_DIAL);	    	
	    	appResolveInfoList.addAll(getMatchingList(intent));
	    	
	    } else if (packageName.contains("contact")) {
	        intent.setAction(Intent.ACTION_MAIN);
	        intent.addCategory(Intent.CATEGORY_LAUNCHER);
	        intent.addCategory(Intent.CATEGORY_APP_CONTACTS);	        
	        appResolveInfoList.addAll(getMatchingList(intent));
	        
	    	Intent intent2 = new Intent(Intent.ACTION_VIEW);
	    	intent2.setType("vnd.android.cursor.dir/contact");		        
	        appResolveInfoList.addAll(getMatchingList(intent2));
	        
	    } else if (packageName.contains("mms")) {
	         intent.setAction(Intent.ACTION_MAIN);
	         intent.setType("vnd.android.cursor.dir/mms");
	         appResolveInfoList.addAll(getMatchingList(intent));
	         
     	     Intent intent2 = new Intent(Intent.ACTION_MAIN, null);
     	     intent2.addCategory(Intent.CATEGORY_LAUNCHER);
     	     intent2.addCategory(Intent.CATEGORY_APP_MESSAGING);	         
	         appResolveInfoList.addAll(getMatchingList(intent2));
	         
	    } else if (packageName.contains("browser")) {
	         intent.setAction(Intent.ACTION_MAIN);
	         intent.addCategory(Intent.CATEGORY_LAUNCHER);
	         intent.addCategory(Intent.CATEGORY_APP_BROWSER);
	         appResolveInfoList.addAll(getMatchingList(intent));
	         
             Uri uri = Uri.parse(NORMAL_URL);
             Intent intent2 = new Intent(Intent.ACTION_WEB_SEARCH,uri);
             appResolveInfoList.addAll(getMatchingList(intent2));
	         
	    } else if (packageName.contains("camera")) {
	         intent.setAction(MediaStore.INTENT_ACTION_STILL_IMAGE_CAMERA);
	         appResolveInfoList.addAll(getMatchingList(intent));
	         
	         Intent intent2 = new Intent();
	         intent2.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
	         appResolveInfoList.addAll(getMatchingList(intent2));
	    }	    
	    
	    //remove the duplicate app
	    if( appResolveInfoList != null) {
	    	List<ResolveInfo> tmpList = new ArrayList<ResolveInfo>();
	    	
	    	for( ResolveInfo rv : appResolveInfoList) {
	    		if( tmpList.size() == 0) 
	    			tmpList.add(rv);
	    		for( ResolveInfo rvTmp : tmpList) {
	    			//maybe we need more 
	    			if(rvTmp.activityInfo.name.equals(rv.activityInfo.name) )
	    				continue;
	    			else
	    				tmpList.add(rvTmp);
	    		}
	    	}
	    	
	    	appResolveInfoList.clear();
	    	appResolveInfoList.addAll(tmpList);
	    }	    
	    
	    //step 3: compare the two lists
	    boolean bFound = false;
	    if( launchResolveInfoList != null) {
	    	for( ResolveInfo rv : launchResolveInfoList) {
	    		if(!bFound && matchingActivity(rv,appResolveInfoList)) {
	    			bFound = true;
	    			pkg = rv.activityInfo.packageName;
	    			cls = rv.activityInfo.name;
	    		}
	    	}
	    }

	    //step 4: if not matching, we have to match by "name"
	    if(!bFound) {
		    if( launchResolveInfoList != null) {
		    	for( ResolveInfo rv : launchResolveInfoList) {
		    		
		    		if(bFound)
		    			break;
		    		
			    	if(packageName.contains("dialer")) {
			    		if( rv.activityInfo.packageName.contains("dial") 
			    			|| rv.activityInfo.packageName.contains("phone") ) {
			    			bFound = true;
			    			pkg = rv.activityInfo.packageName;
			    			cls = rv.activityInfo.name;	
			    		}			    	
			    	} else if (packageName.contains("contact")) {
			    		if( rv.activityInfo.packageName.contains("contact") ) {
			    			bFound = true;
			    			pkg = rv.activityInfo.packageName;
			    			cls = rv.activityInfo.name;	
			    		}			    		
			    	} else if (packageName.contains("mms")) {
			    		if( rv.activityInfo.packageName.contains("mms") 
			    			|| rv.activityInfo.packageName.contains("sms")
			    			|| rv.activityInfo.packageName.contains("message") ) {
			    			bFound = true;
			    			pkg = rv.activityInfo.packageName;
			    			cls = rv.activityInfo.name;	
			    		}			    	
			    	} else if (packageName.contains("browser")) {
			    		if( rv.activityInfo.packageName.contains("web") 
			    			|| rv.activityInfo.packageName.contains("browser")
			    			|| rv.activityInfo.packageName.contains("internet") ) {
			    			bFound = true;
			    			pkg = rv.activityInfo.packageName;
			    			cls = rv.activityInfo.name;	
			    		}		    		
			    	} else if (packageName.contains("camera")) {
			    		if( rv.activityInfo.packageName.contains("cam") ) {
			    			bFound = true;
			    			pkg = rv.activityInfo.packageName;
			    			cls = rv.activityInfo.name;	
			    		}		    		
			    	}
		    	}
		    }
	    }

	    if(!bFound)
	    	Log.e("ZTETEST", "we are over, we try but we cannot!");
	    
    	ComponentName newComp = new ComponentName(pkg,cls);
	    return newComp;
	}    
    
    void testMyDialerIntent() {
		String oldPackageName="com.android.dialer";
		String oldClassName="com.android.dialer.DialtactsActivity";
		
		ComponentName comp = findRealPackage(oldPackageName,oldClassName);
		

        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        
        intent.setComponent(comp);
		startActivity(intent);
    }
    
    void testMyContactIntent() {
		String oldPackageName="com.android.contacts";
		String oldClassName="com.android.contacts.activities.PeopleActivity";
		
		ComponentName comp = findRealPackage(oldPackageName,oldClassName);
		

        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        
        intent.setComponent(comp);
		startActivity(intent);
    }
    
    void testMyMmsIntent() {
		String oldPackageName="com.android.ztemms";
		String oldClassName="com.android.ztemms.ui.BootActivity";
		
		ComponentName comp = findRealPackage(oldPackageName,oldClassName);
		

        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
                
        intent.setComponent(comp);
		startActivity(intent);
    }
    
    void testMyWebIntent() {
		String oldPackageName="com.android.browser";
		String oldClassName="com.android.browser.BrowserActivity";
		
		ComponentName comp = findRealPackage(oldPackageName,oldClassName);
		

        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        
        intent.setComponent(comp);
		startActivity(intent);
    }
    void testMyCameraIntent() {
		String oldPackageName="com.zte.camera";
		String oldClassName="com.zte.camera.CameraActivity";
		
		ComponentName comp = findRealPackage(oldPackageName,oldClassName);
		

        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        
        intent.setComponent(comp);
		startActivity(intent);
    }    
    
    
    public  void  removeDuplicate(List list) { 
	   for  ( int  i  =   0 ; i  <  list.size()  -   1 ; i ++ ) {  
	    for  ( int  j  =  list.size()  -   1 ; j  >  i; j -- ) {  
	      if  (list.get(j).equals(list.get(i)))   { 
	        list.remove(j); 
	        // j--;
	      } 
	    } 
	  } 
	  System.out.println(list); 
    }     
    
    void testAllLaucherIntent() {
    	
//    	List myString = Arrays.asList(
//    		"11",
//    		"12",
//    		"13",
//    		"11",
//    		"14",
//    		"15",
//    		"13",
//    		"17");
//    	removeDuplicate(myString);
    	
        final Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        
		PackageManager packageManager = this.getPackageManager();
		final List<ResolveInfo> resolveInfoList = packageManager.queryIntentActivities(intent, 0);

	      if( resolveInfoList != null) {
	    	
	    	String[] info = new String[resolveInfoList.size()]; 
	    	int i = 0;
	    	for(ResolveInfo ri : resolveInfoList) {
	    		//info[i++] = ri.toString();
	    		info[i++] = ri.activityInfo.name;
	    	}
	    	
	        new AlertDialog.Builder(this)
	        .setTitle("Select One App: " + "NO:" + resolveInfoList.size() + " " + intent.getAction() )
	        .setItems(info, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {

                    /* User clicked so do some stuff */
                	intent.setComponent(new ComponentName(resolveInfoList.get(which).activityInfo.packageName,
                			resolveInfoList.get(which).activityInfo.name));
                	startActivity(intent);	
                }
            })
	        .create()
	        .show();
	    } else {
	    	Toast.makeText(this, "No activity for this", Toast.LENGTH_SHORT).show();
	    	
	    }  		
    	
    }
    
    
    
	/**
     * Assert target intent can be handled by at least one Activity.
     * @param intent - the Intent will be handled.
     */
    private void assertCanBeHandled(final Intent intent) {
        PackageManager packageManager = this.getPackageManager();
        List<ResolveInfo> resolveInfoList = packageManager.queryIntentActivities(intent, 0);
        //assertNotNull(resolveInfoList);
        // one or more activity can handle this intent.
        //assertTrue(resolveInfoList.size() > 0);
        
        startActivity(Intent.createChooser(intent, "Select:" + intent.getAction()));
        
//        if( resolveInfoList != null) {
//        	
//        	String[] info = new String[resolveInfoList.size()]; 
//        	int i = 0;
//        	for(ResolveInfo ri : resolveInfoList) {
//        		info[i++] = ri.toString();
//        	}
//        	
//	        new AlertDialog.Builder(this)
//	        .setTitle("Select One App: " + intent.getAction())
//	        .setItems(info, null)
//	        .create()
//	        .show();
//        } else {
//        	Toast.makeText(this, "No activity for this", Toast.LENGTH_SHORT).show();
//        	
//        }        
    }

    /**
     * Test ACTION_VIEW when url is http://web_address,
     * it will open a browser window to the URL specified.
     */
    public void testViewNormalUrl() {
        //Uri uri = Uri.parse(NORMAL_URL);
    	Uri uri = Uri.parse("www.ba");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        assertCanBeHandled(intent);
    }

    /**
     * Test ACTION_VIEW when url is https://web_address,
     * it will open a browser window to the URL specified.
     */
    public void testViewSecureUrl() {
        Uri uri = Uri.parse(SECURE_URL);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        assertCanBeHandled(intent);
    }

    /**
     * Test ACTION_WEB_SEARCH when url is http://web_address,
     * it will open a browser window to the URL specified.
     */
    public void testWebSearchNormalUrl() {
        Uri uri = Uri.parse(NORMAL_URL);
        Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
        intent.putExtra(SearchManager.QUERY, uri);
        assertCanBeHandled(intent);
    }

    /**
     * Test ACTION_WEB_SEARCH when url is https://web_address,
     * it will open a browser window to the URL specified.
     */
    public void testWebSearchSecureUrl() {
        Uri uri = Uri.parse(SECURE_URL);
        Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
        intent.putExtra(SearchManager.QUERY, uri);
        assertCanBeHandled(intent);
    }

    /**
     * Test ACTION_WEB_SEARCH when url is empty string,
     * google search will be applied for the plain text.
     */
    public void testWebSearchPlainText() {
        String searchString = "where am I?";
        Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
        intent.putExtra(SearchManager.QUERY, searchString);
        assertCanBeHandled(intent);
    }

    /**
     * Test ACTION_CALL when uri is a phone number, it will call the entered phone number.
     */
    public void testCallPhoneNumber() {
        PackageManager packageManager = this.getPackageManager();
        if (packageManager.hasSystemFeature(PackageManager.FEATURE_TELEPHONY)) {
            Uri uri = Uri.parse("tel:2125551212");
            Intent intent = new Intent(Intent.ACTION_CALL, uri);
            assertCanBeHandled(intent);
        }
    }

    /**
     * Test ACTION_DIAL when uri is a phone number, it will dial the entered phone number.
     */
    public void testDialPhoneNumber() {
        PackageManager packageManager = this.getPackageManager();
        if (packageManager.hasSystemFeature(PackageManager.FEATURE_TELEPHONY)) {
            Uri uri = Uri.parse("tel:(212)5551212");
            Intent intent = new Intent(Intent.ACTION_DIAL, uri);
            assertCanBeHandled(intent);
        }
    }

    /**
     * Test ACTION_DIAL when uri is a phone number, it will dial the entered phone number.
     */
    public void testDialVoicemail() {
        PackageManager packageManager = this.getPackageManager();
        if (packageManager.hasSystemFeature(PackageManager.FEATURE_TELEPHONY)) {
            Uri uri = Uri.parse("voicemail:");
            Intent intent = new Intent(Intent.ACTION_DIAL, uri);
            assertCanBeHandled(intent);
        }
    }

    /**
     * Test start camera by intent
     */
    public void testCamera() {
        PackageManager packageManager = this.getPackageManager();
        if (packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA)
                || packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT)) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            assertCanBeHandled(intent);

            intent.setAction(MediaStore.ACTION_VIDEO_CAPTURE);
            assertCanBeHandled(intent);

            intent.setAction(MediaStore.INTENT_ACTION_STILL_IMAGE_CAMERA);
            assertCanBeHandled(intent);

            intent.setAction(MediaStore.INTENT_ACTION_VIDEO_CAMERA);
            assertCanBeHandled(intent);
        }
    }

    public void testSettings() {
        assertCanBeHandled(new Intent(Settings.ACTION_SETTINGS));
    }

    /**
     * Test add event in calendar
     */
    public void testCalendarAddAppointment() {
        Intent addAppointmentIntent = new Intent(Intent.ACTION_EDIT);
        addAppointmentIntent.setType("vnd.android.cursor.item/event");
        assertCanBeHandled(addAppointmentIntent);
    }

    /**
     * Test view call logs
     */
    public void testContactsCallLogs() {
        PackageManager packageManager = this.getPackageManager();
        if (packageManager.hasSystemFeature(PackageManager.FEATURE_TELEPHONY)) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setType("vnd.android.cursor.dir/calls");
            assertCanBeHandled(intent);
        }
    }

    /**
     * Test view music playback
     */
    public void testMusicPlayback() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(ContentUris.withAppendedId(
                MediaStore.Audio.Media.INTERNAL_CONTENT_URI, 1), "audio/*");
        assertCanBeHandled(intent);
    }

    public void testAlarmClock() {
        Intent intent = new Intent(AlarmClock.ACTION_SET_ALARM);
        intent.putExtra(AlarmClock.EXTRA_MESSAGE, "Custom message");
        intent.putExtra(AlarmClock.EXTRA_HOUR, 12);
        intent.putExtra(AlarmClock.EXTRA_MINUTES, 0);
        assertCanBeHandled(intent);
    }

    public void testOpenDocumentAny() {
//        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
//        intent.addCategory(Intent.CATEGORY_OPENABLE);
//        intent.setType("*/*");
//        assertCanBeHandled(intent);
    }

    public void testOpenDocumentImage() {
//        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
//        intent.addCategory(Intent.CATEGORY_OPENABLE);
//        intent.setType("image/*");
//        assertCanBeHandled(intent);
    }

    public void testCreateDocument() {
//        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
//        intent.addCategory(Intent.CATEGORY_OPENABLE);
//        intent.setType("text/plain");
//        assertCanBeHandled(intent);
    }

    public void testGetContentAny() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");
        assertCanBeHandled(intent);
    }

    public void testGetContentImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        assertCanBeHandled(intent);
    }

    public void testRingtonePicker() {
        assertCanBeHandled(new Intent(RingtoneManager.ACTION_RINGTONE_PICKER));
    }

    public void testViewDownloads() {
        assertCanBeHandled(new Intent(DownloadManager.ACTION_VIEW_DOWNLOADS));
    }
}
