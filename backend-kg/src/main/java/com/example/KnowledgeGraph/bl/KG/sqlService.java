package com.example.KnowledgeGraph.bl.KG;

import com.example.KnowledgeGraph.vo.ResponseVO;

public interface sqlService {
    /**
     * 增
     * 添加节点（迭代一）
     * @param name
     * @param flagSeg
     * @return
     */
    ResponseVO addSqlElement(String name, String flagSeg, Integer fileId, String json, String property);

    /**
     * 删
     * 删除节点（迭代一）
     * @param id
     * @return
     */
    ResponseVO deleteSqlElement(Long id, Integer fileId, String json);

    /**
     * 改
     * 修改节点（迭代一）
     * @param id
     * @param name
     * @param flagSeg
     * @return
     */
    ResponseVO updateSqlElement(Long id, String name ,String flagSeg,String property,Integer fileId, String json);

    /**
     * 增
     * 增加关系（迭代一）
     * @param sourceId
     * @param targetId
     * @param name
     * @param flag
     * @return
     */
    ResponseVO addSqlRelation(Long sourceId, Long targetId, String name, Integer flag, Integer fileId, String json);

    /**
     * 删
     * 删除关系（迭代一）
     * @param id
     * @return
     */
    ResponseVO deleteSqlRelation(Long id, Integer fileId, String json);

    /**
     * 改
     * 修改节点，不能修改目标节点（迭代一）
     * @param id
     * @param description
     * @param type
     * @return
     */
    ResponseVO updateSqlRelation(Long id, String description, Integer type, Integer fileId, String json);

    /**
     * 删除所有节点（测试使用，迭代一）
     * @return
     */
    ResponseVO deleteSqlAll(Integer fileId);

    /**
     * 添加属性（迭代二）
     * @return
     */
    ResponseVO addSqlProperty(String name, String value, Long nodeId,Integer fileId, String json);

    /**
     * 修改属性（迭代二）
     * @return
     */
    ResponseVO updateSqlProperty(String name, String value, Long nodeId, Integer fileId, String json);

    /**
     * 删除属性（迭代二）
     * @return
     */
    ResponseVO deleteSqlProperty(String name, Long nodeId, Integer fileId, String json);

    /**
     * 更改x坐标（迭代二）
     * @return
     */
    ResponseVO updateSqlX(Double x, Long nodeId,Integer fileId, String json);

    /**
     * 更改y坐标（迭代二）
     * @return
     */
    ResponseVO updateSqlY(Double y, Long nodeId, Integer fileId, String json);

    /**
     * 更改vx坐标（迭代二）
     * @return
     */
    ResponseVO updateSqlVx(Double vx, Long nodeId, Integer fileId, String json);

    /**
     * 更改vy坐标（迭代二）
     * @return
     */
    ResponseVO updateSqlVy(Double vy, Long nodeId, Integer fileId, String json);

    /**
     * 获取搜索记录（迭代二）
     * @return
     */
    ResponseVO getSearchRecord(Integer fileId);

    /**
     * 更新搜索记录（迭代二）
     * @return
     */
    ResponseVO updateSearchRecord(Integer fileId, String record);

    /**
     * 插入搜索记录（迭代二）
     * @return
     */
    ResponseVO addHistory(Integer fileId, String id, String text, String kd);

}
