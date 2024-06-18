package edu.hebut215054.bighomework1;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class getWordCloud {
    private static final int USER_GETPOST = 2;
    private static final int SUCCESS = 993;

    public static void SendByHttpClient(final String key, final Handler handler, String uid){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // 创建 URL 对象
                    URL url = new URL("http://192.168.31.92:9999/MyAndroidServer/wordCloud");
                    // 打开连接
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    // 设置请求方法为 POST
                    connection.setRequestMethod("POST");
                    // 设置连接超时时间和读取超时时间
                    connection.setConnectTimeout(10000);
                    connection.setReadTimeout(10000);
                    // 设置请求头
                    connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

                    // 构建请求体
                    String requestBody;
                    requestBody = "func=" + URLEncoder.encode(key, "UTF-8");
                    requestBody += "&uid=" + URLEncoder.encode(uid,"UTF-8");

                    // 获取输出流
                    OutputStream outputStream = connection.getOutputStream();
                    // 将请求体写入输出流
                    outputStream.write(requestBody.getBytes("UTF-8"));
                    // 关闭输出流
                    outputStream.close();

                    InputStream inputStream = connection.getInputStream();
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    Log.e("HttpClientUtil", "run: "+bitmap );

                    Message message = new Message();
                    message.what = SUCCESS;
                    message.obj = bitmap;
                    handler.sendMessage(message);
                } catch (IOException e) {
                    Log.e("", "Error: " + e.getMessage()); // 添加日志
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
