package com.gdkm.sfk.utils;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.gdkm.sfk.constant.Constant;
import com.gdkm.sfk.pojo.Photo;
import com.gdkm.sfk.utils.coreProgress.ProgressHelper;
import com.gdkm.sfk.utils.coreProgress.ProgressRequestListener;
import com.gdkm.sfk.utils.coreProgress.ProgressResponseListener;
import com.gdkm.sfk.utils.coreProgress.UIProgressRequestListener;
import com.gdkm.sfk.utils.coreProgress.UIProgressResponseListener;
import com.google.gson.Gson;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2015/9/19.
 */
public class PostMultipart {
    /**
     * The imgur client ID for OkHttp recipes. If you're using imgur for anything
     * other than running these examples, please request your own client ID!
     * https://api.imgur.com/oauth2
     */
    private Context context;
    private OnFileStatusListener onFileStatusListener;
    private OkHttpClient client;
    private ProgressBar uploadProgress;
    private static final String IMGUR_CLIENT_ID = "9199fdef135c122";
    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");
    private ProgressRequestListener progressListener;
    private UIProgressRequestListener uiProgressRequestListener;

    private Map<String,Object> dataMap;

    public OnFileStatusListener getOnFileStatusListener() {
        return onFileStatusListener;
    }

    public void setOnFileStatusListener(OnFileStatusListener onFileStatusListener) {
        this.onFileStatusListener = onFileStatusListener;

    }

    public PostMultipart(Context context, ProgressBar uploadProgress) {
        this.context = context;
        this.uploadProgress = uploadProgress;
        client = new OkHttpClient();
        dataMap = new HashMap<String, Object>();
        initClient();
        initProgressListener();
    }

    private void initProgressListener() {
        //这个是非ui线程回调，不可直接操作UI
        progressListener = new ProgressRequestListener() {
            @Override
            public void onRequestProgress(long bytesWrite, long contentLength, boolean done) {
                Log.e("TAG", "bytesWrite:" + bytesWrite);
                Log.e("TAG", "contentLength" + contentLength);
                Log.e("TAG", (100 * bytesWrite) / contentLength + " % done ");
                Log.e("TAG", "done:" + done);
                Log.e("TAG", "================================");
            }
        };


        //这个是ui线程回调，可直接操作UI
        uiProgressRequestListener = new UIProgressRequestListener() {
            @Override
            public void onUIRequestProgress(long bytesWrite, long contentLength, boolean done) {
                Log.e("TAG", "bytesWrite:" + bytesWrite);
                Log.e("TAG", "contentLength" + contentLength);
                Log.e("TAG_number", (100 * bytesWrite) / contentLength + " % done ");
                Log.e("TAG_done", "done:" + done);
                Log.e("TAG", "================================");
                //ui层回调
                int number = (int) ((100 * bytesWrite) / contentLength);
                uploadProgress.setProgress(number);
                if (number == 100){
                    Log.e("TAGOK", number+"");
//                    uploadProgress.setProgress(0);
                    onFileStatusListener.onFileStatusListener(done);
                }
//                Toast.makeText(context, bytesWrite + " " + contentLength + " " + done, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onUIRequestStart(long bytesWrite, long contentLength, boolean done) {
                super.onUIRequestStart(bytesWrite, contentLength, done);
//                Toast.makeText(context, "start", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onUIRequestFinish(long bytesWrite, long contentLength, boolean done) {
                super.onUIRequestFinish(bytesWrite, contentLength, done);
                Toast.makeText(context,"end",Toast.LENGTH_SHORT).show();
            }
        };
    }

    public PostMultipart() {
        client = new OkHttpClient();
        initClient();
    }

    public PostMultipart(Context context) {
        client = new OkHttpClient();
        initClient();
        this.context = context;
    }

    public void addFormDataPart(String name,Object value){
        dataMap.put(name,value);
    }

    /**
     * 上传文件
     * @param url   服务器端上传路径
     * @param files  文件集合
     */
    public void uploadFile(String url,File[] files) {
        //构造上传请求，类似web表单
//        RequestBody requestBody = new MultipartBuilder()
//                .type(MultipartBuilder.FORM)
//                .addFormDataPart("sfPhoto", "Square Logo")
//                .addFormDataPart("photo", file.getName(),RequestBody.create(MEDIA_TYPE_PNG, file))
//                .build();
        MultipartBuilder multipartBuilder = new MultipartBuilder()
                .type(MultipartBuilder.FORM);
        for(Iterator<String> keys = dataMap.keySet().iterator(); keys.hasNext();){
            String key = keys.next();
            String value = dataMap.get(key)+"";
            System.out.println("----dataMap----"+key+","+value);
            multipartBuilder.addFormDataPart(key,value);
        }
        for (File file : files){
//            multipartBuilder.addPart("photo",new FileBody())
            multipartBuilder.addFormDataPart("photo", file.getName(), RequestBody.create(MEDIA_TYPE_PNG, file));
        }

        RequestBody requestBody = multipartBuilder.build();

        //进行包装，使其支持进度回调
        Request request = new Request.Builder()
                .header("Authorization", "Client-ID " + IMGUR_CLIENT_ID)
                .url(url)
                .post(ProgressHelper.addProgressRequestListener(requestBody, uiProgressRequestListener))
//                .post(requestBody)
                .build();
        //开始请求
        Log.e("request_body", requestBody.contentType().type());
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Log.e("TAGERROR", "error ", e);
            }

            @Override
            public void onResponse(Response response) throws IOException {
                StringBuffer sb = new StringBuffer();
                BufferedReader br = new BufferedReader(response.body().charStream());
                for (String s = br.readLine(); s != null; s = br
                        .readLine()) {
                    sb.append(s);
                }
                Log.e("TAG", sb.toString());
            }
        });


    }

    /**
     * 下载文件
     * @param path
     * @param downloadProgress
     */
    private void download(String path, final ProgressBar downloadProgress) {
        //这个是非ui线程回调，不可直接操作UI
        final ProgressResponseListener progressResponseListener = new ProgressResponseListener() {
            @Override
            public void onResponseProgress(long bytesRead, long contentLength, boolean done) {
                Log.e("TAG", "bytesRead:" + bytesRead);
                Log.e("TAG", "contentLength:" + contentLength);
                Log.e("TAG", "done:" + done);
                if (contentLength != -1) {
                    //长度未知的情况下回返回-1
                    Log.e("TAG", (100 * bytesRead) / contentLength + "% done");
                }
                Log.e("TAG", "================================");
            }
        };


        //这个是ui线程回调，可直接操作UI
        final UIProgressResponseListener uiProgressResponseListener = new UIProgressResponseListener() {
            @Override
            public void onUIResponseProgress(long bytesRead, long contentLength, boolean done) {
                Log.e("TAG", "bytesRead:" + bytesRead);
                Log.e("TAG", "contentLength:" + contentLength);
                Log.e("TAG", "done:" + done);
                if (contentLength != -1) {
                    //长度未知的情况下回返回-1
                    Log.e("TAG", (100 * bytesRead) / contentLength + "% done");
                }
                Log.e("TAG", "================================");
                //ui层回调
                downloadProgress.setProgress((int) ((100 * bytesRead) / contentLength));
                //Toast.makeText(getApplicationContext(), bytesRead + " " + contentLength + " " + done, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onUIResponseStart(long bytesRead, long contentLength, boolean done) {
                super.onUIResponseStart(bytesRead, contentLength, done);
                Toast.makeText(context,"start",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onUIResponseFinish(long bytesRead, long contentLength, boolean done) {
                super.onUIResponseFinish(bytesRead, contentLength, done);
                Toast.makeText(context,"end",Toast.LENGTH_SHORT).show();
            }
        };

        //构造请求
        final Request request1 = new Request.Builder()
                .url(path)
                .build();

        //包装Response使其支持进度回调
        ProgressHelper.addProgressResponseListener(client, uiProgressResponseListener).newCall(request1).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Log.e("TAG", "error ", e);
            }

            @Override
            public void onResponse(Response response) throws IOException {
                Log.e("TAG", response.body().string());
            }
        });
    }

    //设置超时，不设置可能会报异常
    private void initClient() {
        client.setConnectTimeout(1000, TimeUnit.MINUTES);
        client.setReadTimeout(1000, TimeUnit.MINUTES);
        client.setWriteTimeout(1000, TimeUnit.MINUTES);
    }

    public interface OnFileStatusListener{
        public void onFileStatusListener(boolean status);
    }

}
