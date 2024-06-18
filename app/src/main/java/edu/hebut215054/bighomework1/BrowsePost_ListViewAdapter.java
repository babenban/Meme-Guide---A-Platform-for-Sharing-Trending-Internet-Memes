package edu.hebut215054.bighomework1;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.w3c.dom.Text;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

public class BrowsePost_ListViewAdapter extends BaseAdapter {
    private static final int USER_LOGIN = 1;
    private static final int USER_GETPOST = 2;
    private List<Bean> beans;
    private Context context;
    private String TAG = "ListViewAdapter";
    private SharedPreferences sp;

    public BrowsePost_ListViewAdapter(List<Bean> beans, Context context) {
        this.beans = beans;
        this.context = context;
//        Log.e(TAG, "beans.size: "+beans.size());
    }

    @Override
    public int getCount() {

        return beans.size();
    }

    @Override
    public Object getItem(int position) {
        return beans.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
//        if(convertView == null){
        if(true){
            convertView = LayoutInflater.from(context).inflate(R.layout.listview_item, parent, false);

            TextView username = convertView.findViewById(R.id.listView_item_username);
            username.setText(beans.get(position).getPusername());

            TextView post_content = convertView.findViewById(R.id.listView_item_postContent);
            post_content.setText(beans.get(position).getPcontext());
//            Log.e(TAG, "position:"+ position + ", post_content: "+ beans.get(position).getPcontext());

            TextView likenumber = convertView.findViewById(R.id.listView_item_likenumber);
//            Log.e(TAG, "Plike: "+beans.get(position).getPlike());
            likenumber.setText(""+beans.get(position).getPlike());

            ImageView pic = convertView.findViewById(R.id.listview_pic);
            Glide.with(context)
                    .asBitmap()
                    .load(beans.get(position).getPicPath())
                    .error(R.drawable.blank_picture)
                    .into(pic);

            ImageView likeicon = convertView.findViewById((R.id.listView_item_likeicon));
            likeicon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e(TAG, "onClick: "+"点击点赞图标" );

                    sp = context.getSharedPreferences("215054", Context.MODE_PRIVATE);
                    String sql = null;
                    try {
                        sql = "func=" + URLEncoder.encode("like", "UTF-8");
                        sql += "&pid=" + URLEncoder.encode(""+beans.get(position).getPid(),"UTF-8");
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
                    likenumber.setText(Integer.parseInt(likenumber.getText().toString())+1+"");
                }
            });

        }

        return convertView;
    }
}
