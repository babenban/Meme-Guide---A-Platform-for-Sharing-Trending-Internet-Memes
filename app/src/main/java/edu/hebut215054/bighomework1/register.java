package edu.hebut215054.bighomework1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class register extends AppCompatActivity {
    private Toolbar toolbar;
    private Button btn_register;
    private EditText account;
    private EditText password;
    private EditText passwordrepeat;
    private static final int USER_LOGIN = 1;
    private static final int USER_GETPOST = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initComponent();
        setEvent();

    }

    private void initComponent(){
        toolbar = findViewById(R.id.register_toolbar);
        btn_register = findViewById(R.id.register_btnregister);
        account = findViewById(R.id.register_account);
        password = findViewById(R.id.register_password);
        passwordrepeat = findViewById(R.id.register_passwordrepeat);

    }


    private void setEvent(){
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pswd = password.getText().toString();
                String pswdrepeat = passwordrepeat.getText().toString();
                if(!pswd.equals(pswdrepeat)){
                    Toast.makeText(getBaseContext(), "两次密码不一致！", Toast.LENGTH_SHORT).show();
                }
                else{
                    SharedPreferences sp = getSharedPreferences("215054", Context.MODE_PRIVATE);
                    String sql = null;
                    try {
                        sql = "func=" + URLEncoder.encode("register", "UTF-8");
                        sql += "&uname=" + URLEncoder.encode(account.getText().toString(),"UTF-8");
                        sql += "&ucode=" + URLEncoder.encode(pswd,"UTF-8");

                    } catch (UnsupportedEncodingException e) {
                        throw new RuntimeException(e);
                    }


                    Handler handler = new Handler() {
                        @Override
                        public void handleMessage(Message msg) {
                            if (msg.what == USER_LOGIN) {
                                String response = (String) msg.obj;
                                Log.e("register", "handleMessage: "+response);
                                // 处理服务器响应数据

                                Toast.makeText(getBaseContext(), "注册成功！", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }
                    };

                    String id = sp.getString("uname","");
                    String pwd = sp.getString("password","");
                    HttpClientUtil.SendByHttpClient(id, pwd, handler,"insertFunction",sql);


                }
            }
        });

    }

}