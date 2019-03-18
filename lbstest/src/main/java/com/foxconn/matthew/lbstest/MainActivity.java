package com.foxconn.matthew.lbstest;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {
    private static final String TAG = "MainActivity";
    public LocationClient mLocationClient;

    private TextView mPositionTv;
    private MapView mapView;
    private BaiduMap baiduMap;
    private boolean isFirstLoacte = true;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    mPositionTv.setText(msg.obj.toString());
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        mLocationClient = new LocationClient(getApplicationContext());
        mLocationClient.registerLocationListener(new MyLocationListener());
        setContentView(R.layout.activity_main);
        mPositionTv = (TextView) findViewById(R.id.position_tv);
        mapView = (MapView) findViewById(R.id.mapView);
        baiduMap = mapView.getMap();
        baiduMap.setMyLocationEnabled(true);
        if (Build.VERSION.SDK_INT >= 23) {
            List<String> permissionList = new ArrayList<>();
            if (ContextCompat.checkSelfPermission(MainActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
            }
            if (ContextCompat.checkSelfPermission(MainActivity.this,
                    Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(Manifest.permission.READ_PHONE_STATE);
            }
            if (ContextCompat.checkSelfPermission(MainActivity.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }
            if (!permissionList.isEmpty()) {
                String[] permissions = permissionList.toArray(new String[permissionList.size()]);
                ActivityCompat.requestPermissions(MainActivity.this, permissions, 1);
            } else {
                requestLocation();
            }
        } else {
            requestLocation();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    private void requestLocation() {
        Log.e(TAG, "requestLocation: ");
        initLocation();
        mLocationClient.start();
    }

    private void initLocation() {
        Log.e(TAG, "initLocation: ");
        LocationClientOption option = new LocationClientOption();
        option.setScanSpan(5000);
        option.setIsNeedAddress(true);
        //强制使用GPS定位
        //option.setLocationMode(LocationClientOption.LocationMode.Device_Sensors);
        mLocationClient.setLocOption(option);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0) {
                    for (int result : grantResults) {
                        if (result != PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(this, "必须同意所有权限才能使用本程序~", Toast.LENGTH_SHORT).show();
                            finish();
                            return;
                        }
                    }
                    requestLocation();
                } else {
                    Toast.makeText(this, "发生未知错误~", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLocationClient.stop();
        mapView.onDestroy();
        baiduMap.setMyLocationEnabled(false);
    }

    public void novigateTo(BDLocation bdLocation) {
        if (isFirstLoacte) {
            Log.e(TAG, "novigateTo: ");
            LatLng latLng = new LatLng(bdLocation.getLatitude(), bdLocation.getLongitude());
            Log.e(TAG, "novigateTo: " + bdLocation.getLatitude() + "    " + bdLocation.getLongitude());
            //同时设置坐标和缩放级别，如果分开设置的话只有后一项会生效
            MapStatusUpdate updateLoc = MapStatusUpdateFactory.newLatLngZoom(latLng, 16f);
            baiduMap.animateMapStatus(updateLoc);
            //MapStatusUpdate updateZoom = MapStatusUpdateFactory.zoomTo(16f);
            //baiduMap.animateMapStatus(updateZoom);
            isFirstLoacte = false;
        }
        //让设备位置显示在地图上
        MyLocationData.Builder myLocationData = new MyLocationData.Builder();
        myLocationData.latitude(bdLocation.getLatitude());
        myLocationData.longitude(bdLocation.getLongitude());
        baiduMap.setMyLocationData(myLocationData.build());
    }

    public class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            Log.e(TAG, "onReceiveLocation: ");
            if (bdLocation.getLocType() == BDLocation.TypeGpsLocation
                    || bdLocation.getLocType() == BDLocation.TypeNetWorkLocation) {
                Log.e(TAG, "定位");
                novigateTo(bdLocation);
            }
            StringBuilder currentionPosition = new StringBuilder();
            currentionPosition.append("纬度：").append(bdLocation.getLatitude()).append(" ");
            currentionPosition.append("经线：").append(bdLocation.getLongitude()).append(" ");
            currentionPosition.append("国家：").append(bdLocation.getCountry()).append(" ");
            currentionPosition.append("省：").append(bdLocation.getProvince()).append(" ");
            currentionPosition.append("市：").append(bdLocation.getCity()).append(" ");
            currentionPosition.append("区：").append(bdLocation.getDistrict()).append(" ");
            currentionPosition.append("街道：").append(bdLocation.getStreet()).append(" ");

            currentionPosition.append("定位方式：");
            if (bdLocation.getLocType() == BDLocation.TypeGpsLocation) {
                currentionPosition.append("GPS").append(" ");
            } else if (bdLocation.getLocType() == BDLocation.TypeNetWorkLocation) {
                currentionPosition.append("网络").append(" ");
            } else if (bdLocation.getLocType() == BDLocation.TypeOffLineLocation) {
                currentionPosition.append("离线定位").append(" ");
            }
            //Log.e(TAG, "onReceiveLocation: " + bdLocation.getLocType());
            //Log.e(TAG, currentionPosition.toString());
            //Toast.makeText(MainActivity.this, currentionPosition, Toast.LENGTH_SHORT).show();
            Log.e(TAG, "onReceiveLocation: " + currentionPosition);
            Message msg = mHandler.obtainMessage();
            msg.obj = currentionPosition;
            msg.what = 1;
            mHandler.sendMessage(msg);
//            mPositionTv.setText(currentionPosition);
        }

        @Override
        public void onConnectHotSpotMessage(String s, int i) {
            //Log.e(TAG, "onConnectHotSpotMessage: "+s );
        }
    }
}
