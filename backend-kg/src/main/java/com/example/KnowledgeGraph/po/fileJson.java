package com.example.KnowledgeGraph.po;

public class fileJson {
    private Integer id;
    private String json;
    private Integer fileId;

    public Integer getFileId() {
        return fileId;
    }

    public Integer getId() {
        return id;
    }

    public String getJson() {
        return json;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setFileId(Integer fileId) {
        this.fileId = fileId;
    }

    public void setJson(String json) {
        this.json = json;
    }
}
