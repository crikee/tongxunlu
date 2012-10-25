package com.gsf;

import java.util.ArrayList;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

public class SmsReceiver extends BroadcastReceiver {
	
	private DatabaseHelper dbhelper ;  
	
	@Override
	public void onReceive(Context context, Intent intent) {	
		System.out.println("receiver");
		//如果是系统短信Action...
		//if (!intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {  
			Toast.makeText(context, intent.getAction(), Toast.LENGTH_LONG).show();
			
		//}
		if(intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {
			
			dbhelper  = new DatabaseHelper(context) ;			
			
			ArrayList<Contacts> secretContacts = getSecretContacts() ;

			StringBuffer sb = new StringBuffer();
			Bundle bundle = intent.getExtras();
			if(bundle != null) {
				//通过pdus可以获得接收到的所有短信消息
				Object[] pdus = (Object[])bundle.get("pdus");
				//构建短信对象数组
				SmsMessage[] msgs = new SmsMessage[pdus.length];
				for(int i = 0; i < pdus.length; i ++) {
					msgs[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
				}
				for(int i = 0; i<msgs.length; i++) {
					
					SmsMessage msg = msgs[i] ;
					String address = msg.getDisplayOriginatingAddress() ;
					
					System.out.println("receiver:" + address);
					for(int j=0 ; j<secretContacts.size(); j++)
					{
						Contacts tem = secretContacts.get(j);
						if(tem.getPhone().equals(address))
						{
							
							System.out.println("receiver:" + tem.getPhone());
							sb.append("短信来自: " + tem.getName()); 
			                String date = Long.toString(msg.getTimestampMillis());
			                String protocol = Integer.toString(msg.getProtocolIdentifier()) ;  
			                String read = "1" ;
			                String status = Integer.toString(msg.getStatus()); 
			                String type = "1" ;
			                String reply_path_present = "0" ;  
			                String body = msg.getDisplayMessageBody() ;
			                String locked = "0" ;
			                
			                ContentValues values = new ContentValues();
							values.put("address",address);
							values.put("date", date);
							values.put("protocol", protocol);
							values.put("read", read);
							values.put("status", status);
							values.put("type", type);					
							values.put("reply_path_present", reply_path_present);
							values.put("body", body);
							values.put("locked", locked);
							dbhelper.insertSms(values);
			                
			                this.abortBroadcast();
							//break ;
			                Toast.makeText(context, sb.toString(), Toast.LENGTH_LONG).show();
						}
					}
									
				}
			
			}
		}
		
		
	}
	
	/**得到私密联系人信息**/
    private ArrayList<Contacts> getSecretContacts() {
    	
    	final int PHONE_INDEX = 1 ;
    	final int NAME_INDEX = 2 ;
    	String phone = "" ;
    	String name  = "" ;
    	ArrayList<Contacts> datalist = new ArrayList<Contacts>();
    	
    	try{ 
    		
    		Cursor cur = dbhelper.queryCon();
    		if (cur.moveToFirst()) {       			
    			do
    			{
    				phone = cur.getString(PHONE_INDEX);
    				name = cur.getString(NAME_INDEX);
    			
    				Contacts tem = new Contacts(phone,name);	                
	                datalist.add(tem);

    			}
    			while(cur.moveToNext()); 
    		}
    	
    	dbhelper.close();
    	
    	} catch(SQLiteException ex) {     
	        Log.d("SQLiteException in getSecretContacts", ex.getMessage());     
	    }  
    	
    	return datalist ;
    }
	
}