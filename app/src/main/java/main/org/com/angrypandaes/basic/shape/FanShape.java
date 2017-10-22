package main.org.com.angrypandaes.basic.shape;

import android.content.Context;
import android.opengl.GLES20;
import android.util.Log;
import android.view.ViewDebug;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import main.org.com.angrypandaes.basic.shape.abs.AbstractShape;
import main.org.com.angrypandaes.coordinate.MatrixState;
import main.org.com.angrypandaes.shader.ShaderParse;

/**
 * Created by 刘志保 on 2017/10/3.
 */

public class FanShape extends AbstractShape {

    private Context context;

    private int mProgram;
    private int mMVPMatrixHandle;
    private int mPositionHandle;
    private int mColorsHandle;

    private FloatBuffer verticesBuffer;
    private FloatBuffer colorsBuffer;

    private int cnt = 0;//产生弧形的数量
    private int coordination_q = 3; // 采用坐标系维护,这里仍然采用3维
    private float angle = 15; // 弧度的角度大小,初始化15度/每弧
    private float ratio = 0.6f; // 圆弧半径

    public FanShape(Context context,int count,float ang) {

        this.context = context;
        cnt = count;
        angle = ang;

        initVertices();
        initShader();
    }

    @Override
    public void initVertices() {

        float vertices[] = new float[cnt * coordination_q * 3]; // 每个弧度都是由3个顶点组成,每个顶点又是由x,y,z三个值组成坐标
        int offset = 0;
        vertices[offset++] = 0;
        vertices[offset++] = 0;
        vertices[offset++] = 0;
        double angrad=0;
        for (int i = 0; i < cnt+1; i++) {
            angrad=Math.toRadians(angle*i);//当前弧度
            vertices[offset++] = (float) (ratio * Math.cos(angrad));
            vertices[offset++] = (float) (ratio * Math.sin(angrad));
            vertices[offset++] = 0;
        }

        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length*4);
        vbb.order(ByteOrder.nativeOrder());
        verticesBuffer = vbb.asFloatBuffer();
        verticesBuffer.put(vertices);
        verticesBuffer.position(0);

        float colors[] = new float[] {
                0,1,0,0
        };

        ByteBuffer cbb = ByteBuffer.allocateDirect(colors.length*4);
        cbb.order(ByteOrder.nativeOrder());
        colorsBuffer = cbb.asFloatBuffer();
        colorsBuffer.put(colors);
        colorsBuffer.position(0);

    }

    @Override
    public void initShader() {

        String vertex_src = ShaderParse.loadFromAssetsFile("basic_fan_vertex.glsl",context.getResources());
        String frag_src = ShaderParse.loadFromAssetsFile("basic_fan_frag.glsl", context.getResources());

        mProgram = ShaderParse.createProgram(vertex_src,frag_src);

        mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram,"uMVPMatrix");
        mPositionHandle = GLES20.glGetAttribLocation(mProgram,"aPosition");
        mColorsHandle = GLES20.glGetAttribLocation(mProgram,"aColor");

    }

    @Override
    public void draw(int type) {

        GLES20.glUseProgram(mProgram);

        GLES20.glUniformMatrix4fv(mMVPMatrixHandle,1,false, MatrixState.getFinalMatrix(),0);
        GLES20.glVertexAttribPointer(mPositionHandle,3,GLES20.GL_FLOAT,false,3*4,verticesBuffer);
        GLES20.glVertexAttribPointer(mColorsHandle,4,GLES20.GL_FLOAT,false,4*4,colorsBuffer);

        GLES20.glEnableVertexAttribArray(mPositionHandle);
        GLES20.glEnableVertexAttribArray(mColorsHandle);

        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN,0,cnt+2);

    }

}
