package com.example.KnowledgeGraph.kgStruct.Steps;

public class Triple {
    private Word MEntity;
    private Word Relation;
    private Word SEntity;

    String str;

    public Triple(Word MEntity, Word Relation, Word SEntity){
        this.MEntity = MEntity;
        this.Relation = Relation;
        this.SEntity = SEntity;

        str = MEntity.getContent()+","+Relation.getContent()+","+SEntity.getContent();
    }

    public String getStr(){
        return this.str;
    }

    public Word getMEntity() {
        return MEntity;
    }

    public Word getRelation() {
        return Relation;
    }

    public Word getSEntity() {
        return SEntity;
    }

    public void setMEntity(Word MEntity) {
        this.MEntity = MEntity;
    }

    public void setRelation(Word relation) {
        Relation = relation;
    }

    public void setSEntity(Word SEntity) {
        this.SEntity = SEntity;
    }
}
