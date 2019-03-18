package edu.whpu.matthew.multidownload;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends Activity {
    private static final String TAG = "MainActivity";
    //要开启的线程数
    private int threadcount=3;
    private ProgressBar progressBar;
    private TextView progressTv;
    private int currentProgress;
    private int successThread=0;
//    private String filename="eclipse-inst-win64.exe";
    private String filename="ic_20170801.jpg";
//    private String path="http://10.203.146.158:8080/Test/"+filename;
    private String path="http://10.203.147.113:8080/MyServlet/update/"+filename;
    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //计算文本进度并更新UI
            progressTv.setText(progressBar.getProgress()*100/progressBar.getMax()+"%");
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressBar= (ProgressBar) findViewById(R.id.progressBar);
        progressTv= (TextView) findViewById(R.id.progressTV);
    }

    public void click(View view){
        Log.e(TAG, "click: " );
        Thread t=new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    URL url=new URL(path);
                    HttpURLConnection httpURLConnection= (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("GET");
                    httpURLConnection.setConnectTimeout(5000);
                    httpURLConnection.setReadTimeout(5000);
                    Log.e(TAG, "run: "+httpURLConnection.getResponseCode() );

                    if(httpURLConnection.getResponseCode()==201){
                        InputStream inputStream=httpURLConnection.getInputStream();
                        FileOutputStream fileOutputStream=new FileOutputStream(new File(Environment.getExternalStorageDirectory(),filename));
                        int len;
                        byte[] buff=new byte[1024];
                        while ((len=inputStream.read(buff))!=-1){
                            Log.e(TAG, "run: "+len );
                            fileOutputStream.write(buff,0,len);
                        }
                        Log.e(TAG, "run: over" );
                        inputStream.close();
                        fileOutputStream.close();
                    }

                    if(httpURLConnection.getResponseCode()==200){
                        //获取要下载的内容的大小
                        long length=httpURLConnection.getContentLength();
                        //设置进度条的最大值
                        progressBar.setMax((int) length);
                        //创建下载临时文件
                        File file=new File(Environment.getExternalStorageDirectory(),filename);
                        RandomAccessFile randomAccessFile=new RandomAccessFile(file,"rwd");
                        //设置临时文件的大小（临时文件大小和要下载的文件大小一致，起占用空间的作用，下载过程中将数据写入临时文件）
                        randomAccessFile.setLength(length);
                        randomAccessFile.close();
                        //计算下载区间大小
                        long size=length/threadcount;
                        for(int threadId=0;threadId<threadcount;threadId++){
                            //计算 下载区间的开始位置和结束位置
                            long startIndex=threadId*size;
                            long endIndex=size*(threadId+1)-1;
                            //最后一个下载区间的结束位置为length-1
                            if(threadId==threadcount-1){
                                endIndex=length-1;
                            }
                            //创建线程下载文件
                            new DownLoadThread(threadId,startIndex,endIndex).start();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        t.start();
    }

    private class DownLoadThread extends Thread{
        private int threadId;
        private long startIndex;
        private long endIndex;

        public DownLoadThread(int threadId, long startIndex, long endIndex) {
            this.threadId = threadId;
            this.startIndex = startIndex;
            this.endIndex = endIndex;
        }
        //下载
        @Override
        public void run() {
            super.run();
            try {
                File progressfile=new File(Environment.getExternalStorageDirectory(),
                        threadId+".txt");
                //判断是否为中断下载
                if(progressfile.exists()){
                    BufferedReader br=new BufferedReader(new InputStreamReader(
                            new FileInputStream(progressfile)));
                    int lastprogress=Integer.parseInt(br.readLine());
                    br.close();
                    startIndex+=lastprogress;
                    currentProgress+=lastprogress;
                    progressBar.setProgress(currentProgress);
                    mHandler.sendEmptyMessage(1);
                }
                //创建并初始化链接对象
                URL url=new URL(path);
                HttpURLConnection conn= (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setReadTimeout(5000);
                conn.setConnectTimeout(5000);
                //设置链接请求数据区间
                conn.setRequestProperty("Range","bytes="+startIndex+"-"+endIndex);
                //请求部分数据的响应码
                if(conn.getResponseCode()==206){
                    InputStream inputStream=conn.getInputStream();
                    File file=new File(Environment.getExternalStorageDirectory(),filename);
                    RandomAccessFile randomAccessFile=new RandomAccessFile(file,"rwd");
                    //写入文件指定位置
                    randomAccessFile.seek(startIndex);
                    byte[] buffer=new byte[1024];
                    int len = 0;
                    int total=0;
                    //读取流中的数据
                    while ((len=inputStream.read(buffer))!=-1){
                        randomAccessFile.write(buffer,0, len);
                        currentProgress+=len;
                        total+=len;
                        //将当前进度写入临时文件
                        RandomAccessFile progress=new RandomAccessFile(progressfile,"rwd");
                        progress.write((total+"").getBytes());
                        progress.close();
                        //设置进度条
                        progressBar.setProgress(currentProgress);
                        //更新文本进度
                        mHandler.sendEmptyMessage(0);
                    }
                    randomAccessFile.close();
                    inputStream.close();
                    successThread++;
                    //判断是否所有线程都下载完成
                    synchronized (path) {//避免线程安全问题，
                        if (successThread == threadcount) {
                            for (int i = 0; i < threadcount; i++) {
                                //删除临时计算进度文件
                                File fileToDelete = new File(Environment.getExternalStorageDirectory(), i + ".txt");
                                fileToDelete.delete();
                            }
                            //初始化，避免重复删除文件
                            successThread = 0;
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
