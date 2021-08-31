package com.example.KnowledgeGraph.vo;

public class JsonVO {
    private Integer id;
    private String json;

    public JsonVO(){
        this.id = null;
        this.json = "";
    }

    public JsonVO(Integer id, String json){
        this.id = id;
        this.json = json;
    }

    public Integer getId(){
        return this.id;
    }
    public String getJson(){
        return this.json;
    }
    public void setId(Integer id){
        this.id = id;
    }
    public void setJson(String json){
        this.json = json;
    }
}
