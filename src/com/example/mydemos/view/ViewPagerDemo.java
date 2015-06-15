package com.example.mydemos.view;

import com.example.mydemos.R;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ActionBar.Tab;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

public class ViewPagerDemo extends Activity implements ActionBar.TabListener {
	
	private static final int TAB_INDEX_ONE = 0;  
	private static final int TAB_INDEX_TWO = 1;
	private static final int TAB_INDEX_COUNT = 2;
	
	private String[] tabString = {
			"One", "Two"
	};
	
	
	private int mCurPos;
	
    private ViewPager mViewPager;
    private MyPagerAdapter mAdapter;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.view_pager_demo);
        
               
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mAdapter = new MyPagerAdapter(getFragmentManager());
        
        mViewPager.setAdapter(mAdapter);
        mViewPager.setOnPageChangeListener(mAdapter);
        
        
        final ActionBar bar = getActionBar();
        bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        
        bar.addTab(bar.newTab()
                .setText("Simple")
                .setTabListener(this));
        bar.addTab(bar.newTab()
                .setText("Contacts")
                .setTabListener(this));

             
        
    }
	
    
    
    class MyPagerAdapter extends FragmentPagerAdapter implements ViewPager.OnPageChangeListener {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
        	switch(position) {
        	case TAB_INDEX_ONE:
        		return new ArrayListFragment();
        	case TAB_INDEX_TWO:
        		return new ArrayListFragment();
        	}
        	throw new IllegalStateException("No fragment at position " + position);  
        }

        @Override
        public int getCount() {
            return TAB_INDEX_COUNT;
        }

        @Override
        public CharSequence getPageTitle(int position) {
        	switch(position) {
        	case TAB_INDEX_ONE:
        		return tabString[TAB_INDEX_ONE];
        	case TAB_INDEX_TWO:
        		return tabString[TAB_INDEX_TWO]; 
        	}
        	throw new IllegalStateException("No fragment at position " + position);  
        }
        
        public int getCurrentPage() {
            return mCurPos;
        }
        
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            mCurPos = position;
            
            //update action bar state
            final ActionBar actionBar = getActionBar();  
            actionBar.setSelectedNavigationItem(position); 
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            if (state == ViewPager.SCROLL_STATE_IDLE) {
                //updateCurrentTab(mCurPos);
            }
        }
    }
    
    
 

    	    

    	
    	/**
    	 * these are tab listener interface
    	 * */
        public void onTabSelected(Tab tab, FragmentTransaction ft) {
        	// notify view pager to change fragment
        	mViewPager.setCurrentItem(tab.getPosition());
        }

        public void onTabUnselected(Tab tab, FragmentTransaction ft) {

        }

        public void onTabReselected(Tab tab, FragmentTransaction ft) {
            //Toast.makeText(this, "Reselected!", Toast.LENGTH_SHORT).show();
        }    	
    	
    
}