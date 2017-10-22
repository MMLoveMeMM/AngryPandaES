package main.org.com.angrypandaes.texture;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.InputStream;

/**
 * Created by 刘志保 on 2017/10/2.
 */

public class TextureLoadImage {

    public static Bitmap LoadTextureFromAssets(Context context,int resId) {

        Bitmap bitmap=null;
        //通过输入流加载图片
        AssetManager am = context.getResources().getAssets();
        InputStream is = null;
        try {
            Resources res = context.getResources();
            is = res.openRawResource(resId);
            bitmap = BitmapFactory.decodeStream(is);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        return bitmap;
    }
}

