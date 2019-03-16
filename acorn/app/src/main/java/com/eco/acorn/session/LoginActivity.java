package com.eco.acorn.session;

import android.content.Intent;
import android.graphics.Typeface;
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


import com.eco.acorn.MainActivity;
import com.eco.acorn.R;


/**
 * Created by Christophe Gaboury on 16/09/2016. Copyright (c) 2016
 */
public class LoginActivity extends AppCompatActivity {

    private EditText mUserNameField;
    private EditText mPasswordField;
    private Button mLoginButton;
    private TextView mRegisterButton;
    private TextView mErrorText;
    private ProgressBar mLoadingIcon;
    private ProgressBar mBigLoadingIcon;


    @Override
    public void onCreate(Bundle savedInstances) {
        super.onCreate(savedInstances);

        //Set the UI
        setContentView(R.layout.activity_login);
        mUserNameField = findViewById(R.id.username_field);
        mPasswordField = findViewById(R.id.password_field);
        mPasswordField.setTypeface(Typeface.DEFAULT);
        mLoginButton = findViewById(R.id.login_button);
        mRegisterButton = findViewById(R.id.register_button);
        mErrorText = findViewById(R.id.error_text);
        mLoadingIcon = findViewById(R.id.loading_icon);
        mBigLoadingIcon = findViewById(R.id.bigLoadingIcon);

        //Allows the user to register when they are done typing
        mPasswordField.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    onLogin(null);
                }
                return false;
            }
        });

    }

    @Override
    public void onStart(){
        super.onStart();
        startLogin();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 123) {
            if (resultCode == RESULT_OK) {
                completeLogin();
            }
        }
    }
    /**
     * Function that triggers when a user has logged in
     */
    public void onLogin(View v) {
        String username = mUserNameField.getText().toString();
        String password = mPasswordField.getText().toString();
        if (username.equals("")) {
            mErrorText.setText(R.string.error_text);
            mErrorText.setVisibility(View.VISIBLE);
            return;
        }
        if (password.equals("")) {
            mErrorText.setText(R.string.error_text);
            mErrorText.setVisibility(View.VISIBLE);
            return;
        }
        mErrorText.setVisibility(View.INVISIBLE);
        startLoading();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                completeLogin();
            }
        }, 1500);
    }

    /**
     *  Complete login
     */
    public void completeLogin() {
        finish();
        overridePendingTransition(0, 0);
        Intent intent = new Intent(LoginActivity.this,MainActivity.class);
        startActivity(intent);
    }
    /**
     * Display an error message
     */
    public void displayErrorMessage() {
        mErrorText.setVisibility(View.VISIBLE);

    }

    /**
     * Function that triggers when a user has tried registering
     * Start a new activity?
     *
     * @param v
     */
    public void onRegister(View v) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivityForResult(intent, 123);
    }


    public void startCheckAnim(){
        mBigLoadingIcon.setVisibility(View.VISIBLE);
        mLoadingIcon.setVisibility(View.GONE);
        mErrorText.setVisibility(View.INVISIBLE);
        mUserNameField.setVisibility(View.GONE);
        mLoginButton.setVisibility(View.GONE);
        mPasswordField.setVisibility(View.GONE);
        mRegisterButton.setVisibility(View.GONE);
        mUserNameField.setEnabled(true);
        mPasswordField.setEnabled(true);
        mRegisterButton.setEnabled(true);
    }

    public void startLogin(){
        mBigLoadingIcon.setVisibility(View.GONE);
        mLoadingIcon.setVisibility(View.GONE);
        mErrorText.setVisibility(View.INVISIBLE);
        mUserNameField.setVisibility(View.VISIBLE);
        mLoginButton.setVisibility(View.VISIBLE);
        mPasswordField.setVisibility(View.VISIBLE);
        mRegisterButton.setVisibility(View.VISIBLE);
        mUserNameField.setEnabled(true);
        mPasswordField.setEnabled(true);
        mRegisterButton.setEnabled(true);

    }
    public void startLoading() {
        mLoadingIcon.setVisibility(View.VISIBLE);
        mErrorText.setVisibility(View.INVISIBLE);
        mLoginButton.setVisibility(View.GONE);
        mUserNameField.setEnabled(false);
        mPasswordField.setEnabled(false);
        mRegisterButton.setEnabled(false);
    }
    public void stopLoading() {
        mLoadingIcon.setVisibility(View.GONE);
        mErrorText.setVisibility(View.VISIBLE);
        mLoginButton.setVisibility(View.VISIBLE);
        mUserNameField.setEnabled(true);
        mPasswordField.setEnabled(true);
        mRegisterButton.setEnabled(true);
    }

}