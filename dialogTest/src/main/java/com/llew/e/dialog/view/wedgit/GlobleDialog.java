package com.llew.e.dialog.view.wedgit;

import com.llew.e.dialog.R;
import com.llew.e.dialog.view.bean.DialogBean;
import com.llew.e.dialog.view.bean.DialogBean.DialogButtonType;

import android.app.Dialog;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class GlobleDialog extends Dialog implements View.OnClickListener {

	private TextView titleTextView, contentTextView;
	private Button leftButton, centerButton ,rightButton;
	private DialogBean bean;
	
	public GlobleDialog(Context context, int theme, DialogBean bean) {
		super(context, theme);
		this.bean = bean;
		initWedgits();
	}

	/**
	 * 初始化各组件
	 */
	private void initWedgits() {
		try {
			setCancelable(bean.isCancelable());
			setCanceledOnTouchOutside(bean.isOutCancelable());
			View dialogView = getLayoutInflater().inflate(bean.getLayoutResID(), null);
			titleTextView = (TextView) dialogView.findViewById(R.id.button_title);
			contentTextView = (TextView) dialogView.findViewById(R.id.button_content);
			titleTextView.setText(bean.getTitle());
			contentTextView.setText(bean.getContent());
    		leftButton = (Button) dialogView.findViewById(R.id.button_left);
    		centerButton = (Button) dialogView.findViewById(R.id.button_center);
    		rightButton = (Button) dialogView.findViewById(R.id.button_right);
    		
    		leftButton.setOnClickListener(this);
    		centerButton.setOnClickListener(this);
    		rightButton.setOnClickListener(this);
    		
    		if(DialogButtonType.OneButton == bean.getButtonType()) {
    			leftButton.setVisibility(View.GONE);
    			rightButton.setVisibility(View.GONE);
    		} else if(DialogButtonType.TwoButton == bean.getButtonType()) {
    			centerButton.setVisibility(View.GONE);
    		}
    		setContentView(dialogView);
    		
    		Window dialogWindow = getWindow();
    		WindowManager.LayoutParams lp = dialogWindow.getAttributes();
    		dialogWindow.setGravity(Gravity.CENTER);
    		DisplayMetrics dm = new DisplayMetrics();
    		dialogWindow.getWindowManager().getDefaultDisplay().getMetrics(dm);
    		lp.width = dm.widthPixels - 20;
    		dialogWindow.setAttributes(lp);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onClick(View v) {
		try {
			switch (v.getId()) {
			case R.id.button_left:
				bean.getListener().onClick(0);
				break;
			case R.id.button_center:
				bean.getListener().onClick(1);
				break;
			case R.id.button_right:
				bean.getListener().onClick(2);
				break;
			default:
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
