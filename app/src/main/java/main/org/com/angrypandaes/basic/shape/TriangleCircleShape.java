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
 * Created by 刘志保 on 2017/10/2.
 */

public class TriangleCircleShape extends AbstractShape {

    // 每个顶点包含的数据个数 （ x 和 y ）
    private static final int POSITION_COMPONENT_COUNT = 3;
    private static final int BYTES_PER_FLOAT = 4;

    private Context context;

    private float rx,ry,rz;
    private float radio;
    private int cnt;

    private int mMVPMatrixHanle;
    private int mPositionHandle;
    private int mColorHandle;

    private int mProgram;

    private FloatBuffer verticesBuffer;
    //private FloatBuffer colorsBuffer;

    public TriangleCircleShape(Context context) {
        this.context = context;

        rx = 0;
        ry = 0;
        rz = 0;
        radio = 0.4f;
        cnt = 80;

        initVertices();
        initShader();
    }

    @Override
    public void initVertices() {

        // 顶点的个数，我们分割count个三角形，有count+1个点，再加上圆心共有count+2个点
        float angdegSpan=360.0f/cnt;
        final int nodeCount = cnt + 2;
        float circleCoords[] = new float[nodeCount * POSITION_COMPONENT_COUNT];
        // x y
        int offset = 0;
        circleCoords[offset++] = rx;// 中心点
        circleCoords[offset++] = ry;
        circleCoords[offset++] = rz;
        for(float angdeg=0; Math.ceil(angdeg)<=360; angdeg+=angdegSpan) {
            double angrad=Math.toRadians(angdeg);//当前弧度
            //当前点
            circleCoords[offset++]=(float) (-Math.sin(angrad));//顶点坐标
            circleCoords[offset++]=(float) (Math.cos(angrad));
            circleCoords[offset++]=0;
        }
        // 为存放形状的坐标，初始化顶点字节缓冲
        ByteBuffer vbb = ByteBuffer.allocateDirect(
                // (坐标数 * 4)float占四字节
                circleCoords.length * BYTES_PER_FLOAT);
        // 设用设备的本点字节序
        vbb.order(ByteOrder.nativeOrder());
        // 从ByteBuffer创建一个浮点缓冲
        verticesBuffer = vbb.asFloatBuffer();
        // 把坐标们加入FloatBuffer中
        verticesBuffer.put(circleCoords);
        // 设置buffer，从第一个坐标开始读
        verticesBuffer.position(0);

    }

    private final static String VERTEX_SRC = "basic_circle_vertex.glsl";
    private final static String FRAG_SRC = "basic_circle_frag.glsl";
    @Override
    public void initShader() {

        String vertices_src = ShaderParse.loadFromAssetsFile(VERTEX_SRC,context.getResources());
        String frag_src = ShaderParse.loadFromAssetsFile(FRAG_SRC,context.getResources());

        mProgram = ShaderParse.createProgram(vertices_src,frag_src);

        mMVPMatrixHanle = GLES20.glGetUniformLocation(mProgram,"uMVPMatrix");
        mPositionHandle = GLES20.glGetAttribLocation(mProgram,"aPosition");
        mColorHandle = GLES20.glGetAttribLocation(mProgram,"aColor");

    }

    @Override
    public void draw(int type) {

        GLES20.glUseProgram(mProgram);

        GLES20.glUniformMatrix4fv(mMVPMatrixHanle,1,false, MatrixState.getFinalMatrix(),0);
        GLES20.glVertexAttribPointer(mPositionHandle,3, GLES20.GL_FLOAT,false, 3*4,verticesBuffer);
        //GLES20.glVertexAttribPointer(mColorHandle,4,GLES20.GL_FLOAT,false,4*4,colorsBuffer);

        GLES20.glEnableVertexAttribArray(mPositionHandle);
        GLES20.glEnableVertexAttribArray(mColorHandle);

        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN,0,cnt+2);

        GLES20.glDisableVertexAttribArray(mPositionHandle);
        GLES20.glDisableVertexAttribArray(mColorHandle);

    }
}
