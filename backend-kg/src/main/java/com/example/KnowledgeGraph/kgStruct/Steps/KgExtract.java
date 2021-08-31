package com.example.KnowledgeGraph.kgStruct.Steps;

import com.baidu.aip.nlp.AipNlp;
import com.example.KnowledgeGraph.data.nlpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class KgExtract {

    ArrayList<Word> words;
    ArrayList<Triple> triples;

    public ArrayList<Triple> extraction(ArrayList<String> sents){
        try {
            AipNlp aipNlp = nlpClient.getClient();
            triples = new ArrayList<Triple>();
            HashMap<String, Object> options = new HashMap<String, Object>();
            options.put("mode", 1);
            for (String sen : sents) {
                JSONArray res = aipNlp.depParser(sen, options).getJSONArray("items");
                //System.out.println(res.toString(2));
                Word root = new Word("root", 0, -1, null, null);
                words = new ArrayList<>();
                for (int i = 0; i < res.length(); i++) {
                    JSONObject jsobj = res.getJSONObject(i);
                    Word temp = new Word(jsobj.getString("word"), jsobj.getInt("id"), jsobj.getInt("head"), jsobj.getString("postag"), jsobj.getString("deprel"));
                    words.add(temp);
                }
                System.out.println("句子：" + sen);
                wordTree(root);
                extract(root.getWords().get(0), new ArrayList<Word>(), new ArrayList<Word>());
        /*for(Triple t: triples){
            System.out.println(t.getStr());
        }*/
            }
            return triples;
        } catch (Exception e){
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public ArrayList<Word> extract(Word Relation, ArrayList<Word> MEntity, ArrayList<Word> SEntity){
        try{
            //System.out.println("--对 "+ Relation.getContent() + " 进行抽取-----------");
            ArrayList<Word> words = Relation.getWords();
            ArrayList<Word> mEntity = new ArrayList<Word>();
            //抽取主体
            //System.out.println("抽取主体");
            boolean isSubExist = false;
            for(int i=0;i<words.size();i++) {
                if(words.get(i).isDeprelEquals("SBV,IS") && words.get(i).isPostagNotEquals("c")){
                    isSubExist = true;
                    mEntity.add(words.get(i));
                }
            }
            if(!isSubExist){
                for(int i=0;i<words.size();i++){
                    //关联结构，主语可能藏在这里
                    if(words.get(i).isDeprelEquals("CS")) {
                        mEntity = extract(words.get(i),new ArrayList<Word>(), new ArrayList<Word>());
                    }
                }
            }

            if(mEntity.size()!=0){
                MEntity = mEntity;
            }
        /*System.out.println("主体：");
        for(int i=0;i<MEntity.size();i++){
            System.out.println(MEntity.get(i).getContent());
        }*/

            //抽取客体
            //System.out.println("抽取客体");
            boolean isObjExist = false;
            for(int i=0;i<words.size();i++){
                if(words.get(i).isDeprelEquals("VOB,FOB")){
                    isObjExist = true;
                    SEntity.add(words.get(i));
                }
                if(words.get(i).isDeprelEquals("DOB")){
                    isObjExist = true;
                    Word word1 = words.get(i);
                    Word word2 = null;
                    for(int j=i+1;j<words.size();j++){
                        if(words.get(j).isDeprelEquals("DOB")){
                            word2 = words.get(j);
                            break;
                        }
                    }
                    if(word2 != null){
                        Word temp = Relation.copy();
                        ArrayList<Word> sEntity = new ArrayList<>();
                        if(word1.getId()<word2.getId()){
                            temp.extendWord(word2.getContent());
                            sEntity.add(word1);
                        }else {
                            temp.extendWord(word1.getContent());
                            sEntity.add(word2);
                        }
                        addTriples(temp,MEntity,sEntity);
                    }
                }
                //被、把字句
                if(words.get(i).isDeprelEquals("BEI,BA")){
                    ArrayList<Word> wordsTemp = words.get(i).getWords();
                    for(int j=0;j<wordsTemp.size();j++){
                        if(wordsTemp.get(j).isDeprelEquals("BEI,BA")){
                            isObjExist = true;
                            SEntity.add(wordsTemp.get(j));
                        }
                    }
                }
                if(words.get(i).isDeprelEquals("VV")){
                    extract(words.get(i),MEntity,new ArrayList<Word>());
                }
            }
            boolean isPOBExist = false;
            for(int i=0;i<words.size();i++){
                if(words.get(i).isDeprelEquals("POB")){
                    ArrayList<Word> represents = Relation.getRepresent();
                    for(int j=0;j<represents.size();j++){
                        if(represents.get(j).getId()==words.get(i).getId()-1){
                            assert represents.get(j).getPostag().equals("p");
                            Word temp = represents.get(j).copy();
                            temp.extendWord(words.get(i).getContent());
                            if(!isObjExist){
                                isPOBExist = true;
                                SEntity.add(temp);
                            }else{
                                Word Property = new Word("属性", -2, -2, "n", "SBV");
                                ArrayList<Word> sEntity = new ArrayList<>();
                                sEntity.add(temp);
                                addTriples(Property,MEntity,sEntity);
                            }
                        }
                    }
                }
            }
            if(!isObjExist&&isPOBExist){
                isObjExist = true;
            }

            if(isObjExist){
                addTriples(Relation,MEntity,SEntity);
            }else{
                Word Property = new Word("属性", -2, -2, "n", "SBV");
                ArrayList<Word> sEntity = new ArrayList<>();
                sEntity.add(Relation);
                addTriples(Property, MEntity, sEntity);
            }

            //处理独立句式
            for(int i=0;i<words.size();i++){
                if(words.get(i).isDeprelEquals(("IC,COO"))) {
                    extract(words.get(i), MEntity, new ArrayList<Word>());
                }
            }

        /*int index=0;
        for(int i=0;i<this.words.size();i++){
            if(this.words.get(i).getId() == Relation.getId()){
                index = i;
            }
        }
        for(int i=index+1;i<this.words.size();i++){
            if(this.words.get(i).getDeprel().equals("IC") ){
                extract(this.words.get(i), new ArrayList<Word>(), new ArrayList<Word>());
            }
        }*/

            return MEntity;
        }
        catch (Exception e){
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
    private void addTriples(Word Relation, ArrayList<Word> MEntity, ArrayList<Word> SEntity){
        try{
            ArrayList<Word> mEntity = new ArrayList<>();
            ArrayList<Word> sEntity = new ArrayList<>();
            for(int i=0;i<MEntity.size();i++){
                mEntity.add(MEntity.get(i));
                ArrayList<Word> temp = CooExtract(MEntity.get(i));
                for(int j=0;j<temp.size();j++){
                    mEntity.add(temp.get(j));
                }
            }
            for(int i=0;i<SEntity.size();i++){
                sEntity.add(SEntity.get(i));
                ArrayList<Word> temp = CooExtract(SEntity.get(i));
                for(int j=0;j<temp.size();j++){
                    sEntity.add(temp.get(j));
                }
            }
            /*if(sEntity.size()==0){
                Word Property = new Word("属性", -2, -2, "n", "SBV");
                for(int i=0;i<mEntity.size();i++){
                    triples.add(new Triple(mEntity.get(i),Property,Relation));
                }
                return;
            }*/
            for(int i=0;i<mEntity.size();i++){
                for(int j=0;j<sEntity.size();j++){
                    triples.add(new Triple(mEntity.get(i),Relation,sEntity.get(j)));
                }
            }
            return;
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private ArrayList<Word> CooExtract(Word entity){
        try{
            ArrayList<Word> child = entity.getWords();
            if(child.size()==0) return new ArrayList<Word>();
            ArrayList<Word> childCoo = new ArrayList<Word>();
            for(int j=0;j<child.size();j++){
                if(child.get(j).getDeprel().equals("COO")&& child.get(j).isPostagNotEquals("c")){
                    childCoo.add(child.get(j));
                    ArrayList<Word> ens = CooExtract(child.get(j));
                    if(ens != null){
                        for(int k=0;k<ens.size();k++){
                            childCoo.add(ens.get(k));
                        }
                    }
                }
            }
            return childCoo;
        }
        catch (Exception e){
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    private void wordTree(Word word){
        try{
            for(int i=words.size()-1; i>=0; i--) {
                if(word.isRepresent(words.get(i)) && words.get(i).isDeprelEquals("ATT,ADV,DE,QUN,TMP,LOC,CMP")) {
                    word.addRepresent(words.get(i));
                } else if(words.get(i).getHead() == word.getId()){
                    word.addWord(words.get(i));
                    wordTree(words.get(i));
                }
            }
            for(int i=0;i<words.size();i++){
                if(word.isRepresentChild(words.get(i))) {
                    word.addWord(words.get(i));
                    wordTree(words.get(i));
                }
            }
            word.Print();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
