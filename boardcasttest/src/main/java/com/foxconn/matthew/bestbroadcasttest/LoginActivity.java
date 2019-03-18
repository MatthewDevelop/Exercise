package com.foxconn.matthew.bestbroadcasttest;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.foxconn.matthew.boardcasttest.MainActivity;
import com.foxconn.matthew.boardcasttest.R;

/**
 * Created by Matthew on 2017/8/22.
 */

public class LoginActivity extends BaseActivity {
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private EditText accountEdit;
    private EditText passwordEdit;
    private Button loginButton;
    private CheckBox rememberPass;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        accountEdit = (EditText) findViewById(R.id.account_edit);
        passwordEdit = (EditText) findViewById(R.id.password_edit);
        loginButton = (Button) findViewById(R.id.login_btn);
        rememberPass = (CheckBox) findViewById(R.id.rememberPass);
        boolean isRemember = sharedPreferences.getBoolean("isRememberPass", false);
        if (isRemember) {
            String account = sharedPreferences.getString("account", "");
            String password = sharedPreferences.getString("password", "");
            accountEdit.setText(account);
            passwordEdit.setText(password);
            rememberPass.setChecked(true);
        }
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String loginName = accountEdit.getText().toString();
                String loginPassword = passwordEdit.getText().toString();
                if (loginName.equals("admin") && loginPassword.equals("123456")) {
                    editor = sharedPreferences.edit();
                    if (rememberPass.isChecked()) {
                        editor.putBoolean("isRememberPass", true);
                        editor.putString("account", "admin");
                        editor.putString("password", "123456");
                    } else {
                        editor.clear();
                    }
                    editor.apply();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "Account or password is invalid", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
