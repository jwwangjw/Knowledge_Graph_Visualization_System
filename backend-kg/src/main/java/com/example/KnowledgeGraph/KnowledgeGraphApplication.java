
package com.example.KnowledgeGraph;

import com.example.KnowledgeGraph.bl.Info.InfoService;
import com.example.KnowledgeGraph.bllmpl.Info.CSVParseStrategy;

import com.example.KnowledgeGraph.bllmpl.Info.InfoServiceImpl;
import com.example.KnowledgeGraph.data.nlpClient;
import com.example.KnowledgeGraph.vo.ResponseVO;
import org.apache.catalina.connector.Connector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;


@SpringBootApplication
@EnableNeo4jRepositories
public class KnowledgeGraphApplication {

	private final static Logger log = LoggerFactory.getLogger(KnowledgeGraphApplication.class);

	public static void main(String[] args) throws Exception {
		SpringApplication.run(KnowledgeGraphApplication.class, args);
	}

	@Bean
	void createClient(){
		nlpClient.initClient("24390612","xnAqAkjsM06yXwsgfu9Qbp9m", "sW4iNzTyXxUy6ZKKqzwHmvl7hC9bnEjA");
	}

	@Bean
	public TomcatServletWebServerFactory webServerFactory() {
		TomcatServletWebServerFactory factory = new TomcatServletWebServerFactory();
		factory.addConnectorCustomizers((Connector connector) -> {
			connector.setProperty("relaxedPathChars", "\"<>[\\]^`{|}");
			connector.setProperty("relaxedQueryChars", "\"<>[\\]^`{|}");
		});
		return factory;
	}

/*	@Bean
	CommandLineRunner demo(InfoServiceImpl InfoService) {
		return args -> {
			System.out.println(InfoService.getInputString(""));
		};
	}*/


}
