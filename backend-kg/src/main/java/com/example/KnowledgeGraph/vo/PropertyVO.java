package com.example.KnowledgeGraph.vo;

public class PropertyVO {
    private String property;

    public PropertyVO(){
        this.property = "";
    }

    public PropertyVO(String property){
        this.property = property;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }
}
