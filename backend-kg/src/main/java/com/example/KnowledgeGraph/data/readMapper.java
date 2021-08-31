package com.example.KnowledgeGraph.data;

import com.example.KnowledgeGraph.po.SearchRecord;
import com.example.KnowledgeGraph.po.file;
import com.example.KnowledgeGraph.po.fileJson;
import com.example.KnowledgeGraph.po.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Mapper
@Repository
public interface readMapper {

    List<file> getAllFiles();
    List<file> getAllFilesByUserId(Integer userId);

    List<fileJson> getAllFileJson();

    List<SearchRecord> getAllSearchRecord();

    int InsertJson(fileJson fj);

    int saveFileInfo(file f);

    int InsertSearchRecord(SearchRecord s);

    int updateFileJson(@Param("fileId") Integer fileId, @Param("json") String json);

    String getFileJsonByFileId(@Param("fileId") Integer fileId);

    Integer getFileIdByName(@Param("file_name") String file_name);

    String getNameById(@Param("file_Id") Integer file_Id);

    String getSearchRecordByFileId(@Param("fileId") Integer fileId);

    int updateSearchRecord(@Param("fileId") Integer fileId, @Param("record") String record);

    int updateFileName(@Param("id") Integer id, @Param("file_name") String file_name);

    int deleteFileById(@Param("id") Integer id);

    int deleteRecordByFileId(@Param("fileId") Integer fileId);

    int deleteJsonByFileId(@Param("fileId") Integer fileId);

    /**
     * 获取账号的密码
     */
    String getPassword(@Param("name") String name);

    /**
     * 获取账号的密码判断是否存在输入手机号和用户名的账号
     */

    String getPWD(@Param("phone") String phone,@Param("name") String name);

    /**
     * 获取账号的对应id
     */

    Integer getUserId(@Param("name") String name);

    /**
     * 创建一个新的账号
     */
    int register(@Param("phone") String phone, @Param("name") String name,@Param("password") String password);

    /**
     * 更新用户信息
     */
    int updateAccount(@Param("name") String name,@Param("password") String password);

}
