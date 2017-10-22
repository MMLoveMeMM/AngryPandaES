package main.org.com.angrypandaes.texture.abs;

/**
 * Created by 刘志保 on 2017/10/2.
 */

public abstract class AbstractTexture{

    public abstract void initVertices();
    public abstract void initShader();
    public abstract void initTexture(int type);
    public abstract void draw(int type);

}
