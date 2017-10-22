package main.org.com.angrypandaes.basic.shape.abs;

/**
 * Created by 刘志保 on 2017/10/2.
 */

public abstract class AbstractShape {

    public final static int FLOAT_BYTE = 4;

    public abstract void initVertices();
    public abstract void initShader();
    public abstract void draw(int type);

}
