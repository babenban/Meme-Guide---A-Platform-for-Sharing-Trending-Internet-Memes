package edu.hebut215054.bighomework1;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

public class SelectListView_Adapter extends BaseAdapter {
        private List<Bean> beans;
        private Context context;
        private String TAG = "ListViewAdapter";

        public SelectListView_Adapter(List<Bean> beans, Context context) {
            this.beans = beans;
            this.context = context;
        }

        @Override
        public int getCount() {
            Log.e(TAG, "getCount: "+beans.size());
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
            if(true){
                convertView = LayoutInflater.from(context).inflate(R.layout.selectlistview_item, parent, false);

                TextView username = convertView.findViewById(R.id.selectlistViewitem_username);
                username.setText(beans.get(position).getPusername());

                TextView post_content = convertView.findViewById(R.id.selectListViewItem_postContent);
                post_content.setText(beans.get(position).getPcontext());

                ImageView pic = convertView.findViewById(R.id.selectlistView_pic);
                Glide.with(context)
                        .asBitmap()
                        .load(beans.get(position).getPicPath())
                        .error(R.drawable.blank_picture)
                        .into(pic);


            }

            return convertView;
        }
}

