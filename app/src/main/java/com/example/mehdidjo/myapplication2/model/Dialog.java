package com.example.mehdidjo.myapplication2.model;

import com.stfalcon.chatkit.commons.models.IDialog;
import com.stfalcon.chatkit.commons.models.IMessage;
import com.stfalcon.chatkit.commons.models.IUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mehdi Djo on 28/02/2018.
 */

public class Dialog implements IDialog {

    private String id;
    private String dialogphoto;
    private String dialogName;
    private ArrayList<Author> users;
    private Message mylastMessage;
    private int unreadCount;

    public Dialog(){}
    public Dialog(String id, String name, String photo,
                  ArrayList<Author> users, Message mylastMessage, int unreadCount) {

        this.id = id;
        this.dialogName = name;
        this.dialogphoto = photo;
        this.users = users;
        this.mylastMessage = mylastMessage;
        this.unreadCount = unreadCount;
    }


    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getDialogPhoto() {
        return dialogphoto;
    }

    @Override
    public String getDialogName() {
        return dialogName;
    }

    @Override
    public List<? extends IUser> getUsers() {
        return users;
    }

    @Override
    public IMessage getLastMessage() {
        return mylastMessage;
    }
    public Message getMyLastMessage() {
        return mylastMessage;
    }

    @Override
    public void setLastMessage(IMessage message) {
        this.mylastMessage = mylastMessage;
    }
    public void setMyLastMessage(Message message) {
        this.mylastMessage = message;
    }
    @Override
    public int getUnreadCount() {
        return unreadCount;
    }

    public void setDialogPhoto(String url){
        this.dialogphoto = url;
    }
}
