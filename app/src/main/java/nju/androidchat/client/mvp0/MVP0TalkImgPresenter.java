package nju.androidchat.client.mvp0;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import nju.androidchat.client.ClientMessage;

/**
 * @Author: TJW
 * @Date: 16:53 2019/6/12
 * @Description
 */
@AllArgsConstructor
public class MVP0TalkImgPresenter implements Mvp0Contract.Presenter {
    private Mvp0Contract.Model mvp0TalkModel;
    private Mvp0Contract.View iMvp0TalkView;

    private List<ClientMessage> clientMessages;

    @Override
    public void sendMessage(String content) {
        ClientMessage clientMessage = mvp0TalkModel.sendInformation(content);
        showNewMsg(clientMessage);
    }

    @Override
    public void receiveMessage(ClientMessage clientMessage) {
        refreshMessageList(clientMessage);
    }

    @Override
    public String getUsername() {
        return mvp0TalkModel.getUsername();
    }

    private void refreshMessageList(ClientMessage clientMessage) {
        clientMessages.add(clientMessage);
        iMvp0TalkView.showMessageList(clientMessages);
    }
    private void showNewMsg(ClientMessage clientMessage){
        clientMessages.add(clientMessage);
        iMvp0TalkView.showImg(clientMessage);
    }

    //撤回消息，Mvp0暂不实现
    @Override
    public void recallMessage(int index0) {

    }

    @Override
    public void start() {

    }
}
