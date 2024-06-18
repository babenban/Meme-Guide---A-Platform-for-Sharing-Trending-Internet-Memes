package edu.hebut215054.bighomework1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class Personal_Center extends AppCompatActivity {
    private static final int USER_LOGIN = 1;
    private static final int USER_GETPOST = 2;
    private Toolbar toolbar;
    private LinearLayout llchangePersonalData;
    private LinearLayout llbrowseHistory;
    private LinearLayout llaccountSecurity;
    private LinearLayout llselectComment;
    private LinearLayout llwordCloud;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_center);

        initView();
        initEvent();



    }

    private void initView(){
        toolbar = findViewById(R.id.personalCenterToolbar);
        llbrowseHistory = findViewById(R.id.browseHistory);
        llaccountSecurity = findViewById(R.id.accountSecurity);
        llselectComment = findViewById(R.id.selectComment);
//        llwordCloud = findViewById(R.id.generateWordCloud);

    }

    private void initEvent(){
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        llselectComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), SelectComment.class);
                startActivity(intent);
            }
        });



//        llwordCloud.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                SharedPreferences sp = getSharedPreferences("215054", Context.MODE_PRIVATE);
//                String sql = null;
//                try {
//                    sql = "func=" + URLEncoder.encode("setposting", "UTF-8");
//
//                } catch (UnsupportedEncodingException e) {
//                    throw new RuntimeException(e);
//                }
//
//
//                Handler handler = new Handler() {
//                    @Override
//                    public void handleMessage(Message msg) {
//                        if (msg.what == USER_LOGIN) {
//                            String response = (String) msg.obj;
//                            Log.e("PersonalCenter", "handleMessage: "+response);
//                            // 处理服务器响应数据
////                    if (!response.equals("0")) {
////                        // 登录成功，执行相应的操作
////                        Toast.makeText(getBaseContext(), "登录成功", Toast.LENGTH_SHORT).show();
////
////
////                    } else {
////                        // 登录失败，显示相应的消息
////                        Toast.makeText(getBaseContext(), "登录失败", Toast.LENGTH_SHORT).show();
////                    }
//                        }
//                    }
//                };
//
//                String id = sp.getString("uname","");
//                String pwd = sp.getString("password","");
//                HttpClientUtil.SendByHttpClient(id, pwd, handler,"wordCloud",sql);
//
//            }
//        });

    }

}