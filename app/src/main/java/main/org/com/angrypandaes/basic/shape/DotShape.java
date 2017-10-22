package main.org.com.angrypandaes.basic.shape;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLES30;
import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import main.org.com.angrypandaes.coordinate.MatrixState;
import main.org.com.angrypandaes.shader.ShaderParse;

/**
 * Created by 刘志保 on 2017/10/1.
 */

public class DotShape {

    private Context context;

    public DotShape(Context context) {
        this.context = context;

        initVertices();
        initShader();

    }

    private final static int UNIT_SIZE = 1;
    private final static int INT_BYTE = 4;
    private FloatBuffer mVertexBuffer;
    private FloatBuffer mColorBuffer;

    /*
    * x,y,z
    * */
    private float vertices[] = new float[]{
            0, 0, 0,
            1 * UNIT_SIZE, 0, 0,
            1 * UNIT_SIZE, 1 * UNIT_SIZE, 0,
            0, 1 * UNIT_SIZE, 0
    };

    /*
    * R,G,B,A
    * */
    private float coords[] = new float[]{
            1, 1, 0, 0,// 黄
            1, 1, 1, 0,// 白
            0, 1, 0, 0,// 绿
            1, 1, 1, 0,// 白
            1, 1, 0, 0,// 黄
    };

    private void initVertices() {

        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * INT_BYTE);
        vbb.order(ByteOrder.nativeOrder());
        mVertexBuffer = vbb.asFloatBuffer();
        mVertexBuffer.put(vertices);
        mVertexBuffer.position(0);

        ByteBuffer cbb = ByteBuffer.allocateDirect(coords.length * INT_BYTE);
        cbb.order(ByteOrder.nativeOrder());
        mColorBuffer = cbb.asFloatBuffer();
        mColorBuffer.put(coords);
        mColorBuffer.position(0);

    }

    /*
    * shader
    * */
    private final static String VERTEX_SRC = "basic_dot_vertex.glsl";
    private final static String FRAG_SRC = "basic_dot_frag.glsl";

    private int mProgram;

    private int mMVPMatrixHandle;
    private int mPositionHandle;
    private int mColorHandle;

    private void initShader() {

        /*
        * 加载shader程序,编辑编译,链接
        * */
        String vertext_src = ShaderParse.loadFromAssetsFile(VERTEX_SRC, context.getResources());
        String frag_src = ShaderParse.loadFromAssetsFile(FRAG_SRC, context.getResources());

        mProgram = ShaderParse.createProgram(vertext_src, frag_src);

        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "aPosition");
        mColorHandle = GLES20.glGetAttribLocation(mProgram, "aColor");
        mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");

    }

    public void draw() {

        GLES20.glUseProgram(mProgram);

        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, MatrixState.getFinalMatrix(), 0);
        GLES20.glVertexAttribPointer(mPositionHandle, 3, GLES20.GL_FLOAT, false, 3 * 4, mVertexBuffer);
        GLES20.glVertexAttribPointer(mColorHandle, 4, GLES20.GL_FLOAT, false, 4 * 4, mColorBuffer);

        GLES20.glEnableVertexAttribArray(mPositionHandle);
        GLES20.glEnableVertexAttribArray(mColorHandle);

        GLES20.glLineWidth(10.0F);
        GLES20.glDrawArrays(GLES20.GL_POINTS, 0, vertices.length);
        //GLES20.glDrawArrays(GLES20.GL_LINE_STRIP,0,vertices.length);

        GLES20.glDisableVertexAttribArray(mPositionHandle);
        GLES20.glDisableVertexAttribArray(mColorHandle);

    }

}
