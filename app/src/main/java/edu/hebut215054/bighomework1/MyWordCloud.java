package edu.hebut215054.bighomework1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.SharedElementCallback;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;


public class MyWordCloud extends AppCompatActivity {
    private ImageView ivWordCloud;
    private Toolbar toolbar;
    private String Path = "http://192.168.31.92:9999/MyAndroidServer/wordcloud";
    private String site = "wordCloud";
    private static final int SUCCESS = 993;
    private static final int FAIL = 814;
    private static final int USER_GETPOST = 2;
    private SharedPreferences sp;

    Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch(msg.what){
                case SUCCESS:
                    Log.e("MyWordCloud", "handleMessage: "+"SUCCESS" );
                    ivWordCloud.setImageBitmap((Bitmap) msg.obj);
                    break;

                case FAIL:
                    Toast.makeText(getBaseContext(),"接收失败", Toast.LENGTH_SHORT).show();
                    break;

                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_word_cloud);

        initComponent();
        setEvent();
        getPicture();


    }

    private void initComponent(){
        ivWordCloud = findViewById(R.id.myWordCloud_imageView);
        toolbar = findViewById(R.id.myWordCloud_toolbar);
        sp = getSharedPreferences("215054", Context.MODE_PRIVATE);
    }


    private void setEvent(){
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }


    public void getPicture(){

        int uid = sp.getInt("uid",0);
        String key = "null";
        getWordCloud.SendByHttpClient(key,handler,""+uid);

    }

}