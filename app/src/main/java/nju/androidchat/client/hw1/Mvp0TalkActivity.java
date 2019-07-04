package nju.androidchat.client.hw1;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import lombok.extern.java.Log;
import nju.androidchat.client.ClientMessage;
import nju.androidchat.client.R;
import nju.androidchat.client.Utils;
import nju.androidchat.client.component.ItemImgReceive;
import nju.androidchat.client.component.ItemImgSend;
import nju.androidchat.client.component.ItemTextReceive;
import nju.androidchat.client.component.ItemTextSend;
import nju.androidchat.client.component.OnRecallMessageRequested;

@Log
public class Mvp0TalkActivity extends AppCompatActivity implements Mvp0Contract.View, TextView.OnEditorActionListener, OnRecallMessageRequested {
    private Mvp0Contract.Presenter presenter;
    private Mvp0Contract.Presenter img_presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Mvp0TalkModel mvp0TalkModel = new Mvp0TalkModel();

        // Create the presenter
        ArrayList<ClientMessage> list= new ArrayList<>();
        this.presenter = new Mvp0TalkPresenter(mvp0TalkModel, this, list);
        mvp0TalkModel.setIMvp0TalkPresenter(this.presenter);
        this.img_presenter = new MVP0TalkImgPresenter(mvp0TalkModel, this, list);
        mvp0TalkModel.setIMvp0ImgPresenter(this.img_presenter);
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.start();
        img_presenter.start();
    }

    @Override
    public void showMessageList(List<ClientMessage> messages) {
        runOnUiThread(() -> {
                    LinearLayout content = findViewById(R.id.chat_content);

                    // 删除所有已有的ItemText和ImageText
                    content.removeAllViews();

                    // 增加ItemText
                    for (ClientMessage message : messages) {
                        String text = String.format("%s", message.getMessage());

                        if(isMarkdownImg(text)){
                            String url = text.substring(text.indexOf('(')+1,text.indexOf(')'));
                            text = url;
                            if (message.getSenderUsername().equals(this.presenter.getUsername())) {
                                content.addView(new ItemImgSend(this, text, message.getMessageId(), this));
                            } else {
                                content.addView(new ItemImgReceive(this, text, message.getMessageId()));
                            }
                        }else{
                            // 如果是自己发的，增加ItemTextSend
                            if (message.getSenderUsername().equals(this.presenter.getUsername())) {
                                content.addView(new ItemTextSend(this, text, message.getMessageId(), this));
                            } else {
                                content.addView(new ItemTextReceive(this, text, message.getMessageId()));
                            }
                        }

                    }

                    Utils.scrollListToBottom(this);
                }
        );
    }

    @Override
    public void showText(ClientMessage message) {
        runOnUiThread(() -> {
            LinearLayout content = findViewById(R.id.chat_content);
            String text = String.format("%s", message.getMessage());
            if (message.getSenderUsername().equals(this.presenter.getUsername())) {
                content.addView(new ItemTextSend(this, text, message.getMessageId(), this));
            } else {
                content.addView(new ItemTextReceive(this, text, message.getMessageId()));
            }
            Utils.scrollListToBottom(this);
        });
    }

    @Override
    public void showImg(ClientMessage message) {
        runOnUiThread(() -> {
            LinearLayout content = findViewById(R.id.chat_content);
            String text = String.format("%s", message.getMessage());
            String url = text.substring(text.indexOf('(')+1,text.indexOf(')'));
            text = url;
            System.out.println("----------url:"+url);
            if (message.getSenderUsername().equals(this.presenter.getUsername())) {
                content.addView(new ItemImgSend(this, text, message.getMessageId(), this));
            } else {
                content.addView(new ItemImgReceive(this, text, message.getMessageId()));
            }
            Utils.scrollListToBottom(this);
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (null != this.getCurrentFocus()) {
            return hideKeyboard();
        }
        return super.onTouchEvent(event);
    }

    private boolean hideKeyboard() {
        return Utils.hideKeyboard(this);
    }


    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (Utils.send(actionId, event)) {
            hideKeyboard();
            // 异步地让Controller处理事件
            sendText();
        }
        return false;
    }

    private void sendText() {
        EditText text = findViewById(R.id.et_content);
        AsyncTask.execute(() -> {
            if(isMarkdownImg(text.getText().toString())){
                this.img_presenter.sendMessage(text.getText().toString());
            }else{
                this.presenter.sendMessage(text.getText().toString());
            }

        });
    }

    public void onBtnSendClicked(View v) {
        hideKeyboard();
        sendText();
    }

    // 当用户长按消息，并选择撤回消息时做什么，MVP-0不实现
    @Override
    public void onRecallMessageRequested(UUID messageId) {

    }

    @Override
    public void setPresenter(Mvp0Contract.Presenter presenter1, Mvp0Contract.Presenter presenter2) {
        this.presenter = presenter1;
        this.img_presenter = presenter2;
    }

    private boolean isMarkdownImg(String str){
        if(str.contains("![") && str.indexOf(']')>=str.indexOf('[') && str.indexOf('(')>str.indexOf(']') && str.indexOf(')')>str.indexOf('(')){
            return true;
        }
        return false;
    }
}
