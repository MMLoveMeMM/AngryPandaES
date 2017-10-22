package main.org.com.angrypandaes.shader.frag;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

/**
 * Created by 刘志保 on 2017/10/22.
 */

public class FragSurfaceView extends GLSurfaceView {

    private TextureRenderer textureRenderer;
    public FragSurfaceView(Context context) {
        super(context);
        textureRenderer=new TextureRenderer(context);
        this.setEGLContextClientVersion(2);
        this.setRenderer(textureRenderer);
        this.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);

    }

}
