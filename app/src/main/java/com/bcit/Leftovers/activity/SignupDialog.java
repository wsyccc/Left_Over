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

public class SignupDialog extends DialogFragment {

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.signup_dialog, null);
        builder.setView(view)
                .setTitle("Signup")
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText email_txt = (EditText) view.findViewById(R.id.txt_email);
                        EditText username_txt = (EditText) view.findViewById(R.id.txt_username);
                        EditText password_txt = (EditText) view.findViewById(R.id.txt_password);
                        EditText repassword_txt = (EditText) view.findViewById(R.id.txt_repassword);
                        TextView error = (TextView) view.findViewById(R.id.txt_signuperror);

                        String email = email_txt.getText().toString();
                        String username = username_txt.getText().toString();
                        String password = password_txt.getText().toString();
                        String repassword = repassword_txt.getText().toString();
                        if (password.compareToIgnoreCase(repassword) != 0){
                            error.setText("Your email or password not correct!");
                            error.setVisibility(View.VISIBLE);
                        }else{
                            error.setVisibility(View.INVISIBLE);
                            if (!storeData(email,username,password)){
                                Log.d("", "疼死老子了！！");
                                error.setText("Server Error");
                                error.setVisibility(View.VISIBLE);
                                errorFlag = false;
                            }else{
                                ProgressDialog pd = new ProgressDialog(getActivity());
                                pd.setMessage("Please wait!");
                                pd.show();
                                errorFlag = true;
                            }
                        }
                    }
                })
                .setNeutralButton("Login", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }

                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });

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
                    if (errorFlag){
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
