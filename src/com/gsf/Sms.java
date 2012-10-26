package com.gsf;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.TextView;

public class Sms extends Activity {
	
	private ListView listview ;
	private String phone ;
	private SmsViewAdapter smsAdapter;
	private TextView title ;
	private ProgressDialog pd;
	private String fouth ;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

		setContentView(R.layout.sms);
		
		Intent intent = this.getIntent();
		phone = intent.getStringExtra("phone");
		
		title = (TextView)findViewById(R.id.sms_title);
		listview = (ListView)findViewById(R.id.lv_sms);		
		
		title.setText(phone);
		new getMySmsmsg().execute(null, null, null);
		pd = ProgressDialog.show(Sms.this,"请稍等","正在获取信息...");		
		
	}
	
	 public class getMySmsmsg extends AsyncTask<Void, Void, Void> {		
			
		 	ArrayList<SmsMsg> datalist = new ArrayList<SmsMsg>();
			@Override
	        protected Void doInBackground(Void... a) {
				
				datalist = getSmsMsgList() ;				
				return null;
				
	        }
	        @Override
	        protected void onPostExecute(Void v){  
	        	smsAdapter = new SmsViewAdapter(Sms.this,datalist);
				listview.setAdapter(smsAdapter);
	        	pd.dismiss();
	        }
	    }
	
	public ArrayList<SmsMsg> getSmsMsgList()     
	{
		
	    final String SMS_URI_ALL   = "content://sms/";    
	    String body = "" ;     
        String date = "" ;     
       	int typeid;
       	
	    ArrayList<SmsMsg> smsmsglist = new ArrayList<SmsMsg>();
	         
	    	         
	    try{     
	        ContentResolver cr = getContentResolver();     
	        String[] projection = new String[]{"body", "date", "type"};     
	        Uri uri = Uri.parse(SMS_URI_ALL);  
	        String selection = "address like ?" ;	      
	        String[] selectionArgs = new String[] {String.valueOf("%"+phone+"%")} ;
	        Cursor cur = cr.query(uri, projection, selection, selectionArgs, "date desc");     
	        //Cursor cur = cr.query(uri, projection, null, null, "date desc");   
	        
	        int smsbodyColumn = cur.getColumnIndex("body");     
            int dateColumn = cur.getColumnIndex("date");     
            int typeColumn = cur.getColumnIndex("type"); 
	       
	        if (cur.moveToFirst()) {       	

	            SmsMsg msg ;
	            
	            do{     
	            
	            	//person = cur.getString(nameColumn);       
	                body = cur.getString(smsbodyColumn);   
	                
	                SimpleDateFormat dateFormat = new SimpleDateFormat(     
	                        "yyyy-MM-dd hh:mm:ss");     
	                Date d = new Date(Long.parseLong(cur.getString(dateColumn)));     
	                date = dateFormat.format(d);     
	                     
	                typeid = cur.getInt(typeColumn);     
	                //System.out.println(typeid);
	                
	                if(typeid==1)
	    			{
	    				msg = new SmsMsg(body,date);
	    				msg.setBg(R.layout.chat_incoming_item);	
	    				smsmsglist.add(msg);
	    			}
	                if(typeid==2)
	    			{
	    				msg = new SmsMsg(body,date);
	    				msg.setBg(R.layout.chat_outgoing_item);	    
	    				smsmsglist.add(msg);
	    			}
	                
	             
	            }while(cur.moveToNext());  
	             
	        }                
	        
	    } catch(SQLiteException ex) {     
	        Log.d("SQLiteException in getSmsInPhone", ex.getMessage());     
	    }     
	    return smsmsglist ;
	}   
	
	
}