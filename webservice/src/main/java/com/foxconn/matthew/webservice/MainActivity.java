package com.foxconn.matthew.webservice;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class MainActivity extends Activity {

    private static final String TAG=MainActivity.class.getSimpleName();
    //命名空间
    private static final String NAMESPACE = "http://WebXml.com.cn/";
    //WebService地址
    private static String URL = "http://www.webxml.com.cn/webservices/weatherwebservice.asmx";
    //调用的方法名称
    private static final String METHOD_NAME = "getWeatherbyCityName";
    //SOAP ACTION
    private static String SOAP_ACTION = "http://WebXml.com.cn/getWeatherbyCityName";
    private String weatherToday;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClick(View view) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //指定webservice的命名空间和方法名
                    SoapObject so = new SoapObject(NAMESPACE, METHOD_NAME);
                    //传入对应的参数
                    so.addProperty("theCityName", "武汉");
                    //so.addProperty("theUserID","");
                    //生成调用webservice的SOAP信息并指定SOAP版本
                    SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                    envelope.bodyOut = so;
                    //设置是否调用dotNet开发的weibservice
                    envelope.dotNet = true;
                    //等价于envelop.bodyIn=so
                    envelope.setOutputSoapObject(so);
                    HttpTransportSE ht = new HttpTransportSE(URL);
                    ht.debug = true;
                    //调用webservice
                    ht.call(SOAP_ACTION, envelope);
                    //获取返回的数据
                    SoapObject result= (SoapObject) envelope.bodyIn;
                    //detail=result;
                    Log.e(TAG,result.toString());
                    //parseWeather(detail);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
