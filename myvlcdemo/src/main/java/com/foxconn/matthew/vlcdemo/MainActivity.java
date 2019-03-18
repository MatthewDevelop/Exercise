package com.foxconn.matthew.vlcdemo;

import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.media.AudioManager;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;


import org.videolan.libvlc.EventHandler;
import org.videolan.libvlc.IVideoPlayer;
import org.videolan.libvlc.LibVLC;
import org.videolan.libvlc.LibVlcException;
import org.videolan.libvlc.Media;
import org.videolan.util.VLCInstance;

public class MainActivity extends AppCompatActivity implements SurfaceHolder.Callback, IVideoPlayer {
    private static final String TAG = "MainActivity";
    //private static final int SURFACE_SIZE = 1;
    private SurfaceView mSurfaceView;
    private View mLoadingView;
    private LibVLC mMediaPlayer;
    private SurfaceHolder mSurfaceHolder;
    //private int mSurfaceAlign;
    //DisplayMetrics dm;
    private EventHandler eventHandler;
    //private VideoEventHandler mVlcHandler = new VideoEventHandler(this);
    private int mVideoWidth;
    private int mVideoHeight;
    private int mVideoVisiableHeight;
    private int mVideoVisiableWidth;
    private int mSarNum;
    private int mSarDen;


    private static final int SURFACE_BEST_FIT = 0;
    private static final int SURFACE_FIT_HORIZONTAL = 1;
    private static final int SURFACE_FIT_VERTICAL = 2;
    private static final int SURFACE_FILL = 3;
    private static final int SURFACE_16_9 = 4;
    private static final int SURFACE_4_3 = 5;
    private static final int SURFACE_ORIGINAL = 6;
    private int mCurrentSize = SURFACE_BEST_FIT;

    private static final int HANDLER_BUFFER_START = 1;
    private static final int HANDLER_BUFFER_END = 2;
    private static final int HANDLER_SURFACE_SIZE = 3;


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case HANDLER_BUFFER_START:
                    Log.e(TAG, "handleMessage: " + "开始加载");
                    showLoading();
                    break;
                case HANDLER_BUFFER_END:
                    Log.e(TAG, "handleMessage: " + "结束加载");
                    hideLoading();
                    break;
                case HANDLER_SURFACE_SIZE:
                    Log.e(TAG, "handleMessage: " + "size 改变");
                    changeSurfaceSize();
                    break;
                default:
                    break;
            }
        }
    };

    private Handler mVlcHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg == null || msg.getData() == null)
                return;
            switch (msg.getData().getInt("event")) {
                case EventHandler.MediaPlayerTimeChanged:
                    break;
                case EventHandler.MediaPlayerPositionChanged:
                    break;
                case EventHandler.MediaPlayerPlaying:
                    mHandler.removeMessages(HANDLER_BUFFER_END);
                    mHandler.sendEmptyMessage(HANDLER_BUFFER_END);
                    break;
                case EventHandler.MediaPlayerBuffering:
                    break;
                case EventHandler.MediaPlayerLengthChanged:
                    break;
                case EventHandler.MediaPlayerEndReached:
                    break;
                default:
                    break;
            }
        }
    };


    private void hideLoading() {
        mLoadingView.setVisibility(View.GONE);
    }

    private void showLoading() {
        mLoadingView.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSurfaceView = (SurfaceView) findViewById(R.id.video);
        mLoadingView = findViewById(R.id.video_loading);


        //dm = new DisplayMetrics();


        mSurfaceHolder = mSurfaceView.getHolder();
        mSurfaceHolder.setFormat(PixelFormat.RGBX_8888);
        mSurfaceHolder.addCallback(this);
        mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        mSurfaceView.setKeepScreenOn(true);

        try {
            mMediaPlayer = VLCInstance.getLibVlcInstance();
        } catch (LibVlcException e) {
            e.printStackTrace();
            //return;
        }
        this.setVolumeControlStream(AudioManager.STREAM_MUSIC);

        mMediaPlayer.eventVideoPlayerActivityCreated(true);
        eventHandler = EventHandler.getInstance();
        eventHandler.addHandler(mVlcHandler);


        mMediaPlayer.setMediaList();
        mMediaPlayer.getMediaList().add(new Media(mMediaPlayer, LibVLC.PathToURI("/sdcard/Video/a.swf")), false);
        mMediaPlayer.playIndex(0);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mSurfaceView.setKeepScreenOn(false);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mMediaPlayer != null) {
            mMediaPlayer.eventVideoPlayerActivityCreated(false);
            EventHandler em = EventHandler.getInstance();
            em.removeHandler(mVlcHandler);
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        if (mMediaPlayer != null) {
            mSurfaceHolder = surfaceHolder;
            mMediaPlayer.attachSurface(surfaceHolder.getSurface(), this);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int format, int width, int height) {
        mSurfaceHolder = surfaceHolder;
        if (mMediaPlayer != null) {
            mMediaPlayer.attachSurface(surfaceHolder.getSurface(), this);
        }
        if (width > 0) {
            mVideoHeight = height;
            mVideoWidth = width;
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        if (mMediaPlayer != null)
            mMediaPlayer.detachSurface();
    }

    @Override
    public void setSurfaceSize(int width, int height,
                               int visible_width, int visible_height,
                               int sar_num, int sar_den) {
        mVideoHeight = height;
        mVideoWidth = width;
        mVideoVisiableHeight = visible_height;
        mVideoVisiableWidth = visible_width;
        mSarNum = sar_num;
        mSarDen = sar_den;
        mHandler.removeMessages(HANDLER_SURFACE_SIZE);
        mHandler.sendEmptyMessage(HANDLER_SURFACE_SIZE);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        setSurfaceSize(mVideoWidth, mVideoHeight,
                mVideoVisiableWidth, mVideoVisiableHeight, mSarNum, mSarDen);
        super.onConfigurationChanged(newConfig);

    }


    private void changeSurfaceSize() {
        //get screen size
        //int dw = mSurfaceView.getWidth();
        //int dh = mSurfaceView.getHeight();
        int dw = getWindowManager().getDefaultDisplay().getWidth();
        int dh = getWindowManager().getDefaultDisplay().getHeight();

        boolean isPortrait = getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;

        if (dw > dh && isPortrait || dw < dh && !isPortrait) {
            int d = dw;
            dw = dh;
            dh = d;
        }

        if (dw * dh == 0 || mVideoWidth * mVideoHeight == 0) {
            Log.e(TAG, "changeSurfaceSize: " + "invalid surface size");
            return;
        }
        //计算宽高比
        double ar = (double) mVideoWidth / (double) mVideoHeight;
        /*double ar, vw;
        double density = (double) mSarNum / (double) mSarDen;
        if (density == 1.0) {
            vw = mVideoWidth;
            ar = (double) mVideoWidth / (double) mVideoHeight;
        } else {
            vw = mVideoWidth * density;
            ar = vw / mVideoHeight;
        }*/
        //计算显示宽高比
        double dar = (double) dw / (double) dh;

        switch (mCurrentSize) {
            case SURFACE_BEST_FIT:
                if (dar < ar)
                    dh = (int) (dw / ar);
                else
                    dw = (int) (dh * ar);
                break;
            case SURFACE_FIT_HORIZONTAL:
                dh = (int) (dw / ar);
                break;
            case SURFACE_FIT_VERTICAL:
                dw = (int) (dh * ar);
                break;
            case SURFACE_FILL:
                break;
            case SURFACE_16_9:
                ar = 16.0 / 19.0;
                if (dar < ar)
                    dh = (int) (dw / ar);
                else
                    dw = (int) (dh * ar);
                break;
            case SURFACE_4_3:
                ar = 4.0 / 3.0;
                if (dar < ar)
                    dh = (int) (dw / ar);
                else
                    dw = (int) (dh * ar);
                break;
            case SURFACE_ORIGINAL:
                dh = mVideoHeight;
                dw = mVideoWidth;
                break;
        }

        mSurfaceHolder.setFixedSize(mVideoWidth, mVideoHeight);
        ViewGroup.LayoutParams lp = mSurfaceView.getLayoutParams();
        lp.width = dw;
        lp.height = dh;
        mSurfaceView.setLayoutParams(lp);
        mSurfaceView.invalidate();
    }
}
