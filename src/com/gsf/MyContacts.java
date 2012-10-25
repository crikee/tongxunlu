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
	
	
	
	/**�õ��ֻ�ͨѶ¼��ϵ����Ϣ**/
    private ArrayList<Contacts> getContactsInPhone() {
    	
    	/**��ȡ��Phone���ֶ�**/
        final String[] PHONES_PROJECTION = new String[] {
    	    Phone.DISPLAY_NAME, Phone.NUMBER, Photo.PHOTO_ID,Phone.CONTACT_ID };       
        /**��ϵ����ʾ����**/
        final int PHONES_DISPLAY_NAME_INDEX = 0;        
        /**�绰����**/
        final int PHONES_NUMBER_INDEX = 1;        
        /**ͷ��ID**/
        final int PHONES_PHOTO_ID_INDEX = 2;       
        /**��ϵ�˵�ID**/
        final int PHONES_CONTACT_ID_INDEX = 3;
    	
        //List<Map<String, String>> datalist = new ArrayList<Map<String, String>>(); 
        ArrayList<Contacts> datalist = new ArrayList<Contacts>();
        
    	try{
    		ContentResolver cr = getContentResolver();

    		// ��ȡ�ֻ���ϵ��
    		Cursor cur = cr.query(Phone.CONTENT_URI,PHONES_PROJECTION, null, null, null);

    		if (cur.moveToFirst()) {       			
    			do
    			{
    				//�õ��ֻ�����
    				String phone = cur.getString(PHONES_NUMBER_INDEX);
    				//���ֻ�����Ϊ�յĻ���Ϊ���ֶ� ������ǰѭ��
    				if (TextUtils.isEmpty(phone))
    					continue;
    			
    				//�õ���ϵ������
    				String name = cur.getString(PHONES_DISPLAY_NAME_INDEX);
    			
	    			//�õ���ϵ��ID
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