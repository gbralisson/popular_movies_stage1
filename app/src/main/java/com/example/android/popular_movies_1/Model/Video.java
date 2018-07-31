package com.example.android.popular_movies_1.Model;

public class Video {

    private String id;
    private String key;
    private String name;
    private String site;
    private int size;
    private String type;

//    public Video(String id, String key, String name, String site, String size, String type) {
//        this.id = id;
//        this.key = key;
//        this.name = name;
//        this.site = site;
//        this.size = size;
//        this.type = type;
//    }

    public Video(){

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
