package com.gsf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import com.gsf.Sms.getMySmsmsg;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class Mailbox extends Activity {
	
	private ListView listview ;
	private Button   enterSecretmailbox ;
	private ProgressDialog pd;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.mailbox);
		
		listview = (ListView)findViewById(R.id.lv_mailbox);	 
		enterSecretmailbox = (Button)findViewById(R.id.btn_mailbox_secretmsg);
		
		enterSecretmailbox.setOnClickListener(new Button.OnClickListener(){
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(Mailbox.this, SecretMailbox.class);
				startActivity(intent);
			}
		});		
	    
		new getSms().execute(null, null, null);
		pd = ProgressDialog.show(Mailbox.this,"请稍等","正在获取信息...");	
		//showSms();
	}
	
	
	public class getSms extends AsyncTask<Void, Void, Void> {		
		
		List<Map<String, String>> datalist = null ;
		@Override
        protected Void doInBackground(Void... a) {
			
			datalist = getSmsInPhone() ;				
			return null;
			
        }
        @Override
        protected void onPostExecute(Void v){  
        	showSms(datalist);
        	pd.dismiss();
        }
    }

	public List<Map<String, String>> getSmsInPhone()     
	{
		
	    final String SMS_URI_ALL   = "content://sms/";       
	    //final String SMS_URI_INBOX = "content://sms/inbox";     
	    //final String SMS_URI_SEND  = "content://sms/sent";     
	    //final String SMS_URI_DRAFT = "content://sms/draft"; 
	    String person = "" ;      
        String phone  = "" ;            
        String body   = "" ; 
        String display_name = "" ;
	         
	    List<Map<String, String>> datalist = new ArrayList<Map<String, String>>(); 
	         
	    try{     
	    	
	        ContentResolver cr = getContentResolver();     
	        //String[] projection = new String[]{"_id", "address", "person",      
	        //        "body", "date", "type"};   
	        String[] projection = new String[]{"address", "person",      
	                "body"};   
	        
	        Uri uri = Uri.parse(SMS_URI_ALL);  
	        Cursor cur = cr.query(uri, projection, null, null, "date desc");  
	        
	        SortedSet<String> ss = new TreeSet<String>();
	        int nameColumn = cur.getColumnIndex("person");     
            int phoneNumberColumn = cur.getColumnIndex("address");     
            int smsbodyColumn = cur.getColumnIndex("body");     
            //int dateColumn = cur.getColumnIndex("date");     
            //int typeColumn = cur.getColumnIndex("type");
	        
	        if (cur.moveToFirst()) { 
	        	
	            do{              
	            	phone = cur.getString(phoneNumberColumn);   
	            	if(phone.length()>11)
	            		phone = phone.substring(phone.length()-11, phone.length());
	            	if(ss.contains(phone))
	            		continue ;
	            	else
	            	{
	            		ss.add(phone);
	            		
	            		person = cur.getString(nameColumn); 
	            		String selection = Phone.NUMBER + "=?" ;      
	        	        String[] selectionArgs = new String[] {String.valueOf(phone)} ;
	        	        Cursor tcr = cr.query(Phone.CONTENT_URI,new String[] {
	        	        	    Phone.DISPLAY_NAME,Phone.CONTACT_ID}, selection, selectionArgs, null);
	        	        display_name = "" ;
	        	        if (tcr.moveToFirst()) {
	        	        	do{
	        	        		display_name = tcr.getString(0);
	        	        		System.out.println(display_name);
	        	        	}while(tcr.moveToNext());
	        	        }
		                body = cur.getString(smsbodyColumn);   
		                
		                if(display_name.equals("")||display_name=="null")
		                	display_name = phone ;
		                Map<String, String> map = new HashMap<String, String>();		                
		                map.put("display_name", display_name);
		                map.put("phone", phone);		                
		                map.put("body",body);
		                datalist.add(map);
	            	}
	            	
	            }while(cur.moveToNext());     
	        }                
	        
	    } catch(SQLiteException ex) {     
	        Log.d("SQLiteException in getSmsInPhone", ex.getMessage());     
	    }     
	    return datalist ;
	}   
	
	public void showSms(List<Map<String, String>> data)
	{		
		
		//List<Map<String, String>> data = getSmsInPhone();
		//System.out.println(data.size());
		
		String[] from = {"display_name","body"} ;
		int[] to = {R.id.tv_smsslistitem_person,R.id.tv_smsslistitem_body} ;		
		
		SimpleAdapter adapter = new SimpleAdapter(Mailbox.this,data,
				R.layout.smslist_item,from,to) ;
		listview.setAdapter(adapter) ;
		
		
		listview.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                    long arg3) {
            	
            	@SuppressWarnings("unchecked")
				Map<String, String> map = (Map<String, String>)listview.getItemAtPosition(arg2);
            	String  phone = map.get("phone");
            	
            	Intent intent = new Intent(Mailbox.this,Sms.class);
	            intent.putExtra("phone",phone);
	          	startActivity(intent);            	
            }
        });
        
	}
	
	
}