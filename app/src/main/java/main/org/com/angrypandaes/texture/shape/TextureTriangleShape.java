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
 * Created by 刘志保 on 2017/10/2.
 */

public class TextureTriangleShape extends AbstractTexture {

    private Context context;

    private int mProgram;
    private int mMVPMatrixHandle;
    private int mPositionHandle;
    private int mColorHandle;

    private FloatBuffer verticesBuffer;
    private FloatBuffer colorsBuffer;

    private int vCount = 0;
    private int textureID;

    public TextureTriangleShape(Context context) {
        this.context = context;

        initVertices();
        initShader();
    }

    @Override
    public void initVertices() {

        vCount = 3;
        final float UNIT_SIZE = 0.15f;
        float vertices[] = new float[]
                {
                        -11 * UNIT_SIZE, 11 * UNIT_SIZE, 0,
                        -11 * UNIT_SIZE, -11 * UNIT_SIZE, 0,
                        11 * UNIT_SIZE, 11 * UNIT_SIZE, 0,

                        -11 * UNIT_SIZE, -11 * UNIT_SIZE, 0,
                        11 * UNIT_SIZE, -11 * UNIT_SIZE, 0,
                        11 * UNIT_SIZE, 11 * UNIT_SIZE, 0

                };

        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
        vbb.order(ByteOrder.nativeOrder());
        verticesBuffer = vbb.asFloatBuffer();
        verticesBuffer.put(vertices);
        verticesBuffer.position(0);

        float colors[] = new float[]//顶点颜色值数组，每个顶点4个色彩值RGBA
                {
                    0,0,//对应顶点一
                    0,2,//对应顶点二
                        2,0,//对应顶点三,下面一次类推

                        0,2,
                        2,2,
                        2,0
                    /*0.5f * 2, 0,
                        0, 1 * 2,
                        1 * 2, 1 * 2*/
                };

        ByteBuffer cbb = ByteBuffer.allocateDirect(colors.length * 4);
        cbb.order(ByteOrder.nativeOrder());
        colorsBuffer = cbb.asFloatBuffer();
        colorsBuffer.put(colors);
        colorsBuffer.position(0);

    }

    @Override
    public void initShader() {

        String vertices_src = ShaderParse.loadFromAssetsFile("texture_triangle_vertex.glsl", context.getResources());
        String colors_src = ShaderParse.loadFromAssetsFile("texture_triangle_frag.glsl", context.getResources());

        mProgram = ShaderParse.createProgram(vertices_src, colors_src);

        mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "aPosition");
        mColorHandle = GLES20.glGetAttribLocation(mProgram, "aColorCoord");

    }

    @Override
    public void initTexture(int type) {

        int textures[] = new int[1];
        GLES20.glGenTextures(1, textures, 0);

        textureID = textures[0];
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureID);

        /*
        * texture sample
        * */
        /*
        * S T
        * 这个在纹理坐标系大于1的时候,处理图片是以重复方式贴图还是尽量贴
        * */
        switch (type) {
            case 0:
                GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
                GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
                break;
            case 1:
                GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_REPEAT);
                GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_REPEAT);
                break;
            case 2:
                GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
                GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_REPEAT);
                break;
            case 3:
                GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_REPEAT);
                GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
                break;
                default:
                    break;
        }
        /*
        * filter
        * */
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);// 最近采样
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
        //GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR); // 线性采样
        //GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);

        Bitmap bitmap = null;
        bitmap = TextureLoadImage.LoadTextureFromAssets(context, R.mipmap.angrypanda);

        //实际加载纹理
        GLUtils.texImage2D
        (
                GLES20.GL_TEXTURE_2D,//纹理类型，在OpenGL ES中必须为GL10.GL_TEXTURE_2D
                0,//纹理的层次，0表示基本图像层，可以理解为直接贴图
                bitmap,//纹理图像
                0   //纹理边框尺寸
        );
        bitmap.recycle();   //纹理加载成功后释放图片

    }

    @Override
    public void draw(int type) {

        GLES20.glUseProgram(mProgram);

        GLES20.glUniformMatrix4fv(mMVPMatrixHandle,1,false, MatrixState.getFinalMatrix(),0);
        GLES20.glVertexAttribPointer(mPositionHandle,3,GLES20.GL_FLOAT,false,3*4,verticesBuffer);
        GLES20.glVertexAttribPointer(mColorHandle,2,GLES20.GL_FLOAT,false,2*4,colorsBuffer);

        GLES20.glEnableVertexAttribArray(mPositionHandle);
        GLES20.glEnableVertexAttribArray(mColorHandle);

        GLES20.glActiveTexture(textureID);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,textureID);

        GLES20.glDrawArrays(GLES20.GL_TRIANGLES,0,6);

    }
}
