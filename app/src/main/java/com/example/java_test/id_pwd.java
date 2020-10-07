package com.example.java_test;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

public class id_pwd extends AppCompatActivity {

    private IDFragment frag1;
    private PwdFragment frag2;

    private FragmentManager fragmentManager;
    private FragmentTransaction transaction;

    Button search_id;
    Button search_pwd;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_id_pwd);

        frag1 = new IDFragment();
        frag2 = new PwdFragment();


        search_id = findViewById(R.id.search_id);
        search_pwd = findViewById(R.id.search_pwd);


        fragmentManager = getSupportFragmentManager();
        transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.id_pwd_frame, frag1);
        transaction.commit();
        search_id.setBackgroundColor(Color.BLUE);

        search_pwd.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    search_pwd.setBackgroundColor(Color.LTGRAY);
                    search_id.setBackgroundColor(Color.BLUE);
                }
                else if(event.getAction() == MotionEvent.ACTION_UP) {
                    search_pwd.setBackgroundColor(Color.BLUE);
                    search_id.setBackgroundColor(Color.LTGRAY);
                }
                return false;
            }
        });

        search_pwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onFragmentChange(1);
            }
        });

        search_id.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    search_id.setBackgroundColor(Color.LTGRAY);
                    search_pwd.setBackgroundColor(Color.BLUE);

                }
                else if(event.getAction() == MotionEvent.ACTION_UP) {
                    search_id.setBackgroundColor(Color.BLUE);
                    search_pwd.setBackgroundColor(Color.LTGRAY);
                }
                return false;
            }
        });

        search_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onFragmentChange(0);
            }
        });

    }


    public static class IDFragment extends Fragment {

        id_pwd id_pwd;
        String id_result;
        String name, phone;
        EditText search_name;
        EditText search_phone;
        TextView result_id;
        String[] arraysum = new String[2];
        private static String TAG_JSON = "webnautes";

        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        }

        @Override
        public void onAttach(Context context) {
            super.onAttach(context);
            id_pwd = (id_pwd) getActivity();
        }

        @Override
        public void onDetach() {
            super.onDetach();
            id_pwd = null;
        }

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

            ViewGroup rootview = (ViewGroup)inflater.inflate(R.layout.search_id_id, container, false);
            search_name = (EditText) rootview.findViewById(R.id.name_search_re);
            search_phone = (EditText) rootview.findViewById(R.id.phone_num_search);
            Button search_id_result = (Button)rootview.findViewById(R.id.searching_id_phone);
            result_id = (TextView) rootview.findViewById(R.id.search_Result_id);

            search_id_result.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    name = search_name.getText().toString();
                    phone = search_phone.getText().toString();

                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String result) {
                            try {
                                JSONObject jsonObject = new JSONObject(result);
                                if(result.indexOf("true") != -1) {
                                    try {
                                        JSONArray jsonArray = new JSONObject(result).getJSONArray(TAG_JSON);

                                        for(int i=0; i<jsonArray.length(); i++) {
                                            HashMap<String, String> hashMap = new HashMap<>();
                                            JSONObject item = jsonArray.getJSONObject(i);

                                            id_result = item.optString("id");

                                            arraysum[0] = id_result;
                                            arraysum[1] = name;
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    result_id.setText("[" + arraysum[1] + "] 님의 아이디는 {" + arraysum[0] + "} 입니다.");
                              }
                               else {
                                  Toast.makeText(id_pwd.getApplicationContext(),"계정이 정보가 확인되지 않습니다.", Toast.LENGTH_SHORT).show();
                               }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    id_pwd.RegisterRequest registerRequest = new RegisterRequest(name, phone, responseListener);
                    RequestQueue queue = Volley.newRequestQueue(id_pwd);
                    queue.add(registerRequest);
                }
            });
            return rootview;
        }
    }

    public static class PwdFragment extends Fragment {

        id_pwd id_pwd;
        EditText search_id, search_phone, change_pwd_et;
        TextView search_result_pwd;
        Button search_pwd, change_pwd_bt;
        String id, phone;
        String pwd_result;
        String[] arraysum = new String[2];
        private static String TAG_JSON = "webnautes";

        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        }

        @Override
        public void onAttach(Context context) {
            super.onAttach(context);
            id_pwd = (id_pwd) getActivity();
        }

        @Override
        public void onDetach() {
            super.onDetach();
            id_pwd = null;
        }

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

            ViewGroup rootview = (ViewGroup)inflater.inflate(R.layout.search_pwd_pwd, container, false);
            search_id = (EditText)rootview.findViewById(R.id.pwd_search_re);
            search_phone = (EditText)rootview.findViewById(R.id.phone_pwd_search);
            change_pwd_et = (EditText)rootview.findViewById(R.id.changing_pwd);
            search_pwd = (Button)rootview.findViewById(R.id.searching_pwd_phone);
            change_pwd_bt = (Button)rootview.findViewById(R.id.chage_pwd_bt);
            search_result_pwd = (TextView)rootview.findViewById(R.id.search_Result_pwd);

            search_pwd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    id = search_id.getText().toString();
                    phone = search_phone.getText().toString();

                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String result) {


                            try {
                                JSONObject jsonObject = new JSONObject(result);

                                if(result.indexOf("true") != -1) {
                                    try {
                                        JSONArray jsonArray = new JSONObject(result).getJSONArray(TAG_JSON);

                                        for(int i=0; i<jsonArray.length(); i++) {
                                            HashMap<String, String> hashMap = new HashMap<>();
                                            JSONObject item = jsonArray.getJSONObject(i);

                                            pwd_result = item.optString("user_numb");

                                            arraysum[0] = pwd_result;
                                            arraysum[1] = id;
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    search_result_pwd.setText("[" + arraysum[1] + "] 의 회원번호는 {" + arraysum[0] + "} 입니다.");
                                }
                                else {
                                    Toast.makeText(id_pwd.getApplicationContext(),"계정이 정보가 확인되지 않습니다.", Toast.LENGTH_SHORT).show();
                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }
                    };
                    id_pwd.RegisterRequest2 registerRequest = new RegisterRequest2(id, phone, responseListener);
                    RequestQueue queue = Volley.newRequestQueue(id_pwd);
                    queue.add(registerRequest);
                }
            });



            return rootview;
        }

    }
    public void onFragmentChange(int index)  {
        Intent login_intent;
        login_intent = new Intent(this, LoginActivity.class);
        if(index == 0) {
            getSupportFragmentManager().beginTransaction().replace(R.id.id_pwd_frame,frag1).commit();
        }
        else if(index == 1) {
            getSupportFragmentManager().beginTransaction().replace(R.id.id_pwd_frame,frag2).commit();
        }
    }

    public static class RegisterRequest extends StringRequest {

        //서버 url 설정(php파일 연동)
        final static  private String URL="http://54.180.121.142/app/login/findid/";
        private Map<String,String> map;

        public RegisterRequest(String name, String phone, Response.Listener<String>listener){
            super(Method.POST,URL,listener,null);//위 url에 post방식으로 값을 전송

            map=new HashMap<>();
            map.put("name",name);
            map.put("phone",phone);
        }

        @Override
        protected Map<String, String> getParams() throws AuthFailureError {
            return map;
        }
    }

    public static class RegisterRequest2 extends StringRequest {

        //서버 url 설정(php파일 연동)
        final static  private String URL="http://54.180.121.142/app/login/findpw/check/";
        private Map<String,String> map;

        public RegisterRequest2(String id, String phone, Response.Listener<String>listener){
            super(Method.POST,URL,listener,null);//위 url에 post방식으로 값을 전송

            map=new HashMap<>();
            map.put("user_id",id);
            map.put("phone",phone);
        }

        @Override
        protected Map<String, String> getParams() throws AuthFailureError {
            return map;
        }
    }
}