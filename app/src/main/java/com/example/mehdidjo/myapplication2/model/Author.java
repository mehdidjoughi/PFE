package com.example.mehdidjo.myapplication2.model;

import com.firebase.ui.auth.data.model.PhoneNumber;
import com.stfalcon.chatkit.commons.models.IUser;

/**
 * Created by Mehdi Djo on 12/02/2018.
 */

public class Author implements IUser {

    private String id;
    private String name;
    private String email;
    private String avatar;
    private String phone;
    private String poste;


    public Author (){

    }

    public Author(String id, String name) {
        this.id = id;
        this.name = name;
    }
    public Author(String id, String name ,String email , String avatar) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.avatar = avatar;
    }
    public Author(String id, String name ,String email) {
        this.id = id;
        this.name = name;
        this.email = email;

    }
    public Author(String id, String name ,String email , String photoUrl , String phone , String poste) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.avatar = photoUrl;
        this.phone = phone;
        this.poste = poste;
    }

    public void setName(String name){
        this.name=name;
    }
    public void setId(String id){
        this.id=id;
    }
    @Override
    public String getId() {
        return id;
    }
    @Override
    public String getName() {
        return name;
    }
    @Override
    public String getAvatar() {
        return avatar;
    }
    public String getEmail() {
        return email;
    }
    public String getPhone() {
        return phone;
    }
    public String getPoste() {
        return poste;
    }
    public void setPoste(String poste) {
        this.poste = poste;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
