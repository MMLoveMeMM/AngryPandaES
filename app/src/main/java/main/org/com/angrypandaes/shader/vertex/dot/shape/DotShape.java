package main.org.com.angrypandaes.shader.vertex.dot.shape;

import android.content.Context;
import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import main.org.com.angrypandaes.coordinate.MatrixState;
import main.org.com.angrypandaes.shader.ShaderParse;
import main.org.com.angrypandaes.shader.abs.AbstractVertexTexture;

/**
 * Created by 刘志保 on 2017/10/19.
 */

public class DotShape extends AbstractVertexTexture {

    private Context context;

    private int mProgram;
    private int mMVPMatrixHandle;
    private int mPositionHandle;
    private int mColorHandle;
    private int mDeltaHandle;
    private int mAngleHandle;

    private FloatBuffer verticesBuffer;
    private FloatBuffer colorsBuffer;

    private int vertex_len;

    private float mDelta=0;
    private float mAngle=0;

    public DotShape(Context context){
        this.context=context;

        initVertices();
        initShader();
    }

    public void setDelta(float delta ,float angle){
        mDelta=delta;
        mAngle=angle;
    }

    @Override
    public void initVertices() {

        /*
        * 在x,y轴平面上面测试
        * */
        float[] vertices=new float[]{
            0,0,0,
                0.5f,0.5f,0,
                1,1,0
        };

        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length*4);
        vbb.order(ByteOrder.nativeOrder());
        verticesBuffer=vbb.asFloatBuffer();
        verticesBuffer.put(vertices);
        verticesBuffer.position(0);

        float[] colors=new float[]{
                1,0,0,0,
                0,1,0,0,
                0,0,1,0
        };

        ByteBuffer cbb=ByteBuffer.allocateDirect(colors.length*4);
        cbb.order(ByteOrder.nativeOrder());
        colorsBuffer=cbb.asFloatBuffer();
        colorsBuffer.put(colors);
        colorsBuffer.position(0);

    }

    @Override
    public void initShader() {

        String vertex_src= ShaderParse.loadFromAssetsFile("vertex_dot_vertex.glsl",context.getResources());
        String frag_src=ShaderParse.loadFromAssetsFile("vertex_dot_frag.glsl",context.getResources());

        mProgram=ShaderParse.createProgram(vertex_src,frag_src);

        mMVPMatrixHandle= GLES20.glGetUniformLocation(mProgram,"uMVPMatrix");
        mPositionHandle=GLES20.glGetAttribLocation(mProgram,"aPosition");
        mColorHandle=GLES20.glGetAttribLocation(mProgram,"aColor");

        mDeltaHandle=GLES20.glGetUniformLocation(mProgram,"aDelta");
        mAngleHandle=GLES20.glGetUniformLocation(mProgram,"aAngle");
    }

    @Override
    public void initTexture(int type) {

    }

    @Override
    public void draw(int type) {

        GLES20.glUseProgram(mProgram);

        GLES20.glUniformMatrix4fv(mMVPMatrixHandle,1,false, MatrixState.getFinalMatrix(),0);
        GLES20.glVertexAttribPointer(mPositionHandle,3,GLES20.GL_FLOAT,false,0,verticesBuffer);
        GLES20.glVertexAttribPointer(mColorHandle,4,GLES20.GL_FLOAT,false,0,colorsBuffer);

        GLES20.glUniform1f(mDeltaHandle,mDelta);
        GLES20.glUniform1f(mAngleHandle,mAngle);

        GLES20.glEnableVertexAttribArray(mPositionHandle);
        GLES20.glEnableVertexAttribArray(mColorHandle);

        GLES20.glLineWidth(10);
        GLES20.glDrawArrays(GLES20.GL_POINTS,0,3);

    }

}
