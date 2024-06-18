package edu.hebut215054.bighomework1;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class getComment {

    private static final int USER_GETPOST = 2;
    private static String TAG = "getComment";

    public static void SendByHttpClient(final String key, final Handler handler){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // 创建 URL 对象
                    URL url = new URL("http://192.168.31.92:9999/MyAndroidServer/getComment");
                    // 打开连接
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    // 设置请求方法为 POST
                    connection.setRequestMethod("POST");
                    // 设置连接超时时间和读取超时时间
                    connection.setConnectTimeout(5000);
                    connection.setReadTimeout(5000);
                    // 设置请求头
                    connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

                    // 构建请求体
                    String requestBody = "pid=" + URLEncoder.encode(key, "UTF-8");
                    // 获取输出流
                    OutputStream outputStream = connection.getOutputStream();
                    // 将请求体写入输出流
                    outputStream.write(requestBody.getBytes("UTF-8"));
                    // 关闭输出流
                    outputStream.close();

                    // 获取响应状态码
                    int responseCode = connection.getResponseCode();
                    Log.d("HttpClientUtil", "Response Code: " + responseCode); // 添加日志
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        // 读取响应数据
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                        StringBuilder stringBuilder = new StringBuilder();
                        String line;
                        while ((line = bufferedReader.readLine()) != null) {
                            stringBuilder.append(line);
                        }
                        // 关闭输入流
                        bufferedReader.close();
                        // 获取响应内容
                        String response = stringBuilder.toString();
                        Log.d("getComment", "Response: " + response); // 添加日志

                        // 解析JSON数组
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
//                                int uid = jsonObject.getInt("uid");
//                                String username = jsonObject.getString("uname");
//                                String ptitle = jsonObject.getString("ptitle");
//                                String pcontext = jsonObject.getString("pcontext");
//                                int plike = jsonObject.getInt("plike");
//                                int pid = jsonObject.getInt("pid");

                                // 在这里处理解析出的内容，例如将其显示在UI上
//                                Log.d("post", "uid: " + uid);
//                                Log.d("post", "ptitle: " + ptitle);
//                                Log.d("post", "pcontext: " + pcontext);
//                                Log.d("post", "plike: " + plike);
//                                Log.d("post", "pid: " + pid);
                            }

                            // 使用 Handler 发送消息给 UI 线程
                            Message message = new Message();
                            message.what = USER_GETPOST;
                            message.obj = response;
                            handler.sendMessage(message);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            // JSON解析失败，处理异常
                        }
                    } else {
                        Log.e(TAG, "Error Response Code: " + responseCode); // 添加日志
                    }
                } catch (IOException e) {
                    Log.e(TAG, "Error: " + e.getMessage()); // 添加日志
                    e.printStackTrace();
                }
            }
        }).start();
    }

}
