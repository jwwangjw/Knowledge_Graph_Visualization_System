import java.net.URL;

import com.example.KnowledgeGraph.bllmpl.Info.InfoServiceImpl;
import com.example.KnowledgeGraph.bllmpl.KG.sqlServiceImpl;
import com.example.KnowledgeGraph.data.readMapper;
import com.example.KnowledgeGraph.po.fileJson;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.KnowledgeGraph.KnowledgeGraphApplication;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = KnowledgeGraphApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SqlTest {
    @LocalServerPort
    private int port;

    private URL base;

    @Autowired
    private TestRestTemplate restTemplate;



    @Autowired
    InfoServiceImpl infoService = new InfoServiceImpl();

    @Autowired
    sqlServiceImpl sqlService = new sqlServiceImpl();

    @Autowired
    readMapper readMapper;

    @Before
    public void setUp() throws Exception {
        String url = String.format("http://localhost:%d/", port);
        System.out.println(String.format("port is : [%d]", port));
        this.base = new URL(url);
    }

    @Test
    public void addSqlElementTest(){
        fileJson fileJson = new fileJson();
        fileJson.setFileId(1);
        fileJson.setId(1);
        String json = "{\"links\": [{\"id\": 501, \"type\": 0, \"source\": \"wang\", \"target\": \"dog\", \"description\": \"xihuan\"}, {\"id\": 502, \"type\": 0, \"source\": \"1\", \"target\": \"1\", \"description\": \"1\"}], \"nodes\": [{\"x\": 0.0, \"y\": 0.0, \"id\": 1, \"vx\": 0.0, \"vy\": 0.0, \"name\": \"wang\", \"index\": 0, \"flagSeg\": \"Person\", \"property\": \"\"}, {\"x\": 0.0, \"y\": 0.0, \"id\": 2, \"vx\": 0.0, \"vy\": 0.0, \"name\": \"dog\", \"index\": 0, \"flagSeg\": \"animal\", \"property\": \"\"}, {\"x\": 0.0, \"y\": 0.0, \"id\": 3, \"vx\": 0.0, \"vy\": 0.0, \"name\": \"1\", \"index\": 0, \"flagSeg\": \"Person\", \"property\": \"\"}]}";
        fileJson.setJson(json);
        infoService.saveFileJsonToSQL(1,json);
        sqlService.addSqlElement("xiaohu","Person",1,json,"{}");
        System.out.println(infoService.getSqlJson(1).getContent().toString());
    }

    @Test
    public void updateSqlElementTest(){
        fileJson fileJson = new fileJson();
        fileJson.setFileId(1);
        fileJson.setId(1);
        String json = "{\"links\": [{\"id\": 501, \"type\": 0, \"source\": \"wang\", \"target\": \"dog\", \"description\": \"xihuan\"}, {\"id\": 502, \"type\": 0, \"source\": \"1\", \"target\": \"1\", \"description\": \"1\"}], \"nodes\": [{\"x\": 0.0, \"y\": 0.0, \"id\": 1, \"vx\": 0.0, \"vy\": 0.0, \"name\": \"wang\", \"index\": 0, \"flagSeg\": \"Person\", \"property\": \"\"}, {\"x\": 0.0, \"y\": 0.0, \"id\": 2, \"vx\": 0.0, \"vy\": 0.0, \"name\": \"dog\", \"index\": 0, \"flagSeg\": \"animal\", \"property\": \"\"}, {\"x\": 0.0, \"y\": 0.0, \"id\": 3, \"vx\": 0.0, \"vy\": 0.0, \"name\": \"1\", \"index\": 0, \"flagSeg\": \"Person\", \"property\": \"\"}]}";
        fileJson.setJson(json);
        infoService.saveFileJsonToSQL(1,json);
        sqlService.updateSqlElement((long)1,"xioahu","niubi","{}",1,json);
        System.out.println(infoService.getSqlJson(1).getContent().toString());
    }

    @Test
    public void deleteSqlElementTest(){
        fileJson fileJson = new fileJson();
        fileJson.setFileId(1);
        fileJson.setId(1);
        String json = "{\"links\": [{\"id\": 501, \"type\": 0, \"source\": \"wang\", \"target\": \"dog\", \"description\": \"xihuan\"}, {\"id\": 502, \"type\": 0, \"source\": \"1\", \"target\": \"1\", \"description\": \"1\"}], \"nodes\": [{\"x\": 0.0, \"y\": 0.0, \"id\": 1, \"vx\": 0.0, \"vy\": 0.0, \"name\": \"wang\", \"index\": 0, \"flagSeg\": \"Person\", \"property\": \"\"}, {\"x\": 0.0, \"y\": 0.0, \"id\": 2, \"vx\": 0.0, \"vy\": 0.0, \"name\": \"dog\", \"index\": 0, \"flagSeg\": \"animal\", \"property\": \"\"}, {\"x\": 0.0, \"y\": 0.0, \"id\": 3, \"vx\": 0.0, \"vy\": 0.0, \"name\": \"1\", \"index\": 0, \"flagSeg\": \"Person\", \"property\": \"\"}]}";
        fileJson.setJson(json);
        infoService.saveFileJsonToSQL(1,json);
        sqlService.deleteSqlElement((long)1,1,json);
        System.out.println(infoService.getSqlJson(1).getContent().toString());
    }

    @Test
    public void addSqlRelationTest(){
        fileJson fileJson = new fileJson();
        fileJson.setFileId(1);
        fileJson.setId(1);
        String json = "{\"links\": [{\"id\": 501, \"type\": 0, \"source\": \"wang\", \"target\": \"dog\", \"description\": \"xihuan\"}, {\"id\": 502, \"type\": 0, \"source\": \"1\", \"target\": \"1\", \"description\": \"1\"}], \"nodes\": [{\"x\": 0.0, \"y\": 0.0, \"id\": 1, \"vx\": 0.0, \"vy\": 0.0, \"name\": \"wang\", \"index\": 0, \"flagSeg\": \"Person\", \"property\": \"\"}, {\"x\": 0.0, \"y\": 0.0, \"id\": 2, \"vx\": 0.0, \"vy\": 0.0, \"name\": \"dog\", \"index\": 0, \"flagSeg\": \"animal\", \"property\": \"\"}, {\"x\": 0.0, \"y\": 0.0, \"id\": 3, \"vx\": 0.0, \"vy\": 0.0, \"name\": \"1\", \"index\": 0, \"flagSeg\": \"Person\", \"property\": \"\"}]}";
        fileJson.setJson(json);
        infoService.saveFileJsonToSQL(1,json);
        sqlService.addSqlRelation((long)1,(long)2,"bing!",0,1,json);
        System.out.println(infoService.getSqlJson(1).getContent().toString());
    }

    @Test
    public void updateSqlRelationTest(){
        fileJson fileJson = new fileJson();
        fileJson.setFileId(1);
        fileJson.setId(1);
        String json = "{\"links\": [{\"id\": 501, \"type\": 0, \"source\": \"wang\", \"target\": \"dog\", \"description\": \"xihuan\"}, {\"id\": 502, \"type\": 0, \"source\": \"1\", \"target\": \"1\", \"description\": \"1\"}], \"nodes\": [{\"x\": 0.0, \"y\": 0.0, \"id\": 1, \"vx\": 0.0, \"vy\": 0.0, \"name\": \"wang\", \"index\": 0, \"flagSeg\": \"Person\", \"property\": \"\"}, {\"x\": 0.0, \"y\": 0.0, \"id\": 2, \"vx\": 0.0, \"vy\": 0.0, \"name\": \"dog\", \"index\": 0, \"flagSeg\": \"animal\", \"property\": \"\"}, {\"x\": 0.0, \"y\": 0.0, \"id\": 3, \"vx\": 0.0, \"vy\": 0.0, \"name\": \"1\", \"index\": 0, \"flagSeg\": \"Person\", \"property\": \"\"}]}";
        fileJson.setJson(json);
        infoService.saveFileJsonToSQL(1,json);
        sqlService.updateSqlRelation((long)501,"xuanxuan",1,1,json);
        System.out.println(infoService.getSqlJson(1).getContent().toString());
    }

    @Test
    public void deleteSqlRelationTest(){
        fileJson fileJson = new fileJson();
        fileJson.setFileId(1);
        fileJson.setId(1);
        String json = "{\"links\": [{\"id\": 501, \"type\": 0, \"source\": \"wang\", \"target\": \"dog\", \"description\": \"xihuan\"}, {\"id\": 502, \"type\": 0, \"source\": \"1\", \"target\": \"1\", \"description\": \"1\"}], \"nodes\": [{\"x\": 0.0, \"y\": 0.0, \"id\": 1, \"vx\": 0.0, \"vy\": 0.0, \"name\": \"wang\", \"index\": 0, \"flagSeg\": \"Person\", \"property\": \"\"}, {\"x\": 0.0, \"y\": 0.0, \"id\": 2, \"vx\": 0.0, \"vy\": 0.0, \"name\": \"dog\", \"index\": 0, \"flagSeg\": \"animal\", \"property\": \"\"}, {\"x\": 0.0, \"y\": 0.0, \"id\": 3, \"vx\": 0.0, \"vy\": 0.0, \"name\": \"1\", \"index\": 0, \"flagSeg\": \"Person\", \"property\": \"\"}]}";
        fileJson.setJson(json);
        infoService.saveFileJsonToSQL(1,json);
        sqlService.deleteSqlRelation((long)501,1,json);
        System.out.println(infoService.getSqlJson(1).getContent().toString());
    }

    @Test
    public void deleteSqlAll(){
        fileJson fileJson = new fileJson();
        fileJson.setFileId(1);
        fileJson.setId(1);
        String json = "{\"links\": [{\"id\": 501, \"type\": 0, \"source\": \"wang\", \"target\": \"dog\", \"description\": \"xihuan\"}, {\"id\": 502, \"type\": 0, \"source\": \"1\", \"target\": \"1\", \"description\": \"1\"}], \"nodes\": [{\"x\": 0.0, \"y\": 0.0, \"id\": 1, \"vx\": 0.0, \"vy\": 0.0, \"name\": \"wang\", \"index\": 0, \"flagSeg\": \"Person\", \"property\": \"\"}, {\"x\": 0.0, \"y\": 0.0, \"id\": 2, \"vx\": 0.0, \"vy\": 0.0, \"name\": \"dog\", \"index\": 0, \"flagSeg\": \"animal\", \"property\": \"\"}, {\"x\": 0.0, \"y\": 0.0, \"id\": 3, \"vx\": 0.0, \"vy\": 0.0, \"name\": \"1\", \"index\": 0, \"flagSeg\": \"Person\", \"property\": \"\"}]}";
        fileJson.setJson(json);
        infoService.saveFileJsonToSQL(1,json);
        sqlService.deleteSqlAll(1);
        System.out.println(infoService.getSqlJson(1).getContent().toString());
    }

    @Test
    public void addSqlPropertyTest(){
        fileJson fileJson = new fileJson();
        fileJson.setFileId(1);
        fileJson.setId(1);
        String json = "{\"links\": [{\"id\": 501, \"type\": 0, \"source\": \"wang\", \"target\": \"dog\", \"description\": \"xihuan\"}, {\"id\": 502, \"type\": 0, \"source\": \"1\", \"target\": \"1\", \"description\": \"1\"}], \"nodes\": [{\"x\": 0.0, \"y\": 0.0, \"id\": 1, \"vx\": 0.0, \"vy\": 0.0, \"name\": \"wang\", \"index\": 0, \"flagSeg\": \"Person\", \"property\": \"\"}, {\"x\": 0.0, \"y\": 0.0, \"id\": 2, \"vx\": 0.0, \"vy\": 0.0, \"name\": \"dog\", \"index\": 0, \"flagSeg\": \"animal\", \"property\": \"\"}, {\"x\": 0.0, \"y\": 0.0, \"id\": 3, \"vx\": 0.0, \"vy\": 0.0, \"name\": \"1\", \"index\": 0, \"flagSeg\": \"Person\", \"property\": \"\"}]}";
        fileJson.setJson(json);
        infoService.saveFileJsonToSQL(1,json);
        sqlService.addSqlProperty("称号","wangba",(long)1,1,json);
        System.out.println(infoService.getSqlJson(1).getContent().toString());
    }

    @Test
    public void updateSqlPropertyTest(){
        fileJson fileJson = new fileJson();
        fileJson.setFileId(1);
        fileJson.setId(1);
        String json = "{\"links\": [{\"id\": 501, \"type\": 0, \"source\": \"wang\", \"target\": \"dog\", \"description\": \"xihuan\"}, {\"id\": 502, \"type\": 0, \"source\": \"1\", \"target\": \"1\", \"description\": \"1\"}], \"nodes\": [{\"x\": 0.0, \"y\": 0.0, \"id\": 1, \"vx\": 0.0, \"vy\": 0.0, \"name\": \"wang\", \"index\": 0, \"flagSeg\": \"Person\", \"property\": \"\"}, {\"x\": 0.0, \"y\": 0.0, \"id\": 2, \"vx\": 0.0, \"vy\": 0.0, \"name\": \"dog\", \"index\": 0, \"flagSeg\": \"animal\", \"property\": \"\"}, {\"x\": 0.0, \"y\": 0.0, \"id\": 3, \"vx\": 0.0, \"vy\": 0.0, \"name\": \"1\", \"index\": 0, \"flagSeg\": \"Person\", \"property\": \"\"}]}";
        fileJson.setJson(json);
        infoService.saveFileJsonToSQL(1,json);
        sqlService.addSqlProperty("称号","wangba",(long)1,1,json);
        json = infoService.getSqlJson(1).getContent().toString();
        sqlService.updateSqlProperty("称号","w",(long)1,1,json);
        System.out.println(infoService.getSqlJson(1).getContent().toString());
    }

    @Test
    public void deleteSqlPropertyTest(){
        fileJson fileJson = new fileJson();
        fileJson.setFileId(1);
        fileJson.setId(1);
        String json = "{\"links\": [{\"id\": 501, \"type\": 0, \"source\": \"wang\", \"target\": \"dog\", \"description\": \"xihuan\"}, {\"id\": 502, \"type\": 0, \"source\": \"1\", \"target\": \"1\", \"description\": \"1\"}], \"nodes\": [{\"x\": 0.0, \"y\": 0.0, \"id\": 1, \"vx\": 0.0, \"vy\": 0.0, \"name\": \"wang\", \"index\": 0, \"flagSeg\": \"Person\", \"property\": \"\"}, {\"x\": 0.0, \"y\": 0.0, \"id\": 2, \"vx\": 0.0, \"vy\": 0.0, \"name\": \"dog\", \"index\": 0, \"flagSeg\": \"animal\", \"property\": \"\"}, {\"x\": 0.0, \"y\": 0.0, \"id\": 3, \"vx\": 0.0, \"vy\": 0.0, \"name\": \"1\", \"index\": 0, \"flagSeg\": \"Person\", \"property\": \"\"}]}";
        fileJson.setJson(json);
        infoService.saveFileJsonToSQL(1,json);
        sqlService.addSqlProperty("称号","wangba",(long)1,1,json);
        json = infoService.getSqlJson(1).getContent().toString();
        sqlService.deleteSqlProperty("称号",(long)1,1,json);
        System.out.println(infoService.getSqlJson(1).getContent().toString());
    }

    @Test
    public void getSearchRecordTest(){
        sqlService.updateSearchRecord(1,"{\"1\":\"asdasd\"}");
        System.out.println(sqlService.getSearchRecord(1).getContent().toString());
    }

    @Test
    public void updateSearchRecordTest(){
        sqlService.updateSearchRecord(2,"{\"1\":\"asdas\"}");
        System.out.println(sqlService.getSearchRecord(2).getContent().toString());
    }

    @Test
    public void updateFilenameTest(){
        try{
            infoService.updateFileName(14, "relation_test.csv",0);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void deleteKgTest(){
        try{
            infoService.deleteKgByFileId(15);
            Assert.assertNull(sqlService.getSearchRecord(15).getMessage());
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
