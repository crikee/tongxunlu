package com.gsf;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.TabActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;


public class TongXunLuTab extends TabActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.main);      
	 
	        createTab("联系人",new Intent(this,MyContacts.class),"tab1");
	        createTab("信息",new Intent(this, Mailbox.class),"tab2");

	    }  
	   
	    private void createTab(String text ,Intent intent,String drawablename){  
	    	TabHost tabHost = getTabHost();  
	    	tabHost.addTab(tabHost.newTabSpec(text).setIndicator(
	    		createTabView(text,drawablename)).setContent(intent));
	    	
		}

	    private View createTabView(String text ,String drawablename) {
	    		LayoutInflater mInflater = LayoutInflater.from(this) ;
	        	View view = mInflater.inflate(R.layout.tab_indicator, null);
	        	//TextView tv = (TextView) view.findViewById(R.id.tv_tab);
	        	ImageView iv = (ImageView)view.findViewById(R.id.iv_tab);
	        	//Drawable d = null;
	        	
	        	if(drawablename=="tab1")
	        		iv.setImageDrawable(this.getResources().getDrawable(R.drawable.tab_home_selector)) ; 
	        	if(drawablename=="tab2")
	        		iv.setImageDrawable(this.getResources().getDrawable(R.drawable.tab_map_selector)) ; 
	   	        	
	            //tv.setText(text);
	        	return view;
	    }
	   
	  public boolean onCreateOptionsMenu(Menu menu) {
			super.onCreateOptionsMenu(menu);
			menu.add(0, 1, 0, "关于");
			menu.add(0, 2, 0, "退出");
			return true;
		}
	  
		// 现象菜单项单击方法
		public boolean onOptionsItemSelected(MenuItem item) {
			switch (item.getItemId()) {
			case 1:
				return true;
			case 2:
				AlertDialog.Builder builder = new AlertDialog.Builder(TongXunLuTab.this);
				builder.setMessage("你确定要退出*呵呵*吗?")
				.setPositiveButton("确定",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {	
				            	finish();
							}
				})
				.setNegativeButton("取消",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {	
				        
							}
						});
				AlertDialog ad = builder.create();
				ad.show();
			}
			return false;
		}
	  
		
		
	}     
	 
	    
	   