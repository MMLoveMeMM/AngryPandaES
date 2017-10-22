package main.org.com.angrypandaes.texture.shape;

import android.content.Context;
import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import main.org.com.angrypandaes.R;
import main.org.com.angrypandaes.coordinate.MatrixState;
import main.org.com.angrypandaes.shader.ShaderParse;
import main.org.com.angrypandaes.texture.TextureLoadImage;
import main.org.com.angrypandaes.texture.abs.AbstractTexture;

/**
 * Created by 刘志保 on 2017/10/4.
 */

public class TextureCylinderShape extends AbstractTexture {

    private final static float UNIT_SIZE = 1.0F;
    private final static float H = 2.0F;

    private Context context;

    private int mProgram;
    private int mMVPMatrixHandle;
    private int mPositionHandle;
    private int mColorsHandle;

    private FloatBuffer vertiecsBuffer;
    private FloatBuffer colorsBuffer;

    private int textureId;

    private float angle = 5; //每个间隔的角度大小
    private int num = 100; //分割数目

    private float ratio = 1.0f;

    public static float xAngle = 0;//绕x轴旋转的角度
    public static float yAngle = 0;//绕y轴旋转的角度
    public static float zAngle = 0;//绕z轴旋转的角度

    public TextureCylinderShape(Context context) {

        this.context = context;

        initVertices();
        initTexture(0);
        initShader();

    }

    @Override
    public void initVertices() {

        float vertices[] = new float[2 * 3 * 3 * num];
        int offset = 0;

        /*
        * Pn,Pn+1 在x,z轴平面
        * An = (R*cos@*n,H,R*sin@*n);
        * Pn = (R*cos@*n,0,R*sin@*n);
        * Pn+1 = (R*cos@*(n+1),0,R*sin@*(n+1));
        * */
        double angrad;
        double angrad1;
        for (int i = 0; i < num; i++) {
            angrad = Math.toRadians(angle * i);//当前弧度
            angrad1 = Math.toRadians(angle * (i + 1));//当前弧度

            /*
            * An Pn Pn+1 Pn Bn Pn+1 这个顺序不能变
            * */
            /*
            * Pn
            * */
            vertices[offset++] = (float) (ratio * Math.cos(angrad));
            vertices[offset++] = 0;
            vertices[offset++] = (float) (ratio * Math.sin(angrad));
            /*
            * An
            * */
            vertices[offset++] = (float) (ratio * Math.cos(angrad));
            vertices[offset++] = H;
            vertices[offset++] = (float) (ratio * Math.sin(angrad));

            /*
            * Pn+1
            * */
            vertices[offset++] = (float) (ratio * Math.cos(angrad1));
            vertices[offset++] = 0;
            vertices[offset++] = (float) (ratio * Math.sin(angrad1));

            /*
            * 下面是第二个三角形Pn Bn Pn+1
            * */
            vertices[offset++] = (float) (ratio * Math.cos(angrad));
            vertices[offset++] = H;
            vertices[offset++] = (float) (ratio * Math.sin(angrad));

            vertices[offset++] = (float) (ratio * Math.cos(angrad1));
            vertices[offset++] = H;
            vertices[offset++] = (float) (ratio * Math.sin(angrad1));

            vertices[offset++] = (float) (ratio * Math.cos(angrad1));
            vertices[offset++] = 0;
            vertices[offset++] = (float) (ratio * Math.sin(angrad1));

        }

        for (int i = 0; i < vertices.length; i++) {
            Log.i("BYTE", "i : " + i + " " + vertices[i] + "");
        }

        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
        vbb.order(ByteOrder.nativeOrder());
        vertiecsBuffer = vbb.asFloatBuffer();
        vertiecsBuffer.put(vertices);
        vertiecsBuffer.position(0);

        float colorcoords[] = new float[num * 2 * 3 * 2];
        offset = 0;
        for (int i = 0; i < num; i++) {
            angrad = Math.toRadians(angle * i);//当前弧度
            angrad1 = Math.toRadians(angle * (i + 1));//当前弧度
            /*
            * An Pn Pn+1 Pn Bn Pn+1 这个顺序不能变
            * */
            /*
            * An
            * */
            colorcoords[offset++] = (float) (2 * ratio * Math.sin(angrad / 2));
            colorcoords[offset++] = 0;
            /*
            * Pn
            * */
            colorcoords[offset++] = (float) (2 * ratio * Math.sin(angrad / 2));
            colorcoords[offset++] = 1;

            /*
            * Pn+1
            * */
            colorcoords[offset++] = (float) (2 * ratio * Math.sin(angrad1 / 2));
            colorcoords[offset++] = 1;

            /*
            * Pn
            * */
            colorcoords[offset++] = (float) (2 * ratio * Math.sin(angrad / 2));
            colorcoords[offset++] = 1;
            /*
            * Bn
            * */
            colorcoords[offset++] = (float) (2 * ratio * Math.sin(angrad1 / 2));
            colorcoords[offset++] = 0;
            /*
            * Pn+1
            * */
            colorcoords[offset++] = (float) (2 * ratio * Math.sin(angrad1 / 2));
            colorcoords[offset++] = 1;

        }

        ByteBuffer cbb = ByteBuffer.allocateDirect(colorcoords.length * 4);
        cbb.order(ByteOrder.nativeOrder());
        colorsBuffer = cbb.asFloatBuffer();
        colorsBuffer.put(colorcoords);
        colorsBuffer.position(0);

    }

    @Override
    public void initShader() {

        String vertex_src = ShaderParse.loadFromAssetsFile("texture_cylinder_vertex.glsl", context.getResources());
        String frag_src = ShaderParse.loadFromAssetsFile("texture_cylinder_frag.glsl", context.getResources());

        mProgram = ShaderParse.createProgram(vertex_src, frag_src);

        mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "aPosition");
        mColorsHandle = GLES20.glGetAttribLocation(mProgram, "aTextCoords");

    }

    @Override
    public void initTexture(int type) {

        int textures[] = new int[1];
        GLES20.glGenTextures(1, textures, 0);

        textureId = textures[0];

        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);

        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_REPEAT);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_REPEAT);

        Bitmap bitmap = null;
        bitmap = TextureLoadImage.LoadTextureFromAssets(context, R.mipmap.angrypanda);
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
        bitmap.recycle();

    }

    @Override
    public void draw(int type) {

        GLES20.glUseProgram(mProgram);

        //设置绕y轴旋转
        MatrixState.rotate(yAngle, 0, 1, 0);
        //设置绕z轴旋转
        MatrixState.rotate(zAngle, 0, 0, 1);
        //设置绕x轴旋转
        MatrixState.rotate(xAngle, 1, 0, 0);

        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, MatrixState.getFinalMatrix(), 0);
        GLES20.glVertexAttribPointer(mPositionHandle, 3, GLES20.GL_FLOAT, false, 3 * 4, vertiecsBuffer);
        GLES20.glVertexAttribPointer(mColorsHandle, 2, GLES20.GL_FLOAT, false, 2 * 4, colorsBuffer);

        GLES20.glEnableVertexAttribArray(mPositionHandle);
        GLES20.glEnableVertexAttribArray(mColorsHandle);

        GLES20.glActiveTexture(textureId);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);

        GLES20.glLineWidth(10);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, /*2**/2 * 3 * num);
        //GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, /*2**/num);

    }
}
