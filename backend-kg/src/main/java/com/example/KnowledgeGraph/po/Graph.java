package com.example.KnowledgeGraph.po;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.ArrayList;
import java.util.List;

public class Graph {

    @JSONField(name = "name")
    private String name;

    @JSONField(name = "firstCoordinateFlag")
    private boolean firstCoordinateFlag = true;

    @JSONField(name = "nodes", ordinal = 1)
    private List<Nodes> nodes = new ArrayList<>();

    @JSONField(name = "links" , ordinal = 2)
    private List<Links> links = new ArrayList<>();

    @JSONField(serialize=false)
    private List<String> nodeNames = new ArrayList<>();
    @JSONField(serialize=false)
    private List<String> nodeFlagSegs = new ArrayList<>();
    @JSONField(serialize=false)
    private List<String> linkNames = new ArrayList<>();
    @JSONField(serialize=false)
    private List<Integer> linkTypes = new ArrayList<>();
    @JSONField(serialize=false)
    private List<String> linkSources = new ArrayList<>();
    @JSONField(serialize=false)
    private List<String> linkTargets = new ArrayList<>();
    @JSONField(serialize = false)
    private List<String> nodeProperties = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addNodes(String name, Long id, String flagSeg, int index, String property, double vx, double vy, double x, double y){
        Nodes node = new Nodes();
        node.setFlagSeg(flagSeg);
        node.setId(id);
        node.setName(name);
        node.setIndex(index);
        node.setVx(vx);
        node.setVy(vy);
        node.setX(x);
        node.setY(y);
        node.setProperty(property);
        nodes.add(node);
    }

    public void addLinks(String source, String target, String relation, Integer type, Long id){
        Links link = new Links();
        link.setDescription(relation);
        link.setSource(source);
        link.setTarget(target);
        link.setType(type);
        link.setId(id);
        links.add(link);
    }

    public List<Nodes> getNodes() {
        return nodes;
    }

    public void setNodes(List<Nodes> nodes) {
        this.nodes = nodes;
    }

    public List<Links> getLinks() {
        return links;
    }

    public void setLinks(List<Links> links) {
        this.links = links;
    }

    public List<String> getNodeNames() {
        for (Nodes node:nodes){
            nodeNames.add(node.getName());
        }
        return nodeNames;
    }

    public List<String> getNodeProperties() {
        for (Nodes node:nodes){
            nodeProperties.add(node.getProperty());
        }
        return nodeProperties;
    }

    public List<String> getNodeFlagSegs() {
        for (Nodes node:nodes){
            nodeFlagSegs.add(node.getFlagSeg());
        }
        return nodeFlagSegs;
    }

    public List<String> getLinkNames() {
        for (Links link:links){
            linkNames.add(link.getDescription());
        }
        return linkNames;
    }

    public List<Integer> getLinkTypes() {
        for (Links link:links){
            linkTypes.add(link.getType());
        }
        return linkTypes;
    }

    public List<String> getLinkSources() {
        for (Links link:links){
            linkSources.add(link.getSource());
        }
        return linkSources;
    }

    public List<String> getLinkTargets() {
        for (Links link:links){
            linkTargets.add(link.getTarget());
        }
        return linkTargets;
    }
}
