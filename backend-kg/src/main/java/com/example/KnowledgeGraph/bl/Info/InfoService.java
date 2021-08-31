package com.example.KnowledgeGraph.bl.Info;
import com.example.KnowledgeGraph.po.fileJson;
import com.example.KnowledgeGraph.vo.JsonVO;
import com.example.KnowledgeGraph.vo.ResponseVO;
import org.jsoup.select.Evaluator;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

public interface InfoService {
    /**
     * 获取前端输入文本，测试方法
     * @param string
     * @return
     */
    ResponseVO getInputString(String string);

    /**
     * 前端传输文件以生成新的知识图谱
     * @param file
     * @return
     */
    ResponseVO fileParse(MultipartFile file, Integer userId);

    /**
     * 获取所有知识图谱文件的文件名
     * @return
     */
    ResponseVO getKgFile(Integer userId);

    /**
     * Json保存至sql
     * @return
     */
    ResponseVO saveFileJsonToSQL(Integer fileId, String json);

    /**
     * 获取sql内存的json
     * @return
     */
    ResponseVO getSqlJson(Integer fileId);

    /**
     * 文件名保存至sql
     * @return
     */
    ResponseVO saveFileToSQL(String file_name,Integer userId);

    /**
     *知识融合
     * @return
     *
     */
    ResponseVO fusion(Integer userId, List<Integer> lists,String name);

    ResponseVO txtTriples(List<String> mainBodies, List<String> relations, List<String> objects,
                          String name, Integer userId, List<String> Mposts, List<String> Sposts);

    /**
     * 更新文件名（迭代三）
     * @return
     */
    ResponseVO updateFileName(Integer fileId, String filename, Integer userId);

    /**
     * 删除图谱（迭代三）
     * @return
     */
    ResponseVO deleteKgByFileId(Integer fileId);
}
