package nju.androidchat.client.component;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.StyleableRes;

import java.util.UUID;

import lombok.Setter;
import nju.androidchat.client.R;

/**
 * @Author: TJW
 * @Date: 15:09 2019/6/11
 * @Description
 */
public class ItemImgSend extends LinearLayout implements View.OnLongClickListener{

    @StyleableRes
    int index0 = 0;

    private ImageView imageView;
    private String url;
    private Context context;
    private UUID messageId;
    @Setter
    private OnRecallMessageRequested onRecallMessageRequested;


    public ItemImgSend(Context context, String url, UUID messageId, OnRecallMessageRequested onRecallMessageRequested) {
        super(context);
        this.context = context;
        inflate(context, R.layout.item_img_send, this);
        this.imageView = findViewById(R.id.imageView);
        this.messageId = messageId;
        this.onRecallMessageRequested = onRecallMessageRequested;

        this.setOnLongClickListener(this);
        setUrl(url);
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

    @Override
    public boolean onLongClick(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("确定要撤回这条消息吗？")
                .setPositiveButton("是", (dialog, which) -> {
                    if (onRecallMessageRequested != null) {
                        onRecallMessageRequested.onRecallMessageRequested(this.messageId);
                    }
                })
                .setNegativeButton("否", ((dialog, which) -> {
                }))
                .create()
                .show();

        return true;


    }
}
