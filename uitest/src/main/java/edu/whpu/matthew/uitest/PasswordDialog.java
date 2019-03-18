package edu.whpu.matthew.uitest;

import android.app.AlertDialog;
import android.content.Context;
import android.net.wifi.ScanResult;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * Created by matthew on 17-2-13.
 */

public class PasswordDialog extends AlertDialog implements View.OnClickListener {

    private static final String TAG=PasswordDialog.class.getSimpleName();

    private static final int WPA_WPA2_WPS = 0;
    private static final int WPA_WPA2 = 1;
    private static final int WPA = 2;
    private static final int WPA_WPS = 3;
    private static final int WPA2 = 4;
    private static final int WPA2_WPS = 5;
    private static final int WPS = 6;
    private static final int WEP = 7;

    public static final int TYPE_DHCP = 0;
    public static final int TYPE_STATIC = 1;


    private String mSsid;
    private int mSecureType;
    private View mView;
    private TextView mSignal;
    private TextView mSecurity;
    private EditText mPassword;
    private CheckBox mShowpsw;
    private CheckBox mShowAdvance;
    private EditText mIpAdd;
    private EditText mGateway;
    private EditText mNetworkPrefixLength;
    private EditText mDns1;
    private EditText mDns2;
    private LinearLayout mWifiAdvanceField;
    private LinearLayout mWifiStaticField;
    private Spinner mIpSettings;
    private ScanResult mResult;
    private Context mContext;
    private Button cancel;
    private Button save;

    public PasswordDialog(Context context, String ssid, int secureType, ScanResult result) {
        super(context,R.style.theme_dialog_alert);
        this.mSsid = ssid;
        this.mSecureType = secureType;
        this.mResult = result;
        this.mContext=context;
        buildDialogContent(context);
    }

    private void buildDialogContent(Context context) {
        this.setView(mView = getLayoutInflater().inflate(R.layout.wifi_password, null));
        mSignal = (TextView) mView.findViewById(R.id.signal_strength_tv);
        mSecurity = (TextView) mView.findViewById(R.id.security_tv);
        mPassword = (EditText) mView.findViewById(R.id.password_edit);
        mWifiAdvanceField= (LinearLayout) mView.findViewById(R.id.wifi_advance_fields);
        mWifiStaticField= (LinearLayout) mView.findViewById(R.id.wifi_static_fields);
        mIpSettings= (Spinner) mView.findViewById(R.id.ip_settings);
        mIpAdd = (EditText) mView.findViewById(R.id.wifi_ipaddr_edit);
        mGateway = (EditText) mView.findViewById(R.id.wifi_gw_edit);
        mNetworkPrefixLength = (EditText) mView.findViewById(R.id.networkPrefixLength_edit);
        mDns1 = (EditText) mView.findViewById(R.id.wifi_dns1_edit);
        mDns2 = (EditText) mView.findViewById(R.id.wifi_dns2_edit);

        mIpAdd.setEnabled(false);
        mGateway.setEnabled(false);
        mNetworkPrefixLength.setEnabled(false);
        mDns1.setEnabled(false);
        mDns2.setEnabled(false);
        mShowpsw = (CheckBox) mView.findViewById(R.id.show_password_checkbox);
        mShowpsw.setChecked(false);
        mShowpsw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                showPassword(isChecked);
            }
        });
        mShowAdvance= (CheckBox) mView.findViewById(R.id.show_advance_settings_checkbox);
        mShowAdvance.setChecked(false);
        mShowAdvance.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                showAdvance(isChecked);
            }
        });

        mIpSettings.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                showIpSettingField(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



    }

    private void showIpSettingField(int position) {
        if(position==TYPE_STATIC){
            mWifiStaticField.setVisibility(View.VISIBLE);
            mIpAdd.setEnabled(true);
            mGateway.setEnabled(true);
            mNetworkPrefixLength.setEnabled(true);
            mDns1.setEnabled(true);
            mDns2.setEnabled(true);
        }else{
            mWifiStaticField.setVisibility(View.GONE);
            mIpAdd.setEnabled(false);
            mGateway.setEnabled(false);
            mNetworkPrefixLength.setEnabled(false);
            mDns1.setEnabled(false);
            mDns2.setEnabled(false);
        }
    }

    private void showAdvance(boolean isChecked) {
        if(isChecked){
            mWifiAdvanceField.setVisibility(View.VISIBLE);
            mIpSettings.setSelection(TYPE_DHCP);
        }else{
            mWifiAdvanceField.setVisibility(View.GONE);
        }

    }






    /**
     * 密码明文密文切换
     *
     * @param isChecked
     */
    private void showPassword(boolean isChecked) {
        if (isChecked) {
            mPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        } else {
            mPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
        }
    }





    @Override
    public void onClick(View view) {

    }
}
