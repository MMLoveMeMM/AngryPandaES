package main.org.com.angrypandaes.texture.shape;

import android.content.Context;
import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.opengl.GLUtils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import main.org.com.angrypandaes.R;
import main.org.com.angrypandaes.coordinate.MatrixState;
import main.org.com.angrypandaes.shader.ShaderParse;
import main.org.com.angrypandaes.texture.TextureLoadImage;
import main.org.com.angrypandaes.texture.abs.AbstractTexture;

/**
 * Created by 刘志保 on 2017/10/9.
 * 这个地方画一个四边,然后更加清楚纹理坐标和物体坐标的对应关系
 * 提供是一张功夫熊猫的图,运行可以看出即使按照两个独立三角形拼接画成四边形
 * 只要纹理坐标设置对应了,图片纹理正确显示
 * 对应 : 顶点和纹理点一一对印,并且画顶点的顺序和纹理顶点顺序要保持一致
 */

public class TextureTrapeziumShape extends AbstractTexture {

    private Context context;

    private int mProgram;
    private int mMVPMatrixHandle;
    private int mPositionHandle;
    private int mTextureCoords;

    private FloatBuffer veritcesBuffer;
    private FloatBuffer coordsBuffer;

    private int textureID;

    private int vertices_len;

    public TextureTrapeziumShape(Context context) {
        this.context = context;
        initVertices1();
        initShader();
        initTexture(0);
    }

    /*
    * 两个四边形,一个在,xy面,一个在yz面,均正向,用一张图片作纹理铺开
    * 由这个联想扩展圆筒,球形等.
    * */
    public void initVertices1() {

        float vertices[] = new float[]{
                /*
                * x,y 平面
                * */
                0, 0, 0,
                0.5f, 0, 0,
                0, 1, 0,

                0.5f, 0, 0,
                0.5f, 1, 0,
                0, 1, 0,

                /*
                * y,z 平面
                * */
                0, 0, 0.5f,
                0, 0, 0,
                0, 1, 0.5f,

                0, 0, 0,
                0, 1, 0,
                0, 1, 0.5f
        };
        vertices_len = vertices.length / 3;
        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
        vbb.order(ByteOrder.nativeOrder());
        veritcesBuffer = vbb.asFloatBuffer();
        veritcesBuffer.put(vertices);
        veritcesBuffer.position(0);

        float coords[] = new float[]{
                /*
                * x,y 平面
                * */
                0.5f, 1,
                1, 1,
                0.5f, 0,

                1, 1,
                1, 0,
                0.5f, 0,
                /*
                * z,y 平面
                * */
                0, 1,
                0.5f, 1,
                0, 0,

                0.5f, 1,
                0.5f, 0,
                0, 0

        };

        ByteBuffer cbb = ByteBuffer.allocateDirect(coords.length * 4);
        cbb.order(ByteOrder.nativeOrder());
        coordsBuffer = cbb.asFloatBuffer();
        coordsBuffer.put(coords);
        coordsBuffer.position(0);

    }


    /*
    * 这里只画了一个四边形,在xy面
    * */
    @Override
    public void initVertices() {

        float vertices[] = new float[]{
                0, 0, 0,
                1, 0, 0,
                0, 1, 0,

                1, 0, 0,
                1, 1, 0,
                0, 1, 0
        };
        vertices_len = vertices.length / 3;
        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
        vbb.order(ByteOrder.nativeOrder());
        veritcesBuffer = vbb.asFloatBuffer();
        veritcesBuffer.put(vertices);
        veritcesBuffer.position(0);

        float[] coords = new float[]{
                0, 1,
                1, 1,
                0, 0,

                1, 1,
                1, 0,
                0, 0
        };
        ByteBuffer cbb = ByteBuffer.allocateDirect(coords.length * 4);
        cbb.order(ByteOrder.nativeOrder());
        coordsBuffer = cbb.asFloatBuffer();
        coordsBuffer.put(coords);
        coordsBuffer.position(0);

    }

    @Override
    public void initShader() {

        String vertex_src = ShaderParse.loadFromAssetsFile("texture_trapezium_vertex.glsl", context.getResources());
        String frag_src = ShaderParse.loadFromAssetsFile("texture_trapezium_frag.glsl", context.getResources());

        mProgram = ShaderParse.createProgram(vertex_src, frag_src);

        mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "aPosition");
        mTextureCoords = GLES20.glGetAttribLocation(mProgram, "aTextureCoords");

    }

    @Override
    public void initTexture(int type) {

        int textures[] = new int[1];
        GLES20.glGenTextures(1, textures, 0);
        textureID = textures[0];
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureID);

        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);

        Bitmap bitmap = null;
        bitmap = TextureLoadImage.LoadTextureFromAssets(context, R.mipmap.angrypanda);

        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);

        bitmap.recycle();
    }

    @Override
    public void draw(int type) {

        GLES20.glUseProgram(mProgram);

        /*
        * 由于相机摆在z轴正向上,所以要将物体按照y轴顺时针旋转45,这样就能够看到xy,yz里面的图像了
        * 顺便把相机放近一点,相对renderer初始化里面
        * */
        MatrixState.setCamera(0,0,2,0f,0f,0f,0f,1.0f,0.0f);
        MatrixState.setInitStack();
        MatrixState.rotate(-45,0,1,0);

        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, MatrixState.getFinalMatrix(), 0);
        GLES20.glVertexAttribPointer(mPositionHandle, 3, GLES20.GL_FLOAT, false, 3 * 4, veritcesBuffer);
        GLES20.glVertexAttribPointer(mTextureCoords, 2, GLES20.GL_FLOAT, false, 2 * 4, coordsBuffer);

        GLES20.glEnableVertexAttribArray(mPositionHandle);
        GLES20.glEnableVertexAttribArray(mTextureCoords);

        GLES20.glActiveTexture(textureID);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureID);

        GLES20.glLineWidth(10f);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertices_len);

    }
}
