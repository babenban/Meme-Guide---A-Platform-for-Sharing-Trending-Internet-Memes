package edu.hebut215054.bighomework1;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.filter.Filter;
import com.zhihu.matisse.internal.entity.CaptureStrategy;
import com.zhy.view.flowlayout.TagFlowLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;


public class Publish_Post extends AppCompatActivity {
    private String TAG = "Publish_Post";
    private static final int USER_LOGIN = 1;
    private static final int USER_GETPOST = 2;
    private static final int CURRENT_MAX_PID = 4;
    private List<ImageView> pictures;
    private int addPictureRequestCode = 101;
    private List<String> picturePath;
    private LinearLayout llImage;
    private Toolbar toolbar;
    private TagFlowLayout mtagFlowLayout;
    private ArrayList<String> datas;
    private TextView clicktest;
    private FlowTagAdapter flowTagAdapter;
    private EditText postContent;
    private int currentMaxPid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_post);

//        addPicture = findViewById(R.id.publishPostImageView_addPicture);

        pictures = new ArrayList<>();
        pictures.add(findViewById(R.id.publishPostImageView0));
        pictures.add(findViewById(R.id.publishPostImageView1));
        pictures.add(findViewById(R.id.publishPostImageView2));
        pictures.add(findViewById(R.id.publishPostImageView_addPicture));
        mtagFlowLayout = findViewById(R.id.publishPost_FlowLayout);
        postContent = findViewById(R.id.publishPost_postContent);


        picturePath = new ArrayList<>();

        llImage = findViewById(R.id.publishPost_llImage);
        llImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Matisse.from(Publish_Post.this)
                        .choose(MimeType.ofImage())
                        .countable(true)
                        .maxSelectable(9)
                        .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                        .thumbnailScale(0.85f)
                        .imageEngine(new GlideEngine())
                        .theme(com.zhihu.matisse.R.style.Matisse_Zhihu)
                        //Glide加载方式
                        .imageEngine(new GlideEngine())
                        .forResult(addPictureRequestCode);
            }
        });

        toolbar = findViewById(R.id.publishPost_Toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(getBaseContext(), HomePage.class);
//                startActivity(intent);
                finish();
            }
        });


        initData();
        mtagFlowLayout = findViewById(R.id.publishPost_FlowLayout);
        flowTagAdapter = new FlowTagAdapter(datas,this);
        mtagFlowLayout.setAdapter(flowTagAdapter);

        getCurrentMaxPid();


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        picturePath.clear();


        if(requestCode == addPictureRequestCode){
            if(resultCode == RESULT_OK){
                picturePath = Matisse.obtainPathResult(data);
                int i = 0;
                for(; i < picturePath.size() && i < 3; i++){
                    Glide.with(this)
                            .asBitmap()
                            .load(picturePath.get(i))
                            .into(pictures.get(i));
                }

                if(i < 3){
                    Glide.with(this)
                            .asBitmap()
                            .load(R.drawable.choose_picture)
                            .into(pictures.get(i));
                    i++;
                    for(; i <= 3;i++){
                        Glide.with(this)
                                .asBitmap()
                                .load(R.drawable.blank)
                                .into(pictures.get(i));
                    }
                }
                else{
                    if(picturePath.size() == 3){
                        Glide.with(this)
                                .asBitmap()
                                .load(R.drawable.choose_picture)
                                .into(pictures.get(i));
                    }
                    else{
                        Glide.with(this)
                                .asBitmap()
                                .load(R.drawable.more_picture)
                                .into(pictures.get(i));
                    }

                }

            }
        }

    }

    public void Post(View view) {
//        List<Integer> selected = flowTagAdapter.getSelectedTextView();
        List<TextView> selected = flowTagAdapter.getSelectedTextView();
        String selectedTag = "";
        for(int i = 0; i < selected.size(); i++){
            for(int j = 0; j < datas.size(); j++){
                if(selected.get(i).getText().toString().equals(datas.get(j).toString())){
                    selectedTag += j +",";
                }
            }

        }

        if(selectedTag != ""){
            selectedTag = selectedTag.substring(0, selectedTag.length()-1);
        }
        Log.e(TAG, "Post: "+selectedTag);

        for(int i = 0; i < selected.size(); i++){
            Log.e("getSelectedTextView", "Post: "+selected.get(i).getText().toString() );
        }

        SharedPreferences sp = getSharedPreferences("215054", Context.MODE_PRIVATE);
        String sql = null;
        try {
            sql = "func=" + URLEncoder.encode("setposting", "UTF-8");
            sql += "&uid=" + URLEncoder.encode(""+sp.getInt("uid", 0),"UTF-8");
            sql += "&uname=" + URLEncoder.encode(sp.getString("uname",""), "UTF-8");
            sql += "&pcontext=" + URLEncoder.encode(postContent.getText().toString(),"UTF-8");
            sql += "&tag=" + URLEncoder.encode(selectedTag,"UTF-8");
            sql += "&pid=" + URLEncoder.encode(""+(currentMaxPid+1), "UTF-8");
            if(picturePath.size() == 0){
                sql += "&pic=" + URLEncoder.encode("noPicture","UTF-8");
            }
            else{
                sql += "&pic=" + URLEncoder.encode(picturePath.get(0),"UTF-8");
            }

        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

        String id = sp.getString("uname","");
        String pwd = sp.getString("password","");
        HttpClientUtil.SendByHttpClient(id, pwd, handler,"insertFunction",sql);
    }

    private void initData() {
        datas = new ArrayList<String>();
        datas.add("抽象");
        datas.add("牢大");
        datas.add("日常");
        datas.add("洗脑");
        datas.add("怪核");
        datas.add("让你飞起来");
        datas.add("what's love");
        datas.add("肘击");
        datas.add("嘿嘿嘿");

    }

    private void getCurrentMaxPid(){
//        String key = String.valueOf(keyText.getText());
//        if(key.length() == 0) key = "null";
//        String key = "null";
        String key = null;
        try {
            key = "key=" + URLEncoder.encode("null", "UTF-8");
            key += "&uid=" + URLEncoder.encode(""+0,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        getPosting.SendByHttpClient(key,handler1, CURRENT_MAX_PID);
    }

    Handler handler1 = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Log.e(TAG, "handleMessage: " );
            String response = (String) msg.obj;

            if (msg.what == CURRENT_MAX_PID) {
                // 处理服务器响应数据
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
//                            int bestcomment = jsonObject.getInt("bestComment");
//                            String pusername = jsonObject.getString("uname");
//                            int uid = jsonObject.getInt("uid");
//                            String ptitle = jsonObject.getString("ptitle");
//                            String pcontext = jsonObject.getString("pcontext");
//                            int plike = jsonObject.getInt("plike");
                        int pid = jsonObject.getInt("pid");
//                            String tag = jsonObject.getString("tag");

                        if(currentMaxPid < pid){
                            currentMaxPid = pid;
                        }

                    }
                    Log.e(TAG, "currentMaxPid: "+currentMaxPid );

                } catch (JSONException e) {
                    e.printStackTrace();
                    // JSON解析失败，处理异常
                }


            }
        }
    };

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == USER_LOGIN) {
                String response = (String) msg.obj;
                Log.e(TAG, "handleMessage: "+response);
                Toast.makeText(getBaseContext(), "发帖成功!", Toast.LENGTH_SHORT).show();
                finish();
                // 处理服务器响应数据
//                    if (!response.equals("0")) {
//                        // 登录成功，执行相应的操作
//                        Toast.makeText(getBaseContext(), "登录成功", Toast.LENGTH_SHORT).show();
//
//
//                    } else {
//                        // 登录失败，显示相应的消息
//                        Toast.makeText(getBaseContext(), "登录失败", Toast.LENGTH_SHORT).show();
//                    }
            }
        }
    };


}