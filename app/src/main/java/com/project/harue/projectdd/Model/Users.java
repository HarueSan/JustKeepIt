package com.project.harue.projectdd.Model;

public class Users {
    String userid ;
    String usersname ;

    public Users(String userid, String usersname) {
        this.userid = userid;
        this.usersname = usersname;
    }
    public Users() {
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUsersname() {
        return usersname;
    }

    public void setUsersname(String usersname) {
        this.usersname = usersname;
    }
}
