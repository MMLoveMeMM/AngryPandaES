package main.org.com.angrypandaes.shader.vertex;

import android.app.Activity;
import android.os.Bundle;

public class VertexActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new GLVertexSurfaceView(VertexActivity.this));
    }
}
