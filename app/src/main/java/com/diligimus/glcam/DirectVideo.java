package com.diligimus.glcam;

import android.opengl.GLES11Ext;
import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

/**
 * Created by chau on 04.03.15.
 */
public class DirectVideo {


    private final String vertexShaderCode =
            "attribute vec4 position;" +
                    "attribute vec2 inputTextureCoordinate;" +
                    "varying vec2 textureCoordinate;" +
                    "void main()" +
                    "{"+
                    "gl_Position = position;"+
                    "textureCoordinate = inputTextureCoordinate;" +
                    "}";

    private final String fragmentShaderCode =
            "#extension GL_OES_EGL_image_external : require\n"+
                    "precision mediump float;" +
                    "varying vec2 textureCoordinate;                            \n" +
                    "uniform samplerExternalOES s_texture;               \n" +
                    "void main() {" +
                    "  gl_FragColor = texture2D( s_texture, textureCoordinate );\n" +
                    "}";

    private FloatBuffer vertexBuffer, textureVerticesBuffer;
    private ShortBuffer drawListBuffer;
    private final int mProgram;
    private int mPositionHandle;
    private int mColorHandle;
    private int mTextureCoordHandle;


    // number of coordinates per vertex in this array
    static final int COORDS_PER_VERTEX = 2;
    static float squareVertices[] = { -1.0f, 1.0f, -1.0f, -1.0f, 1.0f, -1.0f, 1.0f, 1.0f };
    // in counterclockwise order:
    float textureVertices[] = { 0.0f, 0.0f, 1.0f, 0.0f, 1.0f, 1.0f, 0.0f, 1.0f, };

    private short drawOrder[] = { 0, 1, 2, 0, 2, 3 }; // order to draw vertices



    private final int vertexStride = COORDS_PER_VERTEX * 4; // 4 bytes per vertex

    private int texture;

    public DirectVideo(int _texture)
    {
        texture = _texture;

        ByteBuffer bb = ByteBuffer.allocateDirect(squareVertices.length * 4);
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(squareVertices);
        vertexBuffer.position(0);

        ByteBuffer dlb = ByteBuffer.allocateDirect(drawOrder.length * 2);
        dlb.order(ByteOrder.nativeOrder());
        drawListBuffer = dlb.asShortBuffer();
        drawListBuffer.put(drawOrder);
        drawListBuffer.position(0);

        ByteBuffer bb2 = ByteBuffer.allocateDirect(textureVertices.length * 4);
        bb2.order(ByteOrder.nativeOrder());
        textureVerticesBuffer = bb2.asFloatBuffer();
        textureVerticesBuffer.put(textureVertices);
        textureVerticesBuffer.position(0);

        int vertexShader = MyGL20Renderer.loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode);
        int fragmentShader = MyGL20Renderer.loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode);

        mProgram = GLES20.glCreateProgram();             // create empty OpenGL ES Program
        GLES20.glAttachShader(mProgram, vertexShader);   // add the vertex shader to program
        GLES20.glAttachShader(mProgram, fragmentShader); // add the fragment shader to program
        GLES20.glLinkProgram(mProgram);

        GLES20.glVertexAttribPointer(mTextureCoordHandle, COORDS_PER_VERTEX, GLES20.GL_FLOAT, false,vertexStride, textureVerticesBuffer);
    }

    public void draw()
    {
        GLES20.glUseProgram(mProgram);

        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, texture);

        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "position");
        GLES20.glEnableVertexAttribArray(mPositionHandle);
        GLES20.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX, GLES20.GL_FLOAT, false,vertexStride, vertexBuffer);

        mTextureCoordHandle = GLES20.glGetAttribLocation(mProgram, "inputTextureCoordinate");
        GLES20.glEnableVertexAttribArray(mTextureCoordHandle);

        mColorHandle = GLES20.glGetAttribLocation(mProgram, "s_texture");

        GLES20.glDrawElements(GLES20.GL_TRIANGLES, drawOrder.length,
                GLES20.GL_UNSIGNED_SHORT, drawListBuffer);

        // Disable vertex array
        GLES20.glDisableVertexAttribArray(mPositionHandle);
        GLES20.glDisableVertexAttribArray(mTextureCoordHandle);
    }
}
