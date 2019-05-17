package com.example.chen.simpleparkingapp.utils;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.example.chen.taco.networkaccessor.NetworkResponse;
import com.example.chen.taco.networkservice.ServiceMediator;
import com.example.chen.taco.networkservice.ServiceResponse;
import com.example.chen.taco.networkservice.ServiceUtils;
import com.example.chen.taco.parser.JsonHandler;
import com.google.gson.reflect.TypeToken;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class UploadFileUtil {
    public static final int UPLOAD_SUCCESS = 0x123;
    public static final int UPLOAD_FAIL = 0x124;
    private Handler handler;
    public static List<String> result = new ArrayList<String>();
    private int code;
    private String message;

    public UploadFileUtil(Handler handler) {
        this.handler = handler;
    }

    /**
     * 请求上传的文件类型fileTypes如：“.jpg.png.docx”
     *
     * @param files 要上传的文件集合
     */
    public void uploadFileToServer(final String url, final ArrayList<File> files, final HashMap<String, String> params, final String fileParam) {
        // TODO Auto-generated method stub
        new Thread(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                try {
                    if (uploadFiles(url, files, params, fileParam)) {
                        Message msg = new Message();
                        String resultStr = "";
                        if (result != null && result.size() > 0) {
                            StringBuffer strBuffer = new StringBuffer();

                            for (int i = 0; i < result.size(); i++) {
                                strBuffer.append(result.get(i));
                                strBuffer.append(" ");
                            }
                            resultStr = strBuffer.toString().trim();
                        }
                        Bundle bundle = new Bundle();
                        bundle.putString("result", resultStr);
                        bundle.putInt("code", code);
                        bundle.putString("message", message);
                        msg.setData(bundle);
                        if (code == 0) {
                            msg.what = UPLOAD_SUCCESS;
                        } else {
                            msg.what = UPLOAD_FAIL;
                        }
                        handler.sendMessage(msg);//通知主线程数据发送成功
                    } else {
                        //将数据发送给服务器失败
                        handler.sendEmptyMessage(UPLOAD_FAIL);
                    }
                } catch (Exception e) {
                    handler.sendEmptyMessage(UPLOAD_FAIL);
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * @param url       servlet的地址
     * @param files     要上传的文件
     * @param fileParam
     * @return true if upload success else false
     * @throws ClientProtocolException
     * @throws IOException
     */
    private boolean uploadFiles(String url, ArrayList<File> files, HashMap<String, String> params, String fileParam) throws ClientProtocolException, IOException {
        HttpClient client = new DefaultHttpClient();// 开启一个客户端 HTTP 请求
        HttpPost post = new HttpPost(url);//创建 HTTP POST 请求

        MultipartEntity mutiEntity = new MultipartEntity();
        int count = 0;
        for (File file : files) {
            mutiEntity.addPart(fileParam, new FileBody(file));
            count++;
        }
        String requestUrl = "";
        Set<String> keys = params.keySet();
        for (String key : keys) {
            String value = params.get(key);
            mutiEntity.addPart(key, new StringBody(value, Charset.forName("UTF-8")));
            requestUrl += "&" + key + "=" + value;
        }

        Log.e("url", url + requestUrl);
        post.setEntity(mutiEntity);//设置请求参数
        HttpResponse response = client.execute(post);// 发起请求 并返回请求的响应
        if (response.getStatusLine().getStatusCode() == 200) {
            String resultString = EntityUtils.toString(response.getEntity());
            ServiceResponse<List<String>> reResponse = new ServiceResponse<List<String>>();
            NetworkResponse resultResponse = new NetworkResponse();
            resultResponse.setWithData(resultString,
                    HttpURLConnection.HTTP_OK, null);
            Log.i("info", resultString);
            try {
                JSONObject jsonObject = new JSONObject(resultString);
                code = jsonObject.optInt("code");
                message = jsonObject.optString("message");
                String responseStr = ServiceUtils.getRequestResult(reResponse,
                        resultResponse);
                if (reResponse.getReturnCode() == ServiceMediator.Service_Return_Success) {
                    // 返回成功 解析结果
                    Type type = new TypeToken<List<String>>() {
                    }.getType();
                    JsonHandler.jsonToList(responseStr, type, reResponse);
                }
                result = (ArrayList<String>) reResponse.getResponse();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return true;
        }
        return false;
    }


}
