package com.llew.e.dialog.view.bean;

public class DialogBean extends BaseBean {

	/**
	 * 点击返回键是否可消失
	 */
	boolean cancelable;
	/**
	 * 点击蒙皮是否可以消失
	 */
	boolean outCancelable;
	/**
	 * 事件监听
	 */
	DialogClickListener listener;
	/**
	 * 按钮类型【默认显示两个按钮】
	 */
	DialogButtonType buttonType = DialogButtonType.TwoButton;

	/**
	 * 显示布局资源ID
	 */
	Integer layoutResID;

	public DialogBean() {
	}

	/**
	 * 点击BACK键是否可以消失
	 * 
	 * @return 【true:可消失】【false:不消失】
	 */
	public boolean isCancelable() {
		return cancelable;
	}

	/**
	 * 设置点击BACK键是否可以消失
	 * 
	 * @param cancelable
	 *            【true:可消失】【false:不消失】
	 */
	public void setCancelable(boolean cancelable) {
		this.cancelable = cancelable;
	}

	/**
	 * 点击蒙皮是否可以消失
	 * 
	 * @return 【true:可消失】【false:不消失】
	 */
	public boolean isOutCancelable() {
		return outCancelable;
	}

	/**
	 * 设置点击蒙皮是否可以消失
	 * 
	 * @param outCancelable
	 *            【true:可消失】【false:不消失】
	 */
	public void setOutCancelable(boolean outCancelable) {
		this.outCancelable = outCancelable;
	}

	/**
	 * 获取事件监听器
	 * 
	 * @return 事件监听器
	 */
	public DialogClickListener getListener() {
		return listener;
	}

	/**
	 * 设置事件监听器
	 * 
	 * @param listener
	 *            事件监听器
	 */
	public void setListener(DialogClickListener listener) {
		this.listener = listener;
	}

	/**
	 * 获取按钮类型
	 * 
	 * @return 按钮类型
	 */
	public DialogButtonType getButtonType() {
		return buttonType;
	}

	/**
	 * 设置按钮类型
	 * 
	 * @param buttonType
	 *            按钮类型
	 */
	public void setButtonType(DialogButtonType buttonType) {
		this.buttonType = buttonType;
	}

	/**
	 * 获取要显示的布局ID
	 * 
	 * @return 要显示的布局ID
	 */
	public Integer getLayoutResID() {
		return layoutResID;
	}

	/**
	 * 设置要显示的布局ID
	 * 
	 * @param layoutResID
	 *            要显示的布局ID
	 */
	public void setLayoutResID(Integer layoutResID) {
		this.layoutResID = layoutResID;
	}

	/**
	 * 按钮类型
	 * 
	 * @author llew
	 */
	public enum DialogButtonType {
		/**
		 * 一个按钮
		 */
		OneButton,
		/**
		 * 两个按钮
		 */
		TwoButton,
		/**
		 * 三个按钮
		 */
		ThreeButton
	}

	/**
	 * 按钮点击监听器
	 * 
	 * @author llew
	 * 
	 */
	public interface DialogClickListener {
		/**
		 * 点击按钮
		 * 
		 * @param buttonIndex
		 *            按钮下标【从0开始】
		 */
		public void onClick(int buttonIndex);
	}
}
