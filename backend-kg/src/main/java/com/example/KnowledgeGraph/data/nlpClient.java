package com.example.KnowledgeGraph.data;

import com.baidu.aip.nlp.AipNlp;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class nlpClient {

    private static AipNlp client = null;

    public static void initClient(String APP_ID,String API_KEY,String SECRET_KEY ){
        System.out.println("init nlpClient");
        if(client == null){
            client = new AipNlp(APP_ID,API_KEY,SECRET_KEY);
        }
    }

    public static AipNlp getClient(){
        return client;
    }

}
