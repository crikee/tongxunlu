package com.gsf;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.Photo;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class SecretContacts extends Activity {
	
	private ListView listview ;
	private Button   addSecretContacts ;
	private DatabaseHelper dbhelper = new DatabaseHelper(this) ;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.secret_contacts);
		
		listview          = (ListView)findViewById(R.id.lv_contactslist);	 
		addSecretContacts = (Button)findViewById(R.id.btn_secretcontacts_add);
		
		addSecretContacts.setOnClickListener(new Button.OnClickListener(){
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(SecretContacts.this, ContactsList.class);
				startActivity(intent);
				finish();
			}
		});		
		
		showSecretContacts();
	}
	
	
	
	/**得到私密联系人信息**/
    private List<Map<String, String>> getSecretContacts() {
    	
    	final int PHONE_INDEX = 1 ;
    	final int NAME_INDEX = 2 ;
    	String phone = "" ;
    	String name  = "" ;
        List<Map<String, String>> datalist = new ArrayList<Map<String, String>>(); 
        
    	try{ 
    		
    		Cursor cur = dbhelper.queryCon();
    		if (cur.moveToFirst()) {       			
    			do
    			{
    				phone = cur.getString(PHONE_INDEX);
    				name = cur.getString(NAME_INDEX);
    			
	    			Map<String, String> map = new HashMap<String, String>();		                
	                map.put("phone",phone );
	                map.put("name", name);	
	                datalist.add(map);

    			}
    			while(cur.moveToNext()); 
    		}
    	
    	dbhelper.close();
    	
    	} catch(SQLiteException ex) {     
	        Log.d("SQLiteException in getSecretContacts", ex.getMessage());     
	    }  
    	
    	return datalist ;
    }
    
	
	public void showSecretContacts()
	{		
		
		List<Map<String, String>> data = getSecretContacts();
		System.out.println(data.size());
		
		String[] from = {"name","phone"} ;
		int[] to = {R.id.tv_secret_contactslistitem_name,R.id.tv_secret_contactslistitem_phone} ;		
		
		SimpleAdapter adapter = new SimpleAdapter(SecretContacts.this,data,
				R.layout.secret_contactslist_item,from,to) ;
		listview.setAdapter(adapter) ;
		
		
		listview.setOnItemClickListener(new OnItemClickListener() {
			
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                    long arg3) {
            	
            	/*
            	@SuppressWarnings("unchecked")
				Map<String, String> map = (Map<String, String>)listview.getItemAtPosition(arg2);
            	String  phone = map.get("phone");
            	String  name  = map.get("name");
            	
            	ContentValues values = new ContentValues();
				values.put("phone",phone);
				values.put("name", name);
				dbhelper.insertCon(values);
				
				System.out.println("insert" + name + " " + phone + "successfully!");
				*/
            	
            }
        });
        
	}
	
	
}