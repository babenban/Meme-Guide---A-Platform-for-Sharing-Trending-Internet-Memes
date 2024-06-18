package edu.hebut215054.bighomework1;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;

import java.util.ArrayList;
import java.util.List;

public class InsertPic extends AppCompatActivity {
    private String TAG = "InsertPic";
    private List<String> PicPath;
    private ImageView addPic;
    private int addPictureRequestCode = 101;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_pic);

        initComponent();
        setEvent();


    }

    private void initComponent(){
        PicPath = new ArrayList<>();
        addPic = findViewById(R.id.insertPic_addPic);


    }

    private void setEvent(){
        addPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Matisse.from(InsertPic.this)
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

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        PicPath.clear();
        if(requestCode == addPictureRequestCode){
            if(resultCode == RESULT_OK){
                PicPath = Matisse.obtainPathResult(data);
//                SQLiteOpenHelper helper = MySQLiteOpenHelper.getInstance(getBaseContext());
//                SQLiteDatabase db = helper.getWritableDatabase();
                for(int i = 0; i < PicPath.size(); i++){
                    Log.e(TAG, "PicPath: "+PicPath.get(i) );
//                    String sql = "insert into postPic values("+PicPath.get(i)+")";
//                    db.execSQL(sql);
                }

            }
        }



    }
}