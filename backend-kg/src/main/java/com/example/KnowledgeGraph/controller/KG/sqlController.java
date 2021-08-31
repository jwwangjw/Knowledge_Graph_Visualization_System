package com.example.KnowledgeGraph.controller.KG;

import com.example.KnowledgeGraph.bllmpl.KG.sqlServiceImpl;
import com.example.KnowledgeGraph.vo.JsonVO;
import com.example.KnowledgeGraph.vo.ResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("/api/sql")
public class sqlController {
    @Autowired
    sqlServiceImpl sqlService;

    @PostMapping("/{name}/{flag}/{property}/addSqlNode")
    public ResponseVO addSqlNode(@PathVariable String name, @PathVariable String flag, @PathVariable String property,@RequestBody JsonVO jsonVO){
        return sqlService.addSqlElement(name,flag,jsonVO.getId(), jsonVO.getJson(),property);
    }

    @PostMapping("/{id}/deleteSqlNode")
    public ResponseVO deleteSqlNode(@PathVariable String id, @RequestBody JsonVO jsonVO){
        return sqlService.deleteSqlElement(Long.valueOf(id), jsonVO.getId(), jsonVO.getJson());
    }

    @PostMapping("/{id}/{name}/{flag}/{property}/updateSqlNode")
    public ResponseVO updateSqlNode(@PathVariable String id, @PathVariable String name, @PathVariable String flag, @PathVariable String property, @RequestBody JsonVO jsonVO){
        return sqlService.updateSqlElement(Long.valueOf(id),name,flag,property,jsonVO.getId(), jsonVO.getJson());
    }

    //不知道能否同时有2个requestbody，未细究，可以通过修改成传输id
    @PostMapping("/{sid}/{tid}/{name}/{flag}/addSqlRelation")
    public ResponseVO addSqlRelation(@PathVariable String sid, @PathVariable String tid,@PathVariable String name, @PathVariable String flag, @RequestBody JsonVO jsonVO){
        return sqlService.addSqlRelation(Long.valueOf(sid), Long.valueOf(tid), name,Integer.valueOf(flag), jsonVO.getId(),jsonVO.getJson());
    }


    @PostMapping("/{id}/deleteSqlRelation")
    public ResponseVO deleteSqlRelation(@PathVariable String id, @RequestBody JsonVO jsonVO){
        return sqlService.deleteSqlRelation(Long.valueOf(id), jsonVO.getId(), jsonVO.getJson());

    }

    @PostMapping("/{id}/{description}/{type}/updateSqlRelation")
    public ResponseVO updateSqlRelation(@PathVariable String id, @PathVariable String description, @PathVariable Integer type, @RequestBody JsonVO jsonVO){
        return sqlService.updateSqlRelation(Long.valueOf(id), description, type, jsonVO.getId(), jsonVO.getJson());
    }

    @PostMapping("/{id}/{name}/{value}/addSqlProperty")
    public ResponseVO addSqlProperty(@PathVariable String id, @PathVariable String name, @PathVariable String value, @RequestBody JsonVO jsonVO){
        return sqlService.addSqlProperty(name,value,Long.valueOf(id), jsonVO.getId(), jsonVO.getJson());
    }

    @PostMapping("/{id}/{name}/{value}/updateSqlProperty")
    public ResponseVO updateSqlProperty(@PathVariable String id, @PathVariable String name, @PathVariable String value, @RequestBody JsonVO jsonVO){
        return sqlService.updateSqlProperty(name,value,Long.valueOf(id), jsonVO.getId(), jsonVO.getJson());
    }

    @PostMapping("/{id}/{name}/deleteSqlProperty")
    public ResponseVO deleteSqlProperty(@PathVariable String id, @PathVariable String name, @RequestBody JsonVO jsonVO){
        return sqlService.deleteSqlProperty(name,Long.valueOf(id), jsonVO.getId(), jsonVO.getJson());
    }

    @PostMapping("/{id}/{x}/updateSqlX")
    public ResponseVO updateSqlX(@PathVariable String id, @PathVariable Double x, @RequestBody JsonVO jsonVO){
        return sqlService.updateSqlX(x,Long.valueOf(id), jsonVO.getId(), jsonVO.getJson());
    }

    @PostMapping("/{id}/{y}/updateSqlY")
    public ResponseVO updateSqlY(@PathVariable String id, @PathVariable Double y, @RequestBody JsonVO jsonVO){
        return sqlService.updateSqlX(y,Long.valueOf(id), jsonVO.getId(), jsonVO.getJson());
    }

    @PostMapping("/{id}/{vx}/updateSqlVx")
    public ResponseVO updateSqlVx(@PathVariable String id, @PathVariable Double vx, @RequestBody JsonVO jsonVO){
        return sqlService.updateSqlX(vx,Long.valueOf(id), jsonVO.getId(), jsonVO.getJson());
    }

    @PostMapping("/{id}/{vy}/updateSqlVy")
    public ResponseVO updateSqlVy(@PathVariable String id, @PathVariable Double vy, @RequestBody JsonVO jsonVO){
        return sqlService.updateSqlX(vy,Long.valueOf(id), jsonVO.getId(), jsonVO.getJson());
    }

    @PostMapping("/{id}/deleteSqlAll")
    public ResponseVO deleteSqlAll(@RequestBody JsonVO jsonVO){
        return sqlService.deleteSqlAll(jsonVO.getId());
    }

    @PostMapping("/{fileId}/getSearchRecord")
    public ResponseVO getSearchRecord(@PathVariable String fileId){
        return sqlService.getSearchRecord(Integer.valueOf(fileId));
    }

    @PostMapping("/{fileId}/{record}/updateSearchRecord")
    public ResponseVO updateSearchRecord(@PathVariable String fileId,@PathVariable String record){
        return sqlService.updateSearchRecord(Integer.valueOf(fileId),record);
    }

    @GetMapping("/{fileId}/{id}/{text}/{kd}/addHistory")
    public ResponseVO addHistory(@PathVariable String fileId, @PathVariable String id, @PathVariable String text, @PathVariable String kd){
        return sqlService.addHistory(Integer.valueOf(fileId),id,text,kd);
    }

}
