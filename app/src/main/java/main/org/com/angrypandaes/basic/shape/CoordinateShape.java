package main.org.com.angrypandaes.basic.shape;

import android.content.Context;
import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import main.org.com.angrypandaes.coordinate.MatrixState;
import main.org.com.angrypandaes.shader.ShaderParse;

/**
 * Created by 刘志保 on 2017/10/1.
 */

public class CoordinateShape {

    private final static String TAG = "";
    private final static int INT_BYTE = 4;
    private final static int UNIT_SIZE=3;

    private Context context;

    private FloatBuffer vertexBuffer;
    private FloatBuffer colorBuffer;

    private int mProgram;
    private int mMVPMatrix;
    private int mPositionHandle;
    private int mColorHandle;

    private int vertices_len;
    private int color_len;

    private ShortBuffer indicesBuffer;
    private short vertices_indices[] = new short[] {
            0,
            1,
            0,
            2,
            0,
            3
    };

    public CoordinateShape(Context context) {
        this.context = context;

        initVertices();
        initShader();
    }

    private void initVertices() {

        float vertices[]=new float[]{
                0,0,0,
                UNIT_SIZE*1,0,0,
                0,UNIT_SIZE*1,0,
                0,0,UNIT_SIZE*1
        };
        vertices_len = vertices.length;

        ByteBuffer vbb=ByteBuffer.allocateDirect(vertices.length*INT_BYTE);
        vbb.order(ByteOrder.nativeOrder());
        vertexBuffer=vbb.asFloatBuffer();
        vertexBuffer.put(vertices);
        vertexBuffer.position(0);

        float colors[] = new float[] {
                1,0,0,0,
                0,1,0,0,
                0,0,1,0
        };
        color_len = colors.length;

        ByteBuffer cbb=ByteBuffer.allocateDirect(colors.length*INT_BYTE);
        cbb.order(ByteOrder.nativeOrder());
        colorBuffer = cbb.asFloatBuffer();
        colorBuffer.put(colors);
        colorBuffer.position(0);

        indicesBuffer = ByteBuffer.allocateDirect(vertices_indices.length*2)
                        .order(ByteOrder.nativeOrder())
                        .asShortBuffer();
        indicesBuffer.put(vertices_indices);
        indicesBuffer.position(0);

    }

    private void initShader() {

        String vertices_src = ShaderParse.loadFromAssetsFile("basic_coord_vertex.glsl",context.getResources());
        String frag_src = ShaderParse.loadFromAssetsFile("basic_coord_frag.glsl",context.getResources());

        mProgram = ShaderParse.createProgram(vertices_src,frag_src);

        mMVPMatrix = GLES20.glGetUniformLocation(mProgram,"uMVPMatrix");
        mPositionHandle = GLES20.glGetAttribLocation(mProgram,"aPosition");
        mColorHandle = GLES20.glGetAttribLocation(mProgram,"aColor");

    }

    public void draw() {

        GLES20.glUseProgram(mProgram);

        GLES20.glUniformMatrix4fv(mMVPMatrix,1,false, MatrixState.getFinalMatrix(),0);
        GLES20.glVertexAttribPointer(mPositionHandle,3,GLES20.GL_FLOAT,false,3*INT_BYTE,vertexBuffer);
        GLES20.glVertexAttribPointer(mColorHandle,4,GLES20.GL_FLOAT,false,4*INT_BYTE,colorBuffer);

        GLES20.glEnableVertexAttribArray(mPositionHandle);
        GLES20.glEnableVertexAttribArray(mColorHandle);

        GLES20.glLineWidth(10);
        GLES20.glDrawElements(GLES20.GL_LINES,vertices_indices.length,GLES20.GL_UNSIGNED_SHORT,indicesBuffer);

        //GLES20.glDrawArrays(GLES20.GL_LINES,0,vertices_len);

        GLES20.glDisableVertexAttribArray(mColorHandle);
        GLES20.glDisableVertexAttribArray(mPositionHandle);

    }


}
