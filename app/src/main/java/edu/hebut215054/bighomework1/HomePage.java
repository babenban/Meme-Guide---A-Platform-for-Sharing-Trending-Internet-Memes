package edu.hebut215054.bighomework1;

import static java.lang.Thread.sleep;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.Manifest;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class HomePage extends AppCompatActivity {
    private static final int USER_LOGIN = 1;
    private static final int USER_GETPOST = 2;
    private static final int NO_INTERNET = 3;
    private static final int CLICK = 4;
    private String TAG = "HomePage";
    private NavigationView homepage_menu;
    private Button button, button1, btn_next, btn_pre;
    private TextView drawer_username, drawer_sign;
    private ListView homepageListView;
    private ArrayList<Bean> beans;
    private long time;
    private int currentMaxPid, currentRecommend;
    public boolean haveNetwork, autoclickfinish;
    private List<List<Bean>> beans1;
    private BrowsePost_ListViewAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page1);


        Boolean read = RequestPermissions(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if(read == true){
            Log.e("permission", "onCreate: "+"已有权限" );
        }
        else{
            Log.e("permission", "onCreate: "+"没有权限" );
        }
        RequestPermissions(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        initcomponent();
        setEvent();
        getRecommend();
        initBtn_next();



        SharedPreferences sp = getSharedPreferences("215054", Context.MODE_PRIVATE);
        NavigationView n = (NavigationView) findViewById(R.id.homePage_menu);
        View headerView = n.getHeaderView(0);
        TextView t = headerView.findViewById(R.id.drawer_username);
        t.setText(sp.getString("uname","anonymous").toString());

        TextView t1 = headerView.findViewById(R.id.drawer_sign);
        t1.setText("此用户还没有设置签名...");


    }


    private Boolean RequestPermissions(Context context, String permission){
        if(ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions((Activity) context, new String[]{permission}, 100);
            return false;
        }
        else{
            return true;
        }
    }

    public void initcomponent(){
        button = findViewById(R.id.btn_homepage_viewpost);
        button1 = findViewById(R.id.btn_homepage_publishpost);
        homepage_menu = findViewById(R.id.homePage_menu);
        homepageListView = findViewById(R.id.homepage_listview);
        time = System.currentTimeMillis();
        currentMaxPid = -1;
        currentRecommend = 0;
        btn_next = findViewById(R.id.btn_homepage_next);
        btn_pre = findViewById(R.id.btn_homepage_pre);
        beans1 = new ArrayList<>();
        autoclickfinish = false;

        btn_next.setBackgroundColor(Color.TRANSPARENT);
        btn_pre.setBackgroundColor(Color.TRANSPARENT);


    }

    public void setEvent(){


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), Browse_Post.class);
                startActivity(intent);

            }
        });

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), Publish_Post.class);
                intent.putExtra("currentMaxPid", currentMaxPid);
                startActivity(intent);
            }
        });

        homepage_menu.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId() == R.id.nav_home){
                    Intent intent = new Intent(getBaseContext(), Personal_Center.class);
                    startActivity(intent);
                }
                else if(item.getItemId() == R.id.nav_gallery){
                    Intent intent = new Intent(getBaseContext(), MyWordCloud.class);
                    startActivity(intent);
                }

                return false;
            }
        });



    }

    private void getRecommend(){

        SharedPreferences sp = getSharedPreferences("215054", Context.MODE_PRIVATE);
        String sql = null;
        try {
            sql = "uid=" + URLEncoder.encode(""+sp.getInt("uid", 0),"UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }


        beans = new ArrayList<>();
        Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == USER_LOGIN) {
                    String response = (String) msg.obj;
                    // 处理服务器响应数据
                    try {
                        JSONArray jsonArray = new JSONArray(response);

                        for(int i = 0; i < jsonArray.length(); i++){
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            int bestcomment = jsonObject.getInt("bestComment");
                            String pusername = jsonObject.getString("uname");
                            int uid = jsonObject.getInt("uid");
//                            String ptitle = jsonObject.getString("ptitle");
                            String pcontext = jsonObject.getString("pcontext");
                            int plike = jsonObject.getInt("plike");
                            int pid = jsonObject.getInt("pid");
                            String tag = jsonObject.getString("tag");
                            Log.e(TAG, "tag: "+tag );
                            String picPath = jsonObject.getString("pic");

                            Bean bean = new Bean();
                            bean.setBestcomment(bestcomment);
                            bean.setUid(uid);
//                            bean.setPtitle(ptitle);
                            bean.setPcontext(pcontext);
                            bean.setPlike(plike);
                            bean.setPid(pid);
                            bean.setPusername(pusername);
                            bean.setBestcomment(bestcomment);
                            bean.setTag(tag);
                            bean.setPicPath(picPath);
                            beans.add(bean);
                            beans1.add(new ArrayList<Bean>());
                            beans1.get(currentRecommend).add(bean);
                            currentRecommend += 1;
                        }

                        Log.e(TAG, "beans.size = "+beans.size());
                        adapter = new BrowsePost_ListViewAdapter(beans1.get(currentRecommend%beans.size()), getBaseContext());
                        homepageListView.setAdapter(adapter);
                        currentRecommend += 1;

                        homepageListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Intent intent = new Intent(getBaseContext(), PostDetail.class);
                                intent.putExtra("content",beans1.get((currentRecommend+2)%3).get(0));
                                intent.putExtra("likenumber", beans1.get((currentRecommend+2)%3).get(0).getPlike());
                                startActivity(intent);

                            }
                        });

                    }catch(Exception e){
                        e.printStackTrace();
                    }

                    autoclick();

                }
                else if(msg.what == NO_INTERNET){
                    Toast.makeText(HomePage.this, "无网络连接", Toast.LENGTH_SHORT).show();
                }
            }
        };

        String id = sp.getString("uname","");
        String pwd = sp.getString("password","");
        HttpClientUtil.SendByHttpClient(id, pwd, handler,"recommend",sql);

    }

    private void initBtn_next(){
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter = new BrowsePost_ListViewAdapter(beans1.get(currentRecommend%beans.size()),getBaseContext());
                homepageListView.setAdapter(adapter);
                currentRecommend += 1;
                autoclickfinish = false;
            }
        });

        btn_pre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                autoclickfinish = true;
                currentRecommend = currentRecommend + beans.size() - 2;
                adapter = new BrowsePost_ListViewAdapter(beans1.get(currentRecommend%beans.size()),getBaseContext());
                homepageListView.setAdapter(adapter);
                currentRecommend += 1;

            }
        });


    }


    private void autoclick(){

        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){
                    if(!autoclickfinish){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                btn_next.callOnClick();
                            }
                        });
                    }
//                    Log.e(TAG, "自动点击按钮: ");
                    try {
                        sleep(2000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }).start();
    }

}