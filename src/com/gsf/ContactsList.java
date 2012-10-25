package com.gsf;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import com.gsf.Mailbox.getSms;



import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
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

public class ContactsList extends Activity {
	
	private ListView listview ;
	private Button   confirm  ;
	private DatabaseHelper dbhelper = new DatabaseHelper(this) ;
	private ContactsSelectViewAdapter adapter ;
	private ProgressDialog pd;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	
		setContentView(R.layout.contactslist);
		
		confirm = (Button)findViewById(R.id.btn_contactslist_ok);
		listview = (ListView)findViewById(R.id.lv_contactslist);	
		
		confirm.setOnClickListener(new Button.OnClickListener(){
			public void onClick(View v) {

				/*
            	ArrayList<Contacts> conToAdd = adapter.getConToAdd() ;           	
            	for(int i=0 ; i <conToAdd.size() ; i++)
            	{
            		Contacts tem = conToAdd.get(i);
                	ContentValues values = new ContentValues();
    				values.put("phone",tem.getPhone());
    				values.put("name",tem.getName());
    				dbhelper.insertCon(values);    				
    				System.out.println("insert" + tem.getPhone() + " " + tem.getName() + "successfully!");
    				insertSecretSmsToDb(tem.getPhone());
            	}
				*/
				new removeSmsToDb().execute(null, null, null);
				pd = ProgressDialog.show(ContactsList.this,"请稍等","正在导入私密信息...");	
	          	//finish();  	
	          	
			}
		});		
		
		
		showContacts();
	}
	
	
	
	/**得到手机通讯录联系人信息**/
    private ArrayList<Contacts> getContactsInPhone() {
    	
    	/**获取库Phone表字段**/
        final String[] PHONES_PROJECTION = new String[] {
    	    Phone.DISPLAY_NAME, Phone.NUMBER, Photo.PHOTO_ID,Phone.CONTACT_ID };       
        /**联系人显示名称**/
        final int PHONES_DISPLAY_NAME_INDEX = 0;        
        /**电话号码**/
        final int PHONES_NUMBER_INDEX = 1;        
        /**头像ID**/
        final int PHONES_PHOTO_ID_INDEX = 2;       
        /**联系人的ID**/
        final int PHONES_CONTACT_ID_INDEX = 3;
    	
        //List<Map<String, String>> datalist = new ArrayList<Map<String, String>>(); 
        ArrayList<Contacts> datalist = new ArrayList<Contacts>();
        
    	try{
    		ContentResolver cr = getContentResolver();

    		// 获取手机联系人
    		Cursor cur = cr.query(Phone.CONTENT_URI,PHONES_PROJECTION, null, null, null);

    		if (cur.moveToFirst()) {       			
    			do
    			{
    				//得到手机号码
    				String phone = cur.getString(PHONES_NUMBER_INDEX);
    				//当手机号码为空的或者为空字段 跳过当前循环
    				if (TextUtils.isEmpty(phone))
    					continue;
    			
    				//得到联系人名称
    				String name = cur.getString(PHONES_DISPLAY_NAME_INDEX);
    			
	    			//得到联系人ID
	    			Long contactid = cur.getLong(PHONES_CONTACT_ID_INDEX);
	    			
	    			//Map<String, String> map = new HashMap<String, String>();		                
	                //map.put("phone",phone );
	                //map.put("name", name);	
	    			Contacts tem = new Contacts(phone,name);
	                datalist.add(tem);

    			}
    			while(cur.moveToNext()); 
    		}
    	} catch(SQLiteException ex) {     
	        Log.d("SQLiteException in getContactsInPhone", ex.getMessage());     
	    }  
    	
    	return datalist ;
    }
    
    public class removeSmsToDb extends AsyncTask<Void, Void, Void> {		
		
		
		@Override
        protected Void doInBackground(Void... a) {
			
			ArrayList<Contacts> conToAdd = adapter.getConToAdd() ;           	
        	for(int i=0 ; i <conToAdd.size() ; i++)
        	{
        		Contacts tem = conToAdd.get(i);
            	ContentValues values = new ContentValues();
				values.put("phone",tem.getPhone());
				values.put("name",tem.getName());
				dbhelper.insertCon(values);    				
				System.out.println("insert" + tem.getPhone() + " " + tem.getName() + "successfully!");
				insertSecretSmsToDb(tem.getPhone());
        	}
        	
			return null;
			
        }
        @Override
        protected void onPostExecute(Void v){  
        	pd.dismiss();
        	finish(); 
        }
    }

    private void insertSecretSmsToDb(String phone)     
	{
		
	    final String SMS_URI_ALL   = "content://sms/";   
	    	    
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
	        		"locked"};
	        		//"error_code", 
	        		//"seen"  
	        
	        Uri uri = Uri.parse(SMS_URI_ALL); 
	        String selection = "address like ?" ;	      
	        String[] selectionArgs = new String[] {String.valueOf("%"+phone+"%")} ;
	        Cursor cur = cr.query(uri, projection, selection, selectionArgs, "date desc");  
	         
	        	        
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
	        	//String error_code ; 
	        	//String seen ;
	          
	              
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
	            //int errorCodeColumn = cur.getColumnIndex("error_code");
	            //int seenColumn = cur.getColumnIndex("seen");
	            
	          
	            do{              
	            	address = cur.getString(addressColumn);   
	            	if(address.length()>11)
	            		address = address.substring(address.length()-11, address.length());
	            	//if(phoneslist.contains(address)==false)
	            	//	continue ;
	            	person = cur.getString(personColumn); 	            		
		        	date  = cur.getString(dateColumn); 	
		        	protocol = cur.getString(protocolColumn); 	 
		        	read  = cur.getString(readColumn); 	
		        	status = cur.getString(statusColumn); 	 
		        	type = cur.getString(typeColumn); 	
		        	reply_path_present = cur.getString(replyPathPresentColumn); 	  
		        	body  = cur.getString(bodyColumn); 	
		        	locked = cur.getString(lockedColumn); 	
		        	//error_code = cur.getString(errorCodeColumn); 	
		        	//seen = cur.getString(seenColumn); 	 
		            
		        	System.out.println( "address:" + address 
		        			+ " person:" + person
		        			+ " date:" + date
		        			+ " protocol:" + protocol
		        			+ " read:" + read
		        			+ " status:" + status
		        			+ " type:" + type
		        			+ " reply_path_present:" + reply_path_present  
		        			+ " body:"  + body
		        			+ " locked:" + locked);
		            
		            ContentValues values = new ContentValues();
					values.put("address",address);
					values.put("person", person);
					values.put("date", date);
					values.put("protocol", protocol);
					values.put("read", read);
					values.put("status", status);
					values.put("type", type);					
					values.put("reply_path_present", reply_path_present);
					values.put("body", body);
					values.put("locked", locked);
					//values.put("error_code", error_code);
					//values.put("seen", seen);
					
					dbhelper.insertSms(values);
					
	            }while(cur.moveToNext());     
	        }               
	        
	    dbhelper.close();   
	    
	    } catch(SQLiteException ex) {     
	        Log.d("SQLiteException in getSecretSms", ex.getMessage());     
	    }     
	  
	}   
    
	public void showContacts()
	{		
		
		ArrayList<Contacts> data = getContactsInPhone();
		//System.out.println(data.size());
				
		adapter = new ContactsSelectViewAdapter(this,data);
		listview.setAdapter(adapter) ;
		
		
		listview.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                    long arg3) {
            	
            	System.out.println(adapter.getConToAdd().size());
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
	          	finish();  	
	          	*/
            }
        });
        
	}
	
	
}