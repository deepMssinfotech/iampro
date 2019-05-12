package com.mssinfotech.iampro.co.common;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

public class DBHelper extends SQLiteOpenHelper {

   public static final String DATABASE_NAME = "MyDBName.db";
   public static final String CONTACTS_TABLE_NAME = "Login";
   public static final String USER_TABLE_NAME = "userList";
   public static final String BRANCH_TABLE_NAME = "BranchList";

   public DBHelper(Context context)
   {
      super(context, DATABASE_NAME , null, 1);
   }

   @Override
   public void onCreate(SQLiteDatabase db) {
      // TODO Auto-generated method stub
      db.execSQL(
              "CREATE TABLE IF NOT EXISTS " + CONTACTS_TABLE_NAME +
                      "(id integer PRIMARY KEY AUTOINCREMENT,tcid text,username text,password text)"
      );
      db.execSQL(
              "CREATE TABLE IF NOT EXISTS " + USER_TABLE_NAME +
                      "(id integer PRIMARY KEY AUTOINCREMENT,sid integer,tcid integer,signature text)"
      );
      db.execSQL(
              "CREATE TABLE IF NOT EXISTS " + BRANCH_TABLE_NAME  +
                      "(id integer PRIMARY KEY AUTOINCREMENT,tcid integer,name text,EnrollmentNO text,course text,batch text,courseid integer,batchid integer, thumb_url text )"
      );
      Log.d("create", "table ceated");
   }

   @Override
   public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
      // TODO Auto-generated method stub
      db.execSQL("DROP TABLE IF EXISTS "+CONTACTS_TABLE_NAME);
      onCreate(db);
      Log.d("create", "table droped");
   }
   public boolean insertContact  (Integer id,String tcid,String username, String password) {
      SQLiteDatabase db = this.getWritableDatabase();
      db.execSQL("DROP TABLE IF EXISTS "+CONTACTS_TABLE_NAME);
      onCreate(db);
      ContentValues contentValues = new ContentValues();
      contentValues.put("id", id);
      contentValues.put("tcid", tcid);
      contentValues.put("username", username);
      contentValues.put("password", password);
      db.insert(CONTACTS_TABLE_NAME, null, contentValues);
      Log.d("create", "Record Inserted");
      return true;
   }
   public boolean insertUser (Integer sid,Integer tcid, String signature) {
      SQLiteDatabase db = this.getWritableDatabase();
      ContentValues contentValues = new ContentValues();
      contentValues.put("tcid", tcid);
      contentValues.put("sid", sid);
      contentValues.put("signature", signature);
      db.insert(USER_TABLE_NAME, null, contentValues);
      Log.d("create", "Record Inserted");
      return true;
   }
   public boolean inserBranch (Integer id,Integer tcid,String name,String EnrollmentNO,String course,String batch,Integer courseid,Integer batchid ,String thumb_url) {
      SQLiteDatabase db = this.getWritableDatabase();
      ContentValues contentValues = new ContentValues();
      contentValues.put("id", id);
      contentValues.put("tcid", tcid);
      contentValues.put("name", name);
      contentValues.put("EnrollmentNO", EnrollmentNO);
      contentValues.put("course", course);
      contentValues.put("batch", batch);
      contentValues.put("courseid", courseid);
      contentValues.put("batchid", batchid);
      contentValues.put("thumb_url", thumb_url);
      db.insert(BRANCH_TABLE_NAME, null, contentValues);
      Log.d("create", "Record Inserted");
      return true;
   }

   public Cursor getData(int id){
      SQLiteDatabase db = this.getReadableDatabase();
      Cursor res =  db.rawQuery( "select * from "+CONTACTS_TABLE_NAME+" where id="+id+"", null );
      return res;
   }
   public boolean dropBranch(){
      SQLiteDatabase db = this.getWritableDatabase();
      db.execSQL("DROP TABLE IF EXISTS "+CONTACTS_TABLE_NAME);
      onCreate(db);
      Log.d("droped", "table droped");
      return true;
   }
   public Cursor getUserData(int id){
      SQLiteDatabase db = this.getReadableDatabase();
      Cursor res =  db.rawQuery( "select * from "+USER_TABLE_NAME+" where sid="+id+"", null );
      return res;
   }
   public ArrayList<HashMap<String, String>> getNonRegisterBranchUserBytcid(int tcid) {
      ArrayList<HashMap<String, String>> EmployeeList = new ArrayList<HashMap<String, String>>();
      //hp = new HashMap();
      SQLiteDatabase db = this.getReadableDatabase();
      Cursor res =  db.rawQuery( "select * from "+BRANCH_TABLE_NAME+" where tcid="+tcid+" and id not in (select sid from "+USER_TABLE_NAME+")", null );
      res.moveToFirst();

      while(res.isAfterLast() == false){
         HashMap<String, String> map = new HashMap<String, String>();
         map.put("id", res.getString(res.getColumnIndex("id")));
         map.put("tcid", res.getString(res.getColumnIndex("tcid")));
         map.put("name", res.getString(res.getColumnIndex("name")));
         map.put("EnrollmentNO", res.getString(res.getColumnIndex("EnrollmentNO")));
         map.put("course", res.getString(res.getColumnIndex("course")));
         map.put("batch", res.getString(res.getColumnIndex("batch")));
         map.put("courseid", res.getString(res.getColumnIndex("courseid")));
         map.put("batchid", res.getString(res.getColumnIndex("batchid")));
         map.put("thumb_url", res.getString(res.getColumnIndex("thumb_url")));
         EmployeeList.add(map);
         res.moveToNext();
      }
      return EmployeeList;
   }
   public ArrayList<HashMap<String, String>> getAllBranchBytcid(int tcid) {
      ArrayList<HashMap<String, String>> EmployeeList = new ArrayList<HashMap<String, String>>();
      //hp = new HashMap();
      SQLiteDatabase db = this.getReadableDatabase();
      Cursor res =  db.rawQuery( "select * from "+BRANCH_TABLE_NAME+" where tcid="+tcid, null );
      res.moveToFirst();

      while(res.isAfterLast() == false){
         HashMap<String, String> map = new HashMap<String, String>();
         map.put("id", res.getString(res.getColumnIndex("id")));
         map.put("tcid", res.getString(res.getColumnIndex("tcid")));
         map.put("name", res.getString(res.getColumnIndex("name")));
         map.put("EnrollmentNO", res.getString(res.getColumnIndex("EnrollmentNO")));
         map.put("course", res.getString(res.getColumnIndex("course")));
         map.put("batch", res.getString(res.getColumnIndex("batch")));
         map.put("courseid", res.getString(res.getColumnIndex("courseid")));
         map.put("batchid", res.getString(res.getColumnIndex("batchid")));
         map.put("thumb_url", res.getString(res.getColumnIndex("thumb_url")));
         EmployeeList.add(map);
         res.moveToNext();
      }
      return EmployeeList;
   }
   public Cursor geBrachtData(int tcid){
      SQLiteDatabase db = this.getReadableDatabase();
      Cursor res =  db.rawQuery( "select * from "+BRANCH_TABLE_NAME+" where tcid="+tcid+"", null );
      return res;
   }
   public int numberOfRows(){
      SQLiteDatabase db = this.getReadableDatabase();
      int numRows = (int) DatabaseUtils.queryNumEntries(db, CONTACTS_TABLE_NAME);
      return numRows;
   }
   public int usernumberOfRows(){
      SQLiteDatabase db = this.getReadableDatabase();
      int numRows = (int) DatabaseUtils.queryNumEntries(db, USER_TABLE_NAME);
      return numRows;
   }
   public int brachnumberOfRows(){
      SQLiteDatabase db = this.getReadableDatabase();
      int numRows = (int) DatabaseUtils.queryNumEntries(db, BRANCH_TABLE_NAME);
      return numRows;
   }
   public int BranchCheckRows(int tcid){
      String countQuery = "SELECT  * FROM " + BRANCH_TABLE_NAME + " where tcid="+tcid;
      SQLiteDatabase db = this.getReadableDatabase();
      Cursor cursor = db.rawQuery(countQuery, null);
      int cnt = cursor.getCount();
      cursor.close();
      return cnt;
   }
   public int userCheckRows(int sid){
      String countQuery = "SELECT  * FROM " + USER_TABLE_NAME + " where sid="+sid;
      SQLiteDatabase db = this.getReadableDatabase();
      Cursor cursor = db.rawQuery(countQuery, null);
      int cnt = cursor.getCount();
      cursor.close();
      return cnt;
   }
   public boolean updateContact (Integer id,String tcid, String username, String password) {
      SQLiteDatabase db = this.getWritableDatabase();
      ContentValues contentValues = new ContentValues();
      contentValues.put("tcid", tcid);
      contentValues.put("username", username);
      contentValues.put("password", password);
      db.update(CONTACTS_TABLE_NAME, contentValues, "id = ? ", new String[] { Integer.toString(id) } );
      return true;
   }

   public Integer deleteContact (Integer id) {
      SQLiteDatabase db = this.getWritableDatabase();
      return db.delete(CONTACTS_TABLE_NAME,
      "id = ? ", 
      new String[] { Integer.toString(id) });
   }
   public Integer deleteUser (Integer sid) {
      SQLiteDatabase db = this.getWritableDatabase();
      return db.delete(USER_TABLE_NAME,
              "sid = ? ",
              new String[] { Integer.toString(sid) });
   }
   public Integer deleteBranch (Integer tcid) {
      SQLiteDatabase db = this.getWritableDatabase();
      return db.delete(BRANCH_TABLE_NAME,
              "tcid = ? ",
              new String[] { Integer.toString(tcid) });
   }

   public ArrayList<String> getAllCotacts() {
      ArrayList<String> array_list = new ArrayList<String>();
      
      //hp = new HashMap();
      SQLiteDatabase db = this.getReadableDatabase();
      Cursor res =  db.rawQuery( "select * from "+CONTACTS_TABLE_NAME, null );
      res.moveToFirst();
      
      while(res.isAfterLast() == false){
         array_list.add(res.getString(res.getColumnIndex("id")));
         res.moveToNext();
      }
   return array_list;
   }
   public ArrayList<String> getAllUser() {
      ArrayList<String> array_list = new ArrayList<String>();

      //hp = new HashMap();
      SQLiteDatabase db = this.getReadableDatabase();
      Cursor res =  db.rawQuery( "select * from "+USER_TABLE_NAME, null );
      res.moveToFirst();

      while(res.isAfterLast() == false){
         array_list.add(res.getString(res.getColumnIndex("id")));
         res.moveToNext();
      }
      return array_list;
   }
   public ArrayList<String> getAllBranch() {
      ArrayList<String> array_list = new ArrayList<String>();

      //hp = new HashMap();
      SQLiteDatabase db = this.getReadableDatabase();
      Cursor res =  db.rawQuery( "select * from "+BRANCH_TABLE_NAME, null );
      res.moveToFirst();

      while(res.isAfterLast() == false){
         array_list.add(res.getString(res.getColumnIndex("id")));
         res.moveToNext();
      }
      return array_list;
   }
}