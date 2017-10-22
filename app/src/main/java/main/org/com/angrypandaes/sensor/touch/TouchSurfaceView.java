package main.org.com.angrypandaes.sensor.touch;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

import main.org.com.angrypandaes.sensor.SensorRenderer;

/**
 * Created by 刘志保 on 2017/10/9.
 */

public class TouchSurfaceView extends GLSurfaceView{

    private final float TOUCH_SCALE_FACTOR = 180.0f/320;//角度缩放比例

    private Context context;
    private SensorRenderer sensorRenderer;
    public TouchSurfaceView(Context context) {
        super(context);
        this.context = context;
        sensorRenderer=new SensorRenderer(context);
        this.setEGLContextClientVersion(2);
        this.setRenderer(sensorRenderer);
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);

    }

    private float mPreviousY=0;//上次的触控位置Y坐标
    private float mPreviousX=0;//上次的触控位置X坐标
    private float mPreviousZ=0;//上次的触控位置Z坐标

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        float y=event.getY();
        float x=event.getX();
        switch (event.getAction()){
            case MotionEvent.ACTION_MOVE:
                float dy = y - mPreviousY;//计算触控笔Y位移
                float dx = x - mPreviousX;//计算触控笔X位移
                sensorRenderer.setXY(dx*TOUCH_SCALE_FACTOR,dy*TOUCH_SCALE_FACTOR,mPreviousZ);
                break;
        }
        mPreviousY = y;//记录触控笔位置
        mPreviousX = x;//记录触控笔位置
        return true;
    }
}
