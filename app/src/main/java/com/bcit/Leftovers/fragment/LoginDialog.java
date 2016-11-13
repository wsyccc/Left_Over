package com.bcit.Leftovers.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.bcit.Leftovers.R;

/**
 * Created by siyuanwang on 2016-11-12.
 */

public class LoginDialog extends DialogFragment{

    private static String email;
    private static String username;
    private static String password;
    private static String repassword;
    private static TextView error;
    private static View view;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.login_dialog, null);
        builder.setView(view)
                .setTitle(R.string.signup_title)
                .setPositiveButton(android.R.string.ok,null)
                .setNeutralButton(R.string.already_have_account,null)
                .setNegativeButton(android.R.string.cancel,null);
        return builder.create();
    }




}
