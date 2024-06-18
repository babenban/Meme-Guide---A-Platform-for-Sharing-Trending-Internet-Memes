package edu.hebut215054.bighomework1;

import static java.lang.Thread.sleep;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class Browse_Post extends AppCompatActivity implements View.OnClickListener {
    private ArrayList<Bean> beanList_resolved, beanList_unresolved;
    private ArrayList<Fragment> fragmentList;
    private Toolbar toolbar;
    private ViewPager2 viewPager2;
    private LinearLayout llresolved, llunresolved;;
    private ImageView ivresolved, ivunresolved, ivcurrent;
    private SearchView msearchview;
    private static final int USER_LOGIN = 1;
    private static final int USER_GETPOST = 2;
    private static final int UPDATE_POST = 3;
    private String TAG = "Browse_Post";
    private BrowsePosts_ViewPager_Adapter viewPagerAdapter;
    private int currentPage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_post);

        initComponents();
        configureEvents();
//        getPosts();


    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updatePost();


    }

    Handler handler1 = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Log.e(TAG, "handleMessage: " );
            String response = (String) msg.obj;
            // 处理服务器响应数据
            try {
//                beanList_resolved.clear();
//                beanList_unresolved.clear();
                beanList_resolved = new ArrayList<>();
                beanList_unresolved = new ArrayList<>();
                JSONArray jsonArray = new JSONArray(response);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    Log.e(TAG, "jsonArray[i] "+jsonArray.getJSONObject(i) );
                    int bestcomment = jsonObject.getInt("bestComment");
                    String pusername = jsonObject.getString("uname");
                    int uid = jsonObject.getInt("uid");
                    String pcontext = jsonObject.getString("pcontext");
                    int plike = jsonObject.getInt("plike");
                    int pid = jsonObject.getInt("pid");
                    String tag = jsonObject.getString("tag");
                    String picPath = jsonObject.getString("pic");


                    Bean bean = new Bean();
                    bean.setBestcomment(bestcomment);
                    bean.setUid(uid);
                    bean.setPcontext(pcontext);
                    bean.setPlike(plike);
                    bean.setPid(pid);
                    bean.setPusername(pusername);
                    bean.setBestcomment(bestcomment);
                    bean.setTag(tag);
                    bean.setPicPath(picPath);
                    if(bean.bestcomment == 0){
                        beanList_unresolved.add(bean);
                    }
                    else{
                        beanList_resolved.add(bean);
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
                // JSON解析失败，处理异常
            }

            if (msg.what == USER_GETPOST) {
                initFragmentList();
            }
            else if(msg.what == UPDATE_POST){
//                fragmentList.clear();
//                fragmentList.add(BrowsePost_Fragment.newInstance("已解决", beanList_resolved));
//                fragmentList.add(BrowsePost_Fragment.newInstance("未解决", beanList_unresolved));

                initFragmentList();
                changePage(currentPage);

            }
        }
    };


    private void getPosts(){
//        String key = "null";
        String key = null;
        try {
            key = "key=" + URLEncoder.encode("null", "UTF-8");
            key += "&uid=" + URLEncoder.encode(""+0,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        getPosting.SendByHttpClient(key,handler1, USER_GETPOST);
    }

    private void updatePost(){
        String key = null;
        try {
            key = "key=" + URLEncoder.encode("null", "UTF-8");
            key += "&uid=" + URLEncoder.encode(""+0,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        getPosting.SendByHttpClient(key, handler1, UPDATE_POST);
    }

    private void queryPost(String query){
        String key = null;
        try {
            key = "key=" + URLEncoder.encode(query, "UTF-8");
            key += "&uid=" + URLEncoder.encode(""+0,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        getPosting.SendByHttpClient(key, handler1, UPDATE_POST);
    }

    private void initComponents(){
        toolbar = findViewById(R.id.browse_post_toolbar);
        viewPager2 = findViewById(R.id.browse_post_viewpager);
        llresolved = findViewById(R.id.browsePostsLayout_resolved);
        llunresolved = findViewById(R.id.browsePostsLayout_unresolved);
        ivresolved = findViewById(R.id.browsePostsImageView_resolved);
        ivunresolved = findViewById(R.id.browsePostsImageView_unresolved);
        msearchview = findViewById(R.id.browsePost_msearchview);
        beanList_resolved = new ArrayList<>();
        beanList_unresolved = new ArrayList<>();
        currentPage = 0;

    }


    private void initFragmentList(){
        fragmentList = new ArrayList<>();
        fragmentList.add(BrowsePost_Fragment.newInstance("已解决", beanList_resolved));
        fragmentList.add(BrowsePost_Fragment.newInstance("未解决", beanList_unresolved));

        //viewpager设置适配器
        viewPagerAdapter = new BrowsePosts_ViewPager_Adapter(
                getSupportFragmentManager(), getLifecycle(), fragmentList);
        viewPager2.setAdapter(viewPagerAdapter);
        //viewpager设置滑动监听
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                changePage(position);

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });



    }


    private void configureEvents(){
        //设置当前选中的ImageView
        ivcurrent = ivresolved;
        ivresolved.setSelected(true);

        //已解决未解决layout设置点击事件监听
        llresolved.setOnClickListener(this::onClick);
        llunresolved.setOnClickListener(this::onClick);

        msearchview.setSubmitButtonEnabled(true);
        msearchview.setIconified(true);
        msearchview.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.e(TAG, "query: "+query );
                queryPost(query);

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
//        msearchview.onActionViewExpanded();

        //toolbar navigationIcon设置点击监听
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            //需要返回上级界面

            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void changePage(int position){
        ivcurrent.setSelected(false);
        if(position == 0){
            viewPager2.setCurrentItem(0);
            ivresolved.setSelected(true);
            ivcurrent = ivresolved;
            currentPage = 0;
        }
        else if(position == 1){
            viewPager2.setCurrentItem(1);
            ivunresolved.setSelected(true);
            ivcurrent = ivunresolved;
            currentPage = 1;
        }
        else if(position == R.id.browsePostsLayout_resolved){
            viewPager2.setCurrentItem(0);
            ivresolved.setSelected(true);
            ivcurrent = ivresolved;
            currentPage = 0;
        }
        else if(position == R.id.browsePostsLayout_unresolved){
            viewPager2.setCurrentItem(1);
            ivunresolved.setSelected(true);
            ivcurrent = ivunresolved;
            currentPage = 1;
        }

    }

    @Override
    public void onClick(View v) {
        changePage(v.getId());
    }
}