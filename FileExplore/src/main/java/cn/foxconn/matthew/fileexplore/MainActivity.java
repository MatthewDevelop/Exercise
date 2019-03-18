package cn.foxconn.matthew.fileexplore;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private TextView textView;
    private ListView listView;
    private File currentParent;
    File[] currentFiles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView) findViewById(R.id.currentPath);
        listView = (ListView) findViewById(R.id.list);
        File root = new File("/mnt/sdcard");
        Log.e(TAG, root.getAbsolutePath());
        //if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
        if (root.exists()) {
            //currentParent=Environment.getExternalStorageDirectory();
            currentParent = root;
            Log.e(TAG, "" + (currentParent == null));
            currentFiles = currentParent.listFiles();
            Log.e(TAG, "" + (currentFiles == null));
            updateList(currentFiles);
        } else {
            Toast.makeText(this, "不存在SD卡", Toast.LENGTH_SHORT).show();
        }
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (currentFiles[i].isFile()) return;
                File[] temp = currentFiles[i].listFiles();
                if (temp == null || temp.length == 0) {
                    Toast.makeText(MainActivity.this, "当前路径不可访问或路径下没有文件～", Toast.LENGTH_SHORT).show();
                } else {
                    currentParent = currentFiles[i];
                    currentFiles = temp;
                    updateList(currentFiles);
                }
            }
        });
    }

    private void updateList(File[] files) {
        List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < files.length; i++) {
            Map<String, Object> listItem = new HashMap<String, Object>();
            if (files[i].isDirectory()) {
                listItem.put("icon", R.drawable.dir);
            } else {
                listItem.put("icon", R.drawable.file);
            }
            listItem.put("filename", files[i].getName());
            listItems.add(listItem);
        }
        SimpleAdapter simpleAdapter = new SimpleAdapter(this, listItems, R.layout.listitem,
                new String[]{"icon", "filename"}, new int[]{R.id.icon, R.id.filename});
        listView.setAdapter(simpleAdapter);
        textView.setText(currentParent.getAbsolutePath());

    }

    public void onClick(View v) {
        if (currentParent == null) {
            Toast.makeText(this, "没有上级目录了～", Toast.LENGTH_SHORT).show();
        } else {
            currentParent = currentParent.getParentFile();
            if (currentParent == null) {
                Toast.makeText(this, "没有上级目录了～", Toast.LENGTH_SHORT).show();
            } else {
                currentFiles = currentParent.listFiles();
                updateList(currentFiles);
            }
        }
    }
}
