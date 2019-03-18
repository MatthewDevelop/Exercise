package cn.foxconn.matthew.random;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    StringBuilder sb;
    List<String> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        list = new ArrayList<>();
        Random random = new Random(10);
        for (int i = 0; i < 100; i++) {
            sb = new StringBuilder();
            for (int j = 0; j < 6; j++) {
                sb.append(random.nextInt(10));
            }
            Log.e("number", sb.toString());
            /*for(int k=0;k<list.size();k++){
                if (sb.toString().equals(list.get(k))){
                    break;
                }else {
                    list.add(sb.toString());
                }
            }*/
            list.add(sb.toString());
        }
        Log.e("number", list.toString());
        Log.e("number",list.size()+"");

    }
}
