package com.example.KnowledgeGraph.kgStruct.Steps;

import java.util.ArrayList;

public class Word {
    private String content;
    private int id;
    private int head;
    private String postag;
    private String deprel;
    private ArrayList<Word> words;
    private ArrayList<Word> represent;

    public ArrayList<Word> getRepresent() {
        return represent;
    }

    public ArrayList<Word> getWords() {
        return words;
    }

    public int getHead() {
        return head;
    }

    public int getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public String getDeprel() {
        return deprel;
    }

    public String getPostag() {
        return postag;
    }

    public void setContent(String content) { this.content = content; }

    public Word(String content, int id, int head, String postag, String deprel){
        this.content = content;
        this.id = id;
        this.head = head;
        this.postag = postag;
        this.deprel = deprel;
        this.words = new ArrayList<>();
        this.represent = new ArrayList<>();
    }

    public boolean isDeprelEquals(String conditionStr){
        String[] conditions = conditionStr.split(",");
        for(int i=0;i<conditions.length;i++){
            if(deprel.equals(conditions[i])){
                return true;
            }
        }
        return false;
    }

    public boolean isPostagNotEquals(String conditionStr){
        String[] conditions = conditionStr.split(",");
        for(int i=0;i<conditions.length;i++){
            if(postag.equals(conditions[i])){
                return false;
            }
        }
        return true;
    }

    public void addWord(Word word){
        this.words.add(word);
    }

    public void addRepresent(Word word){
        this.represent.add(word);
    }

    /*public boolean contain(Word word){
        for(int i=0;i<this.represent.size();i++){
            if(word.id == this.represent.get(i).id){
                return false;
            }
        }
        for(int i=0;i<this.represent.size();i++){
            if(word.head == this.represent.get(i).id){
                return true;
            }
        }
        return false;
    }*/

    public boolean isRepresentChild(Word word){
        for(int i=0;i<this.represent.size();i++){
            if(word.id == this.represent.get(i).id){
                return false;
            }
        }
        for(int i=0;i<this.represent.size();i++){
            if(word.head == this.represent.get(i).id){
                return true;
            }
        }
        return false;
    }

    /*public void addRepr(Word word){
        if(word.head == this.id){
            this.represent.add(word);
        }else {
            for(int i=0;i<this.represent.size();i++){
                if(word.head == this.represent.get(i).id){
                    this.represent.add(word);
                }
            }
        }
    }*/
    public boolean isRepresent(Word word){
        if(word.head == this.id){
            return true;
        }else {
            for(int i=0;i<this.represent.size();i++){
                if(word.head == this.represent.get(i).id){
                    return true;
                }
            }
        }
        return false;
    }

    public void extendWord(String s){
        this.content = this.content + s;
    }

    public void Print(){
        System.out.println("------");
        System.out.println(this.id);
        System.out.println(this.content);
        System.out.println(this.postag);
        System.out.println(this.deprel);
        for(int i=0;i<words.size();i++){
            System.out.println("child: "+words.get(i).content + "deprel-"+words.get(i).deprel);
        }
        for(int i=0;i<represent.size();i++){
            System.out.println("represent: "+represent.get(i).content+ "deprel-"+represent.get(i).deprel);
        }
    }

    public Word copy(){
        Word copy = new Word(this.content, this.id, this.head, this.postag, this.deprel);
        for(int i=0;i<this.words.size();i++){
            copy.addWord(this.words.get(i));
        }
        for(int i=0;i<this.represent.size();i++){
            copy.addWord(this.represent.get(i));
        }
        return copy;
    }
}
