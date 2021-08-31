package com.example.KnowledgeGraph.kgStruct;

import com.alibaba.fastjson.JSONObject;
import com.example.KnowledgeGraph.kgStruct.Steps.KgExtract;
import com.example.KnowledgeGraph.kgStruct.Steps.KgSpecify;
import com.example.KnowledgeGraph.kgStruct.Steps.Triple;
import com.example.KnowledgeGraph.kgStruct.Steps.fusion;

import java.util.ArrayList;
import java.util.List;

public class KgStruct {

    public String structKnowledgeGraph(ArrayList<String> sents, String path){
        KgExtract kgExtract = new KgExtract();
        KgSpecify kgSpecify = new KgSpecify();
        ArrayList<Triple> triples = kgExtract.extraction(sents);
        String jsonObject = kgSpecify.specify(triples);
        return jsonObject;
    }
    public JSONObject build_t(List<String> trps){
        fusion f=new fusion();
        JSONObject res=f.buildGraph_t(trps);
        return res;
    }
    public JSONObject build_f(List<String> trps){
        fusion f=new fusion();
        JSONObject res=f.buildGraph_f(trps);
        return res;
    }

}
