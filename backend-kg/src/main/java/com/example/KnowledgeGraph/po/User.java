package com.example.KnowledgeGraph.po;

public class User {
    private Integer uid;
    private String phone;
    private String name;
    private String password;


    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer id) {
        this.uid = uid;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


}