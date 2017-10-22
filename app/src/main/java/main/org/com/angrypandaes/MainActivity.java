package main.org.com.angrypandaes;

import android.app.Activity;
import android.os.Bundle;

import main.org.com.angrypandaes.basic.BasicSurfaceView;
import main.org.com.angrypandaes.sensor.gsensor.SensorSurfaceView;
import main.org.com.angrypandaes.shader.vertex.GLVertexSurfaceView;
import main.org.com.angrypandaes.texture.TextureSurfaceView;

public class MainActivity extends Activity {

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    private BasicSurfaceView basicSurfaceView;
    private TextureSurfaceView textureSurfaceView;
    private SensorSurfaceView sensorSurfaceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);

        basicSurfaceView=new BasicSurfaceView(MainActivity.this);
        textureSurfaceView=new TextureSurfaceView(MainActivity.this);
        sensorSurfaceView=new SensorSurfaceView(MainActivity.this);
        setContentView(new GLVertexSurfaceView(MainActivity.this)/*sensorSurfaceView*//*textureSurfaceView*//*basicSurfaceView*/);

    }

    @Override
    protected void onResume() {
        super.onResume();
        basicSurfaceView.onResume();
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();
}
