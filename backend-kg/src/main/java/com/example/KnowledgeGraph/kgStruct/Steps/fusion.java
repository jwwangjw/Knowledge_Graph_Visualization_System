package com.example.KnowledgeGraph.kgStruct.Steps;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.KnowledgeGraph.bllmpl.Info.CSVParseStrategy;
import com.example.KnowledgeGraph.bllmpl.Info.TXTParseStrategy;
import com.example.KnowledgeGraph.bllmpl.KG.sqlServiceImpl;
import com.example.KnowledgeGraph.data.readMapper;
import com.example.KnowledgeGraph.po.Graph;
import com.example.KnowledgeGraph.po.Links;
import com.example.KnowledgeGraph.po.Nodes;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class fusion {
    public static boolean isNumeric(String str){
        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(str).matches();
    }
    public JSONObject buildGraph_f(List<String> trps){
        JSONObject jsonObject = new JSONObject();
        JSONArray nodeArray = new JSONArray();
        JSONArray linkArray = new JSONArray();
        JSONObject nodes = new JSONObject();
        JSONObject links = new JSONObject();
        List<String> ids=new ArrayList<>();
        List<String> names_temp=new ArrayList<>();
        List<String> flagSegs_temp=new ArrayList<>();
        List<String> descriptions=new ArrayList<>();
        List<Integer> types=new ArrayList<>();
        List<String> name=new ArrayList<>();
        List<String> id_true=new ArrayList<>();
        List<String> spider_res=new ArrayList<>();
        for(int count=0;count<trps.size();count++){
            String temp1=trps.get(count);
            String[] temp = temp1.split(",");
            names_temp.add(temp[0]);
            flagSegs_temp.add(temp[1]);
            descriptions.add(temp[2]);
            types.add(Integer.valueOf(temp[3]));
            names_temp.add(temp[4]);
            flagSegs_temp.add(temp[5]);
        }

        int definedNodeIds = 1;
        int definedLinkIds = 501;
        int m=0;
        for(int i=0;i<names_temp.size();i++){
            try{
                BaiKe baike=new BaiKe();
                baike.getContent(names_temp.get(i));
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        List<String> dts=new ArrayList<>();
        List<String> dds=new ArrayList<>();
        try {
            File file1=new File("a.txt");
            String path1=file1.getAbsolutePath();
            file1.delete();
            String path_t=path1.substring(0,path1.length()-6);
            String UPLOAD_FOLDER=path_t +'/'+"upload/";
            File file = new File(UPLOAD_FOLDER+"test.txt");
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            String str;
            int count=0;
            while ((str = br.readLine())!=null) {
                List<String> tmp= Arrays.asList(str.split(":").clone());
                if(count%2==0){
                    dts.add(tmp.get(1).replaceAll("\\[","").replaceAll("]",""));
                }else{
                    dds.add(str.replaceAll("\\[","").replaceAll("]","").replaceAll(":",", "));
                }
                count=count+1;
            }
            br.close();
            fr.close();
            file.delete();

        } catch (Exception e) {
            e.printStackTrace();
        }
        List<String> res_t=new ArrayList<>();
        for(int i=0;i<dts.size();i++){
            String res="";
            List<String> detectList_tmp=new ArrayList<>();
            detectList_tmp= Arrays.asList(dts.get(i).split(", ").clone());
            List<String> detectList2=new ArrayList<>();
            detectList2=Arrays.asList(dds.get(i).split(", ").clone());
            res=detectList2.get(0);
            for(int j=0;j<detectList_tmp.size();j++){
                String dt=detectList_tmp.get(j);
                if(dt.equals("本名")||dt.equals("别名")||dt.equals("字号")||dt.equals("中文名")||dt.equals("外文名")||dt.equals("简称")){
                    res=res+detectList2.get(j+1);
                }
            }
            res_t.add(res);
        }
        List<String> dd_true=new ArrayList<>();
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
                dd_true.add(res_t.get(m));
                dd_true.add(res_t.get(m+1));
            } else{
                int flag=-1;
                for(int n=0;n<name.size();n++){
                    if(dd_true.get(n).indexOf(names_temp.get(m))!=-1){
                        flag=n;
                    }
                }
                if(flag==-1){
                    definedNodeIds = CreateJsonNode(names_temp, flagSegs_temp, definedNodeIds, m, nodeArray);
                    name.add(names_temp.get(m));
                    id_true.add(names_temp.get(m));
                    ids.add(names_temp.get(m));
                    dd_true.add(res_t.get(m));
                } else{
                    ids.add(id_true.get(flag));
                }
                int flag1=-1;
                for(int n=0;n<name.size();n++){
                    if(dd_true.get(n).indexOf(names_temp.get(m+1))!=-1){
                           flag1=n;
                    }
                }
                if(flag1==-1){
                    definedNodeIds = CreateJsonNode(names_temp, flagSegs_temp, definedNodeIds, m+1, nodeArray);
                    name.add(names_temp.get(m+1));
                    id_true.add(names_temp.get(m+1));
                    ids.add(names_temp.get(m+1));
                    dd_true.add(res_t.get(m+1));
                } else{
                    ids.add(id_true.get(flag1));
                }
                System.out.println(id_true);
            }
            m=m+2;
        }
        for(int k=0;k<descriptions.size();k++){
            if(descriptions.get(k).equals("属于")){
                descriptions.set(k,"");
            }
            definedLinkIds = createJsonLinks(ids,descriptions,types,definedLinkIds,k,linkArray);
        }

        jsonObject.put("links",linkArray);
        jsonObject.put("nodes",nodeArray);
        System.out.println(jsonObject.toString());
        return jsonObject;
    }
    public JSONObject buildGraph_t(List<String> trps){
        JSONObject jsonObject = new JSONObject();
        JSONArray nodeArray = new JSONArray();
        JSONArray linkArray = new JSONArray();
        List<String> ids=new ArrayList<>();
        List<String> names_temp=new ArrayList<>();
        List<String> flagSegs_temp=new ArrayList<>();
        List<String> descriptions=new ArrayList<>();
        List<Integer> types=new ArrayList<>();
        List<String> name=new ArrayList<>();
        List<String> id_true=new ArrayList<>();
        for(int count=0;count<trps.size();count++){
            String temp1=trps.get(count);
            String[] temp = temp1.split(",");
            names_temp.add(temp[0]);
            flagSegs_temp.add(temp[1]);
            descriptions.add(temp[2]);
            types.add(Integer.valueOf(temp[3]));
            names_temp.add(temp[4]);
            flagSegs_temp.add(temp[5]);
        }
        System.out.println(names_temp);

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
                System.out.println("name");
                System.out.println(id_true.toString());
            }
            m=m+2;
        }
        for(int k=0;k<descriptions.size();k++){
            if(descriptions.get(k).equals("属于")){
                descriptions.set(k,"");
            }
            definedLinkIds = createJsonLinks(ids,descriptions,types,definedLinkIds,k,linkArray);
        }

        jsonObject.put("links",linkArray);
        jsonObject.put("nodes",nodeArray);
        return jsonObject;
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
