package main.org.com.angrypandaes.sensor;

import android.content.Context;
import android.opengl.GLES20;
import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import main.org.com.angrypandaes.coordinate.MatrixState;
import main.org.com.angrypandaes.shader.ShaderParse;
import main.org.com.angrypandaes.texture.abs.AbstractTexture;

/**
 * Created by 刘志保 on 2017/10/10.
 */

public class CubeShape extends AbstractTexture {

    private Context context;

    private int mProgram;
    private int mMVPMatrixHandle;
    private int mPositionHandle;
    private int mTexturesHandle;

    private FloatBuffer verticesBuffer;
    private FloatBuffer textureBuffer;

    private int textureID;
    private int vertices_len;

    private float mAngleX = 0, mAngleY = 0, mAngleZ = 0;

    public CubeShape(Context context) {
        this.context = context;

        initVertices();
        initShader();
        initTexture(0);

    }

    public void setXY(float x, float y,float z) {
        mAngleZ += x;
        mAngleY += y;
        Log.i("ANGLE","x : "+mAngleX+" y : "+mAngleY);
    }

    @Override
    public void initVertices() {

        float vertices[] = new float[]{
                //前面
                0, 0, 1.0f,
                1.0f, 1.0f, 1.0f,
                -1.0f, 1.0f, 1.0f,
                0, 0, 1.0f,
                -1.0f, 1.0f, 1.0f,
                -1.0f, -1.0f, 1.0f,
                0, 0, 1.0f,
                -1.0f, -1.0f, 1.0f,
                1.0f, -1.0f, 1.0f,
                0, 0, 1.0f,
                1.0f, -1.0f, 1.0f,
                1.0f, 1.0f, 1.0f,
                //后面
                0, 0, -1.0f,
                1.0f, 1.0f, -1.0f,
                1.0f, -1.0f, -1.0f,
                0, 0, -1.0f,
                1.0f, -1.0f, -1.0f,
                -1.0f, -1.0f, -1.0f,
                0, 0, -1.0f,
                -1.0f, -1.0f, -1.0f,
                -1.0f, 1.0f, -1.0f,
                0, 0, -1.0f,
                -1.0f, 1.0f, -1.0f,
                1.0f, 1.0f, -1.0f,
                //左面
                -1.0f, 0, 0,
                -1.0f, 1.0f, 1.0f,
                -1.0f, 1.0f, -1.0f,
                -1.0f, 0, 0,
                -1.0f, 1.0f, -1.0f,
                -1.0f, -1.0f, -1.0f,
                -1.0f, 0, 0,
                -1.0f, -1.0f, -1.0f,
                -1.0f, -1.0f, 1.0f,
                -1.0f, 0, 0,
                -1.0f, -1.0f, 1.0f,
                -1.0f, 1.0f, 1.0f,
                //右面
                1.0f, 0, 0,
                1.0f, 1.0f, 1.0f,
                1.0f, -1.0f, 1.0f,
                1.0f, 0, 0,
                1.0f, -1.0f, 1.0f,
                1.0f, -1.0f, -1.0f,
                1.0f, 0, 0,
                1.0f, -1.0f, -1.0f,
                1.0f, 1.0f, -1.0f,
                1.0f, 0, 0,
                1.0f, 1.0f, -1.0f,
                1.0f, 1.0f, 1.0f,
                //上面
                0, 1.0f, 0,
                1.0f, 1.0f, 1.0f,
                1.0f, 1.0f, -1.0f,
                0, 1.0f, 0,
                1.0f, 1.0f, -1.0f,
                -1.0f, 1.0f, -1.0f,
                0, 1.0f, 0,
                -1.0f, 1.0f, -1.0f,
                -1.0f, 1.0f, 1.0f,
                0, 1.0f, 0,
                -1.0f, 1.0f, 1.0f,
                1.0f, 1.0f, 1.0f,
                //下面
                0, -1.0f, 0,
                1.0f, -1.0f, 1.0f,
                -1.0f, -1.0f, 1.0f,
                0, -1.0f, 0,
                -1.0f, -1.0f, 1.0f,
                -1.0f, -1.0f, -1.0f,
                0, -1.0f, 0,
                -1.0f, -1.0f, -1.0f,
                1.0f, -1.0f, -1.0f,
                0, -1.0f, 0,
                1.0f, -1.0f, -1.0f,
                1.0f, -1.0f, 1.0f
        };
        vertices_len=vertices.length/3;
        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
        vbb.order(ByteOrder.nativeOrder());
        verticesBuffer = vbb.asFloatBuffer();
        verticesBuffer.put(vertices);
        verticesBuffer.position(0);

        //顶点颜色值数组，每个顶点4个色彩值RGBA
        float colors[] = new float[]{
                //前面
                1, 1, 1, 0,//中间为白色
                1, 0, 0, 0,
                1, 0, 0, 0,
                1, 1, 1, 0,//中间为白色
                1, 0, 0, 0,
                1, 0, 0, 0,
                1, 1, 1, 0,//中间为白色
                1, 0, 0, 0,
                1, 0, 0, 0,
                1, 1, 1, 0,//中间为白色
                1, 0, 0, 0,
                1, 0, 0, 0,
                //后面
                1, 1, 1, 0,//中间为白色
                0, 0, 1, 0,
                0, 0, 1, 0,
                1, 1, 1, 0,//中间为白色
                0, 0, 1, 0,
                0, 0, 1, 0,
                1, 1, 1, 0,//中间为白色
                0, 0, 1, 0,
                0, 0, 1, 0,
                1, 1, 1, 0,//中间为白色
                0, 0, 1, 0,
                0, 0, 1, 0,
                //左面
                1, 1, 1, 0,//中间为白色
                1, 0, 1, 0,
                1, 0, 1, 0,
                1, 1, 1, 0,//中间为白色
                1, 0, 1, 0,
                1, 0, 1, 0,
                1, 1, 1, 0,//中间为白色
                1, 0, 1, 0,
                1, 0, 1, 0,
                1, 1, 1, 0,//中间为白色
                1, 0, 1, 0,
                1, 0, 1, 0,
                //右面
                1, 1, 1, 0,//中间为白色
                1, 1, 0, 0,
                1, 1, 0, 0,
                1, 1, 1, 0,//中间为白色
                1, 1, 0, 0,
                1, 1, 0, 0,
                1, 1, 1, 0,//中间为白色
                1, 1, 0, 0,
                1, 1, 0, 0,
                1, 1, 1, 0,//中间为白色
                1, 1, 0, 0,
                1, 1, 0, 0,
                //上面
                1, 1, 1, 0,//中间为白色
                0, 1, 0, 0,
                0, 1, 0, 0,
                1, 1, 1, 0,//中间为白色
                0, 1, 0, 0,
                0, 1, 0, 0,
                1, 1, 1, 0,//中间为白色
                0, 1, 0, 0,
                0, 1, 0, 0,
                1, 1, 1, 0,//中间为白色
                0, 1, 0, 0,
                0, 1, 0, 0,
                //下面
                1, 1, 1, 0,//中间为白色
                0, 1, 1, 0,
                0, 1, 1, 0,
                1, 1, 1, 0,//中间为白色
                0, 1, 1, 0,
                0, 1, 1, 0,
                1, 1, 1, 0,//中间为白色
                0, 1, 1, 0,
                0, 1, 1, 0,
                1, 1, 1, 0,//中间为白色
                0, 1, 1, 0,
                0, 1, 1, 0,
        };

        ByteBuffer cbb = ByteBuffer.allocateDirect(colors.length * 4);
        cbb.order(ByteOrder.nativeOrder());
        textureBuffer = cbb.asFloatBuffer();
        textureBuffer.put(colors);
        textureBuffer.position(0);

    }

    @Override
    public void initShader() {

        String vertex_src = ShaderParse.loadFromAssetsFile("sensor_cube_vertex.glsl", context.getResources());
        String frag_src = ShaderParse.loadFromAssetsFile("sensor_cube_frag.glsl", context.getResources());

        mProgram=ShaderParse.createProgram(vertex_src,frag_src);

        mMVPMatrixHandle= GLES20.glGetUniformLocation(mProgram,"uMVPMatrix");
        mPositionHandle=GLES20.glGetAttribLocation(mProgram,"aPosition");
        mTexturesHandle=GLES20.glGetAttribLocation(mProgram,"aColors");

    }

    @Override
    public void initTexture(int type) {
        // todo
    }

    @Override
    public void draw(int type) {

        GLES20.glUseProgram(mProgram);

        //MatrixState.setInitStack();
        MatrixState.rotate(mAngleX,1,0,0);
        MatrixState.rotate(mAngleY,0,1,0);
        MatrixState.rotate(mAngleZ,0,0,1);

        GLES20.glUniformMatrix4fv(mMVPMatrixHandle,1,false, MatrixState.getFinalMatrix(),0);
        GLES20.glVertexAttribPointer(mPositionHandle,3,GLES20.GL_FLOAT,false,0,verticesBuffer);
        GLES20.glVertexAttribPointer(mTexturesHandle,4,GLES20.GL_FLOAT,false,0,textureBuffer);

        GLES20.glEnableVertexAttribArray(mPositionHandle);
        GLES20.glEnableVertexAttribArray(mTexturesHandle);

        GLES20.glLineWidth(10);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES,0,vertices_len);

    }
}
