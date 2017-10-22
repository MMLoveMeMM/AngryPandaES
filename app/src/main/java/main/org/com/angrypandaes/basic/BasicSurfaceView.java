package main.org.com.angrypandaes.basic;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by 刘志保 on 2017/10/1.
 */

public class BasicSurfaceView extends GLSurfaceView implements View.OnTouchListener{

    private BasicRenderer basicRenderer;

    public BasicSurfaceView(Context context) {
        super(context);
        this.setEGLContextClientVersion(2);
        basicRenderer=new BasicRenderer(context);
        setRenderer(basicRenderer);
        /*
        * 主动渲染
        * */
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }
}
