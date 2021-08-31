package com.example.KnowledgeGraph.controller.Info;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.KnowledgeGraph.vo.JsonVO;
import com.example.KnowledgeGraph.vo.ResponseVO;
import com.example.KnowledgeGraph.bl.Info.InfoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@RestController()
@RequestMapping("/api/info")
public class InfoController {
    @Autowired
    InfoService infoService;

    @GetMapping("/{inputString}/getInput")
    public ResponseVO getInputString(@PathVariable String inputString){
        return infoService.getInputString(inputString);
    }

    @PostMapping("/{userId}/fileParse")
    public ResponseVO fileParse(@RequestBody MultipartFile file, @PathVariable String userId){
        System.out.println("csv file");
        System.out.println(file.getOriginalFilename());
        return infoService.fileParse(file, Integer.valueOf(userId));
    }

    @PostMapping("/{userId}/fileTxtParse")
    public ResponseVO fileTxtParse(@RequestBody MultipartFile file, @PathVariable String userId){
        System.out.println("txt file");
        System.out.println(file.getOriginalFilename());
        return infoService.fileParse(file, Integer.valueOf(userId));
    }

    @GetMapping("/{userId}/getKgFile")
    public ResponseVO getKgFile(@PathVariable String userId){
        return infoService.getKgFile(Integer.valueOf(userId));
    }

    @PostMapping("/saveFileJson")
    public ResponseVO saveFileJsonToSQL(@RequestBody JsonVO json){
        return infoService.saveFileJsonToSQL(json.getId(), json.getJson());
    }

    @GetMapping("/{fileId}/getSqlJson")
    public ResponseVO getSqlJson(@PathVariable Integer fileId){
        return infoService.getSqlJson(fileId);
    }

    @PostMapping("/fusion")
    public ResponseVO funsion(@RequestBody String json){
        System.out.println(json);
        String str="";
        JSONObject obj= JSON.parseObject(json);
        JSONArray ary=obj.getJSONArray("ids");
        Integer userId=Integer.valueOf((String) obj.get("userId"));
        String name=(String) obj.get("name");
        List<Integer> lists=new ArrayList<>();
        for(int i=0;i<ary.size();i++){
            lists.add(Integer.valueOf((String) ary.get(i)));
        }
        return infoService.fusion(userId,lists,name);
    }
    @PostMapping("/txtTriples")
    public ResponseVO txtTriples(@RequestBody String json){
        JSONObject obj= JSON.parseObject(json);
        JSONArray ary=obj.getJSONArray("options");
        String name=obj.getString("name");
        Integer userId=Integer.valueOf(obj.getString("userId"));
        List<String> mainBodies=new ArrayList<>();
        List<String> relations=new ArrayList<>();
        List<String> objects=new ArrayList<>();
        List<String> Mposts = new ArrayList<>();
        List<String> Sposts = new ArrayList<>();
        for(int i=0;i<ary.size();i++){
            JSONObject o=ary.getJSONObject(i);
            mainBodies.add((String)o.get("mainBody"));
            relations.add((String) o.get("relation"));
            objects.add((String) o.get("object"));
            Mposts.add((String) o.get("Mpost"));
            Sposts.add((String) o.get("Spost"));
        }
        return infoService.txtTriples(mainBodies,relations,objects,name,userId,Mposts,Sposts);
    }

    @PostMapping("/renamePage")
    public ResponseVO updateFileName(@RequestBody String Json){
        System.out.println(Json);
        JSONObject file_info = JSON.parseObject(Json);
        return infoService.updateFileName(Integer.parseInt(file_info.get("id").toString()),file_info.get("name").toString(), Integer.parseInt(file_info.get("userId").toString()));
    }

    @PostMapping("/{fileId}/deletePage")
    public ResponseVO deleteKgByFileId(@PathVariable String fileId){
        System.out.println(fileId);
        return infoService.deleteKgByFileId(Integer.valueOf(fileId));
    }
}
