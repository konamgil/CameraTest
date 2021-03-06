package com.camera.com.cameratest;

import android.content.Context;
import android.hardware.Camera;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Size;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Button;

import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    MyCameraSurface mSurface;
    Button mShutter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    class MyCameraSurface extends SurfaceView implements SurfaceHolder.Callback {
        SurfaceHolder mHolder;
        Camera mCamera;

        public MyCameraSurface(Context context) {
            super(context);
            mHolder = getHolder();
            mHolder.addCallback(this);
        }

        //표면 생성시 카메라 오픈하고 미리보기 설정
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            mCamera = Camera.open();
            try {
                mCamera.setPreviewDisplay(mHolder);
            } catch (IOException e) {
                mCamera.release();
                mCamera = null;
            }
        }
        //표면 파괴시 카메라도 파괴한다
        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            if(mCamera != null){
                mCamera.stopPreview();
                mCamera.release();
                mCamera = null;
            }
        }

        //표면의 크기가 결정될 때 최적의 미리보기 크기를 구해 설정한다
        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            Camera.Parameters params = mCamera.getParameters();
            List<Camera.Size> arSize = params.getSupportedPreviewSizes();
            if(arSize == null){
                params.setPreviewSize(width, height);
            } else {
                int diff = 10000;
                Size opti = null;
                for(Camera.Size s : arSize){
                    if(Math.abs(s.height - height)<diff){
                        opti = s;
                    };
                }
            }
        }
    }
}
