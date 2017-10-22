package main.org.com.angrypandaes.basic;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import main.org.com.angrypandaes.basic.shape.CoordinateShape;
import main.org.com.angrypandaes.basic.shape.DotShape;
import main.org.com.angrypandaes.basic.shape.FanShape;
import main.org.com.angrypandaes.basic.shape.LineShape;
import main.org.com.angrypandaes.basic.shape.TriangleCircleShape;
import main.org.com.angrypandaes.basic.shape.TriangleShape;
import main.org.com.angrypandaes.coordinate.MatrixState;

/**
 * Created by 刘志保 on 2017/10/1.
 */

public class BasicRenderer implements GLSurfaceView.Renderer{

    private DotShape dotShape;
    private CoordinateShape coordinateShape;
    private LineShape lineShape;
    private TriangleShape triangleShape;
    private TriangleCircleShape triangleCircleShape;
    private FanShape fanShape;

    private Context context;

    public BasicRenderer(Context context){
        this.context = context;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {

        GLES20.glClearColor(0,0,0,1);

        GLES20.glEnable(GLES20.GL_DEPTH_TEST);

        GLES20.glEnable(GLES20.GL_CULL_FACE);

        dotShape = new DotShape(context);
        coordinateShape = new CoordinateShape(context);
        lineShape = new LineShape(context);
        triangleCircleShape = new TriangleCircleShape(context);

        triangleShape = new TriangleShape(context);

        /*
        * 第一个参数 : 上下文
        * 第二个参数 : 扇形数目
        * 第三个参数 : 每个扇形角度
        * */
        fanShape = new FanShape(context,30,5);

    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {

        GLES20.glViewport(0,0,width,height);

        float ratio = (float) width/height;

        /*
        * 设置好相机坐标
        * */
        MatrixState.setCamera(0, 0/*8f*/, 30, 0f, 0f, 0f, 0f, 1.0f, 0.0f);

        /*
        * 设置透视矩阵
        * */
        MatrixState.setProjectFrustum(-ratio, ratio, -1, 1, 20, 100);

        /*
        * 初始化基坐标
        * 这个作为每个物体变换的参考相对坐标起点
        * */
        MatrixState.setInitStack();

    }

    @Override
    public void onDrawFrame(GL10 gl) {

        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT|GLES20.GL_COLOR_BUFFER_BIT);

        MatrixState.pushMatrix();
        dotShape.draw();
        MatrixState.popMatrix();

        MatrixState.pushMatrix();
        MatrixState.translate(0.2f,0.5f,1.0f);
        //coordinateShape.draw();
        MatrixState.popMatrix();

        MatrixState.pushMatrix();
        //lineShape.draw(3);
        MatrixState.popMatrix();

        MatrixState.pushMatrix();
        //triangleShape.draw(5);
        MatrixState.popMatrix();

        MatrixState.pushMatrix();
        //triangleCircleShape.draw(0);
        MatrixState.popMatrix();

        MatrixState.pushMatrix();
        fanShape.draw(0);
        MatrixState.popMatrix();

    }
}
