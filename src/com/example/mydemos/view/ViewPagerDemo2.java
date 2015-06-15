package com.example.mydemos.view;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.example.mydemos.R;
import com.example.mydemos.view.ViewPagerDemo2.MyPagerAdapter;

public class ViewPagerDemo2 extends Activity implements OnClickListener{
	
	private ViewPager mViewPager;
	private List<Fragment> mFragments;
    private MyPagerAdapter mAdapter;
    
	private LinearLayout mTabWeixin;
	private LinearLayout mTabFrd;
	private LinearLayout mTabAddress;
	private LinearLayout mTabSettings;

	private ImageButton mImgWeixin;
	private ImageButton mImgFrd;
	private ImageButton mImgAddress;
	private ImageButton mImgSettings;
	

    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.view_pager_demo2);

		initView();
		initEvent();
		
		setSelect(1);
    }
    
	private void initView()
	{
		mViewPager = (ViewPager) findViewById(R.id.id_viewpager);

		mTabWeixin = (LinearLayout) findViewById(R.id.id_tab_weixin);
		mTabFrd = (LinearLayout) findViewById(R.id.id_tab_frd);
		mTabAddress = (LinearLayout) findViewById(R.id.id_tab_address);
		mTabSettings = (LinearLayout) findViewById(R.id.id_tab_settings);

		mImgWeixin = (ImageButton) findViewById(R.id.id_tab_weixin_img);
		mImgFrd = (ImageButton) findViewById(R.id.id_tab_frd_img);
		mImgAddress = (ImageButton) findViewById(R.id.id_tab_address_img);
		mImgSettings = (ImageButton) findViewById(R.id.id_tab_settings_img);

		mFragments = new ArrayList<Fragment>();
		Fragment mTab01 = new WeixinFragment();
		Fragment mTab02 = new FrdFragment();
		Fragment mTab03 = new AddressFragment();
		Fragment mTab04 = new SettingFragment();
		mFragments.add(mTab01);
		mFragments.add(mTab02);
		mFragments.add(mTab03);
		mFragments.add(mTab04);

		mAdapter = new MyPagerAdapter(getFragmentManager()); 

		mViewPager.setAdapter(mAdapter);
		
		mViewPager.setOnPageChangeListener(mAdapter);
	}
	
	
	private void initEvent()
	{
		mTabWeixin.setOnClickListener(this);
		mTabFrd.setOnClickListener(this);
		mTabAddress.setOnClickListener(this);
		mTabSettings.setOnClickListener(this);
	}

 
	private void setTab(int i)
	{
		resetImgs();
		// …Ë÷√Õº∆¨Œ™¡¡…´
		// «–ªªƒ⁄»›«¯”Ú
		switch (i)
		{
		case 0:
			mImgWeixin.setImageResource(R.drawable.tab_weixin_pressed);
			break;
		case 1:
			mImgFrd.setImageResource(R.drawable.tab_find_frd_pressed);
			break;
		case 2:
			mImgAddress.setImageResource(R.drawable.tab_address_pressed);
			break;
		case 3:
			mImgSettings.setImageResource(R.drawable.tab_settings_pressed);
			break;
		}
	}
    	    
	private void setSelect(int i)
	{
		setTab(i);
		mViewPager.setCurrentItem(i);
	}

	private void resetImgs()
	{
		mImgWeixin.setImageResource(R.drawable.tab_weixin_normal);
		mImgFrd.setImageResource(R.drawable.tab_find_frd_normal);
		mImgAddress.setImageResource(R.drawable.tab_address_normal);
		mImgSettings.setImageResource(R.drawable.tab_settings_normal);
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId())
		{
		case R.id.id_tab_weixin:
			setSelect(0);
			break;
		case R.id.id_tab_frd:
			setSelect(1);
			break;
		case R.id.id_tab_address:
			setSelect(2);
			break;
		case R.id.id_tab_settings:
			setSelect(3);
			break;

		default:
			break;
		}
	}    	
    	
    
    class MyPagerAdapter extends FragmentPagerAdapter implements ViewPager.OnPageChangeListener {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }


		@Override
        public Fragment getItem(int position) {
        	return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return  mFragments.size();
        }
        
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
			int currentItem = mViewPager.getCurrentItem();
			setTab(currentItem);
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            if (state == ViewPager.SCROLL_STATE_IDLE) {
                //updateCurrentTab(mCurPos);
            }
        }
    }
    
    	
    
}