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

    private static final String BOUNDARY = UUID.randomUUID().toString();
    private static final String PREFIX = "--";
    private static final String LINE_END = "\r\n";
    private static final String CONTENT_TYPE = "multipart/form-data";
    private static final String path_php = "https://wayneking.me/mongoDB/leftover_images/user_avatar/avatar_uploader.php";
    private int readTimeOut = 10 * 1000;
    private int connectTimeout = 10 * 1000;
    private static final String fileKey = "avatar";
    private String userName, email;
    /***
     * 请求使用多长时间
     */
    private static int requestTime = 0;
    private static final String CHARSET = "utf-8"; 

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
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Charset", CHARSET);
            conn.setRequestProperty("connection", "keep-alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
            conn.setRequestProperty("Content-Type", CONTENT_TYPE + ";boundary="
                    + BOUNDARY);
            conn.setChunkedStreamingMode(128 * 1024);


            DataOutputStream dos = new DataOutputStream(conn.getOutputStream());

            StringBuffer sb = new StringBuffer();


            /**
             * upload params
             */
            if (params != null) {
                    sb.append(PREFIX).append(BOUNDARY).append(LINE_END);
                    sb.append("Content-Disposition: form-data; name=\"")
                            .append("filename").append("\"")
                            .append(LINE_END).append(LINE_END);
                    sb.append(email+"_"+userName).append(LINE_END);

            }


             dos.write(sb.toString().getBytes());

            /**
             * upload images
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
                    sb.append("Content-Type:image/*" + LINE_END);

                    sb.append(LINE_END);


                    dos.write(sb.toString().getBytes());


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


                    dos.write(LINE_END.getBytes());

                    System.out.println("--count--:" + count);
                }
            }


            byte[] after = (PREFIX + BOUNDARY + PREFIX + LINE_END).getBytes();

            dos.write(after);

            dos.flush();

            dos.close();


            int res = conn.getResponseCode();
            responseTime = System.currentTimeMillis();
            requestTime = (int) ((responseTime - startRequestTime));
            System.out.println("--Request Time--:" + requestTime);
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
