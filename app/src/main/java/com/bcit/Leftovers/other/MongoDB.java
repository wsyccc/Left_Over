package com.bcit.Leftovers.other;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;


/**
 * Created by Siyuan on 2016/10/30.
 */

public class MongoDB extends AsyncTask<String,Void,String>{

    private Context context;
    //private ProgressDialog pd;
    private HttpsURLConnection connection;

    public MongoDB(Context context){
        this.context = context;
    }
    @Override
    public void onPreExecute(){
//        this.pd = new ProgressDialog(this.context);
//        this.pd.setMessage("Please wait!");
//        this.pd.show();
    }
    @Override
    public String doInBackground(String... params){
        String result = "null";
        PrintWriter out = null;
        BufferedReader in = null;
        try {
            URL url = new URL("https://wayneking.me/mongoDB/leftover_mongodb.php");
            this.connection = (HttpsURLConnection) url.openConnection();
            this.connection.setRequestProperty("connection", "Keep-Alive");
            this.connection.setConnectTimeout(5*1000);
            this.connection.setReadTimeout(5*1000);
            this.connection.setRequestMethod("POST");
            this.connection.setDoInput(true);
            this.connection.setDoOutput(true);
            this.connection.setUseCaches(false);

            this.connection.connect();
            out = new PrintWriter(this.connection.getOutputStream());
            out.print(params[0]);
            out.flush();
            if (this.connection.getResponseCode() == 200) {
                in = new BufferedReader(new InputStreamReader(this.connection.getInputStream()));
                result = in.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    @Override
    public void onPostExecute(String result){
        this.connection.disconnect();
        //contribute progress dialog but doesn't work
//        try {
//            if (this.pd.isShowing()){
//                Thread.sleep(1000);
//                this.pd.dismiss();
//            }
//        }catch (Exception e){
//            e.printStackTrace();
//        }finally {
//            this.pd.dismiss();
//            this.connection.disconnect();
//        }
    }
}
