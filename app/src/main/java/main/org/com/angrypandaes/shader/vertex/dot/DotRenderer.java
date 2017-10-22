package main.org.com.angrypandaes.shader.vertex.dot;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import main.org.com.angrypandaes.coordinate.MatrixState;
import main.org.com.angrypandaes.shader.vertex.dot.shape.DotShape;

/**
 * Created by 刘志保 on 2017/10/19.
 */

public class DotRenderer implements GLSurfaceView.Renderer {

    private float delta=0;
    private float xAngle=0;
    private float sAngle=0;
    private int direction=0;

    private Context context;
    private DotShape dotShape;

    private Timer timer;

    public DotRenderer(Context context){
        this.context=context;
    }
    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {

        GLES20.glClearColor(0,0,0,0);
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        GLES20.glEnable(GLES20.GL_CULL_FACE);

        timer = new Timer();
        timer.schedule(new Task(), 1 * 1000,100);//第三个参数不能够小于刷新帧的间隔

        dotShape=new DotShape(context);

    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {

            GLES20.glViewport(0,0,width,height);
            float ratio=(float) width/height;

            //调用此方法计算产生透视投影矩阵
            MatrixState.setProjectFrustum(-ratio, ratio, -1, 1, 1, 30);
            //调用此方法产生摄像机9参数位置矩阵
            MatrixState.setCamera(0,0,6,0f,0f,0f,0f,1.0f,0.0f);

            /*
            * 初始化基坐标
            * 这个作为每个物体变换的参考相对坐标起点
            * */
            MatrixState.setInitStack();

    }

    @Override
    public void onDrawFrame(GL10 gl) {

        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);

        MatrixState.pushMatrix();
        dotShape.setDelta(0,xAngle);
        dotShape.draw(0);
        MatrixState.popMatrix();

    }

    class Task extends TimerTask{

        @Override
        public void run() {

            if(delta>=1.0){
                direction=-1;
            }else if(delta<0){
                direction=0;
            }
            if(direction==0){
                delta+=0.1;

            }else{
                delta-=0.1;
            }

            sAngle+=18;
            xAngle=(float)Math.PI*sAngle/180.0f;
            Log.i("ANGLE","xAngle : "+xAngle);

        }
    }

}
