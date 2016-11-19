package com.bcit.Leftovers.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.bcit.Leftovers.R;
import com.bcit.Leftovers.activity.MainActivity;
import com.bcit.Leftovers.other.Encryption;
import com.bcit.Leftovers.other.Login;
import com.bcit.Leftovers.other.MongoDB;
import com.bcit.Leftovers.other.SaveSharedPreference;

import org.json.JSONObject;

/**
 * Created by siyuanwang on 2016-11-12.
 */

public class LogIn_Dialog extends DialogFragment {

    private static String email;
    private static String password;
    private static TextView error;
    private static View view;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.login_dialog, null);
        builder.setView(view)
                .setTitle(R.string.login_tile)
                .setPositiveButton(android.R.string.ok, null)
                .setNeutralButton(R.string.no_account, null)
                .setNegativeButton(android.R.string.cancel, null);
        return builder.create();
    }

    @Override
    public void onStart() {
        super.onStart();
        final AlertDialog dialog = (AlertDialog) getDialog();
        if (dialog != null) {
            Button neutralButton = dialog.getButton(Dialog.BUTTON_NEUTRAL);
            neutralButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.cancel();
                    SignUp_Dialog signupDialog = new SignUp_Dialog();
                    signupDialog.show(getFragmentManager(), "Signup");
                }
            });
            Button positiveButton = dialog.getButton(Dialog.BUTTON_POSITIVE);
            positiveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EditText email_txt = (EditText) view.findViewById(R.id.login_txt_email);
                    EditText password_txt = (EditText) view.findViewById(R.id.login_txt_password);
                    error = (TextView) view.findViewById(R.id.loginerror);
                    email = email_txt.getText().toString();
                    password = password_txt.getText().toString();

                    try {
                        error.setVisibility(View.INVISIBLE);
                        if (email.isEmpty() || password.isEmpty()) {
                            error.setText(R.string.error_field_required);
                            error.setVisibility(View.VISIBLE);
                        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                            error.setText(R.string.error_invalid_email);
                            error.setVisibility(View.VISIBLE);
                        } else if (password.length() < 8) {
                            error.setText(R.string.error_invalid_password);
                            error.setVisibility(View.VISIBLE);
                        } else if (!findUser(email, password)) {
                            error.setText(R.string.incorrect);
                            error.setVisibility(View.VISIBLE);
                        } else {
                            dialog.dismiss();
                            final ProgressDialog pd = new ProgressDialog(getActivity());
                            pd.setMessage("Please Wait!");
                            if (!(new Login(email, getActivity()).login())) {
                                error.setText(R.string.server_error);
                                error.setVisibility(View.VISIBLE);
                                pd.dismiss();
                            } else {
                                final AlertDialog.Builder ab = new AlertDialog.Builder(getActivity())
                                        .setTitle("Welcome " + SaveSharedPreference.getUser(getActivity(), "userName"))
                                        .setMessage(R.string.login_success)
                                        .setNegativeButton(android.R.string.ok, null);
                                pd.show();
                                final Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        pd.dismiss();
                                        ab.show();
                                    }
                                }, 500);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("IOException", e.getMessage());
                    }
                }
            });
        }
    }

    public boolean findUser(String email, String password) {
        String json = "email=" + email + "&collection=usersInfo" + "&action=find";
        MongoDB mongoDB = new MongoDB(getActivity());
        String result;
        String actual_email;
        String encrypt_password;
        String decrypt_password;
        try {
            result = mongoDB.execute(json).get();
            Log.d("result", result);
            if (result.equalsIgnoreCase("null")) {
                return false;
            } else {
                JSONObject jsonObject = new JSONObject(result);
                actual_email = jsonObject.getString("email");
                encrypt_password = jsonObject.getString("password");
                decrypt_password = Encryption.decrypt(actual_email, encrypt_password);
                if (password.equals(decrypt_password)) {
                    return true;
                } else {
                    return false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


}
