package com.bcit.Leftovers.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.bcit.Leftovers.R;
import com.bcit.Leftovers.other.Login;
import com.bcit.Leftovers.other.SaveSharedPreference;
import com.beardedhen.androidbootstrap.BootstrapButtonGroup;
import com.beardedhen.androidbootstrap.BootstrapEditText;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.util.concurrent.ExecutionException;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Siyuan on 2016/12/6.
 */

public class CommentDialog extends DialogFragment {

    private static View view;
    private String title;
    private String content;
    private int rate;
    private int recipeID;

    public CommentDialog(int recipeID){
        this.recipeID = recipeID;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.comment_dialog, null);
        builder.setView(view)
                .setTitle("Comment")
                .setPositiveButton(android.R.string.ok, null)
                .setNeutralButton("Reset", null)
                .setNegativeButton(android.R.string.cancel, null)
                .setCancelable(false);
        return builder.create();
    }
    @Override
    public void onStart() {
        super.onStart();
        final BootstrapEditText title_view = (BootstrapEditText)view.findViewById(R.id.comment_title_text);
        final BootstrapButtonGroup group_view = (BootstrapButtonGroup) view.findViewById(R.id.button_group);
        final BootstrapEditText content_view = (BootstrapEditText) view.findViewById(R.id.comment_content_text);
        final AlertDialog dialog = (AlertDialog) getDialog();
        if (dialog != null) {
            Button neutralButton = dialog.getButton(Dialog.BUTTON_NEUTRAL);
            neutralButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    title_view.setText("");
                    content_view.setText("");
                    group_view.check(0);
                }
            });
            Button positiveButton = dialog.getButton(Dialog.BUTTON_POSITIVE);
            positiveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    title = title_view.getText().toString();
                    content = content_view.getText().toString();
                    rate = group_view.getBottom();
                    String query = "collection=recipe" + "&title=" + title + "&content=" + content
                            + "&rate=" + rate + "&email="+ SaveSharedPreference.getUser(getActivity(), "email")
                            +"&action=findAndInsert"+"&recipeID="+ recipeID;
                    try {
                        String result = new GetData().execute(query).get();
                        if (result != null || !(result.equalsIgnoreCase("null"))){
                            dialog.dismiss();
                            final AlertDialog.Builder ab = new AlertDialog.Builder(getActivity())
                                    .setTitle("Thank you!")
                                    .setMessage("Your comment is valuable for cookers!")
                                    .setPositiveButton(android.R.string.ok, null);
                            ab.show();
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }
    private class GetData extends AsyncTask<String, Void, String> {

        private HttpsURLConnection connection;
        @Override
        protected String doInBackground(String... params) {
            String result = "null";
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
                out.print(params[0]);
                out.flush();
                if (this.connection.getResponseCode() == 200) {
                    in = new BufferedReader(new InputStreamReader(this.connection.getInputStream()));
                    result = in.readLine();
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
            return result;
        }
    }
}
