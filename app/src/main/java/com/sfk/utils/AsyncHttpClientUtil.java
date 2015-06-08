package com.sfk.utils;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.io.File;

/**客户端异步请求服务器工具类
 * Created by WEI on 2015/5/20.
 */
public class AsyncHttpClientUtil {

    private static AsyncHttpClient client=new AsyncHttpClient();

    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.get(url, params, responseHandler);
    }

    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.post(url, params, responseHandler);
    }

    /**
     * 图片上传
     * @param path 图片路径
     * @param url 服务器接收url
     * @throws Exception
     */
    public static void upLoadFile(String path,String url) throws Exception{
        File file=new File(path);

    }

}
