package com.llew.e.dialog.view.activity;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.llew.e.dialog.R;
import com.llew.e.dialog.view.bean.DialogBean;
import com.llew.e.dialog.view.bean.DialogBean.DialogButtonType;
import com.llew.e.dialog.view.bean.DialogBean.DialogClickListener;
import com.llew.e.dialog.view.wedgit.GlobleDialog;

public class MainActivity extends Activity {
    /** Called when the activity is first created. */
	
	private Dialog dialog;
	private TextView textView;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        textView = (TextView) findViewById(R.id.textview);
        textView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				DialogBean dialogBean = new DialogBean();
		        dialogBean.setCancelable(true);
		        dialogBean.setOutCancelable(true);
		        dialogBean.setTitle("小练习");
		        dialogBean.setContent("在这里输入提示信息");
		        dialogBean.setButtonType(DialogButtonType.ThreeButton);
		        dialogBean.setLayoutResID(R.layout.dialog_common);
		        dialogBean.setListener(new DialogClickListener() {
					@Override
					public void onClick(int buttonIndex) {
						switch (buttonIndex) {
						case 0:
							Toast.makeText(MainActivity.this, "点击了播放", Toast.LENGTH_SHORT).show();
							break;
						case 1:
							Toast.makeText(MainActivity.this, "点击了暂停", Toast.LENGTH_SHORT).show();
							break;
						case 2:
							Toast.makeText(MainActivity.this, "点击了停止", Toast.LENGTH_SHORT).show();
							break;
						default:
							break;
						}
					}
				});
		        createDialog(dialogBean);
			}
		});
    }
    
    public void createDialog(DialogBean bean) {
    	if(null == dialog) {
    		dialog = new GlobleDialog(MainActivity.this, R.style.theme_dialog_alert, bean);
    	}
    	dialog.show();
    }
}