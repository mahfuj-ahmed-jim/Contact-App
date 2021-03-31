package com.example.contactappai;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private Button createNewContactList,shareButton;
    private RecyclerView recyclerView;
    private List<App> appList = new ArrayList<>();
    private List<User> userList,finalUserList;
    private List<String> stringList,finalStringList;
    private CustomAdapter adapter;
    private int ContactPermissionCode = 1;
    private FloatingActionButton floatingActionButton;
    private EditText searchView;
    private CharSequence sequence = "";
    private SpeechRecognizer speechRecognizer;
    private Intent speechRecognizerIntent;
    private String finalName,finalApk;
    private MyDataBaseHelper myDataBaseHelper;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myDataBaseHelper = new MyDataBaseHelper(this);
        SQLiteDatabase sqLiteDatabase = myDataBaseHelper.getWritableDatabase();

        shareButton = findViewById(R.id.shareButtonId);
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_SEND);
                    intent.putExtra(Intent.EXTRA_STREAM, FileProvider.getUriForFile(MainActivity.this,
                            MainActivity.this.getPackageName()+".provider",new File(finalApk)));

                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    intent.setType("application/vnd.android.package-archive");
                    startActivity(Intent.createChooser(intent,"Share Apk"));
                }catch (Exception e){
                    Toast.makeText(getApplicationContext(),e.getMessage().toString(),Toast.LENGTH_LONG).show();
                }

            }
        });

        searchView = findViewById(R.id.searchBarId);
        searchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.getFilter().filter(s);
                sequence = s;
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        createNewContactList = findViewById(R.id.createNewContactId);
        createNewContactList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_INSERT);
                intent.setType(ContactsContract.RawContacts.CONTENT_TYPE);
                startActivity(intent);
            }
        });

        floatingActionButton = findViewById(R.id.floatingButtonId);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,ScannerActivity.class));
            }
        });

        intialization();
        getAppPath();

        /*speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        speechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle params) {

            }

            @Override
            public void onBeginningOfSpeech() {

            }

            @Override
            public void onRmsChanged(float rmsdB) {

            }

            @Override
            public void onBufferReceived(byte[] buffer) {

            }

            @Override
            public void onEndOfSpeech() {

            }

            @Override
            public void onError(int error) {

            }

            @Override
            public void onResults(Bundle results) {
                ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                if(matches!=null){
                    searchView.setText(matches.get(0));
                }
            }

            @Override
            public void onPartialResults(Bundle partialResults) {

            }

            @Override
            public void onEvent(int eventType, Bundle params) {

            }
        });*/

        /*speechButton = findViewById(R.id.speechButtonId);
        speechButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.RECORD_AUDIO)== PackageManager.PERMISSION_GRANTED){

                    switch (event.getAction()){
                        case MotionEvent.ACTION_UP :
                            break;
                        case MotionEvent.ACTION_DOWN :
                            searchView.setText("");
                            Toast.makeText(getApplicationContext(),"Listening......",Toast.LENGTH_SHORT).show();
                            speechRecognizer.startListening(speechRecognizerIntent);
                            break;
                    }

                }else{

                    ActivityCompat.requestPermissions(MainActivity.this,new String[] {Manifest.permission.RECORD_AUDIO},ContactPermissionCode);

                }
                return false;
            }
        });*/
        
    }

    private void intialization() {
        recyclerView = findViewById(R.id.recyclerViewId);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        userList = new ArrayList<>();
        stringList = new ArrayList<>();
        finalUserList = new ArrayList<>();
        finalStringList = new ArrayList<>();
        adapter = new CustomAdapter(finalUserList,this);
        recyclerView.setAdapter(adapter);

        Cursor cursor = myDataBaseHelper.getOldData();
        while(cursor.moveToNext()){
            User user = new User(cursor.getString(0),cursor.getString(1),cursor.getString(2),
                    cursor.getString(3),cursor.getString(4),cursor.getString(5),
                    cursor.getString(6),cursor.getString(7));
            finalStringList.add(user.getName()+" ;;;;; "+user.getNumber()+" ;;;;; "+user.getAddress()+" ;;;;; "+user.getEmail()+" ;;;;; "+user.getFacebook()
                    +" ;;;;; "+user.getLinkedin()+" ;;;;; "+user.getLetter()+" ;;;;; "+user.getPhoto());
        }

        Collections.sort(finalStringList);

        for(String temp:finalStringList){
            String [] userInfo = temp.split(" ;;;;; ");
            User user = new User(userInfo[1],userInfo[0],userInfo[2],userInfo[3],userInfo[4],userInfo[5],userInfo[6],userInfo[7]);
            finalUserList.add(user);
            adapter.notifyDataSetChanged();
        }

        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_CONTACTS)== PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_CONTACTS)== PackageManager.PERMISSION_GRANTED ){
            getContact();

        }else{
            requestContactPermission();
        }

    }

    private void getContact() {
        Cursor phone = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,null,null,null);
        while(phone.moveToNext()){
            String name = phone.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String numberSample = phone.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            String photo = phone.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI));
            String id = phone.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone._ID));
            String letter = String.valueOf(name.charAt(0));
            String number = "";
            String address = "";
            String email = "";
            String facebook = "";
            String linkedin = "";
            for(int i=0;i<numberSample.length();i++){
                if(numberSample.charAt(i)=='+'||numberSample.charAt(i)=='0'||numberSample.charAt(i)=='1'||numberSample.charAt(i)=='2'||
                        numberSample.charAt(i)=='3'||numberSample.charAt(i)=='4'||numberSample.charAt(i)=='5'||
                        numberSample.charAt(i)=='6'||numberSample.charAt(i)=='7'||numberSample.charAt(i)=='8'||
                        numberSample.charAt(i)=='9'){
                    number = number+numberSample.charAt(i);
                }
            }
            String sort = number+" ;;;;; "+name+" ;;;;; "+address+" ;;;;; "+email+" ;;;;; "+facebook+" ;;;;; "+linkedin+" ;;;;; "+letter+" ;;;;; "+photo;
            if(!(stringList.contains(sort))){
                stringList.add(sort);
            }
        }
        Collections.sort(stringList);
        if(finalUserList.size()==0){
            for(String temp:stringList){
                String [] userInfo = temp.split(" ;;;;; ");
                User user = new User(userInfo[0],userInfo[1],userInfo[2],userInfo[3],userInfo[4],userInfo[5],userInfo[6],userInfo[7]);
                if(!(userList.contains(user))){
                    userList.add(user);
                    finalUserList.add(user);
                    Long id = myDataBaseHelper.insertData(user.getNumber(),user.getName(),user.getAddress(),
                            user.getEmail(),user.getFacebook(),user.getLinkedin(),user.getLetter(),user.getPhoto());
                }
                adapter.notifyDataSetChanged();
            }
        }else{
            Toast.makeText(getApplicationContext(),"Hello",Toast.LENGTH_LONG).show();
            for(String temp:stringList){
                String [] userInfo = temp.split(" ;;;;; ");
                User user = new User(userInfo[0],userInfo[1],userInfo[2],userInfo[3],userInfo[4],userInfo[5],userInfo[6],userInfo[7]);
                if(!(userList.contains(user))){
                    userList.add(user);
                }
                for(User usersample : userList){
                    if(finalUserList.contains(usersample)){
                        finalUserList.add(usersample);
                    }
                }
                adapter.notifyDataSetChanged();
            }
        }
    }

    private void requestContactPermission() {
        if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.WRITE_CONTACTS) &&
                ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.READ_CONTACTS)){

            new AlertDialog.Builder(this).setTitle("Permission Needed")
                    .setMessage("This permission needed otherwise the app will failed to show the contact list")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(MainActivity.this,new String[] {Manifest.permission.WRITE_CONTACTS,
                                    Manifest.permission.READ_CONTACTS},ContactPermissionCode);
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).create().show();

        }else{
            ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.WRITE_CONTACTS,
                    Manifest.permission.READ_CONTACTS},ContactPermissionCode);
        }
    }

    private void getAppPath() {
        PackageManager packageManager = getApplicationContext().getPackageManager();
        List<ApplicationInfo> packages = packageManager.getInstalledApplications(PackageManager.GET_META_DATA);

        for(ApplicationInfo applicationInfo : packages){
            String name;
            if((name = String.valueOf(packageManager.getApplicationLabel(applicationInfo))).isEmpty()){
                name = applicationInfo.packageName;
            }
            String apkPath = applicationInfo.sourceDir;
            App app = new App(name,apkPath);
            appList.add(app);
        }

        for(App list : appList){
            if(list.getName().equals(getResources().getString(R.string.app_name))){
                finalName = list.getName();
                finalApk = list.getApkPath();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == ContactPermissionCode){
            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                getContact();
            }else{
                Toast.makeText(getApplicationContext(),"Permission Denied",Toast.LENGTH_LONG).show();
            }
        }
    }

    private void showData(String resultData, String string) {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        builder.setTitle(resultData);
        builder.setMessage(string);
        builder.setCancelable(true);
        builder.show();
    }

}