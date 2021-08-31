package com.example.KnowledgeGraph.bllmpl.KG;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.example.KnowledgeGraph.bl.KG.sqlService;
import com.example.KnowledgeGraph.bllmpl.Info.InfoServiceImpl;
import com.example.KnowledgeGraph.data.readMapper;
import com.example.KnowledgeGraph.po.Graph;
import com.example.KnowledgeGraph.po.Links;
import com.example.KnowledgeGraph.po.Nodes;
import com.example.KnowledgeGraph.po.SearchRecord;
import com.example.KnowledgeGraph.vo.ResponseVO;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class sqlServiceImpl implements sqlService {
    @Autowired
    readMapper readMapper;

    @Autowired
    InfoServiceImpl infoService;

    @Override
    public ResponseVO addSqlElement(String name, String flagSeg, Integer fileId, String json, String property) {
        try{
            Graph graph = JSONObject.parseObject(json, Graph.class);
            List<Nodes> nodes = graph.getNodes();
            int index = -1;
            for (int i=0;i<nodes.size();i++) {
                if (nodes.get(i).getName().equals(name)){
                    index = i;
                    break;
                }
            }
            if (index != -1){
                return ResponseVO.buildFailure("存在同名节点！");
            }
            else {
                Long nodeId;
                if (nodes.size() == 0){
                    nodeId = (long)1;
                }
                else {
                    nodeId = nodes.get(nodes.size()-1).getId()+1;
                }

                graph.addNodes(name,nodeId,flagSeg,0,property,0.0,0.0,0.0,0.0);
                json = JSONObject.toJSONString(graph);
                infoService.saveFileJsonToSQL(fileId,json);
            }
        }
        catch (Exception e){
            System.out.println("addSqlElement fail! Fail information:"+e.toString());
            return ResponseVO.buildFailure(e.toString());
        }
        return ResponseVO.buildSuccess();
    }

    @Override
    public ResponseVO deleteSqlElement(Long id, Integer fileId, String json) {
        try{
            Graph graph = JSONObject.parseObject(json, Graph.class);
            List<Nodes> nodes = graph.getNodes();
            List<Links> links = graph.getLinks();
            int index = -1;
            for (int i=0;i<nodes.size();i++) {
                if (nodes.get(i).getId() == id){
                    index = i;
                    break;
                }
            }

            if (index == -1){
                return ResponseVO.buildFailure("不存在该节点！");
            }
            else {
                String nodeName = nodes.get(index).getName();
                for (int i=0;i<links.size();i++){
                    if (links.get(i).getSource().equals(nodeName) || links.get(i).getTarget().equals(nodeName)){
                        return ResponseVO.buildFailure("不能删除含有关系的节点！");
                    }
                }
                nodes.remove(index);
                graph.setNodes(nodes);
            }
            json = JSONObject.toJSONString(graph);
            infoService.saveFileJsonToSQL(fileId,json);
        }
        catch (Exception e){
            System.out.println("deleteSqlElement fail! Fail information:"+e.toString());
            return ResponseVO.buildFailure(e.toString());
        }
        return ResponseVO.buildSuccess();
    }

    @Override
    public ResponseVO updateSqlElement(Long id, String name, String flagSeg,String property, Integer fileId, String json) {
        try{
            Graph graph = JSONObject.parseObject(json, Graph.class);
            List<Nodes> nodes = graph.getNodes();
            int index = -1;
            for (int i=0;i<nodes.size();i++) {
                if (nodes.get(i).getId() == id){
                    index = i;
                    break;
                }
            }

            if (index == -1){
                return ResponseVO.buildFailure("不存在该节点！");
            }
            else {
                Nodes node = nodes.get(index);
                node.setFlagSeg(flagSeg);
                node.setName(name);
                node.setProperty(property);
                graph.setNodes(nodes);
            }
            json = JSONObject.toJSONString(graph);
            infoService.saveFileJsonToSQL(fileId,json);
        }
        catch (Exception e){
            System.out.println("updateSqlElement fail! Fail information:"+e.toString());
            return ResponseVO.buildFailure(e.toString());
        }
        return ResponseVO.buildSuccess();
    }

    @Override
    public ResponseVO addSqlRelation(Long sourceId, Long targetId, String name, Integer flag, Integer fileId, String json) {
        try{
            Graph graph = JSONObject.parseObject(json, Graph.class);
            List<Nodes> nodes = graph.getNodes();
            List<Links> links = graph.getLinks();
            int sourceIndex = -1;
            int targetIndex = -1;
            Long linkId;
            if (links.size() == 0){
                linkId = (long)501;
            }
            else {
                linkId = links.get(links.size()-1).getId()+1;
            }


            for (int i=0;i<nodes.size();i++) {
                if (nodes.get(i).getId() == sourceId){
                    sourceIndex = i;
                }
                if (nodes.get(i).getId() == targetId){
                    targetIndex = i;
                }
            }

            if (sourceIndex == -1 || targetIndex == -1){
                return ResponseVO.buildFailure("不存在该节点！");
            }
            else {
                for (int i=0;i<links.size();i++){
                    if (links.get(i).getSource().equals(nodes.get(sourceIndex).getName()) &&
                            links.get(i).getTarget().equals(nodes.get(targetIndex).getName())){
                        if (links.get(i).getDescription().equals(name) && links.get(i).getType() == flag){
                            System.out.println("该关系已存在！");
                            return ResponseVO.buildFailure("该关系已存在！");
                        }
                    }
                }
                Nodes sourceNode = nodes.get(sourceIndex);
                Nodes targetNode = nodes.get(targetIndex);
                graph.addLinks(sourceNode.getName(),targetNode.getName(),name,flag,linkId);
            }
            json = JSONObject.toJSONString(graph);
            infoService.saveFileJsonToSQL(fileId,json);
        }
        catch (Exception e){
            System.out.println("addSqlRelation fail! Fail information:"+e.toString());
            return ResponseVO.buildFailure(e.toString());
        }
        return ResponseVO.buildSuccess();
    }

    @Override
    public ResponseVO deleteSqlRelation(Long id, Integer fileId, String json) {
        try {
            Graph graph = JSONObject.parseObject(json, Graph.class);
            List<Links> links = graph.getLinks();
            int index = -1;
            for (int i=0;i<links.size();i++) {
                if (links.get(i).getId().equals(id)){
                    index = i;
                    break;
                }
            }

            if (index == -1){
                return ResponseVO.buildFailure("不存在该关系！");
            }
            else {
                links.remove(index);
                graph.setLinks(links);
            }
            json = JSONObject.toJSONString(graph);
            infoService.saveFileJsonToSQL(fileId,json);
        }
        catch (Exception e){
            System.out.println("deleteSqlRelation fail! Fail information:"+e.toString());
            return ResponseVO.buildFailure(e.toString());
        }
        return ResponseVO.buildSuccess();
    }

    @Override
    public ResponseVO updateSqlRelation(Long id, String description, Integer type, Integer fileId, String json) {
        try{
            Graph graph = JSONObject.parseObject(json, Graph.class);
            List<Links> links = graph.getLinks();
            List<Nodes> nodes = graph.getNodes();
            int sourceIndex = -1;
            int targetIndex = -1;
            int index = -1;

            for (int i=0;i<links.size();i++) {
                if (links.get(i).getId().equals(id)){
                    index = i;
                    break;
                }
            }


            if (index == -1){
                return ResponseVO.buildFailure("不存在该关系！");

            }
            else {
                for (int i=0;i<nodes.size();i++) {
                    if (nodes.get(i).getName().equals(links.get(index).getSource())){
                        sourceIndex = i;
                    }
                    if (nodes.get(i).getName().equals(links.get(index).getTarget())){
                        targetIndex = i;
                    }
                }

                for (int i=0;i<links.size();i++){
                    if (links.get(i).getSource().equals(nodes.get(sourceIndex).getName()) &&
                            links.get(i).getTarget().equals(nodes.get(targetIndex).getName())){
                        if (links.get(i).getDescription().equals(description) && links.get(i).getType() == type){
                            System.out.println("该关系已存在！");
                            return ResponseVO.buildFailure("该关系已存在！");
                        }
                    }
                }
                Links link = links.get(index);
                link.setDescription(description);
                link.setType(type);
                graph.setLinks(links);
            }
            json = JSONObject.toJSONString(graph);
            infoService.saveFileJsonToSQL(fileId,json);
        }
        catch (Exception e){
            System.out.println("updateSqlRelation fail! Fail information:"+e.toString());
            return ResponseVO.buildFailure(e.toString());
        }
        return ResponseVO.buildSuccess();
    }

    @Override
    public ResponseVO deleteSqlAll(Integer fileId) {
        try {
            readMapper.updateFileJson(fileId,"{}");
        }
        catch (Exception e){
            System.out.println("deleteSqlAll fail! Fail information:"+e.toString());
            return ResponseVO.buildFailure(e.toString());
        }
        return ResponseVO.buildSuccess();
    }

    @Override
    public ResponseVO addSqlProperty(String name,String value, Long nodeId, Integer fileId, String json) {
        try{
            Graph graph = JSONObject.parseObject(json, Graph.class);
            List<Nodes> nodes = graph.getNodes();
            int index = -1;
            for (int i=0;i<nodes.size();i++) {
                if (nodes.get(i).getId() == nodeId){
                    index = i;
                    break;
                }
            }

            if (index == -1){
                return ResponseVO.buildFailure("不存在该节点！");
            }
            else {
                Nodes node = nodes.get(index);
                String old_property = node.getProperty();
                if (old_property != null && old_property.length() > 0){
                    JSONObject proObject = JSONObject.parseObject(old_property);
                    if(proObject.containsKey(name)){
                        return ResponseVO.buildFailure("该属性已存在！");
                    }
                    else {
                        proObject.put(name,value);
                        old_property = proObject.toJSONString();
                        node.setProperty(old_property);
                    }
                }
                else {
                    JSONObject proObject = new JSONObject();
                    proObject.put(name,value);
                    old_property = proObject.toJSONString();
                    node.setProperty(old_property);

                }
                graph.setNodes(nodes);
            }
            json = JSONObject.toJSONString(graph);
            infoService.saveFileJsonToSQL(fileId,json);
        }
        catch (Exception e){
            System.out.println("addSqlProperty fail! Fail information:"+e.toString());
            return ResponseVO.buildFailure(e.toString());
        }
        return ResponseVO.buildSuccess();
    }

    @Override
    public ResponseVO updateSqlProperty(String name, String value, Long nodeId, Integer fileId, String json) {
        try{
            Graph graph = JSONObject.parseObject(json, Graph.class);
            List<Nodes> nodes = graph.getNodes();
            int index = -1;
            for (int i=0;i<nodes.size();i++) {
                if (nodes.get(i).getId() == nodeId){
                    index = i;
                    break;
                }
            }

            if (index == -1){
                return ResponseVO.buildFailure("不存在该节点！");
            }
            else {
                Nodes node = nodes.get(index);
                String old_property = node.getProperty();
                if (old_property != null && old_property.length() > 0){
                    JSONObject proObject = JSONObject.parseObject(old_property);
                    if(proObject.containsKey(name)){
                        proObject.put(name,value);
                        old_property = proObject.toJSONString();
                        node.setProperty(old_property);
                    }
                    else {
                        return ResponseVO.buildFailure("该属性不存在！");
                    }
                }
                else {
                    return ResponseVO.buildFailure("该属性不存在！");
                }
                graph.setNodes(nodes);
            }
            json = JSONObject.toJSONString(graph);
            infoService.saveFileJsonToSQL(fileId,json);
        }
        catch (Exception e){
            System.out.println("updateSqlProperty fail! Fail information:"+e.toString());
            return ResponseVO.buildFailure(e.toString());
        }
        return ResponseVO.buildSuccess();
    }

    @Override
    public ResponseVO deleteSqlProperty(String name, Long nodeId, Integer fileId, String json) {
        try{
            Graph graph = JSONObject.parseObject(json, Graph.class);
            List<Nodes> nodes = graph.getNodes();
            int index = -1;
            for (int i=0;i<nodes.size();i++) {
                if (nodes.get(i).getId() == nodeId){
                    index = i;
                    break;
                }
            }

            if (index == -1){
                return ResponseVO.buildFailure("不存在该节点！");
            }
            else {
                Nodes node = nodes.get(index);
                String old_property = node.getProperty();
                if (old_property != null && old_property.length() > 0){
                    JSONObject proObject = JSONObject.parseObject(old_property);
                    if(proObject.containsKey(name)){
                        proObject.remove(name);
                        old_property = proObject.toJSONString();
                        node.setProperty(old_property);
                    }
                    else {
                        return ResponseVO.buildFailure("该属性不存在！");
                    }
                }
                else {
                    return ResponseVO.buildFailure("该属性不存在！");
                }
                graph.setNodes(nodes);
            }
            json = JSONObject.toJSONString(graph);
            infoService.saveFileJsonToSQL(fileId,json);
        }
        catch (Exception e){
            System.out.println("deleteSqlProperty fail! Fail information:"+e.toString());
            return ResponseVO.buildFailure(e.toString());
        }
        return ResponseVO.buildSuccess();
    }

    @Override
    public ResponseVO updateSqlX(Double x, Long nodeId, Integer fileId, String json) {
        return null;
    }

    @Override
    public ResponseVO updateSqlY(Double y, Long nodeId, Integer fileId, String json) {
        return null;
    }

    @Override
    public ResponseVO updateSqlVx(Double vx, Long nodeId, Integer fileId, String json) {
        return null;
    }

    @Override
    public ResponseVO updateSqlVy(Double vy, Long nodeId, Integer fileId, String json) {
        return null;
    }

    @Override
    public ResponseVO getSearchRecord(Integer fileId) {
        try {
            String record = readMapper.getSearchRecordByFileId(fileId);
            return ResponseVO.buildSuccess(record);
        }
        catch (Exception e){
            System.out.println("getSearchRecord fail! Fail information:"+e.toString());
            return ResponseVO.buildFailure(e.toString());
        }
    }

    @Override
    public ResponseVO updateSearchRecord(Integer fileId, String record) {
        try {
            SearchRecord sr = new SearchRecord();
            sr.setFileId(fileId);
            sr.setRecord(record);
            String old_record = readMapper.getSearchRecordByFileId(fileId);
            if (old_record == null||old_record.length()==0){
                readMapper.InsertSearchRecord(sr);
            }
            else {
                readMapper.updateSearchRecord(fileId, record);
            }
        }
        catch (Exception e){
            System.out.println("updateSearchRecord fail! Fail information:"+e.toString());
            return ResponseVO.buildFailure(e.toString());
        }
        return ResponseVO.buildSuccess();
    }

    @Override
    public ResponseVO addHistory(Integer fileId, String id, String text, String kd) {
        try {
            String old_record = readMapper.getSearchRecordByFileId(fileId);
            if (old_record == null||old_record.length()==0){
                JSONArray jsonArray = new JSONArray();
                JSONObject jsonObject = new JSONObject(true);
                jsonObject.put("kd",kd);
                jsonObject.put("id",id);
                jsonObject.put("text",text);
                jsonArray.add(jsonObject);
                readMapper.updateSearchRecord(fileId,jsonArray.toString());
            }
            else {
                JSONArray jsonArray = JSON.parseArray(old_record);
                int max_kd = -1;
                JSONObject js_obj = new JSONObject();
                String old_kd = "0";
                if (jsonArray.size()>=10){
                    JSONArray temp = new JSONArray();
                    for (int i=0;i<9;i++){
                        temp.add(i,jsonArray.get(i));

                    }
                    jsonArray = temp;
                }

                for (int i=0;i<jsonArray.size();i++){
                    js_obj = (JSONObject) jsonArray.get(i);
                    old_kd = (String) js_obj.get("kd");
                    if (Integer.valueOf((old_kd)) > max_kd){
                        max_kd = Integer.valueOf(old_kd);
                    }
                }

                System.out.println(max_kd+"max");
                max_kd = max_kd+1;
                JSONObject jsonObject = new JSONObject(true);
                jsonObject.put("kd",""+max_kd);
                jsonObject.put("id",id);
                jsonObject.put("text",text);
                jsonArray.add(0,jsonObject);
                readMapper.updateSearchRecord(fileId,jsonArray.toString());
            }
        }
        catch (Exception e){
            System.out.println("addHistory fail! Fail information:"+e.toString());
            return ResponseVO.buildFailure(e.toString());
        }
        return ResponseVO.buildSuccess();
    }

}
