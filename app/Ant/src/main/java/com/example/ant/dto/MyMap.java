package com.example.ant.dto;

import java.io.Serializable;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class MyMap implements Serializable {
    private int id;
    private String mapName;
    private byte[] points;
    private byte[] navigation;
    private boolean canNavigation;
    private boolean isPub;
    private int authorId;
    private String author;
    private Date createTime;


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

    public byte[] getPoints() {
        return points;
    }

    public void setPoints(byte[] points) {
        this.points = points;
    }

    public byte[] getNavigation() {
        return navigation;
    }

    public void setNavigation(byte[] navigation) {
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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public int getAuthorId() {
        return authorId;
    }

    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }

    public boolean isPub() {
        return isPub;
    }

    public void setPub(boolean pub) {
        isPub = pub;
    }

    @Override
    public String toString() {
        return "MyMap{" +
                "id=" + id +
                ", mapName='" + mapName + '\'' +
                ", points=" + Arrays.toString(points) +
                ", navigation=" + Arrays.toString(navigation) +
                ", canNavigation=" + canNavigation +
                ", isPub=" + isPub +
                ", authorId=" + authorId +
                ", author='" + author + '\'' +
                ", createTime=" + createTime +
                '}';
    }
}
