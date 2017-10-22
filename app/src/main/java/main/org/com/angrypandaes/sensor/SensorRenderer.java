package main.org.com.angrypandaes.sensor;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import main.org.com.angrypandaes.coordinate.MatrixState;

/**
 * Created by 刘志保 on 2017/10/10.
 */

public class SensorRenderer implements GLSurfaceView.Renderer {

    private Context context;

    private CubeShape cubeShape;

    public SensorRenderer(Context context) {
        this.context = context;
        cubeShape=new CubeShape(context);
    }

    public void setXY(float x, float y,float z) {
        cubeShape.setXY(x,y,z);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {

        GLES20.glClearColor(0, 0, 0, 1);
        GLES20.glEnable(GLES20.GL_CULL_FACE);
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);

        cubeShape = new CubeShape(context);

    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {

        GLES20.glViewport(0, 0, width, height);
        float ratio = (float) width / height;

        MatrixState.setProjectFrustum(-ratio, ratio, -1, 1, 1, 30);
        MatrixState.setCamera(0, 0, 3, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
        MatrixState.setInitStack();
    }

    @Override
    public void onDrawFrame(GL10 gl) {

        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);

        MatrixState.pushMatrix();
        cubeShape.draw(0);
        MatrixState.popMatrix();

    }
}
