package main.org.com.angrypandaes.shader.vertex;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

import main.org.com.angrypandaes.shader.vertex.dot.DotRenderer;

/**
 * Created by 刘志保 on 2017/10/19.
 */

public class GLVertexSurfaceView extends GLSurfaceView{

    private DotRenderer mRenderer;
    public GLVertexSurfaceView(Context context) {
        super(context);
        mRenderer=new DotRenderer(context);
        this.setEGLContextClientVersion(2);
        this.setRenderer(mRenderer);
        this.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }
}
