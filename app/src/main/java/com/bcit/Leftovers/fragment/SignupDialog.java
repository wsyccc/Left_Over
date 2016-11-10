package com.bcit.Leftovers.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.bcit.Leftovers.R;
import com.bcit.Leftovers.other.MongoDB;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    private static final MongoDB db = new MongoDB();

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.signup_dialog, null);
        builder.setView(view)
                .setTitle(R.string.signup_title)
                .setPositiveButton(android.R.string.ok,null)
                .setNeutralButton(R.string.already_have_account,null)
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
                    EditText email_txt = (EditText) view.findViewById(R.id.txt_email);
                    EditText username_txt = (EditText) view.findViewById(R.id.txt_username);
                    EditText password_txt = (EditText) view.findViewById(R.id.txt_password);
                    EditText repassword_txt = (EditText) view.findViewById(R.id.txt_repassword);
                    error = (TextView) view.findViewById(R.id.txt_signuperror);

                    email = email_txt.getText().toString();
                    username = username_txt.getText().toString();
                    password = password_txt.getText().toString();
                    repassword = repassword_txt.getText().toString();
                    if (email.isEmpty() || username.isEmpty() || password.isEmpty() || repassword.isEmpty()){
                        error.setText(R.string.error_field_required);
                        error.setVisibility(View.VISIBLE);
                    }else if (password.compareToIgnoreCase(repassword) != 0){
                        error.setText(R.string.not_match_pwd);
                        error.setVisibility(View.VISIBLE);
                    }else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                        error.setText(R.string.error_invalid_email);
                        error.setVisibility(View.VISIBLE);
                    }else if(password.length() < 8){
                        error.setText(R.string.error_invalid_password);
                        error.setVisibility(View.VISIBLE);
                    }else if (!existUser(email)){
                        error.setText(R.string.user_exist_error);
                        error.setVisibility(View.VISIBLE);
                    }
                    else if (!storeData(email,username,password)) {
                        error.setText(R.string.server_error);
                        error.setVisibility(View.VISIBLE);
                    }else {
                        ProgressDialog pd = new ProgressDialog(getActivity());
                        pd.setMessage("Please wait!");
                        pd.show();
                    }
                }
            });
        }
    }
    public boolean existUser(String email){
        if (!db.mongoConnect()){
            error.setText(R.string.server_error);
            error.setVisibility(View.VISIBLE);
        }else if(db.find("usersInfo","email",email) != null){
            return false;
        }else {
            return true;
        }
        return false;
    }
    public boolean storeData(String email, String username, String password){
        if (!db.mongoConnect()){
            error.setText(R.string.server_error);
            error.setVisibility(View.VISIBLE);
        }else{
            String json = "{'email':'"+email+"','username':'"+username+"','password':'"+password+"'}";
            if (!db.insertValue("usersInfo",json)){
                error.setText(R.string.insert_error);
                error.setVisibility(View.VISIBLE);
            }else {
                return true;
            }
        }
        return false;
    }

    public static boolean isValidPassword(final String password) {

        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();

    }
}
