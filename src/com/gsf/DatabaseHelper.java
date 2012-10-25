package com.gsf;



import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

public class DatabaseHelper extends SQLiteOpenHelper{
	Cursor cursor;
	private SQLiteDatabase db;
	private static final String DATABASE_NAME = "safe_v_1";
	private static final int DATABASE_VERSION = 1;
	
	private static final String TBL_NAME_Sms = "SmsTbl" ;
	//private static final String COLUMNS_Sms = "phone text,name text,body text,date text,type integer" ;
	private static final String COLUMNS_Sms = 
			"address text, " +
			"person integer," +
			"date text," +
			"protocol integer," +
			"read integer," +
			"status integer," +
			"type integer," +
			"reply_path_present integer," + 
			"body text," +
			"locked integer";

			
	
	private static final String CREATE_TBL_Sms = " create table "
			+ " SmsTbl(_id integer primary key autoincrement, "   + COLUMNS_Sms + ")";
	
	private static final String TBL_NAME_Con = "ConTbl" ;
	private static final String COLUMNS_Con = "phone text,name text " ;
	private static final String CREATE_TBL_Con = " create table "
			+ " ConTbl(_id integer primary key autoincrement, "   + COLUMNS_Con + ")";
	
	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	public DatabaseHelper() {
		super(null, DATABASE_NAME, null, DATABASE_VERSION);
	}

	public void onCreate(SQLiteDatabase db) {
		this.db = db;
		db.execSQL(CREATE_TBL_Sms);
		db.execSQL(CREATE_TBL_Con);
	}
	
	
	public void insertSms(ContentValues values) {
		SQLiteDatabase db = getWritableDatabase();
		db.insert(TBL_NAME_Sms, null, values);
		db.close();
	}
	
	public void insertCon(ContentValues values) {
		SQLiteDatabase db = getWritableDatabase();
		db.insert(TBL_NAME_Con, null, values);
		db.close();
	}
	
	public Cursor querySms() {
		String[] projection = new String[]{"address","body", "date", "type"}; 
		SQLiteDatabase db = getWritableDatabase();
		Cursor c = db.query(TBL_NAME_Sms, projection, null, null, null, null, "date desc");
		return c;
	}
	
	public Cursor querySms(String phone) {
		String[] projection = new String[]{"address","body", "date", "type"};     
		String selection = "address=?" ;
		String[] selectionArgs = new String[] {String.valueOf(phone)} ;
		SQLiteDatabase db = getWritableDatabase();
		Cursor c = db.query(TBL_NAME_Sms, projection, selection , selectionArgs , null, null, "date desc");
		return c;
	}
	
	public Cursor queryCon() {
		SQLiteDatabase db = getWritableDatabase();
		Cursor c = db.query(TBL_NAME_Con, null, null , null , null, null, null);
		return c;
	}
	

	/*
	public void delete(int spitballnumber,String category) {
		String where = "username=? and spitballnumber=? and category=?" ;
		String[] whereArgs = new String[] { String.valueOf(ClientManager.getUserName()) , 
											String.valueOf(spitballnumber), 
											String.valueOf(category)} ;
		if (db == null)
			db = getWritableDatabase();
		db.delete(TBL_NAME,where,whereArgs);
	}
	
	public int update(int spitballnumber,String ownernickname,String title,int participants ,String category)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues cv=new ContentValues(); 
        cv.put("ownernickname", ownernickname);
        cv.put("title", title);
        cv.put("participants", participants);
        String where = "username=? and spitballnumber=? and category=?" ;
		String[] whereArgs = new String[] { String.valueOf(ClientManager.getUserName()) , 
											String.valueOf(spitballnumber),
											String.valueOf(category)} ;
        return db.update(TBL_NAME, cv, where , whereArgs );
    }
	*/
		
	public void close() {
		if (db != null)
			db.close();
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}

	
}