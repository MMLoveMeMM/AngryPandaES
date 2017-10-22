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
 * Created by 刘志保 on 2017/10/3.
 */

public class TextureFanShape extends AbstractTexture {

    private Context context;

    private int mProgram;
    private int mMVPMatrixHandle;
    private int mPositionHandle;
    private int mColorsHandle;

    private FloatBuffer verticesBuffer;
    private FloatBuffer colorsBuffer;

    private float angle = 15; // 每段弧的角度
    private int num = 1; // 弧段的数量
    private int coordition_num = 3; // 采用3维
    private float ratio = 1f;

    private int textureId;

    public TextureFanShape(Context context,int num,int angle) {

        this.context = context;

        this.num=num;
        this.angle=angle;

        initVertices();
        initShader();

    }

    @Override
    public void initVertices() {

        float vertices[] = new float[num * coordition_num*3];
        int offset = 0;
        vertices[offset++] = 0;
        vertices[offset++] = 0;
        vertices[offset++] = 0;

        double angrad;
        for (int i = 0; i < num + 1; i++) {
            angrad = Math.toRadians(angle * i);//当前弧度
            vertices[offset++] = (float) (ratio * Math.cos(angrad));
            vertices[offset++] = (float) (ratio * Math.sin(angrad));
            vertices[offset++] = 0;
        }

        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
        vbb.order(ByteOrder.nativeOrder());
        verticesBuffer = vbb.asFloatBuffer();
        verticesBuffer.put(vertices);
        verticesBuffer.position(0);

        /*
        * 产生扇形纹理坐标\
        * 其实就是上面每个顶点对应纹理坐标的对应关系
        * */
        float colors[]=new float[num*3*2];//每个弧形
        offset =0;
        colors[offset++]=ratio;
        colors[offset++]=ratio;
        for(int i=0;i<num+1;i++){
            angrad = Math.toRadians(angle*i);
            colors[offset++]=(float) (ratio*(1+Math.cos(angrad)));
            colors[offset++]=(float)(ratio*(1-Math.sin(angrad)));
        }

        ByteBuffer cbb=ByteBuffer.allocateDirect(colors.length*4);
        cbb.order(ByteOrder.nativeOrder());
        colorsBuffer = cbb.asFloatBuffer();
        colorsBuffer.put(colors);
        colorsBuffer.position(0);


    }

    @Override
    public void initShader() {

        String vertex_src = ShaderParse.loadFromAssetsFile("texture_fan_vertex.glsl", context.getResources());
        String frag_src = ShaderParse.loadFromAssetsFile("texture_fan_frag.glsl", context.getResources());

        mProgram = ShaderParse.createProgram(vertex_src, frag_src);

        mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "aPosition");
        mColorsHandle = GLES20.glGetAttribLocation(mProgram, "aCoordTexture");

    }

    @Override
    public void initTexture(int type) {

        int textures[] = new int[1];
        GLES20.glGenTextures(1, textures, 0);

        textureId = textures[0];
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);

        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);
        /*
        * 如果上面的半径ratio>0.5,那么纹理采样坐标有可能大于1,
        * 1+Math.cos(angrad)) = value , 这个 0 < value < 2
        * */
        if(ratio<=0.5){
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
        }else{
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_REPEAT);
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_REPEAT);
        }


        Bitmap bitmap = null;
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

        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN,0,num+2);

    }
}
