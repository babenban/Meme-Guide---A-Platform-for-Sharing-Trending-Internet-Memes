package edu.hebut215054.bighomework1;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class BrowsePost_Fragment extends Fragment {
    private String TAG = "BrowsePost_Fragment";
    private View rootView;
    private BrowsePost_ListViewAdapter listViewAdapter;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private List<Bean> beans;


    public BrowsePost_Fragment() {

    }


    public static BrowsePost_Fragment newInstance(String param1, ArrayList<Bean> beans) {
        BrowsePost_Fragment fragment = new BrowsePost_Fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putParcelableArrayList(ARG_PARAM2, beans);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            this.beans = getArguments().getParcelableArrayList(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if(rootView == null){
            rootView = inflater.inflate(R.layout.browsepost_fragment, container, false);
        }
        initView();

        return rootView;
    }

    private void initView(){
        TextView tv = rootView.findViewById(R.id.fragment_textview);
        tv.setText(mParam1);

        ListView listView = rootView.findViewById(R.id.fragment_listview);
        listView.setBackground(getResources().getDrawable(R.drawable.gengbaike_gray));
        listViewAdapter = new BrowsePost_ListViewAdapter(beans, getContext());
        listView.setAdapter(listViewAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(), PostDetail.class);

                TextView likenumber = view.findViewById(R.id.listView_item_likenumber);
                intent.putExtra("content", beans.get(position));
                intent.putExtra("likenumber", Integer.parseInt(likenumber.getText().toString()));
                startActivity(intent);
            }
        });
    }
}