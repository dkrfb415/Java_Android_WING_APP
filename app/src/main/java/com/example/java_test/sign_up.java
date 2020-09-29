package com.example.java_test;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

public class sign_up extends AppCompatActivity {

    private MainFragment fragment1;
    private MenuFragment fragment2;

    private FragmentManager fragmentManager;
    private FragmentTransaction transaction;

    Intent login_intent;

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
    public static class MenuFragment extends Fragment {

        SharedPreferences pref;
        sign_up sign_up;
        EditText sign_name, sign_id, sign_pwd, sign_phone, sign_email, sign_birth;
        CheckBox sign_chk_male;
        CheckBox sign_chk_female;
        String id, password, name, birth, phone, gender, email;

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
            sign_chk_male = (CheckBox) rootview.findViewById(R.id.sign_chk_male);
            sign_chk_female = (CheckBox)rootview.findViewById(R.id.sign_chk_female);
            Button sign_join_bt = (Button) rootview.findViewById(R.id.sign_join_bt);

            sign_name = (EditText)rootview.findViewById(R.id.sign_name);
            sign_id = (EditText)rootview.findViewById(R.id.sign_id);
            sign_pwd = (EditText)rootview.findViewById(R.id.sign_pwd);
            sign_email = (EditText)rootview.findViewById(R.id.sign_email);
            sign_phone = (EditText)rootview.findViewById(R.id.sign_phone);
            sign_birth = (EditText)rootview.findViewById(R.id.sign_birth);

            id = sign_id.getText().toString();
            password = sign_pwd.getText().toString();
            email = sign_email.getText().toString();
            name = sign_name.getText().toString();
            phone = sign_phone.getText().toString();
            birth = sign_birth.getText().toString();

            sign_chk_male.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sign_chk_female.setChecked(false);
                    Toast.makeText(sign_up.getApplicationContext(),"남",Toast.LENGTH_SHORT).show();
                }
            });

            sign_chk_female.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sign_chk_male.setChecked(false);
                    Toast.makeText(sign_up.getApplicationContext(),"여",Toast.LENGTH_SHORT).show();

                }
            });

            sign_join_bt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
    //                if(id != null && password != null && name != null && email != null && phone != null && birth != null) {
     //                 if(sign_chk_male.isChecked() || sign_chk_female.isChecked()) {
                    sign_up.onFragmentChange(1);
      //                  }
       //            }
        //            else {
         //              Toast.makeText(sign_up.getApplicationContext(), "선택 또는 작성하지 않는 내용이 있습니다.",Toast.LENGTH_SHORT).show();
          //         }
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

    public void onFragmentChange(int index)  {
        login_intent = new Intent(this, LoginActivity.class);
        if(index == 0) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_main,fragment2).commit();
        }
        else if(index == 1) {
            startActivity(login_intent);
            finish();
        }
    }
}