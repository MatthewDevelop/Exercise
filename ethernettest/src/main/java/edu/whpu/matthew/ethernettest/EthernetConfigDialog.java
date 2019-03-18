package edu.whpu.matthew.ethernettest;


import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.mstar.android.ethernet.EthernetDevInfo;
import com.mstar.android.ethernet.EthernetManager;
import com.mstar.android.ethernet.EthernetStateTracker;

import java.net.InetAddress;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EthernetConfigDialog extends AlertDialog implements
        DialogInterface.OnClickListener, DialogInterface.OnShowListener,
        DialogInterface.OnDismissListener {
    private final String TAG = "EthConfDialog";
    private static final boolean localLOGV = true;

    private static final boolean ENABLE_PROXY = true;
    /* These values come from "wifi_proxy_settings" resource array */
    public static final int PROXY_NONE = 0;
    public static final int PROXY_STATIC = 1;

    /* These values come from "network_ip_settings" resource array */
    private static final int DHCP = 0;
    private static final int STATIC_IP = 1;

    // Matches blank input, ips, and domain names
    private static final String HOSTNAME_REGEXP =
            "^$|^[a-zA-Z0-9]+(\\-[a-zA-Z0-9]+)*(\\.[a-zA-Z0-9]+(\\-[a-zA-Z0-9]+)*)*$";
    private static final Pattern HOSTNAME_PATTERN;
    private static final String EXCLLIST_REGEXP =
            "$|^(.?[a-zA-Z0-9]+(\\-[a-zA-Z0-9]+)*(\\.[a-zA-Z0-9]+(\\-[a-zA-Z0-9]+)*)*)+" +
            "(,(.?[a-zA-Z0-9]+(\\-[a-zA-Z0-9]+)*(\\.[a-zA-Z0-9]+(\\-[a-zA-Z0-9]+)*)*))*$";
    private static final Pattern EXCLLIST_PATTERN;
    static {
        HOSTNAME_PATTERN = Pattern.compile(HOSTNAME_REGEXP);
        EXCLLIST_PATTERN = Pattern.compile(EXCLLIST_REGEXP);
    }

    private View mView;
    private Spinner mDevList;
	private String[] devList;
    private TextView mDevs;
    private RadioButton mConTypeDhcp;
    private RadioButton mConTypeManual;
    private EditText mIpaddr;
    private EditText mDns;
    private EditText mGw;
    private EditText mMask;

    // Indicates if we are in the process of setting up values and should not validate them yet.
    private boolean mSettingUpValues;
    private Spinner mProxySettingsSpinner;
    private TextView mProxyHostView;
    private TextView mProxyPortView;
    private TextView mProxyExclusionListView;
    private final TextWatcher textWatcher = new TextWatcherImpl();

    //private EthernetLayer mEthLayer;
    private EthernetManager mEthManager;
    private EthernetDevInfo mEthInfo;
    private boolean mEnablePending;
    private Handler mHandler;

    private Context mContext;
	private IntentFilter mIntentFilter;
	//private EthernetStateTracker mEthernetStateTracker;


    public EthernetConfigDialog(Context context) {
        super(context);        
        mContext = context;
        mEthManager = EthernetManager.getInstance();
        //mEthernetStateTracker= EthernetStateTracker.getInstance();
		mHandler=new Handler();
		mIntentFilter = new IntentFilter(
				EthernetManager.ETHERNET_STATE_CHANGED_ACTION);
		
        buildDialogContent(context);
        setOnShowListener(this);
        setOnDismissListener(this);
		enableAfterConfig();
    }

    public void onShow(DialogInterface dialog) {
        if (localLOGV) Log.d(TAG, "onShow");
        //mEthLayer.resume();
        // soft keyboard pops up on the disabled EditText. Hide it.
        mContext.registerReceiver(mEthConnectReciever, mIntentFilter);
        if (getCurrentFocus() != null && getCurrentFocus().getWindowToken() != null) {
            InputMethodManager imm = (InputMethodManager)mContext.getSystemService(
                    Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_IMPLICIT_ONLY);
        }
    }

    public void onDismiss(DialogInterface dialog) {
        if (localLOGV) Log.d(TAG, "onDismiss");
        //mEthLayer.pause();
        mContext
		.unregisterReceiver(mEthConnectReciever);
    }

    /*private static String getAddress(int addr) {
        return NetworkUtils.intToInetAddress(addr).getHostAddress();
    }*/


    /* proxy */
    private void showProxyFields() {
        if (mProxySettingsSpinner.getSelectedItemPosition() == PROXY_STATIC) {
            mView.findViewById(R.id.proxy_fields).setVisibility(View.VISIBLE);
        } else {
            mView.findViewById(R.id.proxy_fields).setVisibility(View.GONE);
        }
    }

    private void enableSubmitIfAppropriate() {
        //setPositiveButtonEnabled(isProxyFieldsValid() && isIpFieldsValid());
        isProxyFieldsValid();
        //skip disabling PositveButton for now
    }

    private boolean isProxyFieldsValid() {
        if (mProxySettingsSpinner.getSelectedItemPosition() == PROXY_STATIC) {
            return validateProxyFields();
        }
        return true;
    }

    public static boolean isValidIpAddress(String ipAddress, boolean allowEmptyValue) {
        if (ipAddress == null || ipAddress.length() == 0) {
            return allowEmptyValue;
        }

        try {
            InetAddress.getByName(ipAddress);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    /**
     * Validates string with proxy exclusion list.
     *
     * @param exclList string to validate.
     * @return resource id of error message string or 0 if valid.
     */
    public static int validateProxyExclusionList(String exclList) {
        Matcher listMatch = EXCLLIST_PATTERN.matcher(exclList);
        return !listMatch.matches() ? R.string.proxy_error_invalid_exclusion_list : 0;
    }

    private boolean validateProxyFields() {
        if (!ENABLE_PROXY) {
            return true;
        }

        final Context context = getContext();
        boolean errors = false;

        if (isValidIpAddress(mProxyHostView.getText().toString(), false)) {
            mProxyHostView.setError(null);
        } else {
            mProxyHostView.setError(
                    context.getString(R.string.wifi_ip_settings_invalid_ip_address));
            errors = true;
        }

        int port = -1;
        try {
            port = Integer.parseInt(mProxyPortView.getText().toString());
            mProxyPortView.setError(null);
        } catch (NumberFormatException e) {
            // Intentionally left blank
        }
        if (port < 0) {
            mProxyPortView.setError(context.getString(R.string.proxy_error_invalid_port));
            errors = true;
        }

        final String exclusionList = mProxyExclusionListView.getText().toString();
        final int listResult = validateProxyExclusionList(exclusionList);
        if (listResult == 0) {
            mProxyExclusionListView.setError(null);
        } else {
            mProxyExclusionListView.setError(context.getString(listResult));
            errors = true;
        }

        return !errors;
    }

    private void setPositiveButtonEnabled(boolean enabled) {
        getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(enabled);
    }

    private class TextWatcherImpl implements TextWatcher {
        @Override
        public void afterTextChanged(Editable s) {
            // Do not validate fields while values are being setted up.
            if (!mSettingUpValues) {
                enableSubmitIfAppropriate();
            }
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }
    }

    private void setProxyPropertiesFromEdits(EthernetDevInfo info) {
        /*final ProxySettings proxySettings =
                ENABLE_PROXY && mProxySettingsSpinner.getSelectedItemPosition() == PROXY_STATIC
                        ? ProxySettings.STATIC : ProxySettings.NONE;*/

        if (mProxySettingsSpinner.getSelectedItemPosition() == PROXY_STATIC) {
            String port = mProxyPortView.getText().toString();
            if (TextUtils.isEmpty(port))
                port = "0";
            try {
                /*info.setProxy(
                        mProxyHostView.getText().toString(),
                        Integer.parseInt(port),
                        mProxyExclusionListView.getText().toString());*/
                info.setProxyOn(true);
                info.setProxyHost(mProxyHostView.getText().toString());
                info.setProxyPort(port);
                info.setProxyExclusionList(mProxyExclusionListView.getText().toString());
            } catch (IllegalArgumentException e) {
                // Should not happen if validations are done right
                throw new RuntimeException(e);
            }
        } else {
            //info.setProxy(null, 0, null);
            info.setProxyOn(false);
            info.setProxyHost(null);
            info.setProxyPort("0");
            info.setProxyExclusionList(null);
        }
    }

    private void buildProxyContent() {
        mProxySettingsSpinner = (Spinner) mView.findViewById(R.id.proxy_settings);
        mProxySettingsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                showProxyFields();
                enableSubmitIfAppropriate();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        mProxySettingsSpinner.setVisibility(View.VISIBLE);

        mProxyHostView = (TextView) mView.findViewById(R.id.proxy_hostname);
        mProxyHostView.addTextChangedListener(textWatcher);

        mProxyPortView = (TextView) mView.findViewById(R.id.proxy_port);
        mProxyPortView.addTextChangedListener(textWatcher);

        mProxyExclusionListView = (TextView) mView.findViewById(R.id.proxy_exclusionlist);
        mProxyExclusionListView.addTextChangedListener(textWatcher);

    }
    /* /proxy */

    public int buildDialogContent(Context context) {
        mSettingUpValues = true;
        this.setTitle(R.string.eth_config_title);
        this.setView(mView = getLayoutInflater().inflate(R.layout.eth_configure, null));
        mDevs = (TextView) mView.findViewById(R.id.eth_dev_list_text);
        mDevList = (Spinner) mView.findViewById(R.id.eth_dev_spinner);
        mConTypeDhcp = (RadioButton) mView.findViewById(R.id.dhcp_radio);
        mConTypeManual = (RadioButton) mView.findViewById(R.id.manual_radio);
        mIpaddr = (EditText)mView.findViewById(R.id.ipaddr_edit);
        mMask = (EditText)mView.findViewById(R.id.netmask_edit);
        mDns = (EditText)mView.findViewById(R.id.eth_dns_edit);
        mGw = (EditText)mView.findViewById(R.id.eth_gw_edit);

        mConTypeDhcp.setChecked(true);
        mConTypeManual.setChecked(false);
        mIpaddr.setEnabled(false);
        mMask.setEnabled(false);
        mDns.setEnabled(false);
        mGw.setEnabled(false);
        mConTypeManual.setOnClickListener(new RadioButton.OnClickListener() {
            public void onClick(View v) {
                mView.findViewById(R.id.eth_static_fields).setVisibility(View.VISIBLE);
                mIpaddr.setEnabled(true);
                mDns.setEnabled(true);
                mGw.setEnabled(true);
                mMask.setEnabled(true);
            }
        });

        mConTypeDhcp.setOnClickListener(new RadioButton.OnClickListener() {
            public void onClick(View v) {
                mView.findViewById(R.id.eth_static_fields).setVisibility(View.GONE);
                mIpaddr.setEnabled(false);
                mDns.setEnabled(false);
                mGw.setEnabled(false);
                mMask.setEnabled(false);
            }
        });

        buildProxyContent();

        this.setInverseBackgroundForced(true);
        this.setButton(BUTTON_POSITIVE, context.getText(R.string.menu_save), this);
        this.setButton(BUTTON_NEGATIVE, context.getText(R.string.menu_cancel), this);
        String[] Devs = mEthManager.getDeviceNameList();
        updateDevNameList(Devs);
        if (Devs != null) {
            if (mEthManager.isConfigured()) {
                /*String propties = Utils.getEtherProperties(mContext);
                Slog.d(TAG, "Properties: " + propties);*/

                mEthInfo = mEthManager.getSavedConfig();
                for (int i = 0 ; i < Devs.length; i++) {
                    if (Devs[i].equals(mEthInfo.getIfName())) {
                        mDevList.setSelection(i);
                        break;
                    }
                }
                /*if (mEthInfo.getConnectMode().equals(EthernetDevInfo.ETH_CONN_MODE_DHCP)) 
                {
                    DhcpInfo dhcpInfo = mEthManager.getDhcpInfo();
                    Slog.d(TAG, "ip  : " + getAddress(dhcpInfo.ipAddress));
                    Slog.d(TAG, "gw  : " + getAddress(dhcpInfo.gateway));
                    Slog.d(TAG, "mask: " + getAddress(dhcpInfo.netmask));
                    Slog.d(TAG, "dns1:" + getAddress(dhcpInfo.dns1));
                    Slog.d(TAG, "dns2:" + getAddress(dhcpInfo.dns2));
                }*/
                mIpaddr.setText(mEthInfo.getIpAddress());
                mGw.setText(mEthInfo.getRouteAddr());
                mDns.setText(mEthInfo.getDnsAddr());
                mMask.setText(mEthInfo.getNetMask());
                if (mEthInfo.getConnectMode().equals(EthernetDevInfo.ETHERNET_CONN_MODE_DHCP)) {
                    mView.findViewById(R.id.eth_static_fields).setVisibility(View.GONE);
                    mIpaddr.setEnabled(false);
                    mDns.setEnabled(false);
                    mGw.setEnabled(false);
                    mMask.setEnabled(false);
                } else {
                    mConTypeDhcp.setChecked(false);
                    mConTypeManual.setChecked(true);
                    mView.findViewById(R.id.eth_static_fields).setVisibility(View.VISIBLE);
                    mIpaddr.setEnabled(true);
                    mDns.setEnabled(true);
                    mGw.setEnabled(true);
                    mMask.setEnabled(true);
                }
                if (ENABLE_PROXY) {
                    if (mEthInfo.getProxyOn()) {
                        mProxySettingsSpinner.setSelection(PROXY_STATIC);
                        mProxyHostView.setText(mEthInfo.getProxyHost());
                        mProxyPortView.setText(String.valueOf(mEthInfo.getProxyPort()));
                        mProxyExclusionListView.setText(mEthInfo.getProxyExclusionList());
                    }
                }
            }
        }
        mSettingUpValues = false;
        return 0;
    }

    private void handle_saveconf() {
        String selected = null;
        if (mDevList.getSelectedItem() != null)
            selected = mDevList.getSelectedItem().toString();
        if (selected == null || selected.isEmpty())
            return;
        EthernetDevInfo info = new EthernetDevInfo();
        info.setIfName(selected);
        if (localLOGV)
            Log.v(TAG, "Config device for " + selected);
        if (mConTypeDhcp.isChecked()) {
            info.setConnectMode(EthernetDevInfo.ETHERNET_CONN_MODE_DHCP);
            info.setIpAddress(null);
            info.setRouteAddr(null);
            info.setDnsAddr(null);
            info.setNetMask(null);
        } else {
            Log.i(TAG,"mode manual");
            if (isIpAddress(mIpaddr.getText().toString())
                    && isIpAddress(mGw.getText().toString())
                    && isIpAddress(mDns.getText().toString())
                    && isIpAddress(mMask.getText().toString())) {
                info.setConnectMode(EthernetDevInfo.ETHERNET_CONN_MODE_MANUAL);
                info.setIpAddress(mIpaddr.getText().toString());
                info.setRouteAddr(mGw.getText().toString());
                info.setDnsAddr(mDns.getText().toString());
                info.setNetMask(mMask.getText().toString());
            } else {
                //Toast.makeText(mContext, R.string.eth_settings_error, Toast.LENGTH_LONG).show();
                return;
            }
        }

        setProxyPropertiesFromEdits(info);
        
        //mEthernetStateTracker.startMonitoring(mContext, mHandler);
        
        mEthManager.updateDevInfo(info);
        
         if (mEnablePending) {
            	if(mEthManager.getState()==mEthManager.ETHERNET_STATE_ENABLED){
					mEthManager.setEnabled(true);
				}
            mEnablePending = false;
        }
    }


    private boolean isIpAddress(String value) {
        int start = 0;
        int end = value.indexOf('.');
        int numBlocks = 0;

        while (start < value.length()) {
            if (end == -1) {
                end = value.length();
            }

            try {
                int block = Integer.parseInt(value.substring(start, end));
                if ((block > 255) || (block < 0)) {
                        return false;
                }
            } catch (NumberFormatException e) {
                    return false;
            }

            numBlocks++;

            start = end + 1;
            end = value.indexOf('.', start);
        }
        return numBlocks == 4;
    }


    public void onClick(DialogInterface dialog, int which) {
        switch (which) {
            case BUTTON_POSITIVE:
               

				/*final ProgressDialog waiting_dialog = new ProgressDialog(this.getContext());
				waiting_dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
				
				waiting_dialog.setCancelable(false);
				waiting_dialog.setTitle("Ethernet Configuration");
				waiting_dialog.setMessage("Saving...Please Wait");
				waiting_dialog.show();*/
		new Thread(new Runnable(){
		public void run()
		{

		handle_saveconf();


		//waiting_dialog.cancel();
		}
		}
		).start();
		
		//waiting_dialog.show(this.getContext(),"Ethernet Configuration","Saving...Please Wait",false,false);
		//waiting_dialog.dismiss();
                break;
            case BUTTON_NEGATIVE:
                //Don't need to do anything
                break;
            default:
        }
    }

    public void updateDevNameList(String[] DevList) {
        if (DevList == null) {
            DevList = new String[] {};
        }
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(
                getContext(), android.R.layout.simple_spinner_item, DevList);
        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);
        mDevList.setAdapter(adapter);
    }

    public void enableAfterConfig() {
        mEnablePending = true;
    }
    
    /**
	 * 声明广播接收器
	 */
	private BroadcastReceiver mEthConnectReciever = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			Log.d(TAG, " Ethernet onRecevice action = " + action);
			if (action.equals(EthernetManager.ETHERNET_STATE_CHANGED_ACTION)) {
				int state = intent.getIntExtra(EthernetManager.EXTRA_ETHERNET_STATE,
						EthernetStateTracker.EVENT_HW_DISCONNECTED);
				Log.i(TAG, "recv state" + state);
				if (state == EthernetStateTracker.EVENT_HW_CONNECTED
						|| state == EthernetStateTracker.EVENT_HW_PHYCONNECTED) {
					handleDevListChanges();
				} else if (state == EthernetStateTracker.EVENT_HW_DISCONNECTED) {
					// Unfortunately, the interface will still be listed when
					// this
					// intent is sent, so delay updating.
					mHandler.postDelayed(new Runnable() {
						public void run() {
							handleDevListChanges();
						}
					}, 700);
				}
			}
		}
	};
	
	
	
	private void handleDevListChanges() {
		devList = mEthManager.getDeviceNameList();
		updateDevNameList(devList);
		devList = null;
	}

	
}
