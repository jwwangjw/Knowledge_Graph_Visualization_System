package com.example.KnowledgeGraph.bllmpl.Info;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.KnowledgeGraph.bl.Info.FileParseStrategy;
import com.example.KnowledgeGraph.data.readMapper;
import com.opencsv.CSVParser;
import com.opencsv.CSVReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
@Service
public class CSVParseStrategy implements FileParseStrategy {


    @Autowired
    InfoServiceImpl infoService;

    @Autowired
    readMapper readMapper;

    @Override
    public String fileParser (String file) throws FileNotFoundException {
        JSONObject jsonObject = new JSONObject();
        JSONArray nodeArray = new JSONArray();
        JSONArray linkArray = new JSONArray();
        JSONObject nodes = new JSONObject();
        JSONObject links = new JSONObject();
        try {
            DataInputStream in = new DataInputStream(new FileInputStream(new File(file)));
            CSVReader csvReader = new CSVReader(new InputStreamReader(in, "utf-8"), CSVParser.DEFAULT_SEPARATOR,
                    CSVParser.DEFAULT_QUOTE_CHARACTER, CSVParser.DEFAULT_ESCAPE_CHARACTER, 0);
            String temp1;
            int i=0;
            List<String> ids=new ArrayList<>();
            List<String> names_temp=new ArrayList<>();
            List<String> flagSegs_temp=new ArrayList<>();
            List<String> descriptions=new ArrayList<>();
            List<Integer> types=new ArrayList<>();
            List<String> name=new ArrayList<>();
            List<String> id_true=new ArrayList<>();

            BufferedReader br=new BufferedReader(new FileReader(file));
            while((temp1= br.readLine())!=null){
                if(i==0){

                }
                else {
                    String[] temp = temp1.split(",");
                    names_temp.add(temp[0]);
                    flagSegs_temp.add(temp[1]);
                    descriptions.add(temp[2]);
                    types.add(Integer.valueOf(temp[3]));
                    names_temp.add(temp[4]);
                    flagSegs_temp.add(temp[5]);
                }
                i=i+1;
            }

            int definedNodeIds = 1;
            int definedLinkIds = 501;

            int m=0;
            for(int j=0;j<descriptions.size();j++){
                if(j==0){
                    definedNodeIds = CreateJsonNode(names_temp, flagSegs_temp, definedNodeIds, m, nodeArray);

                    name.add(names_temp.get(m));
                    id_true.add(names_temp.get(m));
                    ids.add(names_temp.get(m));
                    definedNodeIds = CreateJsonNode(names_temp, flagSegs_temp, definedNodeIds, m+1, nodeArray);
                    id_true.add(names_temp.get(m+1));
                    ids.add(names_temp.get(m+1));
                    name.add(names_temp.get(m+1));
                } else{
                    int flag=-1;
                    for(int n=0;n<name.size();n++){
                        if(name.get(n).equals(names_temp.get(m))){
                            flag=n;
                        }
                    }
                    if(flag==-1){
                        definedNodeIds = CreateJsonNode(names_temp, flagSegs_temp, definedNodeIds, m, nodeArray);
                        name.add(names_temp.get(m));
                        id_true.add(names_temp.get(m));
                        ids.add(names_temp.get(m));
                    } else{
                        ids.add(id_true.get(flag));
                    }
                    int flag1=-1;
                    for(int n=0;n<name.size();n++){
                        if(name.get(n).equals(names_temp.get(m+1))){
                            flag1=n;
                            System.out.println("success");
                        }
                    }
                    if(flag1==-1){
                        definedNodeIds = CreateJsonNode(names_temp, flagSegs_temp, definedNodeIds, m+1, nodeArray);
                        name.add(names_temp.get(m+1));
                        id_true.add(names_temp.get(m+1));
                        ids.add(names_temp.get(m+1));
                    } else{
                        ids.add(id_true.get(flag1));
                    }
                }
                m=m+2;
            }
            for(int k=0;k<descriptions.size();k++){
                if(descriptions.get(k).equals("属于")){
                    descriptions.set(k,"");
                }
                definedLinkIds = createJsonLinks(ids,descriptions,types,definedLinkIds,k,linkArray);
                //kg.addRelation(ids.get(2*k),ids.get(2*k+1),descriptions.get(k),types.get(k));
            }
            csvReader.close();
            br.close();

            jsonObject.put("links",linkArray);
            jsonObject.put("nodes",nodeArray);

        } catch (Exception e) {
            e.printStackTrace();
        }
        File file1=new File(file);
        file1.delete();
        return jsonObject.toString();
}

    private int createJsonLinks(List<String> ids, List<String> descriptions, List<Integer> types, int definedLinkIds, int k, JSONArray jsonArray) {
        JSONObject links = new JSONObject();
        links.put("id",definedLinkIds);
        definedLinkIds++;
        links.put("type",types.get(k));
        links.put("source",ids.get(2*k));
        links.put("target",ids.get(2*k+1));
        links.put("description",descriptions.get(k));
        JSONObject iter = new JSONObject();
        for (int i = 0;i < jsonArray.size();i++){
            iter = (JSONObject) jsonArray.get(i);
            if(iter.get("type") == types.get(k) &&
                    ids.get(2*k).equals(iter.get("source")) &&
                    ids.get(2*k+1).equals(iter.get("target")) &&
                    descriptions.get(k).equals(iter.get("description"))){
                System.out.println("关系已存在！");
                definedLinkIds--;
                return definedLinkIds;
            }
        }
        jsonArray.add(links);
        return definedLinkIds;
    }

    private int CreateJsonNode(List<String> names_temp, List<String> flagSegs_temp, int definedNodeIds, int m, JSONArray jsonArray) {
        JSONObject nodes = new JSONObject();
        nodes.put("x",0.0);
        nodes.put("y",0.0);
        nodes.put("id",definedNodeIds);
        definedNodeIds++;
        nodes.put("vx",0.0);
        nodes.put("vy",0.0);
        nodes.put("name",names_temp.get(m));
        nodes.put("property","");
        nodes.put("index",0);
        nodes.put("flagSeg",flagSegs_temp.get(m));
        jsonArray.add(nodes);
        return definedNodeIds;
    }

}
