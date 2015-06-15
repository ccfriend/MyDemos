package com.example.mydemos.view;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.ListPreference;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.mydemos.R;


public class IconDecorListPreference extends ListPreference{
	
	public static String key = "icon_background_set_preference";
	private Context mContext;
	ArrayList<RadioButton> rButtonList;
	private static int mSelectedId = -1;
	private static int []imgIds = {R.drawable.icon_bg_0, R.drawable.icon_bg_01, R.drawable.icon_bg_02,
		R.drawable.icon_bg_03,R.drawable.icon_bg_04,R.drawable.icon_bg_05};
	private static int []textIds = {R.string.pref_icon_decor_0, R.string.pref_icon_decor_01, 
		R.string.pref_icon_decor_02,R.string.pref_icon_decor_03,R.string.pref_icon_decor_04,R.string.pref_icon_decor_05};
	private static float []scale = {0, 0.7f, 0.7f, 0.6f, 0.6f, 0.6f,};
	private static float []padding = {0f, 0f, 0f, 0.2f, 0.2f, 0.03f,};
	
	private CharSequence[] mEntries = {"111","111","111","111","111","111","111"};
	
	public IconDecorListPreference(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		Init(context);
	}
	
	public IconDecorListPreference(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		Init(context);
	}
	
	private void Init(Context context){
		mContext = context;
		getCurrentImgId(context);
	}
	
	@Override
	protected void onPrepareDialogBuilder(Builder builder) {
		rButtonList = new ArrayList<RadioButton>();
        this.setSummary(textIds[mSelectedId]);
        builder.setPositiveButton(null, null);
        List<CustomItem> items = new ArrayList<CustomItem>();
        for(int i=0; i<imgIds.length; i++){
        	CustomItem item = new CustomItem(i, imgIds[i], textIds[i]);
        	items.add(item);
        }
        CustomArrayAdapter adapter = new CustomArrayAdapter(mContext, R.xml.icon_decor_preference,items, mSelectedId);
        builder.setAdapter(adapter, this);
        //builder.setSingleChoiceItems(adapter, mSelectedId, this);
        
        

	}
	
	public static String getSummaryInfo(Context context){
		getCurrentId(context);
		return context.getResources().getString(textIds[mSelectedId]);
	}
	
	public static int getCurrentId(Context context){
		if(mSelectedId < 0){
			SharedPreferences iconSizeprefs = PreferenceManager.getDefaultSharedPreferences(context);  
			mSelectedId = Integer.parseInt(iconSizeprefs.getString(key, "0"));
		}
		return mSelectedId;
	}
	
	public static int getCurrentImgId(Context context){
		getCurrentId(context);
		return imgIds[mSelectedId];
	}
	
	public static float getCurrentScale(Context context){
		getCurrentId(context);
		return scale[mSelectedId];
	}
	
	public static float getCurrentPadding(Context context){
		getCurrentId(context);
		return padding[mSelectedId];
	}
	
	class CustomItem{
		public int imgId;
		public int textId;
		public int itemId;
		public CustomItem(int id, int img, int text){
			imgId = img;
			textId = text;
			itemId = id;
		}
	}
	
	// listview can be focused and selected for P816E20_COM
	public void onClick(DialogInterface dialog, int which) {
		
		//this means we choose the list, not the dlg button
		if(which >= 0 ) {
			saveRes(which);
		}
	}
	
	private void saveRes(int id){
        mSelectedId = id;
		Editor sharedata = PreferenceManager.getDefaultSharedPreferences(mContext).edit();
		sharedata.putString(key, String.valueOf(id));
		sharedata.commit();
		IconDecorListPreference.this.setSummary(IconDecorListPreference.getSummaryInfo(mContext));		
	}
	//end
	
	class CustomArrayAdapter extends ArrayAdapter<CustomItem>{
		
		private int selectedId = 0;
		private int resId ;
		
		public CustomArrayAdapter(Context context, int textViewResourceId, List<CustomItem> objects, int selected){
			super(context, textViewResourceId, objects);
			
			resId = textViewResourceId;
			selectedId = selected;
		}
		
		@Override  
	    public View getView(int position, View convertView, ViewGroup parent){
			
			final CustomItem item = getItem(position);
			
			LayoutInflater inflater = ((Activity) getContext()).getLayoutInflater();
			View row = inflater.inflate(resId, parent, false);
			row.setId(item.itemId);
			
			ImageView img = (ImageView) row.findViewById(R.id.icon_decor_preference_img);
			img.setImageResource(item.imgId);

			// listview can be focused and selected for P816E20_COM
            CheckedTextView checkedTextView = (CheckedTextView)row.findViewById(R.id.icon_decor_preference_button);
            checkedTextView.setText(item.textId);
            if (selectedId == position) {
                 checkedTextView.setChecked(true);
            } else
                 checkedTextView.setChecked(false);
            
//			TextView tv = (TextView) row.findViewById(R.id.icon_decor_preference_text);
//			tv.setText(item.textId);
//
//			RadioButton tb = (RadioButton) row.findViewById(R.id.icon_decor_preference_button);
//			tb.setOnClickListener(new OnClickListener(){
//
//				@Override
//				public void onClick(View arg0) {
//					// TODO Auto-generated method stub
//					for(RadioButton rb : rButtonList){
//					    if(rb == arg0){
//                            rb.setChecked(true);
//                        }else{
//                            rb.setChecked(false);
//                        }
//					}
//					saveRes(item.itemId);
//					Dialog mDialog = getDialog();
//                    mDialog.dismiss();
//				}});
//			if(selectedId == position){
//				tb.setChecked(true);
//			}
//			if(!rButtonList.contains(tb)){
//			    rButtonList.add(tb);
//			}
            //end
			return row;
		}
		
		private void saveRes(int id){
            mSelectedId = id;
			Editor sharedata = PreferenceManager.getDefaultSharedPreferences(mContext).edit();
			sharedata.putString(key, String.valueOf(id));
			sharedata.commit();
			IconDecorListPreference.this.setSummary(IconDecorListPreference.getSummaryInfo(mContext));
			//AdaptTools.CommitSuicide();
		}
		
	}

}