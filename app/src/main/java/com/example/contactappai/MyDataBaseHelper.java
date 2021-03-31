package com.example.contactappai;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class MyDataBaseHelper extends SQLiteOpenHelper {

    private static final String DataBaseName = "Contact.db";
    private static final String TableName = "contact_details";
    private static final String Number = "_Number";
    private static final String Name = "Name";
    private static final String Address = "Address";
    private static final String Email = "Email";
    private static final String Facebook = "Facebook";
    private static final String Linkedin = "Linkedin";
    private static final String Letter = "letter";
    private static final String Photo_Uri = "Photo_Uri";
    private static final String CreateTable = "CREATE TABLE "+TableName+"( "+Number+" VARCHAR(20) PRIMARY KEY, "+Name+" VARCHAR(100), "+Address+" VARCHAR(250) ,"+Email+" VARCHAR(100) ,"+Facebook+" VARCHAR(100) ,"+Linkedin+" VARCHAR(100) ,"+Letter+" VARCHAR(1) ,"+Photo_Uri+" VARCHAR(100) ); ";
    private static final int VERSION_NUMBER = 1;
    private static final String DropTable = "DROP TABLE IF EXISTS "+TableName;
    private static final String SelcetOldData = "SELECT * FROM "+TableName;
    private Context context;

    public MyDataBaseHelper(@Nullable Context context) {
        super(context, DataBaseName, null, VERSION_NUMBER);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try{
            db.execSQL(CreateTable);
            //Toast.makeText(context,"Table Created",Toast.LENGTH_LONG).show();
        }catch (Exception e){
            //Toast.makeText(context,e.getMessage().toString(),Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try{
            db.execSQL(DropTable);
            //Toast.makeText(context,"Table Updated",Toast.LENGTH_LONG).show();
            onCreate(db);
        }catch (Exception e){
            //Toast.makeText(context,e.getMessage().toString(),Toast.LENGTH_LONG).show();
        }
    }

    public Long insertData(String number, String name, String address, String email, String facebook, String linkedin, String letter, String photo_Uri){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Number,number);
        contentValues.put(Name,name);
        contentValues.put(Address,address);
        contentValues.put(Email,email);
        contentValues.put(Facebook,facebook);
        contentValues.put(Linkedin,linkedin);
        contentValues.put(Letter,letter);
        contentValues.put(Photo_Uri,photo_Uri);
        Long id = sqLiteDatabase.insert(TableName,null,contentValues);
        return id;
    }

    public Cursor getOldData(){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(SelcetOldData,null);
        return cursor;
    }

    public Boolean updateData(String number, String name, String address, String email, String facebook, String linkedin, String letter, String photo_Uri){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Number,number);
        contentValues.put(Name,name);
        contentValues.put(Address,address);
        contentValues.put(Email,email);
        contentValues.put(Facebook,facebook);
        contentValues.put(Linkedin,linkedin);
        contentValues.put(Letter,letter);
        contentValues.put(Photo_Uri,photo_Uri);
        sqLiteDatabase.update(TableName,contentValues,this.Number+" = ?",new String[]{number});
        return true;
    }

}
