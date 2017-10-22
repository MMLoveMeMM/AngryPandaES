package main.org.com.angrypandaes.shader.abs;

/**
 * Created by 刘志保 on 2017/10/19.
 */

public abstract class AbstractVertexTexture {

    public abstract void initVertices();
    public abstract void initShader();
    public abstract void initTexture(int type);
    public abstract void draw(int type);

}
