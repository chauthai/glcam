package com.diligimus.glcam;

import android.content.Context;
import android.opengl.GLSurfaceView;

/**
 * Created by chau on 04.03.15.
 */
class MyGLSurfaceView extends GLSurfaceView
{
    MyGL20Renderer renderer;
    public MyGLSurfaceView(Context context)
    {
        super(context);

        setEGLContextClientVersion(2);

        renderer = new MyGL20Renderer((CameraActivity)context);
        setRenderer(renderer);
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);

    }
    public MyGL20Renderer getRenderer()
    {
        return renderer;
    }
}
