package main.org.com.angrypandaes.coordinate;

import android.opengl.Matrix;

import java.nio.ByteBuffer;

import main.org.com.angrypandaes.utils.Constants;

/**
 * Created by 刘志保 on 2017/10/1.
 * Model : 物体坐标,程序里面给的那些顶点(如画线,画球等)坐标都是Model的.
 * View : 观察者坐标/相机
 * Proj : 投影矩阵,分为正交投影和透视投影
 *
 * 首先 : 要获得物体在观察者的坐标系中对应的坐标,P = M*V,即将两个矩阵相乘
 * 然后 : 照相机/观察者纳入到投影视野中,即将这两个矩阵相乘,就可以得到总投影.
 */

public class MatrixState {

    private static float[] mProjMatrix = new float[16];//4x4矩阵 投影用
    private static float[] mVMatrix = new float[16];//摄像机位置朝向9参数矩阵
    private static float[] currMatrix;//当前变换矩阵

    //保护变换矩阵的栈
    static float[][] mStack=new float[10][16];
    static int stackTop=-1;

    public static void setInitStack()//获取不变换初始矩阵
    {
        currMatrix=new float[16];
        Matrix.setRotateM(currMatrix, 0, 0, 1, 0, 0);
    }

    public static void pushMatrix()//保护变换矩阵
    {
        stackTop++;
        for(int i=0;i<16;i++)
        {
            mStack[stackTop][i]=currMatrix[i];
        }
    }

    public static void popMatrix()//恢复变换矩阵
    {
        for(int i=0;i<16;i++)
        {
            currMatrix[i]=mStack[stackTop][i];
        }
        stackTop--;
    }

    public static void translate(float x,float y,float z)//设置沿xyz轴移动
    {
        Matrix.translateM(currMatrix, 0, x, y, z);
    }

    public static void rotate(float angle,float x,float y,float z)//设置绕xyz轴转动
    {
        Matrix.rotateM(currMatrix,0,angle,x,y,z);
    }

    //缩放变换
    public static void scale(float x,float y,float z)
    {
        Matrix.scaleM(currMatrix,0, x, y, z);
    }

    //设置摄像机
    static ByteBuffer llbb= ByteBuffer.allocateDirect(3* Constants.INT_BYTE);
    static float[] cameraLocation=new float[3];//摄像机位置
    public static void setCamera
    (
            float cx,	//摄像机位置x
            float cy,   //摄像机位置y
            float cz,   //摄像机位置z
            float tx,   //摄像机目标点x
            float ty,   //摄像机目标点y
            float tz,   //摄像机目标点z
            float upx,  //摄像机UP向量X分量
            float upy,  //摄像机UP向量Y分量
            float upz   //摄像机UP向量Z分量
    )
    {
        Matrix.setLookAtM
                (
                        mVMatrix,
                        0,
                        cx,
                        cy,
                        cz,
                        tx,
                        ty,
                        tz,
                        upx,
                        upy,
                        upz
                );
    }

    //设置透视投影参数
    public static void setProjectFrustum
    (
            float left,		//near面的left
            float right,    //near面的right
            float bottom,   //near面的bottom
            float top,      //near面的top
            float near,		//near面距离
            float far       //far面距离
    )
    {
        Matrix.frustumM(mProjMatrix, 0, left, right, bottom, top, near, far);
    }

    //设置正交投影参数
    public static void setProjectOrtho
    (
            float left,		//near面的left
            float right,    //near面的right
            float bottom,   //near面的bottom
            float top,      //near面的top
            float near,		//near面距离
            float far       //far面距离
    )
    {
        Matrix.orthoM(mProjMatrix, 0, left, right, bottom, top, near, far);
    }

    //获取具体物体的总变换矩阵
    static float[] mMVPMatrix=new float[16];
    public static float[] getFinalMatrix()
    {
        Matrix.multiplyMM(mMVPMatrix, 0, mVMatrix, 0, currMatrix, 0);
        Matrix.multiplyMM(mMVPMatrix, 0, mProjMatrix, 0, mMVPMatrix, 0);
        return mMVPMatrix;
    }

    //获取具体物体的变换矩阵
    public static float[] getMMatrix()
    {
        return currMatrix;
    }

}
