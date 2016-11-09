package com.bcit.Leftovers.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.bcit.Leftovers.R;

/**
 * Created by Siyuan on 2016/11/8.
 */

public class SignupDialog extends DialogFragment{
    private static String email;
    private static String username;
    private static String password;
    private static String repassword;
    private static TextView error;
    private static View view;
    private AlertDialog.Builder builder;

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.signup_dialog, null);
        builder.setView(view)
                .setTitle("Signup")
                .setPositiveButton(android.R.string.ok,null)
                .setNeutralButton("already have an account?Login",null)
                .setNegativeButton(android.R.string.cancel,null);
        return builder.create();
    }
    @Override
    public void onStart(){
        super.onStart();
        final AlertDialog dialog = (AlertDialog)getDialog();
        if (dialog != null){
            Button positiveButton = dialog.getButton(Dialog.BUTTON_POSITIVE);
            positiveButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    Boolean flag = false;
                    EditText email_txt = (EditText) view.findViewById(R.id.txt_email);
                    EditText username_txt = (EditText) view.findViewById(R.id.txt_username);
                    EditText password_txt = (EditText) view.findViewById(R.id.txt_password);
                    EditText repassword_txt = (EditText) view.findViewById(R.id.txt_repassword);
                    error = (TextView) view.findViewById(R.id.txt_signuperror);

                    email = email_txt.getText().toString();
                    username = username_txt.getText().toString();
                    password = password_txt.getText().toString();
                    repassword = repassword_txt.getText().toString();
                    if (password.compareToIgnoreCase(repassword) != 0){
                        error.setText("Your password not match!");
                        error.setVisibility(View.VISIBLE);
                    }else{
                        error.setVisibility(View.INVISIBLE);
                        if (!storeData(email,username,password)){
                            Log.d("", "疼死老子了！！");
                            error.setText("Server Error");
                            error.setVisibility(View.VISIBLE);
                        }else{
                            ProgressDialog pd = new ProgressDialog(getActivity());
                            pd.setMessage("Please wait!");
                            pd.show();
                        }
                    }
                    if (flag){
                        dialog.dismiss();
                    }
                }
            });
        }
    }
    public boolean storeData(String email, String username, String password){
        return false;
    }
}
