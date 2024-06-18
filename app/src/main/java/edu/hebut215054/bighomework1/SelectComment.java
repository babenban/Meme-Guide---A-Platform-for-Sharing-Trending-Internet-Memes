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
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class SelectComment extends AppCompatActivity {
    private String TAG = "SelectComment";
    private static final int USER_LOGIN = 1;
    private static final int USER_GETPOST = 2;
    private Toolbar toolbar;
    private ListView listView;
    private List<Bean> beans;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_comment);

        initComponent();
        setEvent();
        getPosts();

    }

    private void initComponent(){

        toolbar = findViewById(R.id.selectComment_toolbar);
        listView = findViewById(R.id.selectComment_listview);
        beans = new ArrayList<>();

    }

    private void setEvent(){
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void getPosts(){
        SharedPreferences sp = getSharedPreferences("215054", Context.MODE_PRIVATE);
        String key = null;
        try {
            key = "key=" + URLEncoder.encode("-1", "UTF-8");
            key += "&uid=" + URLEncoder.encode(""+sp.getInt("uid",0),"UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        getPosting.SendByHttpClient(key,handler1, USER_GETPOST);

    }

    private void initListView(){
        SelectListView_Adapter adapter = new SelectListView_Adapter(beans, this);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getBaseContext(), SelectComment_Detail.class);
                intent.putExtra("content",beans.get(position));
                startActivity(intent);
            }
        });

    }

    Handler handler1 = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == USER_GETPOST) {
                Log.e(TAG, "handleMessage: " );
                String response = (String) msg.obj;
                // 处理服务器响应数据
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
//                            int bestcomment = jsonObject.getInt("bestComment");
                        String pusername = jsonObject.getString("uname");
                        int uid = jsonObject.getInt("uid");
//                            String ptitle = jsonObject.getString("ptitle");
                        String pcontext = jsonObject.getString("pcontext");
//                            int plike = jsonObject.getInt("plike");
                        int pid = jsonObject.getInt("pid");
                        Log.e(TAG, "pid= "+pid );
                        String pic = jsonObject.getString("pic");

                        Bean bean = new Bean();
//                            bean.setBestcomment(bestcomment);
                        bean.setUid(uid);
                        bean.setPcontext(pcontext);
//                            bean.setPlike(plike);
                        bean.setPid(pid);
                        bean.setPusername(pusername);
                        bean.setPicPath(pic);
                        beans.add(bean);

                    }

                    initListView();

                } catch (JSONException e) {
                    e.printStackTrace();
                    // JSON解析失败，处理异常
                }

            }
        }
    };


}