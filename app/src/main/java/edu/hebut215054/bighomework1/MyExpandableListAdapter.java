package edu.hebut215054.bighomework1;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MyExpandableListAdapter extends BaseExpandableListAdapter {
    private ArrayList<Comment> gData;
    private ArrayList<ArrayList<Comment>> iData;
    private Context context;

    public MyExpandableListAdapter(ArrayList<Comment> gData, ArrayList<ArrayList<Comment>> iData, Context context) {
        this.gData = gData;
        this.iData = iData;
        this.context = context;
    }

    @Override
    public int getGroupCount() {
        return gData.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return iData.get(groupPosition).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return gData.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return iData.get(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.expandable_title, parent, false);

        }

        TextView tv = convertView.findViewById(R.id.expandable_title_textview);
        tv.setText(gData.get(groupPosition).getName());

//        convertView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // 获取 ExpandableListView
//                ExpandableListView expandableListView = (ExpandableListView) parent;
//
//                if (expandableListView.isGroupExpanded(groupPosition)) {
//                    // 如果父项已展开，则折叠它
//                    expandableListView.collapseGroup(groupPosition);
//                } else {
//                    // 如果父项未展开，则展开它
//                    expandableListView.expandGroup(groupPosition);
//                }
//            }
//        });

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.expandable_item, parent, false);

        }

        TextView name = convertView.findViewById(R.id.expandable_item_textviewName);
        TextView content = convertView.findViewById(R.id.expandable_item_textviewContent);
        name.setText(iData.get(groupPosition).get(childPosition).getName()+": ");
        content.setText(iData.get(groupPosition).get(childPosition).getContent());

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
