package nju.androidchat.client.component;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.IOException;
import java.net.URL;

/**
 * @Author: TJW
 * @Date: 22:19 2019/5/22
 * @Description
 */
public class NetworkBitmapUtil {
    public Bitmap loadImageFromNetwork(String url) {
        //得到可用的图片
        Bitmap bitmap = simpleNetworkImage(url);
        if (bitmap == null) {
            Log.i("loadImageFromNetwork:", "bitmap is null");
        }
        return bitmap;
    }

    public Bitmap simpleNetworkImage(String url) {
        Bitmap imgBitmap = null;
        try {
            URL picUrl = new URL(url);
            imgBitmap = BitmapFactory.decodeStream(picUrl.openStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return imgBitmap;
    }
}
