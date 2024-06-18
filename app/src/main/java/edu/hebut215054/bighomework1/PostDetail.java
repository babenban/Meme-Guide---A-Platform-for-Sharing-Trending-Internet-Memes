package edu.hebut215054.bighomework1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.zhy.view.flowlayout.TagFlowLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

public class PostDetail extends AppCompatActivity {
    private Toolbar toolbar;
    private TextView postContent, postusername, postlikenumber;
    private EditText reply;
    private Button sendreply;
    private ExpandableListView expandableListView;
    private static final int USER_LOGIN = 1;
    private static final int USER_GETPOST = 2;
    private String TAG = "PostDetail";
    private JSONArray commentArray;
    private ArrayList<Comment> gData;
    private ArrayList<ArrayList<Comment>> iData;
    private Bean bean;
    private ImageButton iblike;
    private SharedPreferences sp;
    private TagFlowLayout flowLayout;
    private ArrayList<String> datas;
    private FlowTagAdapter flowTagAdapter;
    private ImageView postPic;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);

        initComponent();
        getComments();
        configureEvents();
        initflowlayout();
        inflatePic();

    }

    public void initComponent(){
        toolbar = findViewById(R.id.postDetail_toolbar);
        postContent = findViewById(R.id.postDetail_postContent);
        postusername = findViewById(R.id.postDetail_username);
        postlikenumber = findViewById(R.id.postDetail_likenumber);
        expandableListView = findViewById(R.id.postDetail_expandablelistview);
        reply = findViewById(R.id.postDetail_reply);
        sendreply = findViewById(R.id.postDetail_sendreply);
        sp = getSharedPreferences("215054", Context.MODE_PRIVATE);
        iblike = findViewById(R.id.iblike);
        bean = getIntent().getParcelableExtra("content");
        gData = new ArrayList<>();
        iData = new ArrayList<ArrayList<Comment>>();
        flowLayout = findViewById(R.id.postDetail_FlowLayout);
        postPic = findViewById(R.id.postDetail_pic);

    }

    public void configureEvents(){
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

        postContent.setText(bean.getPcontext());
        postusername.setText(bean.getPusername());
        postlikenumber.setText(""+getIntent().getIntExtra("likenumber",0));

        MyExpandableListAdapter myExpandableListAdapter = new MyExpandableListAdapter(gData, iData, this);
        expandableListView.setAdapter(myExpandableListAdapter);


        sendreply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sp = getSharedPreferences("215054", Context.MODE_PRIVATE);
                String sql = null;

                try {
                    sql = "func=" + URLEncoder.encode("setcomment","UTF-8");
                    sql += "&pid=" + URLEncoder.encode(""+bean.getPid(), "UTF-8");
                    sql += "&uid=" + URLEncoder.encode(""+sp.getInt("uid",0),"UTF-8");
                    sql += "&comment=" + URLEncoder.encode(reply.getText().toString(), "UTF-8");
                    sql += "&uname=" + URLEncoder.encode(sp.getString("uname",""),"UTF-8");

                } catch (UnsupportedEncodingException e) {
                    throw new RuntimeException(e);
                }

                Handler handler = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        if (msg.what == USER_LOGIN) {
                            String response = (String) msg.obj;
                            Log.e(TAG, "handleMessage: "+response);
                            Toast.makeText(PostDetail.this, "评论成功！", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                };

                String id = sp.getString("uname","");
                String pwd = sp.getString("password","");
                HttpClientUtil.SendByHttpClient(id, pwd, handler,"insertFunction",sql);
            }
        });

        iblike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sql = null;
                try {
                    sql = "func=" + URLEncoder.encode("like", "UTF-8");
                    sql += "&pid=" + URLEncoder.encode(""+bean.getPid(),"UTF-8");
                    sql += "&uid=" + URLEncoder.encode(sp.getInt("uid",0)+"","UTF-8");

                } catch (UnsupportedEncodingException e) {
                    throw new RuntimeException(e);
                }

                Handler handler = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        if (msg.what == USER_LOGIN) {
                            String response = (String) msg.obj;
                            Log.e(TAG, "handleMessage: "+response);
                            // 处理服务器响应数据

                        }
                    }
                };

                String id = sp.getString("uname","");
                String pwd = sp.getString("password","");
                HttpClientUtil.SendByHttpClient(id, pwd, handler,"updateFunction",sql);
                postlikenumber.setText(Integer.parseInt(postlikenumber.getText().toString())+1+"");
            }
        });

    }

    private void getComments(){
        Handler handler1 = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == USER_GETPOST) {
                    String response = (String) msg.obj;
                    // 处理服务器响应数据
                    try {
                        commentArray = new JSONArray(response);
                        gData.add(new Comment("展开评论区", "111"));
                        for(int i = 0; i < gData.size(); i++){
                            iData.add(new ArrayList<Comment>());
                            for (int j = 0; j < commentArray.length(); j++) {
                                JSONObject jsonObject = commentArray.getJSONObject(j);
                                Comment comment = new Comment(jsonObject.getString("uname"), jsonObject.getString("comment"));
                                iData.get(i).add(comment);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        // JSON解析失败，处理异常
                    }
                }
            }
        };

        String key = ""+bean.getPid();
        getComment.SendByHttpClient(key,handler1);
    }

    private void initflowlayout(){
        datas = new ArrayList<>();
        datas.add("抽象");
        datas.add("牢大");
        datas.add("日常");
        datas.add("洗脑");
        datas.add("怪核");
        datas.add("让你飞起来");
        datas.add("what's love");
        datas.add("肘击");
        datas.add("嘿嘿嘿");

        String tagString = bean.getTag();
        Log.e(TAG, "tagString : "+tagString );

        String[] splitedString = tagString.split(",");
        int[] tagnums = new int[splitedString.length];
        Log.e(TAG, "splitedString.length: "+splitedString.length);

        for(int i = 0; i < splitedString.length; i++){
            Log.e(TAG, "tagID: "+splitedString[i] );
            tagnums[i] = Integer.parseInt(splitedString[i]);
        }

        ArrayList<String> tags = new ArrayList<>();
        for(int i = 0; i < tagnums.length; i++){
            tags.add(datas.get(tagnums[i]));
        }

        flowTagAdapter = new FlowTagAdapter(tags, this);
        flowLayout.setAdapter(flowTagAdapter);
        flowLayout.setClickable(false);

    }

    private void inflatePic(){
        Glide.with(this)
                .asBitmap()
                .load(bean.getPicPath())
                .error(R.drawable.blank_picture)
                .into(postPic);

    }



}