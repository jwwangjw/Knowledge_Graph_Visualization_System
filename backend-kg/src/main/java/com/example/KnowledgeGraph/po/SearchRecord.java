package com.example.KnowledgeGraph.po;

public class SearchRecord {
    private Integer id;
    private Integer fileId;
    private String record;
    public Integer getId(){return id;}
    public String getRecord(){return record;}
    public Integer getFileId() { return fileId; }
    public void setFileId(Integer fileId) {this.fileId = fileId;}
    public void setId(Integer id){this.id=id;}
    public void setRecord(String record){this.record=record;}
}
