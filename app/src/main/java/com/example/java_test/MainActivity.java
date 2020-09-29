package com.example.java_test;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.AndroidRuntimeException;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private static String TAG = "test";
    private static String TAG_name = "name";
    private static String TAG_user_numb = "user_numb";
    private static String TAG_license_allow = "면허인증";
    private static String TAG_e_mail = "이메일";
    private static String TAG_JSON = "webnautes";

    ImageView main_logo_img;
    TextView main_name, person_num, license_num, email_tv;
    ListView main_list;
    Button logout;
    Intent login_put_intent;
    Intent array_put_intent;
    String result;
    String mJsonString;
    ArrayList<HashMap<String, String>> mArrayList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        person_num = findViewById(R.id.person_num);
        license_num = findViewById(R.id.license_num);
        email_tv = findViewById(R.id.email_tv);
        array_put_intent = getIntent();
        login_put_intent = getIntent();
        main_logo_img = findViewById(R.id.main_logo_img);
        main_name = findViewById(R.id.main_name);


        logout = findViewById(R.id.logout_bt);

        result = login_put_intent.getStringExtra("result_data");
        mJsonString = result;
        showResult();



        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplication(), LoginActivity.class));
                SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
                SharedPreferences.Editor editor = auto.edit();
                editor.clear();
                editor.commit();
                MainActivity.this.finish();
            }
        });

    }

    private void showResult() {

        String user_name = null;
        String user_numb = null;
        String license_allow = null;
        String e_mail = null;

        String[] arraysum = new String[4];

        try {
            JSONArray jsonArray = new JSONObject(mJsonString).getJSONArray(TAG_JSON);

            for(int i=0; i<jsonArray.length(); i++) {
                HashMap<String, String> hashMap = new HashMap<>();
                JSONObject item = jsonArray.getJSONObject(i);

                user_name = item.optString("name");
                user_numb = item.optString("user_numb");
                license_allow = item.optString("license_allow");
                e_mail = item.optString("e_mail");


                arraysum[0] = user_name;
                arraysum[1] = user_numb;
                arraysum[2] = license_allow;
                arraysum[3] = e_mail;
            }
        } catch (JSONException e) {
            Log.d(TAG, "showResult : ", e);
            e.printStackTrace();
        }
        main_name.setText("[" + arraysum[0] + "] 님 환영합니다.");
        person_num.setText("회원번호 : " + arraysum[1]);
        license_num.setText("면허인증 : " + arraysum[2]);
        email_tv.setText("이메일 : " + arraysum[3]);
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