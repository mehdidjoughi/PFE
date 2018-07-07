package com.example.mehdidjo.myapplication2.model;

/**
 * Created by Mehdi Djo on 17/05/2018.
 */

public class Document {

    private String id;
    private String name;
    private String path;
    private String author;
    private String date;
    private int type;


    public Document(){}
    public Document(String id, String name, String path,String date, int type) {
        this.id = id;
        this.name = name;
        this.path = path;
        this.type = type;
        this.date =date;

    }
    public Document(String id, String name, String path,String date, int type , String author) {
        this.id = id;
        this.name = name;
        this.path = path;
        this.type = type;
        this.date =date;
        this.author= author;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
