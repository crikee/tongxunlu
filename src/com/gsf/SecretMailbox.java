package com.gsf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class SecretMailbox extends Activity {
	
	private ListView listview ;
	private Button   manage ;
	private DatabaseHelper dbhelper = new DatabaseHelper(this) ;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		
		setContentView(R.layout.secret_sms);
		
		listview = (ListView)findViewById(R.id.lv_secertsms_sms);
		manage = (Button)findViewById(R.id.btn_secertsms_manage);
		manage.setOnClickListener(new Button.OnClickListener(){
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(SecretMailbox.this, SecretContacts.class);
				startActivity(intent);
				finish();
			}
		});		
		
		//insertSecretSmsToDb();
		showSms();
		
	}
	
	
	private List<Map<String, String>> getSecretSms()
	{
		String address  = "" ;     
        String name   = "" ;  
        String body   = "" ;  
       
        
		List<Map<String, String>> datalist = new ArrayList<Map<String, String>>(); 
		
		try{     
	    	
			SortedSet<String> ss = new TreeSet<String>();
	        Cursor cur = dbhelper.querySms();  
	        
	        int addressColumn = cur.getColumnIndex("address");     
            int bodyColumn = cur.getColumnIndex("body");
	     	        	        
	        if (cur.moveToFirst()) {    
	           
	            do{  
	            	
	            	address = cur.getString(addressColumn);     
	            	if(ss.contains(address))
	            		continue ;
	            	ss.add(address);
	            	
	            	//name = cur.getString(NAME_INDEX); 	            		
		            body = cur.getString(bodyColumn);  
		            
		            //System.out.println(phone + " " +  name + " " + body);
		            Map<String, String> map = new HashMap<String, String>();	            
		            map.put("address", address);	
		            //map.put("name", name);
		            map.put("body",body);		           
		            datalist.add(map);
		            
	            }while(cur.moveToNext());     
	        }                
	        dbhelper.close();
	        
	    } catch(SQLiteException ex) {     
	        Log.d("SQLiteException in getSecretSms", ex.getMessage());     
	    }     
		
		return datalist ;
	}
	
	
	
	private void insertSecretSmsToDb()     
	{
		
	    final String SMS_URI_ALL   = "content://sms/";   
	    ArrayList<String> phoneslist = getSecretPhones() ;	
	    
	    try{     
	    	
	        ContentResolver cr = getContentResolver();     
	        //String[] projection = new String[]{"address", "person",      
	        //       "body", "date", "type"};   
	        String[] projection = new String[]{"address", 
	        		"person", 
	        		"date", 
	        		"protocol", 
	        		"read",
	        		"status", 
	        		"type", 
	        		"reply_path_present",  
	        		"body",  
	        		"locked", 
	        		"error_code", 
	        		"seen"};  
	        
	        Uri uri = Uri.parse(SMS_URI_ALL);  
	        Cursor cur = cr.query(uri, projection, null, null, "date desc");  
	         
	        	        
	        if (cur.moveToFirst()) {  
	        	
	        	String address  ;
	        	String person ;
	        	String date  ;
	        	String protocol ;  
	        	String read  ;
	        	String status ; 
	        	String type ;
	        	String reply_path_present ;  
	        	String body  ;
	        	String locked ;
	        	String error_code ; 
	        	String seen ;
	          
	              
	            int addressColumn = cur.getColumnIndex("address");     
	            int personColumn = cur.getColumnIndex("person");     
	            int dateColumn = cur.getColumnIndex("date");     
	            int protocolColumn = cur.getColumnIndex("protocol");     
	            int readColumn = cur.getColumnIndex("read");  
	            int statusColumn = cur.getColumnIndex("status");
	            int typeColumn = cur.getColumnIndex("type");
	            int replyPathPresentColumn = cur.getColumnIndex("reply_path_present");
	            int bodyColumn = cur.getColumnIndex("body");
	            int lockedColumn = cur.getColumnIndex("locked");
	            int errorCodeColumn = cur.getColumnIndex("error_code");
	            int seenColumn = cur.getColumnIndex("seen");
	            
	          
	            do{              
	            	address = cur.getString(addressColumn);   
	            	if(address.length()>11)
	            		address = address.substring(address.length()-11, address.length());
	            	if(phoneslist.contains(address)==false)
	            		continue ;
	            	person = cur.getString(personColumn); 	            		
		        	date  = cur.getString(dateColumn); 	
		        	protocol = cur.getString(protocolColumn); 	 
		        	read  = cur.getString(readColumn); 	
		        	status = cur.getString(statusColumn); 	 
		        	type = cur.getString(typeColumn); 	
		        	reply_path_present = cur.getString(replyPathPresentColumn); 	  
		        	body  = cur.getString(bodyColumn); 	
		        	locked = cur.getString(lockedColumn); 	
		        	error_code = cur.getString(errorCodeColumn); 	
		        	seen = cur.getString(seenColumn); 	 
		            
		            System.out.println(address + body);
		            
		            ContentValues values = new ContentValues();
					values.put("address",address);
					values.put("person", person);
					values.put("protocol", protocol);
					values.put("read", read);
					values.put("status", status);
					values.put("type", type);					
					values.put("reply_path_present", reply_path_present);
					values.put("body", body);
					values.put("locked", locked);
					values.put("error_code", error_code);
					values.put("seen", seen);
					
					dbhelper.insertSms(values);
					
	            }while(cur.moveToNext());     
	        }               
	        
	    dbhelper.close();   
	    
	    } catch(SQLiteException ex) {     
	        Log.d("SQLiteException in getSecretSms", ex.getMessage());     
	    }     
	  
	}   
	
	private ArrayList<String> getSecretPhones()
	{
		final int PHONE_INDEX = 1 ;		
		String phone = "" ;
		
		ArrayList<String> phoneslist = new ArrayList<String>();
		
		try{ 
    		
    		Cursor cur = dbhelper.queryCon();
    		if (cur.moveToFirst()) {       			
    			do
    			{
    				phone = cur.getString(PHONE_INDEX);    	
    				System.out.println(phone);
	                phoneslist.add(phone);

    			}
    			while(cur.moveToNext()); 
    		}
    	} catch(SQLiteException ex) {     
	        Log.d("SQLiteException in getSecretContacts", ex.getMessage());     
	    }  
		
		return phoneslist ;		
	}
	
	public void showSms()
	{		
		
		List<Map<String, String>> data = getSecretSms();
		System.out.println(data.size());
		
		String[] from = {"address","body"} ;
		int[] to = {R.id.tv_smsslistitem_person,R.id.tv_smsslistitem_body} ;		
		
		SimpleAdapter adapter = new SimpleAdapter(SecretMailbox.this,data,
				R.layout.smslist_item,from,to) ;
		listview.setAdapter(adapter) ;
		
		
		listview.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                    long arg3) {
           
            	@SuppressWarnings("unchecked")
				Map<String, String> map = (Map<String, String>)listview.getItemAtPosition(arg2);
            	String  address = map.get("address");
            	
            	Intent intent = new Intent(SecretMailbox.this,SecretSms.class);
	            intent.putExtra("address",address);
	          	startActivity(intent);     
	                	
            }
        });
        
	}
	
	
}