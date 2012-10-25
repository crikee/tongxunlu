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

public class SecretSms extends Activity {
	
	private ListView listview ;
	private String address ;
	private SmsViewAdapter smsAdapter;
	private TextView title ;
	private ProgressDialog pd;
	private DatabaseHelper dbhelper = new DatabaseHelper(this) ;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

	
		setContentView(R.layout.sms);
		
		Intent intent = this.getIntent();
		address = intent.getStringExtra("address");
		
		title = (TextView)findViewById(R.id.sms_title);
		listview = (ListView)findViewById(R.id.lv_sms);		
		
		title.setText(address);
		new getMySecretSmsmsg().execute(null, null, null);
		pd = ProgressDialog.show(SecretSms.this,"请稍等","正在获取信息...");		
		
	}
	
	 public class getMySecretSmsmsg extends AsyncTask<Void, Void, Void> {		
			
		 	ArrayList<SmsMsg> datalist = new ArrayList<SmsMsg>();
			@Override
	        protected Void doInBackground(Void... a) {
				
				datalist = getSecretSmsMsgList() ;				
				return null;
				
	        }
	        @Override
	        protected void onPostExecute(Void v){  
	        	smsAdapter = new SmsViewAdapter(SecretSms.this,datalist);
				listview.setAdapter(smsAdapter);
	        	pd.dismiss();
	        }
	    }
	
	public ArrayList<SmsMsg> getSecretSmsMsgList()     
	{
		 
	    String body = "" ;     
        String date = "" ;     
       	int typeid;
       	
	    ArrayList<SmsMsg> smsmsglist = new ArrayList<SmsMsg>();
	    
	    try{     	      
	        Cursor cur = dbhelper.querySms(address);	       
	        int addressColumn = cur.getColumnIndex("address");     
            int bodyColumn = cur.getColumnIndex("body");
            int typeColumn = cur.getColumnIndex("type");
            int dateColumn = cur.getColumnIndex("date");
            
	        if (cur.moveToFirst()) {       	

	            SmsMsg msg ;
	            
	            do{     
	            
	            	//person = cur.getString(nameColumn);       
	                body = cur.getString(bodyColumn);   
	                
	                SimpleDateFormat dateFormat = new SimpleDateFormat(     
	                        "yyyy-MM-dd hh:mm:ss");     
	                Date d = new Date(Long.parseLong(cur.getString(dateColumn)));     
	                date = dateFormat.format(d);     
	                     
	                typeid = cur.getInt(typeColumn);     
	                System.out.println(typeid);
	                
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