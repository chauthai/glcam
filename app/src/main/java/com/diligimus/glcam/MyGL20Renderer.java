package com.diligimus.glcam;

import android.graphics.SurfaceTexture;
import android.opengl.EGLConfig;
import android.opengl.GLES11Ext;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import javax.microedition.khronos.opengles.GL10;


/**
 * Created by chau on 04.03.15.
 */
public class MyGL20Renderer implements GLSurfaceView.Renderer
{

    DirectVideo mDirectVideo;
    int texture;
    private SurfaceTexture surface;
    CameraActivity delegate;

    public MyGL20Renderer(CameraActivity _delegate)
    {
        delegate = _delegate;
    }

    public void onSurfaceCreated(GL10 unused, javax.microedition.khronos.egl.EGLConfig config)
    {
        texture = createTexture();
        mDirectVideo = new DirectVideo(texture);
        GLES20.glClearColor(0.5f, 0.5f, 0.5f, 1.0f);
        delegate.startCamera(texture);
    }

    public void onDrawFrame(GL10 unused)
    {
        float[] mtx = new float[16];
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        surface.updateTexImage();
        surface.getTransformMatrix(mtx);

        mDirectVideo.draw();
    }

    public void onSurfaceChanged(GL10 unused, int width, int height)
    {
        GLES20.glViewport(0, 0, width, height);
    }

    static public int loadShader(int type, String shaderCode)
    {
        int shader = GLES20.glCreateShader(type);

        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);

        return shader;
    }

    static private int createTexture()
    {
        int[] texture = new int[1];

        GLES20.glGenTextures(1,texture, 0);
        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, texture[0]);
        GLES20.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
                GL10.GL_TEXTURE_MIN_FILTER,GL10.GL_LINEAR);
        GLES20.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
                GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
        GLES20.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
                GL10.GL_TEXTURE_WRAP_S, GL10.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
                GL10.GL_TEXTURE_WRAP_T, GL10.GL_CLAMP_TO_EDGE);

        return texture[0];
    }

    public void setSurface(SurfaceTexture _surface)
    {
        surface = _surface;
    }
}
