package com.project.harue.projectdd.Model;

public class Contener {

    String imgurl;
    String name_object;
    String price_object;
    String curdate_object;
    String date_object;
    String postid;
    String userid;
    int startPrice;


    public Contener(String imgurl, String name_object, String price_object, String curdate_object, String date_object,String userid,int startPrice) {
        this.imgurl = imgurl;
        this.name_object = name_object;
        this.price_object = price_object;
        this.curdate_object = curdate_object;
        this.date_object = date_object;
        this.userid = userid;
        this.startPrice = startPrice;
    }


    public Contener() {

    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public int getStartPrice() {
        return startPrice;
    }

    public void setStartPrice(int startPrice) {
        this.startPrice = startPrice;
    }

    public String getUserid() { return  userid;}

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public String getName_object() {
        return name_object;
    }

    public void setName_object(String name_object) {
        this.name_object = name_object;
    }

    public String getPrice_object() {
        return price_object;
    }

    public void setPrice_object(String price_object) {
        this.price_object = price_object;
    }

    public String getCurdate_object() {
        return curdate_object;
    }

    public void setCurdate_object(String curdate_object) {
        this.curdate_object = curdate_object;
    }

    public String getDate_object() {
        return date_object;
    }

    public void setDate_object(String date_object) {
        this.date_object = date_object;
    }
    public String getPostid() {
        return postid;
    }

    public void setPostid(String postid) {
        this.postid = postid;
    }

}
