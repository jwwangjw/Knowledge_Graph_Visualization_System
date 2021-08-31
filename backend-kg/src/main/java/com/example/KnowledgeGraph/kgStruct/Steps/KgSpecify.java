package com.example.KnowledgeGraph.kgStruct.Steps;

import java.util.ArrayList;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class KgSpecify {

    ArrayList<Triple> trustTriples = new ArrayList<>();
    ArrayList<Triple> untrustTriples = new ArrayList<>();

    public String specify(ArrayList<Triple> triples) {
        try{
            filiter(triples);
            //System.out.println(trustTriples.size());
            //System.out.println(untrustTriples.size());
            representer();
            String result = compose();
            return result;
        }
        catch (Exception e){
            e.printStackTrace();
            return "";
        }
    }

    public void representer(){
        try{
            int len_trust = trustTriples.size();
            int len_untrust = untrustTriples.size();
            ArrayList<Integer> already_modified = new ArrayList<>();
            ArrayList<Word> rep;
            Word Property = new Word("属性", -2, -2, "n", "SBV");
            Word Time = new Word("时间", -2, -2, "n", "SBV");
            Word MEntity;
            Word SEntity;
            Word Relation;
            ArrayList<Word> SE_child;
            for (int i=0;i<len_trust;i++){
                MEntity = trustTriples.get(i).getMEntity();
                SEntity = trustTriples.get(i).getSEntity();
                Relation = trustTriples.get(i).getRelation();
                if (SEntity.getDeprel().equals("BEI") && SEntity.getHead() == Relation.getId()){
                    Word temp = MEntity;
                    MEntity = SEntity;
                    SEntity = temp;
                }
                rep = MEntity.getRepresent();
                if (!already_modified.contains(MEntity.getId())){
                    already_modified.add(MEntity.getId());
                    for (Word single_rep:rep) {
                        if (single_rep.getDeprel().equals("ATT")) {
                            if (single_rep.getPostag().equals("a")){
                                trustTriples.add(new Triple(MEntity, Property, single_rep));
                            }
                            else {
                                trustTriples.get(i).getMEntity().setContent(single_rep.getContent()+trustTriples.get(i).getMEntity().getContent());
                            }
                        }
                        else if (single_rep.getDeprel().equals("DE")){
                            if (single_rep.getPostag().equals("a")){
                                trustTriples.add(new Triple(MEntity, Property, single_rep));
                            }
                            else if (!single_rep.getContent().equals("的")){
                                System.out.println(single_rep.getContent());
                                System.out.println(single_rep.getContent().equals("的"));
                                trustTriples.get(i).getMEntity().setContent(single_rep.getContent()+trustTriples.get(i).getMEntity().getContent());
                            }
                        }
                    }
                }


                if (!already_modified.contains(SEntity.getId())){
                    already_modified.add(SEntity.getId());
                    rep = SEntity.getRepresent();
                    SE_child = SEntity.getWords();
                    for (Word single_child:SE_child){
                        if (single_child.getDeprel().equals("VOB") && SEntity.getPostag().equals("v")){
                            ArrayList<Word> child_rep = single_child.getRepresent();
                            for (Word s_child_rep:child_rep) {
                                single_child.setContent(s_child_rep.getContent()+single_child.getContent());
                            }
                            trustTriples.get(i).getSEntity().setContent(trustTriples.get(i).getSEntity().getContent()+single_child.getContent());
                        }
                    }
                    for (Word single_rep:rep) {
                        if (single_rep.getDeprel().equals("ATT")) {
                            if (single_rep.getPostag().equals("a")){
                                trustTriples.add(new Triple(SEntity, Property, single_rep));
                            }
                            else {
                                trustTriples.get(i).getSEntity().setContent(single_rep.getContent()+trustTriples.get(i).getSEntity().getContent());
                            }
                        }
                        if (single_rep.getDeprel().equals("DE")){
                            if (single_rep.getPostag().equals("a")){
                                trustTriples.add(new Triple(SEntity, Property, single_rep));
                            }
                            else if (!single_rep.getContent().equals("的")){
                                trustTriples.get(i).getSEntity().setContent(single_rep.getContent()+trustTriples.get(i).getSEntity().getContent());
                            }
                        }
                    }

                }

                if (!already_modified.contains(Relation.getId())){
                    already_modified.add(Relation.getId());
                    rep = Relation.getRepresent();
                    for (Word single_rep:rep) {
                        if (single_rep.getDeprel().equals("TMP")) {
                            trustTriples.get(i).getRelation().setContent(single_rep.getContent()+trustTriples.get(i).getRelation().getContent());
                        }
                    }
                }
            }

            for (int i=0;i<len_untrust;i++){
                MEntity = untrustTriples.get(i).getMEntity();
                SEntity = untrustTriples.get(i).getSEntity();
                Relation = untrustTriples.get(i).getRelation();
                rep = MEntity.getRepresent();
                if (SEntity.getDeprel().equals("BEI") && SEntity.getHead() == Relation.getId()){
                    Word temp = MEntity;
                    MEntity = SEntity;
                    SEntity = temp;
                }
                if (!already_modified.contains(MEntity.getId())) {
                    already_modified.add(MEntity.getId());
                    for (Word single_rep:rep) {
                        if (single_rep.getDeprel().equals("ATT")) {
                            if (single_rep.getPostag().equals("a")){

                                untrustTriples.add(new Triple(MEntity, Property, single_rep));
                            }
                            else {
                                untrustTriples.get(i).getMEntity().setContent(single_rep.getContent()+untrustTriples.get(i).getMEntity().getContent());
                            }
                        }
                        else if (single_rep.getDeprel().equals("DE")){
                            if (single_rep.getPostag().equals("a")){
                                untrustTriples.add(new Triple(MEntity, Property, single_rep));
                            }
                            else if (!single_rep.getContent().equals("的")){
                                untrustTriples.get(i).getMEntity().setContent(single_rep.getContent()+untrustTriples.get(i).getMEntity().getContent());
                            }
                        }
                    }
                }

                if (!already_modified.contains(MEntity.getId())) {
                    already_modified.add(MEntity.getId());
                    rep = SEntity.getRepresent();
                    SE_child = SEntity.getWords();
                    for (Word single_child:SE_child){
                        if (single_child.getDeprel().equals("VOB") && SEntity.getPostag().equals("v")){
                            ArrayList<Word> child_rep = single_child.getRepresent();
                            for (Word s_child_rep:child_rep) {
                                single_child.setContent(s_child_rep.getContent()+single_child.getContent());
                            }
                            trustTriples.get(i).getSEntity().setContent(trustTriples.get(i).getSEntity().getContent()+single_child.getContent());
                        }
                    }
                    for (Word single_rep:rep) {
                        if (single_rep.getDeprel().equals("ATT")) {
                            if (single_rep.getPostag().equals("a")){
                                untrustTriples.add(new Triple(SEntity, Property, single_rep));
                            }
                            else {
                                untrustTriples.get(i).getSEntity().setContent(single_rep.getContent()+untrustTriples.get(i).getSEntity().getContent());
                            }
                        }
                        if (single_rep.getDeprel().equals("DE")){
                            if (single_rep.getPostag().equals("a")){
                                untrustTriples.add(new Triple(SEntity, Property, single_rep));
                            }
                            else if (!single_rep.getContent().equals("的")){
                                untrustTriples.get(i).getSEntity().setContent(single_rep.getContent()+untrustTriples.get(i).getSEntity().getContent());
                            }
                        }
                    }
                }

                if (!already_modified.contains(Relation.getId())){
                    already_modified.add(Relation.getId());
                    rep = Relation.getRepresent();
                    for (Word single_rep:rep) {
                        if (single_rep.getDeprel().equals("TMP")) {
                            untrustTriples.get(i).getRelation().setContent(single_rep.getContent()+untrustTriples.get(i).getRelation().getContent());
                        }
                    }
                }
            }

            for (int i=0;i<trustTriples.size();i++){
                for (int j=i+1;j<trustTriples.size();j++){
                    if (trustTriples.get(i).getMEntity().getContent().equals(trustTriples.get(j).getMEntity().getContent())
                            && trustTriples.get(i).getRelation().getContent().equals(trustTriples.get(j).getRelation().getContent())
                            && trustTriples.get(i).getSEntity().getContent().equals(trustTriples.get(j).getSEntity().getContent())){
                        trustTriples.remove(j);
                        j--;
                    }
                }
            }

            for (int i=0;i<untrustTriples.size();i++){
                for (int j=i+1;j<untrustTriples.size();j++){
                    if (untrustTriples.get(i).getMEntity().getContent().equals(untrustTriples.get(j).getMEntity().getContent())
                            && untrustTriples.get(i).getRelation().getContent().equals(untrustTriples.get(j).getRelation().getContent())
                            && untrustTriples.get(i).getSEntity().getContent().equals(untrustTriples.get(j).getSEntity().getContent())){
                        untrustTriples.remove(j);
                        j--;
                    }
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private void filiter(ArrayList<Triple> triples) {
        try{
            ArrayList<Triple> t = new ArrayList<Triple>();
            for(int i=0;i<triples.size();i++){
                boolean isRepeat = false;
                for(int j=i+1;j<triples.size();j++){
                    if(triples.get(i).getMEntity().getId()==triples.get(j).getMEntity().getId() && triples.get(i).getRelation().getId()==triples.get(j).getRelation().getId() && triples.get(i).getSEntity().getId()==triples.get(j).getSEntity().getId()) {
                        isRepeat = true;
                        break;
                    }
                }
                if(!isRepeat){
                    t.add(triples.get(i));
                }
            }

            Float[] confidence = new Float[t.size()];
            for(int i=0;i<t.size();i++){
                Triple temp = t.get(i);
                confidence[i] = (float)1/(float)Math.abs(temp.getMEntity().getId()-temp.getSEntity().getId()) + (float)1/(float)Math.abs(temp.getMEntity().getId()-temp.getRelation().getId()) + (float)1/(float)Math.abs(temp.getSEntity().getId()-temp.getRelation().getId());
            }

            Float[] confidence2 = confidence;
            for(int i=0;i<confidence2.length;i++){
                for(int j=i+1;j<confidence2.length;j++){
                    if(confidence2[j]>confidence2[i]){
                        float temp = confidence2[i];
                        confidence2[i] = confidence2[j];
                        confidence2[j] = confidence2[i];
                    }
                }
            }

            if(confidence.length!=0){
                int position = (int)(confidence.length*0.7)>=confidence.length?confidence.length-1:(int)(confidence.length*0.7);
                float trust = confidence2[position]>=0.8? (float) 0.8 :confidence2[position];

                for(int i=0;i<confidence.length;i++){
                    if(confidence[i]>=trust){
                        trustTriples.add(t.get(i));
                    }else{
                        untrustTriples.add(t.get(i));
                    }
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

    private String compose() {
        try{
            JSONArray options = new JSONArray();
            int id = 0;

            for (int i=0;i<trustTriples.size();i++){
                putInOption(id,
                        trustTriples.get(i).getMEntity().getContent(),
                        trustTriples.get(i).getRelation().getContent(),
                        trustTriples.get(i).getSEntity().getContent(),
                        options,
                        trustTriples.get(i).getMEntity().getPostag(),
                        trustTriples.get(i).getSEntity().getPostag(),
                        "可信");
                id++;
            }

            for (int i=0;i<untrustTriples.size();i++){
                putInOption(id,
                        untrustTriples.get(i).getMEntity().getContent(),
                        untrustTriples.get(i).getRelation().getContent(),
                        untrustTriples.get(i).getSEntity().getContent(),
                        options,
                        untrustTriples.get(i).getMEntity().getPostag(),
                        untrustTriples.get(i).getSEntity().getPostag(),
                        "不可信");
                id++;
            }

            System.out.println(options.toString());
            return options.toString();
        }
        catch (Exception e){
            return "";
        }
    }

    private void putInOption(int id, String MEntity, String Relation, String SEntity, JSONArray option, String Mpost, String SPost, String dependable){
        try{
            JSONObject temp = new JSONObject(true);
            temp.put("id",id);
            id++;
            temp.put("mainBody", MEntity);
            temp.put("relation", Relation);
            temp.put("object", SEntity);
            temp.put("label", dependable);
            temp.put("Mpost", Mpost);
            temp.put("Spost", SPost);
            option.add(temp);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
