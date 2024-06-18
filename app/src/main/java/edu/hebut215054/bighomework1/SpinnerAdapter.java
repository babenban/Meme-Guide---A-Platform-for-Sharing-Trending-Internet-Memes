package edu.hebut215054.bighomework1;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class SpinnerAdapter extends BaseAdapter {
    private ArrayList<String> datas;
    private Context context;

    public SpinnerAdapter(ArrayList<String> datas, Context context) {
        this.datas = datas;
        this.context = context;
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.spinner_item, parent, false);
        }

        TextView spinner_textview = convertView.findViewById(R.id.spinner_textview);
        spinner_textview.setText(datas.get(position).toString());

        return null;
    }
}
