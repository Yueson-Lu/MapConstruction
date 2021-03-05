package com.example.ant.dto;

import java.io.Serializable;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class MyMap implements Serializable {
    private int id;
    private String mapName;
    private String points;
    private String navigation;
    private boolean canNavigation;
    private int authorId;
    private String author;
    private String createTime;

    public MyMap() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMapName() {
        return mapName;
    }

    public void setMapName(String mapName) {
        this.mapName = mapName;
    }

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

    public String getNavigation() {
        return navigation;
    }

    public void setNavigation(String navigation) {
        this.navigation = navigation;
    }

    public boolean isCanNavigation() {
        return canNavigation;
    }

    public void setCanNavigation(boolean canNavigation) {
        this.canNavigation = canNavigation;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public int getAuthorId() {
        return authorId;
    }

    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }

    @Override
    public String toString() {
        return "MyMap{" +
                "id=" + id +
                ", mapName='" + mapName + '\'' +
                ", points='" + points + '\'' +
                ", navigation='" + navigation + '\'' +
                ", canNavigation=" + canNavigation +
                ", authorId=" + authorId +
                ", author='" + author + '\'' +
                ", createTime='" + createTime + '\'' +
                '}';
    }
}
