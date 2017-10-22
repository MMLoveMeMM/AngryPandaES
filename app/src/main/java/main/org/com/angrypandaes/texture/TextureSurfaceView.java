package main.org.com.angrypandaes.texture;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;
import android.view.View;

import main.org.com.angrypandaes.texture.shape.TextureCylinderShape;

/**
 * Created by 刘志保 on 2017/10/2.
 */

public class TextureSurfaceView extends GLSurfaceView{

    private TextureRenderer renderer;
    public TextureSurfaceView(Context context) {
        super(context);
        this.setEGLContextClientVersion(2);
        renderer = new TextureRenderer(context);
        this.setRenderer(renderer);
        this.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
    }

    private final float TOUCH_SCALE_FACTOR = 180.0f/320;//角度缩放比例
    private float mPreviousY;//上次的触控位置Y坐标
    private float mPreviousX;//上次的触控位置X坐标

    @Override
    public boolean onTouchEvent(MotionEvent e) {
            float y = e.getY();
            float x = e.getX();
            switch (e.getAction()) {
                case MotionEvent.ACTION_MOVE:
                    float dy = y - mPreviousY;//计算触控笔Y位移
                    float dx = x - mPreviousX;//计算触控笔X位移
                    TextureCylinderShape.yAngle += dx * TOUCH_SCALE_FACTOR;//设置纹理矩形绕y轴旋转角度
                    TextureCylinderShape.zAngle+= dy * TOUCH_SCALE_FACTOR;//设置纹理矩形绕z轴旋转角度
            }
            mPreviousY = y;//记录触控笔位置
            mPreviousX = x;//记录触控笔位置
            return true;
    }
}
