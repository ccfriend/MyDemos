package com.example.mydemos.view;

import android.app.Fragment;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.Toast;

import com.example.mydemos.R;

public class ContextMenuFragment extends Fragment{  
	@Override  
	public View onCreateView(LayoutInflater inflater, ViewGroup container,  
				Bundle savedInstanceState) {  
      // TODO Auto-generated method stub   
       View root = inflater.inflate(R.layout.fragment_context_menu, container, false);  
       registerForContextMenu(root.findViewById(R.id.long_press));  
       return root;  
    }  
	      
	@Override  
	public void onCreateContextMenu(ContextMenu menu, View v,  
	  ContextMenuInfo menuInfo) {  
         // TODO Auto-generated method stub   
         super.onCreateContextMenu(menu, v, menuInfo);
         MenuInflater inflater = getActivity().getMenuInflater();
         inflater.inflate(R.layout.fragment_context_menu, menu);
         //menu.add(Menu.NONE, R.id.a_item, Menu.NONE, "Menu A");  
         //menu.add(Menu.NONE, R.id.b_item, Menu.NONE, "Menu B");  
	}  
	       
	@Override  
	public boolean onContextItemSelected(MenuItem item) {  
    	// TODO Auto-generated method stub   
    	switch (item.getItemId()) {  
    	case R.id.a_item:  
    		Toast.makeText(getActivity(), "a_item...", Toast.LENGTH_SHORT).show();  
    		break;  
    	case R.id.b_item:  
    		Toast.makeText(getActivity(), "b_item...", Toast.LENGTH_SHORT).show();  
    		break;  
    	default:  
    		break;  
    	}  
    	return super.onContextItemSelected(item);  
	}  
}