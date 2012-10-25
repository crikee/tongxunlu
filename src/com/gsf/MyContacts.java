package com.gsf;

import java.util.ArrayList;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.Photo;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class MyContacts extends Activity {
	
	private ListView listview ;
	private ContactsViewAdapter adapter ;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	
		setContentView(R.layout.mycontacts);
		
		listview = (ListView)findViewById(R.id.lv_mycontacts);	
		
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
    
	
	public void showContacts()
	{		
		
		ArrayList<Contacts> data = getContactsInPhone();
		
		adapter = new ContactsViewAdapter(this,data);
		listview.setAdapter(adapter) ;
		
		
		listview.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                    long arg3) {
           
            }
        });
        
	}
	
	
}