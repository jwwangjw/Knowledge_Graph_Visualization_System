package com.example.KnowledgeGraph.po;

import com.alibaba.fastjson.annotation.JSONField;

public class Links {
    @JSONField
    private String source;

    @JSONField
    private String target;

    @JSONField
    private String description;

    @JSONField
    private Integer type;

    @JSONField Long id;

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public void setType(Integer type) {
        this.type = type;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTarget() {
        return target;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSource() {
        return source;
    }

    public Integer getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }
}
