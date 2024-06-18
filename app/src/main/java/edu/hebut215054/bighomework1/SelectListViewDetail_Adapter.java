package edu.hebut215054.bighomework1;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class SelectListViewDetail_Adapter extends BaseAdapter {
    private ArrayList<Bean> beans;
    private Context context;
    private String TAG = "ListViewAdapter";

    public SelectListViewDetail_Adapter(ArrayList<Bean> iData, Context context) {
        this.beans = iData;
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
//            Log.e(TAG, "getView: "+"渲染评论");
            convertView = LayoutInflater.from(context).inflate(R.layout.expandable_item, parent, false);

            TextView username = convertView.findViewById(R.id.expandable_item_textviewName);
            username.setText(beans.get(position).getPusername()+": ");

            TextView post_content = convertView.findViewById(R.id.expandable_item_textviewContent);
            post_content.setText(beans.get(position).getPcontext());

            if(beans.get(position).getBestcomment() == 1){
                convertView.setBackgroundColor(Color.GREEN);
            }

        }

        return convertView;
    }
}

