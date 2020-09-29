package com.example.java_test;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.chip.ChipGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;


public class LoginActivity extends AppCompatActivity {


    private static String TAG = "test";
    private static String TAG_name = "name";
    private static String TAG_user_numb = "회원번호";
    private static String TAG_license_allow = "면허인증";
    private static String TAG_e_mail = "이메일";
    private static String TAG_JSON = "JSON";

    private TextView mTextViewResult;

    Intent login_intent;
    Intent Array_data;
    Intent sign_intent;
    Intent id_pwd_intent;

    ImageView login_logo_img;
    TextView sign_up, id_pwd;
    EditText login_ID_et, login_PWD_et;
    CheckBox login_auto_chk;
    Button login_bt;
    ArrayList<HashMap<String, String>> mArrayList;
    String sid, spw;
    String mJsonString;
    String errorString;
    String loginid, loginpwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        String result;

        login_intent = new Intent(this, MainActivity.class);
        sign_intent = new Intent(this, sign_up.class);
        id_pwd_intent = new Intent(this, id_pwd.class);

        sign_up = (TextView)findViewById(R.id.sign_up);
        id_pwd = (TextView)findViewById(R.id.id_pwd);
        login_logo_img = findViewById(R.id.logo_img);
        login_ID_et = findViewById(R.id.ID_et);
        login_PWD_et = findViewById(R.id.PWD_et);
        login_auto_chk = findViewById(R.id.auto_login);
        login_bt = findViewById(R.id.login_bt);

        SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
        loginid = auto.getString("inputId", null);
        loginpwd = auto.getString("inputPWD", null);


        if(loginid !=null && loginpwd !=null || loginid == "ID" && loginpwd =="PWD") {
            login_auto_chk.setChecked(true);
            GetData task = new GetData();
            task.execute(loginid, loginpwd);
        }


            login_bt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(login_auto_chk.isChecked()) {
                        if (loginid == null && loginpwd == null) {
                                SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
                                SharedPreferences.Editor autoLogin = auto.edit();
                                autoLogin.putString("inputId", login_ID_et.getText().toString());
                                autoLogin.putString("inputPWD", login_PWD_et.getText().toString());
                                autoLogin.commit();
                        }
                    }
                    GetData task = new GetData();
                    task.execute(login_ID_et.getText().toString(), login_PWD_et.getText().toString());
                }
            });


        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(sign_intent);
                LoginActivity.this.finish();
            }
        });

        id_pwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(id_pwd_intent);
                LoginActivity.this.finish();
            }
        });

    }


    private class GetData extends AsyncTask<String, Void, String>{

        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(LoginActivity.this,
                    "Please Wait", null, true, true);
        }

        void Ashow() {
            AlertDialog.Builder Abuilder = new AlertDialog.Builder(LoginActivity.this);
            Abuilder.setTitle("계정 불일치");
            Abuilder.setMessage("아이디와 패스워드를 재확인 해주세요.");
            Abuilder.setPositiveButton("예", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            Abuilder.show();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();
            Log.d(TAG, "response - " + result);

            if (result == null){
                mTextViewResult.setText(errorString);
                SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
                SharedPreferences.Editor editor = auto.edit();
                editor.clear();
                editor.commit();
            }
            else if(result.indexOf("true") != -1) {
                login_intent.putExtra("result_data", result);
                startActivity(login_intent);
                LoginActivity.this.finish();
            }
            else {
                Ashow();
                SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
                SharedPreferences.Editor editor = auto.edit();
                editor.clear();
                editor.commit();
            }
        }


        @Override
        protected String doInBackground(String... params) {
            String searchID = params[0];
            String searchPWD = params[1];



            String serverURL = "http://54.180.121.142/app/login/";
            String postParameters = "id=" + searchID + "&password=" + searchPWD;



            try {

                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();


                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.connect();


                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(postParameters.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();


                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d(TAG, "response code - " + responseStatusCode);

                InputStream inputStream;
                if(responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                }
                else{
                    inputStream = httpURLConnection.getErrorStream();
                }


                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line;

                while((line = bufferedReader.readLine()) != null){
                    sb.append(line);
                }

                bufferedReader.close();


                return sb.toString();


            } catch (Exception e) {

                Log.d(TAG, "InsertData: Error ", e);
                errorString = e.toString();

                return null;
            }

        }
    }

    private long backKeyPressedTime = 0;
    private Toast toast;

    public void onBackPressed() {
        if(System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis();
            toast = Toast.makeText(this, "\'뒤로\' 버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }
        else {
            finish();
            toast.cancel();
        }
    }


}



