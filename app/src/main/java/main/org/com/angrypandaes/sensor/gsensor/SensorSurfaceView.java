package main.org.com.angrypandaes.sensor.gsensor;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.opengl.GLSurfaceView;
import android.util.Log;

import main.org.com.angrypandaes.sensor.SensorRenderer;

/**
 * Created by 刘志保 on 2017/10/11.
 */

public class SensorSurfaceView extends GLSurfaceView implements SensorEventListener {

    private final float SENSOR_SCALE_FACTOR = 180.0f/320;//角度缩放比例

    private int mPreviousX = 0;
    private int mPreviousY = 0;
    private int mPreviousZ = 0;

    private SensorManager mSensorManager;
    private Sensor mSensor;

    private SensorRenderer sensorRenderer;
    public SensorSurfaceView(Context context) {
        super(context);

        this.setEGLContextClientVersion(2);
        sensorRenderer=new SensorRenderer(context);
        this.setRenderer(sensorRenderer);
        this.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);

        mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);// TYPE_GRAVITY
        if (null == mSensorManager) {
            Log.d("DEV", "deveice not support SensorManager");
        }
        // 参数三，检测的精准度
        mSensorManager.registerListener(this, mSensor,
                SensorManager.SENSOR_DELAY_NORMAL);// SENSOR_DELAY_GAME
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        if (event.sensor == null) {
            return;
        }

        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            int x = (int) event.values[0];
            int y = (int) event.values[1];
            int z = (int) event.values[2];

            int px = Math.abs(mPreviousX - x);
            int py = Math.abs(mPreviousY - y);
            int pz = Math.abs(mPreviousZ - z);

            int maxvalue = getMaxValue(px, py, pz);

            sensorRenderer.setXY(px*SENSOR_SCALE_FACTOR,py*SENSOR_SCALE_FACTOR,pz*SENSOR_SCALE_FACTOR);

            mPreviousX = x;
            mPreviousY = y;
            mPreviousZ = z;

        }

    }

    /**
     * 获取一个最大值
     *
     * @param px
     * @param py
     * @param pz
     * @return
     */
    public int getMaxValue(int px, int py, int pz) {
        int max = 0;
        if (px > py && px > pz) {
            max = px;
        } else if (py > px && py > pz) {
            max = py;
        } else if (pz > px && pz > py) {
            max = pz;
        }
        return max;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
