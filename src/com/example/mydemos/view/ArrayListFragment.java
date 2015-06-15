package com.example.mydemos.view;

import com.example.mydemos.R;
import android.os.Bundle;
import android.app.ListFragment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ArrayListFragment extends ListFragment{  
    	
	@Override  
	public void onActivityCreated(Bundle savedInstanceState) {  
		// TODO Auto-generated method stub   
		super.onActivityCreated(savedInstanceState);  
		String [] array = new String[]{"C", "C++", "Java"};  
		setListAdapter(new ArrayAdapter<String>(getActivity(),   
			android.R.layout.simple_list_item_1, array));  
		}  
	 
	@Override  
	public void onListItemClick(ListView l, View v, int position, long id) {  
	     // TODO Auto-generated method stub   
	     super.onListItemClick(l, v, position, id);  
	}  
	
} 