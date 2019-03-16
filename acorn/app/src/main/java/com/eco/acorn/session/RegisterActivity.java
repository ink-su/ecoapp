package com.eco.acorn.session;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.eco.acorn.R;

/**
 * Created by Christophe Gaboury on 18/09/2016. Copyright (c) 2016
 */
public class RegisterActivity extends AppCompatActivity {


    private EditText mUsernameField;
    private EditText mEmailField;
    private EditText mPasswordField;
    private EditText mConfirmPasswordField;
    private TextView mErrorText;
    private Button mRegisterButton;
    private ProgressBar mProgressBar;



    @Override
    public void onCreate(Bundle SavedInstances){
        super.onCreate(SavedInstances);
        setContentView(R.layout.activity_register);

        mUsernameField = findViewById(R.id.username_field);
        mEmailField = findViewById(R.id.email_field);
        mPasswordField = findViewById(R.id.password_field);
        mPasswordField.setTypeface(Typeface.DEFAULT);
        mConfirmPasswordField = findViewById(R.id.password_confirm_field);
        mConfirmPasswordField.setTypeface(Typeface.DEFAULT);
        mErrorText = findViewById(R.id.error_text);
        mRegisterButton = findViewById(R.id.register_button);
        mProgressBar = findViewById(R.id.loading_icon);
        mConfirmPasswordField.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    onRegister(null);
                }
                return false;
            }
        });

    }


    /**
     * Display an error message
     */
    public void displayErrorMessage(){
        mErrorText.setText(R.string.register_error);
        mErrorText.setVisibility(View.VISIBLE);
    }
    public void displayErrorMessage(String message){
        mErrorText.setText(message);
        mErrorText.setVisibility(View.VISIBLE);
    }
    public void onRegister(View v){

        if(mUsernameField.getText().toString().equals("")){
            mErrorText.setText(R.string.invalid_username);
            mErrorText.setVisibility(View.VISIBLE);
            return;
        }
        if(mEmailField.getText().toString().equals("")){
            mErrorText.setText(R.string.invalid_email);
            mErrorText.setVisibility(View.VISIBLE);
            return;
        }
        if(mPasswordField.getText().toString().equals("")){
            mErrorText.setText(R.string.invalid_password);
            mErrorText.setVisibility(View.VISIBLE);
            return;
        }
        if(!mConfirmPasswordField.getText().toString().equals(mPasswordField.getText().toString())){
            mErrorText.setText(R.string.invalid_password_confirm);
            mErrorText.setVisibility(View.VISIBLE);
            return;
        }
        startLoading();
        String username = mUsernameField.getText().toString();
        String password = mPasswordField.getText().toString();
        String email = mEmailField.getText().toString();

        Toast.makeText(RegisterActivity.this,"Account Registered",Toast.LENGTH_LONG).show();
        finish();
//        new LoginAndGetSession().execute(username,password,email);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(RegisterActivity.this,"Account Registered",Toast.LENGTH_LONG).show();
                Intent data = new Intent();
//                String text = "Result to be returned....";
//                data.setData(Uri.parse(text));
                setResult(RESULT_OK,data);
                finish();
            }
        }, 1500);
    }

    public void startLoading(){
        mErrorText.setVisibility(View.INVISIBLE);
        mProgressBar.setVisibility(View.VISIBLE);
        mRegisterButton.setVisibility(View.GONE);
        mUsernameField.setEnabled(false);
        mPasswordField.setEnabled(false);
        mEmailField.setEnabled(false);
        mConfirmPasswordField.setEnabled(false);
    }
    public void stopLoading(){
        mErrorText.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.GONE);
        mRegisterButton.setVisibility(View.VISIBLE);
        mUsernameField.setEnabled(true);
        mPasswordField.setEnabled(true);
        mEmailField.setEnabled(true);
        mConfirmPasswordField.setEnabled(true);
    }

}