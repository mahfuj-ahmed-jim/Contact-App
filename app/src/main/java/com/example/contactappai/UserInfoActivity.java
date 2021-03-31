package com.example.contactappai;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.zxing.Result;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;
import de.hdodenhof.circleimageview.CircleImageView;

public class UserInfoActivity extends AppCompatActivity {

    private String name1="",number1="",letter1="",address1="",email1="",facebook1="",linkedin1="",photo1=null;
    private Button backbutton,favButton,callButton,addressButton,emailButton,facebookButton,linkedinButton,shareButton;
    private TextView letter,name,number,address,email,facebook,linkedin;
    private int CallPermission = 1;
    private Intent intent;
    private CircleImageView circleImageView;
    private MyDataBaseHelper myDataBaseHelper;
    private String add;
    private ImageView
            qrCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        myDataBaseHelper = new MyDataBaseHelper(this);
        SQLiteDatabase sqLiteDatabase = myDataBaseHelper.getWritableDatabase();

        qrCode = findViewById(R.id.qrCodeImageId);
        letter = findViewById(R.id.userLetterId);
        name = findViewById(R.id.userNameId);
        number = findViewById(R.id.userNumberId);
        address = findViewById(R.id.addressNameId);
        email = findViewById(R.id.emailTextId);
        facebook = findViewById(R.id.facebookTextId);
        linkedin = findViewById(R.id.linkedinTextId);
        circleImageView = findViewById(R.id.userCircleImageView);

        addressButton = findViewById(R.id.addressEditButtonId);
        emailButton = findViewById(R.id.emailEditButtonId);
        facebookButton = findViewById(R.id.facebookEditButtonId);
        linkedinButton = findViewById(R.id.linkedinEditButtonId);

        String value = getIntent().getExtras().getString("value");
        String photo1 = getIntent().getExtras().getString("photo");

        String qrCodeString = value;

        QRGEncoder qrgEncoder = new QRGEncoder(qrCodeString,null, QRGContents.Type.TEXT,1000);
        Bitmap qrBitMap = qrgEncoder.getBitmap();
        qrCode.setImageBitmap(qrBitMap);


        ////MECARD:N:Owen,Sean;ADR:76 9th Avenue, 4th Floor, New York, NY 10011;TEL:12125551212;EMAIL:srowen@example.com;
        //                String pass = "MECARD:N:"+filterUserDataList.get(position).getName()+";ADR:"+filterUserDataList.get(position).getAddress()+
        //                        ";TEL:"+filterUserDataList.get(position).getNumber()+";EMAIL:"+filterUserDataList.get(position).getEmail()+
        //                        ";FACEBOOK:"+filterUserDataList.get(position).getFacebook()+";LINKEDIN:"+filterUserDataList.get(position).getLinkedin()+
        //                        ";PHTOTO:"+filterUserDataList.get(position).getPhoto()+";LETTER:"+filterUserDataList.get(position).getLetter()+";";

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
            if(value.charAt(i)=='P' && value.charAt(i+1)=='H' && value.charAt(i+2)=='O' && value.charAt(i+3)=='T' && value.charAt(i+4)=='O' && value.charAt(i+5)==':'){
                i+=6;
                while(value.charAt(i)!=';'){
                    this.photo1 = this.photo1+value.charAt(i);
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

        addressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(UserInfoActivity.this);
                builder.setTitle("Enter Address:");
                EditText editText = new EditText(UserInfoActivity.this);
                builder.setView(editText);
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        address1 = editText.getText().toString().trim();
                        address.setText(address.getText()+address1);
                        Boolean b =  myDataBaseHelper.updateData(number1,name1,address1,email1,facebook1,linkedin1,letter1,photo1);
                        if(b==true){
                            Toast.makeText(getApplicationContext(),"Updated",Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(getApplicationContext(),"Not Updated",Toast.LENGTH_LONG).show();
                        }
                    }
                });
                builder.show();
            }
        });

        emailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(UserInfoActivity.this);
                builder.setTitle("Enter Email: ");
                EditText editText = new EditText(UserInfoActivity.this);
                builder.setView(editText);
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        email1 = editText.getText().toString().trim();
                        email.setText(email.getText()+email1);
                        Boolean b =  myDataBaseHelper.updateData(number1,name1,address1,email1,facebook1,linkedin1,letter1,photo1);
                        if(b==true){
                            Toast.makeText(getApplicationContext(),"Updated",Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(getApplicationContext(),"Not Updated",Toast.LENGTH_LONG).show();
                        }
                    }
                });
                builder.show();
            }
        });

        facebookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(UserInfoActivity.this);
                builder.setTitle("Enter Facebook: ");
                EditText editText = new EditText(UserInfoActivity.this);
                builder.setView(editText);
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        facebook1 = editText.getText().toString().trim();
                        facebook.setText(facebook.getText()+facebook1);
                        Boolean b =  myDataBaseHelper.updateData(number1,name1,address1,email1,facebook1,linkedin1,letter1,photo1);
                        if(b==true){
                            Toast.makeText(getApplicationContext(),"Updated",Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(getApplicationContext(),"Not Updated",Toast.LENGTH_LONG).show();
                        }
                    }
                });
                builder.show();
            }
        });

        linkedinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(UserInfoActivity.this);
                builder.setTitle("Enter Linkedin:");
                EditText editText = new EditText(UserInfoActivity.this);
                builder.setView(editText);
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        linkedin1 = linkedin.getText().toString().trim();
                        linkedin.setText(linkedin.getText()+linkedin1);
                        Boolean b =  myDataBaseHelper.updateData(number1,name1,address1,email1,facebook1,linkedin1,letter1,photo1);
                        if(b==true){
                            Toast.makeText(getApplicationContext(),"Updated",Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(getApplicationContext(),"Not Updated",Toast.LENGTH_LONG).show();
                        }
                    }
                });
                builder.show();
            }
        });

        name.setText(name1);
        number.setText(number1);
        address.setText(address.getText()+address1);
        email.setText(email.getText()+email1);
        facebook.setText(facebook.getText()+facebook1);
        linkedin.setText(linkedin.getText()+linkedin1);
        letter.setText(letter1);

        if(photo1!=null){
            Picasso.get().load(photo1).into(circleImageView);
        }

        backbutton = findViewById(R.id.userBackButtonId);
        callButton = findViewById(R.id.callButtonId);
        favButton = findViewById(R.id.favButtonId);

        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:"+number1));
                if(ContextCompat.checkSelfPermission(UserInfoActivity.this, Manifest.permission.CALL_PHONE)== PackageManager.PERMISSION_GRANTED ){

                    startActivity(intent);

                }else{
                    ActivityCompat.requestPermissions(UserInfoActivity.this,new String[] {Manifest.permission.CALL_PHONE},CallPermission);
                }
            }
        });

        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        favButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Added to favourite",Toast.LENGTH_LONG).show();
            }
        });

        /*shareButton = findViewById(R.id.shareId);
        shareButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                try{
                    View content = findViewById(R.id.qrCodeImageId);
                    content.setDrawingCacheEnabled(true);
                    Bitmap bitmap = content.getDrawingCache();
                    File root = Environment.getExternalStorageDirectory();
                    File cachePath = new File(root.getAbsolutePath() + "/DCIM/Camera/shareableimage.jpg");
                    try
                    {
                        root.createNewFile();
                        FileOutputStream ostream = new FileOutputStream(root);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, ostream);
                        ostream.close();
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }


                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    Uri phototUri = Uri.parse("/DCIM/Camera/shareableimage.jpg");
                    shareIntent.setData(phototUri);
                    shareIntent.setType("image/*");
                    shareIntent.putExtra(Intent.EXTRA_STREAM, phototUri);
                    startActivity(Intent.createChooser(shareIntent, "Share Via"));
                }catch (Exception e){
                    Toast.makeText(getApplicationContext(),e.getMessage().toString(),Toast.LENGTH_LONG).show();
                }

            }

        });*/

        shareButton = findViewById(R.id.shareId);
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                image();
            }
        });

    }

    public void image(){
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        BitmapDrawable drawable = (BitmapDrawable) qrCode.getDrawable();
        Bitmap bitmap = drawable.getBitmap();

        File file = new File(getExternalCacheDir()+"/"+getResources().getString(R.string.app_name)+".png");

        Intent intent = null;

        try{

            FileOutputStream outputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG,100,outputStream);

            outputStream.flush();
            outputStream.close();
            intent = new Intent(Intent.ACTION_SEND);
            intent.setType("image/*");
            intent.putExtra(Intent.EXTRA_STREAM,Uri.fromFile(file));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        }catch (Exception e){
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
        }

        startActivity(Intent.createChooser(intent,"share image"));

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == CallPermission){
            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                startActivity(intent);
            }else{
                Toast.makeText(getApplicationContext(),"Permission Denied",Toast.LENGTH_LONG).show();
            }
        }
    }

}