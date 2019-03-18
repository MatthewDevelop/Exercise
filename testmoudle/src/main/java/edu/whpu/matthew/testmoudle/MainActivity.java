package edu.whpu.matthew.testmoudle;

import android.app.Activity;
import android.net.ConnectivityManager;
import android.net.IpConfiguration;
import android.net.IpConfiguration.ProxySettings;
import android.net.IpConfiguration.IpAssignment;
import android.net.LinkAddress;
import android.net.NetworkInfo;
import android.net.NetworkUtils;
import android.net.ProxyInfo;
import android.net.StaticIpConfiguration;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.net.Inet4Address;

public class MainActivity extends Activity {
    private static final String TAG=MainActivity.class.getSimpleName();
    private ConnectivityManager mConnectivityManager;
    private NetworkInfo mNetworkInfo_wlan;
    private NetworkInfo mNetworkInfo_ethernet;
    private NetworkConfiguration mNetworkConfiguration;
    private TextView tv;
    private IpConfiguration mIpConfiguration;
    private StaticIpConfiguration mStaticIpConfiguration;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv= (TextView) findViewById(R.id.tv);

    }

    private void testEthernet() {
        if(mNetworkConfiguration!=null){
            mIpConfiguration=mNetworkConfiguration.getIpConfiguration();
            Log.e(TAG,"get ip info");
        }
        mIpConfiguration.setProxySettings(ProxySettings.STATIC);
        Log.e(TAG,"setProxySettings");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mIpConfiguration.setHttpProxy(new ProxyInfo("10.203.145.30", 8080, ""));
            Log.e(TAG,"setHttpProxy");
        }
        mIpConfiguration.setIpAssignment(IpAssignment.STATIC);
        Log.e(TAG,"setIpAssignment");
        String ipAddr="10.203.146.153";
        Inet4Address inetAddr= (Inet4Address) NetworkUtils.numericToInetAddress(ipAddr);

        int networkPrefixLength = 24;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mStaticIpConfiguration.ipAddress = new LinkAddress(inetAddr, networkPrefixLength);
            Log.e(TAG,"LinkAddress");
        }
        mStaticIpConfiguration.gateway =
                (Inet4Address) NetworkUtils.numericToInetAddress("255.255.254.0");
        Log.e(TAG,"gateway");
        mStaticIpConfiguration.dnsServers.add(
                (Inet4Address) NetworkUtils.numericToInetAddress("10.202.2.102"));
        Log.e(TAG,"set dns1");
        mStaticIpConfiguration.dnsServers.add(
                (Inet4Address) NetworkUtils.numericToInetAddress("1.1.1.1"));
        Log.e(TAG,"set dns2");
        mIpConfiguration.setStaticIpConfiguration(mStaticIpConfiguration);
        Log.e(TAG,"setStaticIpConfiguration");
        mNetworkConfiguration.setIpConfiguration(mIpConfiguration);
        Log.e(TAG,"setIpConfiguration");
        mNetworkConfiguration.save(null);
        Log.e(TAG,"save");
    }

    private void showInfo() {
        StringBuilder sb=new StringBuilder();
        if(mNetworkInfo_wlan!=null){
            sb.append("wifi_Info:\n");
            sb.append(mNetworkInfo_wlan.toString());
        }
        if(mNetworkInfo_ethernet!=null){
            sb.append("ethernet_info\n");
            sb.append(mNetworkInfo_ethernet.toString());
        }
        tv.setText(sb.toString());
    }

    public void click1(View v){
        mConnectivityManager= (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        mNetworkInfo_wlan=mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        mNetworkInfo_ethernet=mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_ETHERNET);
        showInfo();
    }

    public void click2(View v){
        mNetworkConfiguration=NetworkConfigurationFactory.createNetworkConfiguration(this,NetworkConfigurationFactory.TYPE_ETHERNET);
        Log.e(TAG,"mNetworkConfiguration==null?"+(mNetworkConfiguration==null));
        mStaticIpConfiguration = new StaticIpConfiguration();
        Log.e(TAG,"mStaticIpConfiguration==null?"+(mStaticIpConfiguration==null));
        ((EthernetConfig)mNetworkConfiguration).load();
        testEthernet();
    }
}
