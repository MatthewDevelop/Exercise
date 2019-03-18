package com.foxconn.matthew.datapersistencetest;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText = (EditText) findViewById(R.id.editText);
        String inputText = loadData();
        if (!TextUtils.isEmpty(inputText)) {
            editText.setText(inputText.toString());
            editText.setSelection(inputText.length());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        String inputText = editText.getText().toString();
        saveData(inputText);
    }

    private String loadData() {
        FileInputStream fis = null;
        BufferedReader reader = null;
        StringBuilder sb = new StringBuilder();
        try {
            fis = openFileInput("data");
            reader = new BufferedReader(new InputStreamReader(fis));
            String line = "";
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            reader.readLine();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return sb.toString();
    }

    private void saveData(String inputText) {
        FileOutputStream fos = null;
        BufferedWriter writer = null;
        try {
            fos = openFileOutput("data", MODE_PRIVATE);
            writer = new BufferedWriter(new OutputStreamWriter(fos));
            writer.write(inputText);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_save:
                SharedPreferences.Editor editor = getSharedPreferences("data", MODE_PRIVATE).edit();
                editor.putString("name", "tom");
                editor.putInt("age", 18);
                editor.putBoolean("isMarried", false);
                editor.apply();
                break;
            case R.id.bt_get:
                SharedPreferences sp = getSharedPreferences("data", MODE_PRIVATE);
                String name = sp.getString("name", "jack");
                int age = sp.getInt("age", 15);
                boolean isMarried = sp.getBoolean("isMarried", true);
                Log.e(TAG, "name " + name + ", age " + age + ", isMarried " + isMarried);
                break;
            default:
                break;
        }
    }
}
