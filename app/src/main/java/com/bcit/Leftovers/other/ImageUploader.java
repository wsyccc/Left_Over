package com.bcit.Leftovers.other;

/**
 * Created by Siyuan on 2016/11/16.
 */

import android.os.AsyncTask;
import android.util.Log;

import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;

import javax.net.ssl.HttpsURLConnection;

public class ImageUploader extends AsyncTask<String, Void, String> {

    private static final String BOUNDARY = UUID.randomUUID().toString();// 边界标识、随机生成、数据分割线
    private static final String PREFIX = "--"; // 前缀
    private static final String LINE_END = "\r\n"; // 一行的结束标识
    private static final String CONTENT_TYPE = "multipart/form-data"; // 内容类型
    private static final String path_php = "https://wayneking.me/mongoDB/leftover_images/user_avatar/avatar_uploader.php";
    private int readTimeOut = 10 * 1000; // 读取超时
    private int connectTimeout = 10 * 1000; // 超时时间
    private static final String fileKey = "avatar";
    private String userName, email;
    /***
     * 请求使用多长时间
     */
    private static int requestTime = 0;
    private static final String CHARSET = "utf-8"; // 设置编码

    public ImageUploader(String userName, String email){
        this.userName = userName;
        this.email = email;
    }
    @Override
    public void onPreExecute() {

    }

    @Override
    public String doInBackground(String... params) {
        String result;
        requestTime = 0;

        long startRequestTime = System.currentTimeMillis();
        long responseTime;

        try {

            URL url = new URL(path_php);
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.setReadTimeout(readTimeOut);
            conn.setConnectTimeout(connectTimeout);
            conn.setDoInput(true); // 允许输入流
            conn.setDoOutput(true); // 允许输出流
            conn.setUseCaches(false); // 不允许使用缓存
            conn.setRequestMethod("POST"); // 请求方式
            conn.setRequestProperty("Charset", CHARSET); // 设置编码
            conn.setRequestProperty("connection", "keep-alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
            conn.setRequestProperty("Content-Type", CONTENT_TYPE + ";boundary="
                    + BOUNDARY);
            conn.setChunkedStreamingMode(128 * 1024);

            /**
             * 当文件不为空，把文件包装并且上传
             */
            DataOutputStream dos = new DataOutputStream(conn.getOutputStream());

            StringBuffer sb = new StringBuffer();

            /***
             * 以下是用于上传参数
             */

            if (params != null) {
                    sb.append(PREFIX).append(BOUNDARY).append(LINE_END);
                    sb.append("Content-Disposition: form-data; name=\"")
                            .append("filename").append("\"")
                            .append(LINE_END).append(LINE_END);
                    sb.append(email+"_"+userName).append(LINE_END);

            }

             // 写入参数信息
             dos.write(sb.toString().getBytes());

            /**
             * 构造要上传文件的前段参数内容，和普通参数一样，在这些设置后就可以紧跟文件内容了。 这里重点注意：
             * name里面的值为服务器端需要key 只有这个key 才可以得到对应的文件 filename是文件的名字，包含后缀名的
             * 比如:abc.png
             */

            if (params[0] != null) {
                for (String path : params) {
                    sb = new StringBuffer();
                    sb.append(PREFIX).append(BOUNDARY).append(LINE_END);
                    sb.append("Content-Disposition:form-data; name=\""
                            + fileKey + "\"; filename=\""
                            + path.substring(path.lastIndexOf("/") + 1)
                            + "\"" + LINE_END);
                    Log.d("dfadsfasdfas!!!!!!!!!!!", path.substring(path.lastIndexOf("/") + 1));
                    sb.append("Content-Type:image/*" + LINE_END); // 这里配置的Content-type很重要的
                    // ，用于服务器端辨别文件的类型的
                    sb.append(LINE_END);

                    // 写入文件前段参数信息
                    dos.write(sb.toString().getBytes());

                    // 写入文件数据
                    System.out.println("--文件--:" + path);
                    InputStream is = new FileInputStream(path);
                    byte[] bytes = new byte[1024];
                    int len;
                    int count = 0;
                    while ((len = is.read(bytes)) != -1) {
                        count += len;
                        dos.write(bytes, 0, len);
                    }

                    is.close();

                    // 这个很容易遗忘，刚开始一直不成功就是忘了写这个了
                    dos.write(LINE_END.getBytes());

                    System.out.println("--count--:" + count);
                }
            }

            // 请求结束符
            byte[] after = (PREFIX + BOUNDARY + PREFIX + LINE_END).getBytes();
            // 写结束符，代表该HTTP组包完毕
            dos.write(after);

            // 发送出去
            dos.flush();

            // 关闭流
            dos.close();

            /**
             * 获取响应码 200=成功 当响应成功，获取响应的流
             */
            int res = conn.getResponseCode();
            responseTime = System.currentTimeMillis();
            requestTime = (int) ((responseTime - startRequestTime));
            System.out.println("--请求时间--:" + requestTime);
            if (res == 200) {
                InputStream input = conn.getInputStream();
                StringBuffer sb1 = new StringBuffer();
                int ss;
                while ((ss = input.read()) != -1) {
                    sb1.append((char) ss);
                }
                result = sb1.toString();

                System.out.println("--result--:" + result);
                return result;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(getClass().getName(), e.getMessage());
            return null;
        }
        return null;
    }

    @Override
    public void onPostExecute(String result) {

    }


}
