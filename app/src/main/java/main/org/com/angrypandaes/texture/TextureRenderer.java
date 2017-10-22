package main.org.com.angrypandaes.texture;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import main.org.com.angrypandaes.basic.shape.CoordinateShape;
import main.org.com.angrypandaes.coordinate.MatrixState;
import main.org.com.angrypandaes.texture.shape.TextureCylinderShape;
import main.org.com.angrypandaes.texture.shape.TextureFanShape;
import main.org.com.angrypandaes.texture.shape.TextureTrapeziumShape;
import main.org.com.angrypandaes.texture.shape.TextureTriangle3DShape;
import main.org.com.angrypandaes.texture.shape.TextureTriangleShape;

/**
 * Created by 刘志保 on 2017/10/2.
 */

public class TextureRenderer implements GLSurfaceView.Renderer {

    private Context context;
    private TextureTriangleShape triangle;
    private TextureFanShape textureFanShape;

    private CoordinateShape coordinateShape;
    private TextureTriangle3DShape textureTriangle3DShape;
    private TextureCylinderShape textureCylinderShape;

    private TextureTrapeziumShape textureTrapeziumShape;

    public TextureRenderer(Context context) {
        this.context = context;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {

        GLES20.glClearColor(1,1,1,1); // 这个以白色作为背景,方便纹理的观看
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        GLES20.glEnable(GLES20.GL_CULL_FACE);

        triangle = new TextureTriangleShape(context);
        triangle.initTexture(2);

        textureFanShape = new TextureFanShape(context,40,5);
        textureFanShape.initTexture(0);

        coordinateShape = new CoordinateShape(context);

        textureTriangle3DShape = new TextureTriangle3DShape(context);

        textureCylinderShape = new TextureCylinderShape(context);

        textureTrapeziumShape = new TextureTrapeziumShape(context);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {

        GLES20.glViewport(0,0,width,height);

        float ratio = (float)width/height;

        //调用此方法计算产生透视投影矩阵
        MatrixState.setProjectFrustum(-ratio, ratio, -1, 1, 1, 30);
        //调用此方法产生摄像机9参数位置矩阵
        MatrixState.setCamera(0,0,3,0f,0f,0f,0f,1.0f,0.0f);

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
        //triangle.draw(0);
        MatrixState.popMatrix();

        MatrixState.pushMatrix();
        //textureFanShape.draw(0);
        MatrixState.popMatrix();

        MatrixState.pushMatrix();
        //MatrixState.rotate(-15,0,1,0);
        //coordinateShape.draw();
        MatrixState.popMatrix();

        MatrixState.pushMatrix();
        MatrixState.rotate(-15,0,1,0);
        //textureTriangle3DShape.draw(0);
        MatrixState.popMatrix();

        MatrixState.pushMatrix();
        //MatrixState.rotate(-15,0,1,1);
        //textureCylinderShape.draw(0);
        MatrixState.popMatrix();

        MatrixState.pushMatrix();
        textureTrapeziumShape.draw(0);
        MatrixState.popMatrix();

    }
}
