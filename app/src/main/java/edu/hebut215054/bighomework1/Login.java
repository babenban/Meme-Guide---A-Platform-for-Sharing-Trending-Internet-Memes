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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

public class Login extends AppCompatActivity {
    private String TAG = "Login";
    private static final int USER_LOGIN = 1;
    private static final int USER_GETPOST = 2;
    private Toolbar toolbar;
    private CheckBox remember;
    private CheckBox autologin;
    private Button btnlogin;
    private Button btnregister;
    private EditText uname;
    private EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initcomponent();
        setEvent();
        readSP();

    }

    private void initcomponent(){
        toolbar = findViewById(R.id.login_toolbar);
        remember = findViewById(R.id.login_remember);
        autologin = findViewById(R.id.login_autologin);
        btnlogin = findViewById(R.id.login_login);
        btnregister = findViewById(R.id.login_register);
        uname = findViewById(R.id.login_account);
        password = findViewById(R.id.login_password);

    }

    private void setEvent(){
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Handler handler = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        if (msg.what == USER_LOGIN) {
                            String response = (String) msg.obj;
                            // 处理服务器响应数据
                            if (!response.equals("0")) {
                                // 登录成功，执行相应的操作
                                Toast.makeText(Login.this, "登录成功", Toast.LENGTH_SHORT).show();
                                int uid = Integer.parseInt(response);
                                Log.e(TAG, "handleMessage: "+uid);
                                userlogin(uid);

                            } else {
                                // 登录失败，显示相应的消息
                                Toast.makeText(Login.this, "登录失败", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                };

                String id = String.valueOf(uname.getText());
                String pwd = String.valueOf(password.getText());
                HttpClientUtil.SendByHttpClient(id, pwd, handler,"Login",null);
            }
        });

        btnregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), register.class);
                startActivity(intent);
            }
        });

    }

    public void readSP(){
        SharedPreferences sp = getSharedPreferences("215054", Context.MODE_PRIVATE);

        Boolean isremember = sp.getBoolean("remember",false);
        if(isremember){
            remember.setChecked(true);
            uname.setText(sp.getString("uname",""));
            password.setText(sp.getString("password",""));
        }
        else{
            remember.setChecked(false);
        }

        Boolean isautologin = sp.getBoolean("autologin", false);
        if(isautologin){
            autologin.setChecked(true);
        }
        else{
            autologin.setChecked(false);
        }

        if(isautologin && isremember){
            btnlogin.callOnClick();
        }

    }

    private void userlogin(int uid){
        SharedPreferences sp = getSharedPreferences("215054", Context.MODE_PRIVATE);
        sp.edit().putString("uname", uname.getText().toString())
                .putString("password", password.getText().toString())
                .putBoolean("remember", remember.isChecked())
                .putBoolean("autologin", autologin.isChecked())
                .putInt("uid", uid)
                .apply();

        Intent intent = new Intent(this, HomePage.class);
        startActivity(intent);

    }


}