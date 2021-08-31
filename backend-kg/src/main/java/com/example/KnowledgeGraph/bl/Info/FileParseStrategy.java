package com.example.KnowledgeGraph.bl.Info;

import java.io.FileNotFoundException;

public interface FileParseStrategy {
    /**
     * 不同格式文件的解析
     * 策略模式
     * @param file
     * @return
     */
    String fileParser(String file) throws FileNotFoundException;
}
