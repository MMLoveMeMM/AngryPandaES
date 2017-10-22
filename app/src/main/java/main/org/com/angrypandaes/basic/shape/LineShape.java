package main.org.com.angrypandaes.basic.shape;

import android.content.Context;
import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import main.org.com.angrypandaes.basic.shape.abs.AbstractShape;
import main.org.com.angrypandaes.coordinate.MatrixState;
import main.org.com.angrypandaes.shader.ShaderParse;

/**
 * Created by 刘志保 on 2017/10/1.
 */

public class LineShape extends AbstractShape {

    private final static int UNIT = 1;
    private Context context;

    private FloatBuffer verticesBuffer;
    private FloatBuffer colorsBuffer;

    private int mMVPMatrixHandle;
    private int mPositionHandle;
    private int mColorHandle;
    private int mProgram;

    public LineShape(Context context) {

        this.context = context;

        initVertices();
        initShader();

    }

    @Override
    public void initVertices() {

        float verttices[] = new float[]{
                0, 0, 0,
                2, 0, 0,
                0, 1, 0,
                1, 1, 0,
                0, 1, 1
        };

        ByteBuffer vbb = ByteBuffer.allocateDirect(verttices.length * 4); // float byte = 4 byte
        vbb.order(ByteOrder.nativeOrder());
        verticesBuffer = vbb.asFloatBuffer();
        verticesBuffer.put(verttices);
        verticesBuffer.position(0);

        float colors[] = new float[]{
                1, 0, 0, 0,
                0, 1, 0, 0,
                1, 0, 1, 0
        };

        ByteBuffer cbb = ByteBuffer.allocateDirect(colors.length * 4);
        cbb.order(ByteOrder.nativeOrder());
        colorsBuffer = cbb.asFloatBuffer();
        colorsBuffer.put(colors);
        colorsBuffer.position(0);

    }

    @Override
    public void initShader() {

        String vertices_src = ShaderParse.loadFromAssetsFile("basic_line_vertex.glsl", context.getResources());
        String frag_src = ShaderParse.loadFromAssetsFile("basic_line_frag.glsl", context.getResources());

        mProgram = ShaderParse.createProgram(vertices_src, frag_src);

        mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "aPosition");
        mColorHandle = GLES20.glGetAttribLocation(mProgram, "aColor");

    }

    @Override
    public void draw(int type) {

        GLES20.glUseProgram(mProgram);

        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, MatrixState.getFinalMatrix(), 0);
        GLES20.glVertexAttribPointer(mPositionHandle, 3, GLES20.GL_FLOAT, false, 3*4, verticesBuffer);
        GLES20.glVertexAttribPointer(mColorHandle, 4, GLES20.GL_FLOAT, false, 4*4, colorsBuffer);

        GLES20.glEnableVertexAttribArray(mPositionHandle);
        GLES20.glEnableVertexAttribArray(mColorHandle);

        GLES20.glLineWidth(10F);
        switch (type) {
            case GLES20.GL_LINES:
                GLES20.glDrawArrays(GLES20.GL_LINES, 0, 5);
                break;
            case GLES20.GL_LINE_LOOP:
                GLES20.glDrawArrays(GLES20.GL_LINE_LOOP, 0, 5);
                break;
            case GLES20.GL_LINE_STRIP:
                GLES20.glDrawArrays(GLES20.GL_LINE_STRIP, 0, 5);
                break;
            default:
                GLES20.glDrawArrays(GLES20.GL_LINE_LOOP, 0, 5);
                break;
        }

        /*
        * 上面的可以直接如下:
        * */
        //GLES20.glDrawArrays(type,0,5);

        GLES20.glDisableVertexAttribArray(mColorHandle);
        GLES20.glDisableVertexAttribArray(mPositionHandle);

    }
}
