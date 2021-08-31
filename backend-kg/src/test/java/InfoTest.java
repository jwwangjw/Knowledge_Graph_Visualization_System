import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.example.KnowledgeGraph.bllmpl.Info.CSVParseStrategy;
import com.example.KnowledgeGraph.bllmpl.Info.InfoServiceImpl;


import com.example.KnowledgeGraph.bllmpl.Info.TXTParseStrategy;
import com.example.KnowledgeGraph.controller.Info.InfoController;
import com.example.KnowledgeGraph.kgStruct.KgStruct;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.mock.web.MockMultipartFile;
import org.apache.http.entity.ContentType;
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
public class InfoTest {

    @LocalServerPort
    private int port;

    private URL base;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    InfoController infoController = new InfoController();

    @Autowired
    InfoServiceImpl infoService = new InfoServiceImpl();

    @Autowired
    TXTParseStrategy txt = new TXTParseStrategy();

    @Autowired
    CSVParseStrategy csv = new CSVParseStrategy();
    @Before
    public void setUp() throws Exception {
        String url = String.format("http://localhost:%d/", port);
        System.out.println(String.format("port is : [%d]", port));
        this.base = new URL(url);
    }


    @Test
    public void csvTest() throws Exception{
        try{
            File file = new File("C:\\Users\\86183\\Desktop\\test25.txt");
            FileInputStream fileInputStream = new FileInputStream(file);
            MultipartFile multipartFile = new MockMultipartFile(file.getName(), file.getName(),
                    ContentType.APPLICATION_OCTET_STREAM.toString(), fileInputStream);

            infoService.fileParse(multipartFile, 0);

        }
        catch (Exception e){
            System.out.println("路径错误或不存在！");
            e.printStackTrace();
            Assert.fail();
        }
    }

    @Test
    public void txtTest() throws Exception{
        try{
            txt.fileParser("src/test/java/sentence1.txt");
        }
        catch (Exception e){
            System.out.println("路径错误或不存在！");
            System.out.println(e.toString());
            Assert.fail();
        }
    }

    @Test
    public void getKgFileTest() throws Exception{
        try{
            String fileJsons = infoService.getKgFile(0).getContent().toString();
            System.out.println(fileJsons);
            Assert.assertNotNull(fileJsons);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void fusion() throws Exception{
        try {
            List<String> lists=new ArrayList<>();
            lists.add("南京大学,学校,拥有,0,两个校区,校区");
            lists.add("仙林校区,校区,是,0,南大,机构");
            KgStruct k=new KgStruct();
            k.build_f(lists);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void txtTripleTest() throws Exception{
        try{
            infoController.txtTriples("{\"options\":[{\"mainBody\":\"这个事项\",\"relation\":\"称为\",\"object\":\"事项\",\"Mpost\":\"r\",\"Spost\":\"n\"}, " +
                    "{\"mainBody\":\"qweqw\",\"relation\":\"称er\",\"object\":\"sdfasdf\",\"Mpost\":\"r\",\"Spost\":\"n\"}," +
                    "{\"mainBody\":\"这sdfs\",\"relation\":\"2wewe\",\"object\":\"dsgfggg\",\"Mpost\":\"r\",\"Spost\":\"n\"}],\"name\":\"sb\",\"userId\":\"1\"}");
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}

