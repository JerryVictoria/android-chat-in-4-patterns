package nju.androidchat.client.component;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.UUID;

import nju.androidchat.client.R;

/**
 * @Author: TJW
 * @Date: 16:22 2019/6/12
 * @Description
 */
public class ItemImgReceive extends LinearLayout {

    private ImageView imageView;
    private Context context;
    private UUID messageId;
    private String url;
    private OnRecallMessageRequested onRecallMessageRequested;
    public ItemImgReceive(Context context, String url, UUID messageId) {
        super(context);
        this.context = context;
        inflate(context, R.layout.item_img_receive, this);
        this.imageView = findViewById(R.id.imageView_receive);
        this.messageId = messageId;
        this.url = url;
        setUrl(url);
    }

    public void init(Context context) {

    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
        LoadBitmapFromUrl loadBitmapFromUrl = new LoadBitmapFromUrl();
        loadBitmapFromUrl.img = imageView;
        loadBitmapFromUrl.execute(url);
    }

    private class LoadBitmapFromUrl extends AsyncTask<String,Void, Bitmap> {
        ImageView img;
        @Override
        protected Bitmap doInBackground(String... params) {
            return new NetworkBitmapUtil().loadImageFromNetwork(params[0]);
        }
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            img.setImageBitmap(bitmap);//要加载的图片
            /*if (bitmap.isRecycled()) {
                bitmap.recycle();
            }*/
            System.out.println("load img");
        }
    }
}
