package com.diligimus.glcam;

import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.io.IOException;


public class CameraActivity extends ActionBarActivity implements SurfaceTexture.OnFrameAvailableListener{

    private Camera mCamera;
    private MyGLSurfaceView glSurfaceView;
    private SurfaceTexture surface;
    MyGL20Renderer renderer;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        glSurfaceView = new MyGLSurfaceView(this);
        renderer = glSurfaceView.getRenderer();
        setContentView(glSurfaceView);
    }

    public void startCamera(int texture)
    {
        surface = new SurfaceTexture(texture);
        surface.setOnFrameAvailableListener(this);
        renderer.setSurface(surface);

        mCamera = Camera.open();

        try
        {
            mCamera.setPreviewTexture(surface);
            mCamera.startPreview();
        }
        catch (IOException ioe)
        {
            Log.w("MainActivity","CAM LAUNCH FAILED");
        }
    }

    public void onFrameAvailable(SurfaceTexture surfaceTexture)
    {
        glSurfaceView.requestRender();
    }

    @Override
    public void onPause()
    {
        mCamera.stopPreview();
        mCamera.release();
        System.exit(0);
    }
}
