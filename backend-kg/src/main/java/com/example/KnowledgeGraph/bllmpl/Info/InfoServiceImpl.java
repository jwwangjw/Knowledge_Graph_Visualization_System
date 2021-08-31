package com.example.KnowledgeGraph.bllmpl.Info;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.KnowledgeGraph.bl.Info.InfoService;
import com.example.KnowledgeGraph.bllmpl.KG.sqlServiceImpl;
import com.example.KnowledgeGraph.data.readMapper;
import com.example.KnowledgeGraph.kgStruct.Steps.BaiKe;
import com.example.KnowledgeGraph.po.*;
import com.example.KnowledgeGraph.vo.ResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.example.KnowledgeGraph.kgStruct.KgStruct;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

@Service
public class InfoServiceImpl implements InfoService {

    @Autowired
    readMapper readmapper;

    @Autowired
    CSVParseStrategy cs;
    @Autowired
    TXTParseStrategy tx;

    @Autowired
    sqlServiceImpl kg;

    @Autowired
    sqlServiceImpl sqlService;

    @Override
    public ResponseVO getInputString(String string) {

        System.out.println(sqlService.addHistory(17,"5","w!","6"));
        /*
        String js = "{\"nodes\":" +
                "[{\"name\":\"a\", \"id\": 1, \"flagSeg\":\"Person\"}," +
                "{\"name\":\"b\", \"id\": 2, \"flagSeg\":\"Person\"}," +
                "{\"name\":\"c\", \"id\": 3, \"flagSeg\":\"Animal\"}]," +
                "\"links\":" +
                "[{\"source\":\"a\", \"target\":\"b\" ,\"relation\":\"喜欢\",\"type\":0}," +
                "{\"source\":\"b\", \"target\":\"c\" ,\"relation\":\"讨厌\",\"type\":0}]" +
                "}";

        fileJson fj = new fileJson();
        fj.setFileId(2);
        fj.setJson(js);


        String js2 = "{\"k1\": \"value\", \"k2\": 10}";
        fileJson fj2 = new fileJson();
        fj2.setFileId(2);
        fj2.setJson(js2);
        saveFileJsonToSQL(2,js);
        saveFileJsonToSQL(2,js2);

         */

        return ResponseVO.buildSuccess(true);
    }


    @Override
    public ResponseVO fileParse(MultipartFile file, Integer userId) {
        try {
            byte[] bytes = file.getBytes();
            File file1=new File("a.txt");
            String path1=file1.getAbsolutePath();
            file1.delete();
            String path_t=path1.substring(0,path1.length()-6);
            String UPLOAD_FOLDER=path_t +'/'+"upload";
            Path path = Paths.get(UPLOAD_FOLDER+'/'+ file.getOriginalFilename());

            //如果没有files文件夹，则创建
            if (!Files.isWritable(path)) {
                Files.createDirectories(Paths.get(UPLOAD_FOLDER));
            }
            Files.write(path, bytes);
            System.out.println(path);
            String sourceType = path.toString().substring(path.toString().length()-4,path.toString().length());
            System.out.println("文件上传成功");
            String sqlJson = null;
            if(sourceType.equals(".csv")) {
                System.out.println("deal with csv");
                sqlJson = cs.fileParser(path.toString());
                String file_name = file.getOriginalFilename();
                String fn = saveFileToSQL(file_name,userId).getContent().toString();

                System.out.println(fn);
                Integer fileId = getFileIdByName(fn);
                System.out.println(fileId);
                saveFileJsonToSQL(fileId,sqlJson);
                sqlService.updateSearchRecord(fileId,"[]");
                return ResponseVO.buildSuccess(true);
            }else if(sourceType.equals(".txt")) {
                System.out.println("deal with txt");
                sqlJson = tx.fileParser(path.toString());
                return ResponseVO.buildSuccess(sqlJson);
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("后端异常...");
            return ResponseVO.buildFailure("读入异常！");
        }
        return ResponseVO.buildSuccess(false);
    }

    @Override
    public ResponseVO getKgFile(Integer userId){
        List<file> files = readmapper.getAllFilesByUserId(userId);
        JSONArray jsonArray = new JSONArray();
        JSONObject kg = new JSONObject(true);
        JSONArray kgChildren = new JSONArray();

        Integer menuId = 1;
        kg.put("MenuTitle","知识图谱");
        kg.put("Icon","deployment-unit");
        kg.put("MenuID",menuId);
        menuId++;
        for(int i=0;i<files.size();i++){
            putInJson(i,kgChildren,files,menuId,kg);
            menuId++;
        }
        kg.put("Children",kgChildren);

        JSONObject typeset = new JSONObject(true);
        JSONArray tsChildren = new JSONArray();
        typeset.put("MenuTitle","图谱排版");
        typeset.put("Icon","hdd");
        typeset.put("MenuID",menuId);
        menuId++;
        for(int i=0;i<files.size();i++){
            putInTypeJson(i,tsChildren,files,menuId,typeset);
            menuId++;
        }
        typeset.put("Children",tsChildren);

        JSONObject userCenter = new JSONObject(true);
        JSONArray ucChildren = new JSONArray();
        userCenter.put("MenuTitle","个人中心");
        userCenter.put("Icon","user");
        userCenter.put("MenuID",menuId);
        menuId++;
        JSONObject childrenTemp = new JSONObject(true);
        childrenTemp.put("MenuID",menuId);
        menuId++;
        childrenTemp.put("MenuTitle","欢迎使用");
        childrenTemp.put("MenuPath","/");
        ucChildren.add(childrenTemp);
        userCenter.put("Children",ucChildren);

        jsonArray.add(kg);
        jsonArray.add(typeset);
        jsonArray.add(userCenter);

        String json = jsonArray.toString();
        return ResponseVO.buildSuccess(json);
    }

    public void putInJson(int index, JSONArray jsonArray, List<file> files, Integer menuId, JSONObject jsonObject){
        JSONObject childrenTemp = new JSONObject(true);
        childrenTemp.put("MenuID",menuId);
        menuId++;
        String filename = files.get(index).getFilename();
        if (filename.indexOf(".") != -1){
            filename = filename.substring(0,filename.lastIndexOf("."));
        }
        childrenTemp.put("MenuTitle",filename);
        childrenTemp.put("MenuPath","/Demo/"+filename+"/"+files.get(index).getId()+"/null");
        jsonArray.add(childrenTemp);
        jsonObject.put("Children",jsonArray);
    }

    public void putInTypeJson(int index, JSONArray jsonArray, List<file> files, Integer menuId, JSONObject jsonObject){
        JSONObject childrenTemp = new JSONObject(true);
        childrenTemp.put("MenuID",menuId);
        menuId++;
        String filename = files.get(index).getFilename();
        if (filename.indexOf(".") != -1){
            filename = filename.substring(0,filename.lastIndexOf("."));
        }
        childrenTemp.put("MenuTitle",filename);
        childrenTemp.put("MenuPath","/Typesetting/"+filename+"/"+files.get(index).getId());
        jsonArray.add(childrenTemp);
        jsonObject.put("Children",jsonArray);
    }

    @Override
    public ResponseVO saveFileJsonToSQL(Integer fileId, String json){
        try {
            fileJson f = new fileJson();
            f.setFileId(fileId);
            f.setJson(json);
            List<fileJson> fileJsons = readmapper.getAllFileJson();
            List<Integer> fileIds = new ArrayList<>();
            for (fileJson fj : fileJsons) {
                fileIds.add(fj.getFileId());
            }
            if (fileIds.contains(f.getFileId())) {
                readmapper.updateFileJson(f.getFileId(), f.getJson());
            } else {
                readmapper.InsertJson(f);
            }
        }
        catch (Exception e){
            e.printStackTrace();
            return ResponseVO.buildFailure("保存失败");
        }
        return ResponseVO.buildSuccess(true);
    }

    @Override
    public ResponseVO getSqlJson(Integer fileId) {
        String filejson = "";
        try{
            System.out.println(fileId);
            filejson = readmapper.getFileJsonByFileId(fileId);
            /*
            Graph graph = JSONObject.parseObject(filejson,Graph.class);
            List<String> nodeNames = graph.getNodeNames();
            List<String> nodeFlagSegs = graph.getNodeFlagSegs();
            List<String> linkNames = graph.getLinkNames();
            List<Integer> linkTypes = graph.getLinkTypes();
            List<String> linkSources = graph.getLinkSources();
            List<String> linkTargets = graph.getLinkTargets();
            List<String> nodeProperties = graph.getNodeProperties();
            List<Long> fromIds = new ArrayList<>();
            List<Long> targetIds = new ArrayList<>();

            for(int i = 0;i < nodeNames.size();i++){
                kg.addElement(nodeNames.get(i),nodeFlagSegs.get(i));
            }

            for(int i = 0;i < nodeNames.size();i++){
                kg.setProperty(nodeProperties.get(i),elementRepository.getNodeIdByName(nodeNames.get(i)));
            }


            for(int i = 0;i < linkNames.size();i++){
                fromIds.add(elementRepository.getNodeIdByName(linkSources.get(i)));
                targetIds.add(elementRepository.getNodeIdByName(linkTargets.get(i)));
            }

            for(int i = 0;i < linkNames.size();i++){
                kg.addRelation(fromIds.get(i),targetIds.get(i),linkNames.get(i),linkTypes.get(i));
            }

             */

        }
        catch (Exception e){
            e.printStackTrace();
            return ResponseVO.buildFailure(e.toString());
        }
        return ResponseVO.buildSuccess(filejson);
    }


    @Override
    public ResponseVO saveFileToSQL(String file_name,Integer userId) {
        String duplicatedName = file_name;
        System.out.println(file_name);
        try {
            List<file> files = readmapper.getAllFilesByUserId(userId);

            List<String> fileNames = new ArrayList<>();
            for (file f:files) {
                fileNames.add(f.getFilename());
            }
            int count = 0;
            while(fileNames.contains(duplicatedName)){
                count++;
                if(file_name.split("\\.").length!=0){
                    duplicatedName = file_name.split("\\.")[0] + "(" + count + ")"+"."+ file_name.split("\\.")[1];
                }else{
                    duplicatedName=file_name+"(" + count + ")";
                }
            }
            file f = new file();
            f.setFilename(duplicatedName);
            f.setUserId(userId);
            readmapper.saveFileInfo(f);
        }
        catch (Exception e){
            e.printStackTrace();
            return ResponseVO.buildFailure("保存失败");
        }
        return ResponseVO.buildSuccess(duplicatedName);
    }

    public Integer getFileIdByName(String file_name){
        Integer fileId = 0;
        try{
            fileId = readmapper.getFileIdByName(file_name);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return fileId;
    }
    @Override
    public ResponseVO txtTriples(List<String> mainBodies,List<String> relations,List<String> objects,
                                 String name,Integer userId,List<String> Mposts,List<String> Sposts){
        List<String> trps=new ArrayList<>();
        for(int i=0;i<mainBodies.size();i++){
            String trp=mainBodies.get(i)+","+Mposts.get(i)+",";
            if(relations.get(i).equals("属性")){
                trp=trp+relations.get(i)+","+"1"+",";
            }else{
                trp=trp+relations.get(i)+","+"0"+",";
            }
            trp=trp+objects.get(i)+","+Sposts.get(i);
            trps.add(trp);
        }
        System.out.println(trps.toString());
        KgStruct k=new KgStruct();
        JSONObject jsonObject=k.build_t(trps);
        String file_name=name;
        String fn = saveFileToSQL(file_name,userId).getContent().toString();

        Integer fileId = getFileIdByName(fn);
        saveFileJsonToSQL(fileId,jsonObject.toString());
        sqlService.updateSearchRecord(fileId,"[]");
        return ResponseVO.buildSuccess(true);

    }
    @Override
    public ResponseVO fusion(Integer userId, List<Integer> lists,String name){
        KgStruct k=new KgStruct();
        JSONObject jsonObject=fusion_Impl(lists);
        String file_name=name;
        String fn = saveFileToSQL(file_name,userId).getContent().toString();

        Integer fileId = getFileIdByName(fn);
        saveFileJsonToSQL(fileId,jsonObject.toString());
        sqlService.updateSearchRecord(fileId,"[]");
        return ResponseVO.buildSuccess(true);
    }
    public JSONObject fusion_Impl(List<Integer> fileIds){
        List<String> trps=new ArrayList<>();
        for(int i=0;i<fileIds.size();i++) {
            Integer fileId=fileIds.get(i);
            try {
                String filejson = "";
                System.out.println();
                filejson = readmapper.getFileJsonByFileId(fileId);
                Graph graph = JSONObject.parseObject(filejson, Graph.class);
                List<Nodes> nodeNames = graph.getNodes();
                List<Links> relations = graph.getLinks();
                for(int j=0;j<relations.size();j++){
                    String trp="";
                    String source="";
                    String target="";
                    String relation=relations.get(j).getDescription()+","+relations.get(j).getType();
                    for(int s=0;s<nodeNames.size();s++){
                        if(relations.get(j).getSource().equals(nodeNames.get(s).getName())){
                            source=nodeNames.get(s).getName()+","+nodeNames.get(s).getFlagSeg();
                        }
                        if(relations.get(j).getTarget().equals(nodeNames.get(s).getName())){
                            target=nodeNames.get(s).getName()+","+nodeNames.get(s).getFlagSeg();
                        }
                    }
                    System.out.println(source);
                    System.out.println(target);
                    System.out.println(relation);
                    trp=source+","+relation+","+target;
                    trps.add(trp);
                }
                System.out.println(trps.toString());

            } catch (Exception e) {
                System.out.println("getSqlJsonFail:" + e.toString());
            }
        }
        KgStruct k=new KgStruct();
        JSONObject jsonObject=k.build_f(trps);
        return jsonObject;

    }

    @Override
    public ResponseVO updateFileName(Integer fileId, String filename, Integer userId) {
        try {
            List<file> files = readmapper.getAllFilesByUserId(userId);
            List<String> file_names = new ArrayList<>();
            for (int i=0;i<files.size();i++){
                file_names.add(files.get(i).getFilename());
            }
            if (file_names.contains(filename)){
                return ResponseVO.buildFailure("不能将文件名改为"+filename+",已有重名文件！");
            }
            readmapper.updateFileName(fileId, filename);
        }
        catch (Exception e){
            System.out.println("updateFilename fail! Fail information:"+e.toString());
            return ResponseVO.buildFailure(e.toString());
        }
        return ResponseVO.buildSuccess();
    }

    @Override
    public ResponseVO deleteKgByFileId(Integer fileId) {
        try{
            readmapper.deleteFileById(fileId);
            readmapper.deleteRecordByFileId(fileId);
            readmapper.deleteJsonByFileId(fileId);
        }
        catch (Exception e){
            System.out.println("updateFilename fail! Fail information:"+e.toString());
            return ResponseVO.buildFailure(e.toString());
        }
        return ResponseVO.buildSuccess();
    }
}
