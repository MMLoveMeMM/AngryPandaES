package main.org.com.angrypandaes.basic.shape;

import android.content.Context;
import android.opengl.GLES20;
import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import main.org.com.angrypandaes.basic.shape.abs.AbstractShape;
import main.org.com.angrypandaes.coordinate.MatrixState;
import main.org.com.angrypandaes.shader.ShaderParse;

/**
 * Created by 刘志保 on 2017/10/2.
 */

public class TriangleShape extends AbstractShape{

    private Context context;

    private int mProgram;
    private int mMVPMatrixHandle;
    private int mPositionHandle;
    private int mColorHandle;

    private FloatBuffer verticesBuffer;
    private FloatBuffer colorsBuffer;

    private int vertices_len =0;

    public TriangleShape(Context context) {

        this.context = context;
        initVertices();
        initShader();

    }
    @Override
    public void initVertices() {
        float vertices[] = new float[] {
                1.0f,0.0f,0.0f,
                0.0f,1.0f,0.0f,
                0.0f,0.0f,1.0f,
                1.0f,0.0f,1.0f,
                0.5f,1.0f,0.0f,
                0.5f,1.0f,1.0f
        };

        vertices_len = vertices.length;

        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
        vbb.order(ByteOrder.nativeOrder());
        verticesBuffer = vbb.asFloatBuffer();
        verticesBuffer.put(vertices);
        verticesBuffer.position(0);

        float colors[] = new float[] {
                1,0,0,0,
                0,1,0,0,
                0,0,1,0,
                0,1,0,0,
                0,0,1,0,
                0,0,1,0
        };

        ByteBuffer cbb = ByteBuffer.allocateDirect(colors.length * 4);
        cbb.order(ByteOrder.nativeOrder());
        colorsBuffer = cbb.asFloatBuffer();
        colorsBuffer.put(colors);
        colorsBuffer.position(0);

    }

    private final static String VERTEX_SRC = "basic_triangle_vertex.glsl";
    private final static String FRAG_SRC = "basic_triangle_frag.glsl";

    @Override
    public void initShader() {

        String vertices_src = ShaderParse.loadFromAssetsFile(VERTEX_SRC,context.getResources());
        String frag_src = ShaderParse.loadFromAssetsFile(FRAG_SRC,context.getResources());

        Log.i("SRC",vertices_src);
        mProgram = ShaderParse.createProgram(vertices_src,frag_src);

        mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram,"uMVPMatrix");
        mPositionHandle = GLES20.glGetAttribLocation(mProgram,"aPosition");
        mColorHandle = GLES20.glGetAttribLocation(mProgram,"aColor");

    }

    @Override
    public void draw(int type) {

        GLES20.glUseProgram(mProgram);

        GLES20.glUniformMatrix4fv(mMVPMatrixHandle,1,false, MatrixState.getFinalMatrix(),0);
        GLES20.glVertexAttribPointer(mPositionHandle,3,GLES20.GL_FLOAT,false,3*4,verticesBuffer);
        GLES20.glVertexAttribPointer(mColorHandle,4,GLES20.GL_FLOAT,false,4*4,colorsBuffer);

        GLES20.glEnableVertexAttribArray(mPositionHandle);
        GLES20.glEnableVertexAttribArray(mColorHandle);

        switch (type) {
            case GLES20.GL_TRIANGLES:
                GLES20.glDrawArrays(GLES20.GL_TRIANGLES,0,vertices_len/3);
                break;
            case GLES20.GL_TRIANGLE_STRIP:
                GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP,0,vertices_len/3);
                break;
            case GLES20.GL_TRIANGLE_FAN:
                GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN,0,vertices_len/3);
                break;
                default:
                    GLES20.glDrawArrays(GLES20.GL_TRIANGLES,0,vertices_len/3);
        }

        GLES20.glDisableVertexAttribArray(mColorHandle);
        GLES20.glDisableVertexAttribArray(mPositionHandle);

    }
}
