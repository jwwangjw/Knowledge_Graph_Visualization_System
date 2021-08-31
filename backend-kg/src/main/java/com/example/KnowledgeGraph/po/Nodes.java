package com.example.KnowledgeGraph.po;

import com.alibaba.fastjson.annotation.JSONField;

public class Nodes {
    @JSONField
    private String name;

    @JSONField
    private Long id;

    @JSONField
    private String flagSeg;

    @JSONField
    private int index;

    @JSONField
    private double x;

    @JSONField
    private double y;

    @JSONField
    private double vx;

    @JSONField
    private double vy;

    @JSONField
    private String property = "";

    public String getProperty() { return property; }

    public void setProperty(String property) { this.property = property; }

    public double getVx() {
        return vx;
    }

    public double getVy() {
        return vy;
    }

    public double getX() { return x; }

    public double getY() {
        return y;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public void setVx(double vx) {
        this.vx = vx;
    }

    public void setVy(double vy) {
        this.vy = vy;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setFlagSeg(String flagSeg) {
        this.flagSeg = flagSeg;
    }

    public String getName() {
        return name;
    }

    public String getFlagSeg() {
        return flagSeg;
    }

    public Long getId() {
        return id;
    }
}
