package com.bcit.Leftovers.other;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bcit.Leftovers.R;
import com.bumptech.glide.Glide;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Siyuan on 2016/12/5.
 */

public class CommentListAdapter extends BaseAdapter {

    private Activity activity;
    private List<Recipe.CommentBean> data;
    private LayoutInflater inflater;

    public CommentListAdapter(Activity activity, List<Recipe.CommentBean> data){
        this.activity = activity;
        this.data = data;
        inflater = LayoutInflater.from(this.activity);
    }
    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //View vi = convertView;
        View vi = inflater.inflate(R.layout.comment_list_adapter,null);
        TextView title = (TextView)vi.findViewById(R.id.title_comment);
        TextView email = (TextView)vi.findViewById(R.id.email_comment);
        ImageView icon = (ImageView)vi.findViewById(R.id.list_icon);
        TextView content = (TextView)vi.findViewById(R.id.content);
        ImageView rate = (ImageView)vi.findViewById(R.id.rate);
        Recipe.CommentBean one = data.get(position);
        if (one.getTitle() != null){
            title.setText(one.getTitle());
        }
        if (one.getEmail() != null){
            email.setText(one.getEmail());
        }
        if (one.getContent() != null){
            content.setText(one.getContent());
        }
        try {
            String avatar = new GetData().execute(one.getEmail()).get();
            Glide.with(activity)
                    .load(avatar)
                    .into(icon);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        switch (one.getRate()){
            case 0:
                rate.setImageResource(R.drawable.btn_favourite_lvl_1);
                break;
            case 1:
                rate.setImageResource(R.drawable.btn_favourite_lvl_2);
                break;
            case 2:
                rate.setImageResource(R.drawable.btn_favourite_lvl_3);
                break;
            case 3:
                rate.setImageResource(R.drawable.btn_favourite_lvl_4);
                break;
            case 4:
                rate.setImageResource(R.drawable.btn_favourite_lvl_5);
                break;
            default:
                rate.setImageResource(R.drawable.btn_favourite_lvl_1);
                break;
        }
        return vi;
    }

    private class GetData extends AsyncTask<String, Void, String>{

        private HttpsURLConnection connection;
        private String avatar = null;
        @Override
        protected String doInBackground(String... params) {
            String query = "collection=usersInfo" + "&email=" + params[0] + "&action=find";
            String result;
            PrintWriter out = null;
            BufferedReader in = null;
            try {
                URL url = new URL("https://wayneking.me/mongoDB/leftover_mongodb.php");
                this.connection = (HttpsURLConnection) url.openConnection();
                this.connection.setRequestProperty("connection", "Keep-Alive");
                this.connection.setConnectTimeout(5 * 1000);
                this.connection.setReadTimeout(5 * 1000);
                this.connection.setRequestMethod("POST");
                this.connection.setDoInput(true);
                this.connection.setDoOutput(true);
                this.connection.setUseCaches(false);
                this.connection.connect();
                out = new PrintWriter(this.connection.getOutputStream());
                out.print(query);
                out.flush();
                if (this.connection.getResponseCode() == 200) {
                    in = new BufferedReader(new InputStreamReader(this.connection.getInputStream()));
                    result = in.readLine();
                    JSONObject jsonObject = new JSONObject(result);
                    avatar = jsonObject.getString("avatar");
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
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
            return avatar;
        }
    }
}
