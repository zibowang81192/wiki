package com.wangzb.wiki.resp;

public class UserQueryResp {
    private Long id;

    private String loginName;

    private String Name;

    private String password;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    @Override
    public String toString() {
        return "UserQueryResp{" +
                "id=" + id +
                ", loginName='" + loginName + '\'' +
                ", Name='" + Name + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}