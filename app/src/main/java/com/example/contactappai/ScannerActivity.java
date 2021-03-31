package com.example.contactappai;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.zxing.Result;

public class ScannerActivity extends AppCompatActivity {
    private CodeScannerView codeScannerView;
    private CodeScanner codeScanner;
    private int CameraPermissionCode = 1;
    private MyDataBaseHelper myDataBaseHelper;
    private String name1="",number1="",letter1="",address1="",email1="",facebook1="",linkedin1="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);

        myDataBaseHelper = new MyDataBaseHelper(this);
        SQLiteDatabase sqLiteDatabase = myDataBaseHelper.getWritableDatabase();

        if(ContextCompat.checkSelfPermission(ScannerActivity.this, Manifest.permission.CAMERA)== PackageManager.PERMISSION_GRANTED ){

            initialization();

        }else{
            ActivityCompat.requestPermissions(ScannerActivity.this,new String[] {Manifest.permission.CAMERA},CameraPermissionCode);
        }

    }

    private void initialization() {
        codeScannerView = findViewById(R.id.scannerId);
        codeScanner = new CodeScanner(this,codeScannerView);
        codeScanner.isAutoFocusEnabled();

        codeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull Result result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String value = result.getText();

                        for(int i=0;i<value.length();i++){
                            if(value.charAt(i)=='M' && value.charAt(i+1)=='E' && value.charAt(i+2)=='C' && value.charAt(i+3)=='A' && value.charAt(i+4)=='R' &&
                                    value.charAt(i+5)=='D' && value.charAt(i+6)==':' && value.charAt(i+7)=='N' && value.charAt(i+8)==':'){
                                i+=9;
                                while(value.charAt(i)!=';'){
                                    name1 = name1+value.charAt(i);
                                    i++;
                                }
                            }
                            if(value.charAt(i)=='T' && value.charAt(i+1)=='E' && value.charAt(i+2)=='L' && value.charAt(i+3)==':'){
                                i+=4;
                                while(value.charAt(i)!=';'){
                                    number1 = number1+value.charAt(i);
                                    i++;
                                }
                            }
                            if(value.charAt(i)=='A' && value.charAt(i+1)=='D' && value.charAt(i+2)=='R' && value.charAt(i+3)==':'){
                                i+=4;
                                while(value.charAt(i)!=';'){
                                    address1 = address1+value.charAt(i);
                                    i++;
                                }
                            }
                            if(value.charAt(i)=='E' && value.charAt(i+1)=='M' && value.charAt(i+2)=='A' && value.charAt(i+3)=='I' && value.charAt(i+4)=='L' && value.charAt(i+5)==':'){
                                i+=6;
                                while(value.charAt(i)!=';'){
                                    email1 = email1+value.charAt(i);
                                    i++;
                                }
                            }
                            if(value.charAt(i)=='F' && value.charAt(i+1)=='A' && value.charAt(i+2)=='C' && value.charAt(i+3)=='E' && value.charAt(i+4)=='B' &&
                                    value.charAt(i+5)=='O' && value.charAt(i+6)=='O' && value.charAt(i+7)=='K' && value.charAt(i+8)==':'){
                                i+=9;
                                while(value.charAt(i)!=';'){
                                    facebook1 = facebook1+value.charAt(i);
                                    i++;
                                }
                            }
                            if(value.charAt(i)=='L' && value.charAt(i+1)=='I' && value.charAt(i+2)=='N' && value.charAt(i+3)=='K' && value.charAt(i+4)=='E' &&
                                    value.charAt(i+5)=='D' && value.charAt(i+6)=='I' && value.charAt(i+7)=='N' && value.charAt(i+8)==':'){
                                i+=9;
                                while(value.charAt(i)!=';'){
                                    linkedin1 = linkedin1+value.charAt(i);
                                    i++;
                                }
                            }
                            if(value.charAt(i)=='L' && value.charAt(i+1)=='E' && value.charAt(i+2)=='T' && value.charAt(i+3)=='T' && value.charAt(i+4)=='E'
                                    && value.charAt(i+5)=='R' && value.charAt(i+6)==':'){
                                i+=7;
                                while(value.charAt(i)!=';'){
                                    letter1 = letter1+value.charAt(i);
                                    i++;
                                }
                            }
                        }

                        myDataBaseHelper.insertData(number1,name1,address1,
                                                    email1,facebook1,linkedin1,letter1,null);

                        //name.replace(";","");
                        Intent intent = new Intent(Intent.ACTION_INSERT);
                        intent.setType(ContactsContract.RawContacts.CONTENT_TYPE);
                        intent.putExtra(ContactsContract.Intents.Insert.NAME,name1);
                        intent.putExtra(ContactsContract.Intents.Insert.PHONE,number1);
                        startActivity(intent);
                    }
                });
            }
        });

        codeScannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                codeScanner.startPreview();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == CameraPermissionCode){
            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                Toast.makeText(getApplicationContext(),"Permission Granted",Toast.LENGTH_LONG).show();
                initialization();
            }else{
                Toast.makeText(getApplicationContext(),"Permission Denied",Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(ContextCompat.checkSelfPermission(ScannerActivity.this, Manifest.permission.CAMERA)== PackageManager.PERMISSION_GRANTED ){

            codeScanner.startPreview();

        }else{

        }
    }

}