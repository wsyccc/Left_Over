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

    private static Context context;
    private ProgressDialog pd;
    private HttpsURLConnection connection;

    public MongoDB(Context context){
        this.context = context;
    }
    @Override
    public void onPreExecute(){
        pd = new ProgressDialog(context);
        pd.setMessage("Please wait!");
        pd.show();
    }
    @Override
    public String doInBackground(String... params){
        String result = "null";
        PrintWriter out = null;
        BufferedReader in = null;
        try {
            URL url = new URL("https://wayneking.me/mongoDB/leftover_mongodb.php");
            connection = (HttpsURLConnection) url.openConnection();
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setConnectTimeout(5*1000);
            connection.setReadTimeout(5*1000);
            connection.setRequestMethod("POST");
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setUseCaches(false);

            connection.connect();
            out = new PrintWriter(connection.getOutputStream());
            out.print(params[0]);
            out.flush();
            if (connection.getResponseCode() == 200) {
                in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
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
        try {
            if (pd.isShowing()){
                pd.dismiss();
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            pd.dismiss();
            connection.disconnect();
        }
    }
}
