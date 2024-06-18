package edu.hebut215054.bighomework1;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class FlowTagAdapter extends TagAdapter {
    private ArrayList<String> datalist;
    private ArrayList<TextView> textViews;
//    private ArrayList<Integer> selected;
    private ArrayList<TextView> selected;
    private Context context;


    public FlowTagAdapter(ArrayList datas, Context context) {
        super(datas);
        this.datalist = datas;
        this.context = context;
        textViews = new ArrayList<>();
        selected = new ArrayList<>();
    }

    @Override
    public View getView(FlowLayout parent, int position, Object o) {
        TextView tv = (TextView) View.inflate(context, R.layout.flowlayout_textview, null);
        textViews.add(tv);
        tv.setText(datalist.get(position));
        return tv;
    }

//    public List<Integer> getSelectedTextView(){
//        return selected;
//    }
    public List<TextView> getSelectedTextView(){
        return selected;
    }

    @Override
    public int getCount() {
        return datalist.size();
    }


    @Override
    public void onSelected(int position, View view) {
        super.onSelected(position, view);
//        selected.add(position);
        selected.add(textViews.get(position));
    }

    @Override
    public void unSelected(int position, View view) {
        super.unSelected(position, view);
//        selected.remove(position);
        selected.remove(textViews.get(position));
    }
}
