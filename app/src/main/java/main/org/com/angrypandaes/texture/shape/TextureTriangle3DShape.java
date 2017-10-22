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
 * Created by 刘志保 on 2017/10/4.
 */

public class TextureTriangle3DShape extends AbstractTexture {

    private final static float UNIT_SIZE = 1.5f;

    private int mProgram;
    private int mMVPMatrixHandle;
    private int mPositionHandle;
    private int mColorsHandle;

    private Context context;

    private FloatBuffer verticesBuffer;
    private FloatBuffer colorsBuffer;

    private int textureId;

    public TextureTriangle3DShape(Context context){
        this.context = context;

        initVertices();
        initTexture(0);
        initShader();
    }

    @Override
    public void initVertices() {

        float vertices[]=new float[]{
                UNIT_SIZE*1,0,0,
                0,UNIT_SIZE*1,0,
                0,0,UNIT_SIZE*1
        };

        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length*4);
        vbb.order(ByteOrder.nativeOrder());
        verticesBuffer = vbb.asFloatBuffer();
        verticesBuffer.put(vertices);
        verticesBuffer.position(0);

        float colorscoord[]=new float[]{
                1,1,
                0,0,
                0,1
        };
        ByteBuffer cbb = ByteBuffer.allocateDirect(colorscoord.length*4);
        cbb.order(ByteOrder.nativeOrder());
        colorsBuffer=cbb.asFloatBuffer();
        colorsBuffer.put(colorscoord);
        colorsBuffer.position(0);

    }

    @Override
    public void initShader() {

        String vertex_src = ShaderParse.loadFromAssetsFile("texture_triangle3d_vertex.glsl",context.getResources());
        String frag_src = ShaderParse.loadFromAssetsFile("texture_triangle3d_frag.glsl",context.getResources());

        mProgram=ShaderParse.createProgram(vertex_src,frag_src);

        mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram,"uMVPMatrix");
        mPositionHandle = GLES20.glGetAttribLocation(mProgram,"aPosition");
        mColorsHandle = GLES20.glGetAttribLocation(mProgram,"aTextureCoords");

    }

    @Override
    public void initTexture(int type) {

        int textures[] =new int[1];
        GLES20.glGenTextures(1,textures,0);
        textureId = textures[0];
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,textureId);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,GLES20.GL_TEXTURE_MIN_FILTER,GLES20.GL_LINEAR);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,GLES20.GL_TEXTURE_MAG_FILTER,GLES20.GL_NEAREST);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,GLES20.GL_TEXTURE_WRAP_S,GLES20.GL_REPEAT);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,GLES20.GL_TEXTURE_WRAP_T,GLES20.GL_REPEAT);

        Bitmap bitmap=null;
        bitmap = TextureLoadImage.LoadTextureFromAssets(context, R.mipmap.angrypanda);
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D,0,bitmap,0);
        bitmap.recycle();

    }

    @Override
    public void draw(int type) {

        GLES20.glUseProgram(mProgram);
        GLES20.glUniformMatrix4fv(mMVPMatrixHandle,1,false, MatrixState.getFinalMatrix(),0);
        GLES20.glVertexAttribPointer(mPositionHandle,3,GLES20.GL_FLOAT,false,3*4,verticesBuffer);
        GLES20.glVertexAttribPointer(mColorsHandle,2,GLES20.GL_FLOAT,false,2*4,colorsBuffer);

        GLES20.glEnableVertexAttribArray(mPositionHandle);
        GLES20.glEnableVertexAttribArray(mColorsHandle);

        GLES20.glActiveTexture(textureId);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,textureId);

        GLES20.glDrawArrays(GLES20.GL_TRIANGLES,0,3);

    }
}
