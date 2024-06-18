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
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class SelectComment_Detail extends AppCompatActivity {
    private static final int USER_LOGIN = 1;
    private static final int USER_GETPOST = 2;
    private JSONArray commentArray;
    private Bean bean1;
    private ArrayList<Bean> iData;
    private ListView commentListView;
    private TextView pcontext;
    private Toolbar toolbar;
    private int currentpos = 0;
    private ImageView iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_comment_detail);

        initComponent();
        getComments();
        Log.e("SelectCommentDetail", "onCreate: iData.size() =  "+ iData.size());
        setEvent();

    }

    private void initComponent(){
        commentListView = findViewById(R.id.selectCommentDetail_listview);
        toolbar = findViewById(R.id.postDetail_toolbar);
        iData = new ArrayList<>();
        bean1 = getIntent().getParcelableExtra("content");
        pcontext = findViewById(R.id.postDetail_postContent);
        iv = findViewById(R.id.selectCommentDetail_imageView);


    }

    private void setEvent(){
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //set iv
        Glide.with(this)
                .asBitmap()
                .load(bean1.getPicPath())
                .into(iv);

        pcontext.setText(bean1.getPcontext());


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
                        for (int j = 0; j < commentArray.length(); j++) {
                            JSONObject jsonObject = commentArray.getJSONObject(j);
                            Bean bean = new Bean();
//                            bean.setPid(jsonObject.getInt("pid"));
                            bean.setUid(jsonObject.getInt("uid"));
                            bean.setPusername(jsonObject.getString("uname"));
                            bean.setPcontext(jsonObject.getString("comment"));
                            int best = jsonObject.getInt("cid");
//                            if(best == 1){
//                                currentBest = best;
//                            }
                            bean.setBestcomment(best);
                            bean.setCid(jsonObject.getInt("cid"));

                            iData.add(bean);
                            Log.e("SelectCommentDetail", "iData.size() = : "+ iData.size());
                        }

                        initListView();

                    } catch (JSONException e) {
                        e.printStackTrace();
                        // JSON解析失败，处理异常
                    }
                }
            }
        };

        String key = ""+bean1.getPid();
        Log.e("SelectCommentDetail", "bean1.getPid(): "+bean1.getPid());
        getComment.SendByHttpClient(key,handler1);

    }

    private void initListView(){
        SelectListViewDetail_Adapter adapter = new SelectListViewDetail_Adapter(iData,this);
        commentListView.setAdapter(adapter);


        commentListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                SharedPreferences sp = getSharedPreferences("215054", Context.MODE_PRIVATE);
                String sql = null;
                try {
                    sql = "func=" + URLEncoder.encode("setbestcomment", "UTF-8");
                    sql += "&cid=" + URLEncoder.encode(iData.get(position).getCid() + "", "UTF-8");
                    sql += "&pid=" + URLEncoder.encode(bean1.getPid() + "", "UTF-8");
                    Log.e("SelectCommentDetail", "cid=: "+ iData.get(position).getCid() +", pid=" + bean1.getPid());

                } catch (UnsupportedEncodingException e) {
                    throw new RuntimeException(e);
                }

                Handler handler = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        if (msg.what == USER_LOGIN) {
                            String response = (String) msg.obj;
                            Log.e("SelectCommentDetail", "handleMessage: " + response);
                            // 处理服务器响应数据
                        }
                    }
                };

                String id1 = sp.getString("uname", "");
                String pwd = sp.getString("password", "");
                HttpClientUtil.SendByHttpClient(id1, pwd, handler, "updateFunction", sql);

                View view1 = commentListView.getChildAt(currentpos);
                view1.setBackgroundColor(Color.TRANSPARENT);
                view.setBackgroundColor(Color.GREEN);
                currentpos = position;

            }
        });
    }

}