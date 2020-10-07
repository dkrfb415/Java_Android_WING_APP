package com.example.java_test;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.sip.SipSession;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class sign_up extends AppCompatActivity {

    private MainFragment fragment1;
    private MenuFragment fragment2;

    private FragmentManager fragmentManager;
    private FragmentTransaction transaction;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up_frag);

        fragment1 = new MainFragment();
        fragment2 = new MenuFragment();


        // 기본 프래그먼트 지정
        fragmentManager = getSupportFragmentManager();
        transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.fragment_main, fragment1);
        transaction.commit();

    }




    // 약관 동의 후 프래그먼트 클래스
    public static class MenuFragment extends Fragment   {

        SharedPreferences pref;
        sign_up sign_up;
        EditText sign_name, sign_id, sign_pwd, sign_phone, sign_email, sign_birth;
        CheckBox sign_chk_male;
        CheckBox sign_chk_female;
        String id, password, name, birth, phone, gender, email;
        RelativeLayout sign_Relative;

        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

        }

        @Override
        public void onAttach(Context context) {
            super.onAttach(context);
            sign_up = (sign_up) getActivity();
        }

        @Override
        public void onDetach() {
            super.onDetach();
            sign_up = null;
        }

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

            ViewGroup rootview = (ViewGroup)inflater.inflate(R.layout.sign_fragment, container, false);
            sign_Relative = (RelativeLayout) rootview.findViewById(R.id.sign_relative);
            sign_chk_male = (CheckBox) rootview.findViewById(R.id.sign_chk_male);
            sign_chk_female = (CheckBox)rootview.findViewById(R.id.sign_chk_female);
            final Button sign_join_bt = (Button) rootview.findViewById(R.id.sign_join_bt);

            sign_name = (EditText)rootview.findViewById(R.id.sign_name);
            sign_id = (EditText)rootview.findViewById(R.id.sign_id);
            sign_pwd = (EditText)rootview.findViewById(R.id.sign_pwd);
            sign_email = (EditText)rootview.findViewById(R.id.sign_email);
            sign_phone = (EditText)rootview.findViewById(R.id.sign_phone);
            sign_birth = (EditText)rootview.findViewById(R.id.sign_birth);

            sign_id.setMovementMethod(new ScrollingMovementMethod());

            sign_chk_male.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sign_chk_female.setChecked(false);
                    gender = "male";
                }
            });

            sign_chk_female.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sign_chk_male.setChecked(false);
                    gender = "female";

                }
            });

            sign_join_bt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    id = sign_id.getText().toString();
                    password = sign_pwd.getText().toString();
                    name = sign_name.getText().toString();
                    phone = sign_phone.getText().toString();
                    birth = sign_birth.getText().toString();
                    email = sign_email.getText().toString();

                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String result) {
                                    try {
                                        JSONObject jasonObject = new JSONObject(result);
                                        if (result.indexOf("true") != -1) {
                                            Toast.makeText(sign_up.getApplicationContext(), "회원 등록 성공", Toast.LENGTH_SHORT).show();
                                            sign_up.onFragmentChange(1);
                                        } else {
                                            Toast.makeText(sign_up.getApplicationContext(), "회원 등록 실패", Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                        }
                    };
                    RegisterRequest registerRequest = new RegisterRequest(id, password, name, birth, phone, gender, email, responseListener);
                    RequestQueue queue = Volley.newRequestQueue(sign_up);
                    queue.add(registerRequest);
                }
            });
            return rootview;
        }
    }


    // 기본 화면으로 사용할 프래그먼트 (약관 동의화면)
    public static class MainFragment extends Fragment {

        sign_up sign_up;

        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

        }

        @Override
        public void onAttach(Context context) {
            super.onAttach(context);
           sign_up = (sign_up) getActivity();
        }

        @Override
        public void onDetach() {
            super.onDetach();
            sign_up = null;
        }

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
           ViewGroup rootview = (ViewGroup) inflater.inflate(R.layout.activity_sign_up, container, false);
           Button next_bt = (Button)rootview.findViewById(R.id.next_bt);
            WebView privarcy_tv = (WebView) rootview.findViewById(R.id.privacy_detail);
            WebView rental_tv = (WebView) rootview.findViewById(R.id.rental_detail);
            WebView service_tv = (WebView) rootview.findViewById(R.id.service_detail);
            final CheckBox privarcy = (CheckBox)rootview.findViewById(R.id.privacy);
            final CheckBox rental = (CheckBox)rootview.findViewById(R.id.rental);
            final CheckBox service = (CheckBox)rootview.findViewById(R.id.service);


            privarcy_tv.getSettings().setJavaScriptEnabled(true);
            rental_tv.getSettings().setJavaScriptEnabled(true);
            service_tv.getSettings().setJavaScriptEnabled(true);


            rental_tv.setHorizontalScrollBarEnabled(true);
            service_tv.setHorizontalScrollBarEnabled(true);
            rental_tv.setVerticalScrollBarEnabled(true);
            service_tv.setVerticalScrollBarEnabled(true);
            privarcy_tv.setHorizontalScrollBarEnabled(true);
            privarcy_tv.setVerticalScrollBarEnabled(true);

            String privarcy_s = "<a href='http://54.180.121.142/foruser/joinus/privarcy.html'>자세히</a>";
            String rental_s = "<a href='http://54.180.121.142/foruser/joinus/service.html'>자세히</a>";
            String service_s = "<a href='http://54.180.121.142/foruser/joinus/kickboard.html'>자세히</a>";

            privarcy_tv.loadData(privarcy_s, "text/html","UTF-8");
            rental_tv.loadData(rental_s,"text/html", "UTF-8");
            service_tv.loadData(service_s,"text/html","UTF-8");


           next_bt.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {



                   if(privarcy.isChecked() && rental.isChecked() && service.isChecked()) {
                       sign_up.onFragmentChange(0);
                   }
                   else {
                       Toast.makeText(sign_up.getApplicationContext(),"약관 동의를 완료하지 않았습니다.", Toast.LENGTH_SHORT).show();
                   }
               }
           });
            return rootview;

        }
    }

    public static class RegisterRequest extends StringRequest {

        //서버 url 설정(php파일 연동)
        final static  private String URL="http://54.180.121.142/app/login/joinus/";
        private Map<String,String>map;

        public RegisterRequest(String id, String password, String name, String birth, String phone, String gender, String email, Response.Listener<String>listener){
            super(Method.POST,URL,listener,null);//위 url에 post방식으로 값을 전송

            map=new HashMap<>();
            map.put("id",id);
            map.put("password",password);
            map.put("name",name);
            map.put("birth",birth);
            map.put("phone",phone);
            map.put("gender",gender);
            map.put("email",email);
        }

        @Override
        protected Map<String, String> getParams() throws AuthFailureError {
            return map;
        }
    }

    public void onFragmentChange(int index)  {
        Intent login_intent;
        login_intent = new Intent(this, LoginActivity.class);
        if(index == 0) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_main,fragment2).commit();
        }
        else if(index == 1) {
            startActivity(login_intent);
            finish();
        }
    }

    public class ValidateRequest extends StringRequest {
        //서버 url 설정(php파일 연동)
        final static private String URL = "http://54.180.121.142/app/login/joinus/";
        private Map<String, String> map;

        public ValidateRequest(String userID, Response.Listener<String> listener) {
            super(Request.Method.POST, URL, listener, null);

            map = new HashMap<>();
            map.put("userID", userID);
        }

        @Override
        protected Map<String, String> getParams() throws AuthFailureError {
            return map;
        }

    }
}