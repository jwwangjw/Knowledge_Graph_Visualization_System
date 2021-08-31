package com.example.KnowledgeGraph.kgStruct.Steps;
import java.io.*;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.ConsolePipeline;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;

public class BaiKe implements PageProcessor {
    // 抓取配置
    private Site site = Site.me().setRetryTimes(3).setSleepTime(500).setCharset("UTF-8").setUserAgent(
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.84 Safari/537.36");
    // 检索词
    private static String word;
    // 存储路经
    private static final String PATH = "upload/";
    @Override
    public Site getSite() {
        return site;
    }
    @Override
    public void process(Page page) {
        File file1=new File("a.txt");
        String path1=file1.getAbsolutePath();
        file1.delete();
        String path_t=path1.substring(0,path1.length()-6);
        String UPLOAD_FOLDER=path_t +'/'+"upload/";
        Html html = page.getHtml();
        String dts=html.xpath("/html/body/div[3]/div[@class='content-wrapper']/div[@class='content']/div[@class='main-content J-content']/div[@class='basic-info J-basic-info cmn-clearfix']/dl/dt/allText()").all().toString().replaceAll(" ","");
        String dds=html.xpath("/html/body/div[3]/div[@class='content-wrapper']/div[@class='content']/div[@class='main-content J-content']/div[@class='basic-info J-basic-info cmn-clearfix']/dl/dd/allText()").all().toString().replaceAll(" ","");
        if(dts.equals("[]")){
            dts=html.xpath("/html/body/div[3]/div[@class='content-wrapper']/div[@class='content']/div[@class='main-content J-content']/div[1]/div[@class='basic-info J-basic-info cmn-clearfix']/dl/dt/allText()").all().toString().replaceAll(" ","");
            dds=html.xpath("/html/body/div[3]/div[@class='content-wrapper']/div[@class='content']/div[@class='main-content J-content']/div[1]/div[@class='basic-info J-basic-info cmn-clearfix']/dl/dd/allText()").all().toString().replaceAll(" ","");
        }
        System.out.println(dts);
        try {
            outPut(UPLOAD_FOLDER + "test.txt", dts);
            outPut(UPLOAD_FOLDER + "test.txt", dds);
        } catch (IOException e) {
            e.printStackTrace();
        }

        /**if (html.xpath("/html/body/div[3]/div[2]/div/div[2]/dl[1]/dd/h1/text()").match()) {
                // 标题
                String title = html.xpath("/html/body/div[3]/div[2]/div/div[2]/dl[1]/dd/h1/text()").toString();
                // 同义词
                String synonym = html.xpath("/html/body/div[3]/div[2]/div/div[2]/span/span/text()").all().toString();
                // 摘要
                String summary = html.xpath("/html/body/div[3]/div[2]/div/div[2]/div[@class='lemma-summary']/allText()")
                        .all().toString().replaceAll(",", "");
                // 信息框
                String infobox = html
                        .xpath("/html/body/div[3]/div[2]/div/div[2]/div[@class='basic-info cmn-clearfix']/dl/allText()")
                        .all().toString();
                StringBuilder sb = new StringBuilder();
                sb.append(word + "\t" + title + "\t" + synonym + "\t" + summary + "\t" + infobox + "\n");
                try {
                    outPut(PATH + "百科词条.txt", sb.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                // 百度知心,由于是动态加载，先获取id
                String zhixinID = html.xpath("//*[@id=\"zhixinWrap\"]/@data-newlemmaid").toString();
                String zhixinURL = "https://baike.baidu.com/wikiui/api/zhixinmap?lemmaId=" + zhixinID;
                // 添加任务
                page.addTargetRequest(zhixinURL);
            }
            // 如果不存在词条则获取最相关的词条，添加url到任务
            else {
                page.addTargetRequest("https://baike.baidu.com/search/none?word=" + word);
            }
        }
        // 解析百度知心Json
        else if (page.getUrl().regex("https://baike.baidu.com/wikiui/.*").match()) {
            Map<String, List<String>> resultMap = new LinkedHashMap<>();
            // 获取相关类型的数量，因为有的词是两个，有些是三个
            List<String> tempList = new JsonPathSelector("$.[*]").selectList(page.getRawText());
            int typeNums = tempList.size();
            int count = typeNums;
            while (count > 0) {
                resultMap.put(new JsonPathSelector("$.[" + (typeNums - count) + "].tipTitle").select(page.getRawText()),
                        new JsonPathSelector("$.[" + (typeNums - count) + "].data[*].title")
                                .selectList(page.getRawText()));
                count--;
            }
            StringBuilder sb = new StringBuilder();
            sb.append(word + "\t");
            resultMap.forEach((key, value) -> {
                sb.append(key + "：" + value + "\t");
            });
            sb.append("\n");
            try {
                outPut(PATH + "相关词条_知心.txt", sb.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // 百度百科尚未收录词条，获取相关词条
        else if (page.getUrl().regex("https://baike.baidu.com/search/none\\?word=.*").match()) {
            List<String> list = page.getHtml().xpath("//*[@id=\"body_wrapper\"]/div[1]/dl/dd/a/@href").all();
            try {
                list = UrlDecode(list);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            StringBuilder sb = new StringBuilder();
            sb.append(word + "\t" + list + "\n");
            try {
                outPut(PATH + "相关词条_未收录.txt", sb.toString());
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else
            System.out.println("Nothing!");*/}
    // URL解码
    @SuppressWarnings("unused")
    private static List<String> UrlDecode(List<String> rawList) throws UnsupportedEncodingException {
        List<String> resultList = new LinkedList<>();
        String reg = "https://baike.baidu.com/item/(.*)/\\d+";
        Pattern p = Pattern.compile(reg);
        Matcher m;
        for (String str : rawList) {
            m = p.matcher(str);
            if (m.find()) {
                resultList.add(java.net.URLDecoder.decode(m.group(1), "UTF-8"));
            }
        }
        return resultList;
    }
    // 存储
    private static void outPut(String path, String content) throws IOException {
        FileOutputStream fos = new FileOutputStream(path, true);
        OutputStreamWriter osw = new OutputStreamWriter(fos);
        BufferedWriter bw = new BufferedWriter(osw);
        bw.write(word+":"+content+"\n");
        bw.close();
    }
    public static void getContent(String words) throws IOException {
        System.out.println("wordsss");
        word=words;
        Spider.create(new BaiKe()).addPipeline(new ConsolePipeline())
                .addUrl("https://baike.baidu.com/item/" + words).run();
    }
}