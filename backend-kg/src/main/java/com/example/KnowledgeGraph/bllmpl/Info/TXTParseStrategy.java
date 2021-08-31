package com.example.KnowledgeGraph.bllmpl.Info;

import com.example.KnowledgeGraph.bl.Info.FileParseStrategy;
import com.example.KnowledgeGraph.data.nlpClient;
import com.example.KnowledgeGraph.kgStruct.KgStruct;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

@Service
public class TXTParseStrategy implements FileParseStrategy {
    @Autowired
    nlpClient nlpclient;

    @Override
    public String fileParser(String file) {
        System.out.println("txt parser");
        String str = "";
        try{
            BufferedReader rd = new BufferedReader(new FileReader(new File(file)));
            String s = rd.readLine();
            while (null != s) {
                str += s + "<br/>";
                s = rd.readLine();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ArrayList<String> sentences = sentenceSplit(str);
        KgStruct kgStruct = new KgStruct();
        String jsonObject = kgStruct.structKnowledgeGraph(sentences, file);
        File file1=new File(file);
        file1.delete();
        return jsonObject;
    }

    private ArrayList<String> sentenceSplit(String str) {
        ArrayList<String> strs = new ArrayList<>();
        int sentence_start_index = 0;
        int sentence_end_index;
        if (str.contains("!") || str.contains("!") || str.contains("。") || str.contains("?") || str.contains("？"))
        {
            for (int i=0;i<str.length();i++) {
                if (str.charAt(i) == '!'||str.charAt(i) == '！'||str.charAt(i) == '。'||str.charAt(i) == '？'||str.charAt(i) == '?'){
                    sentence_end_index = i;
                    strs.add(str.substring(sentence_start_index, sentence_end_index));
                    sentence_start_index = i+1;
                }
                if (str.charAt(i) == '<'&&str.charAt(i+1) == 'b'&&str.charAt(i+2) == 'r'&&str.charAt(i+3) == '/'&&str.charAt(i+4) == '>'){
                    str = str.substring(0, i)+str.substring(i+5);
                    continue;
                }
            }
            for(int i=0;i<strs.size();i++){
                if (strs.get(i).length()<3){
                    strs.remove(i);
                    i--;
                }
            }
        }
        else {
            for (int i=0;i<str.length();i++) {
                if (str.charAt(i) == '<'&&str.charAt(i+1) == 'b'&&str.charAt(i+2) == 'r'&&str.charAt(i+3) == '/'&&str.charAt(i+4) == '>'){
                    str = str.substring(0, i)+str.substring(i+5);
                    continue;
                }
            }
            strs.add(str);
        }
        return strs;
    }
}
