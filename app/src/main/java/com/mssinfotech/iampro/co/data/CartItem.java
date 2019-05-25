package com.mssinfotech.iampro.co.data;

import java.io.Serializable;

public class CartItem implements Serializable {
    public Integer id;
    public String uid;
    public String pid;
    public String qty;
    public String unit_rate;
    public String total_rate;
    public String p_type;
    public String udate;
    public String status;
    public String p_cat;
    public String p_image;
    public String p_nane;
    public String selling_cost;
    public CartItem(){
    }
    public CartItem(int id, String uid, String pid,String qty, String unit_rate, String total_rate, String p_type, String udate,String status,String p_cat,String p_image,String p_nane,String selling_cost) {
        super();
        this.id = id;
        this.uid = uid;
        this.pid= pid;
        this.qty = qty;
        this.unit_rate = unit_rate;
        this.total_rate = total_rate;
        this.p_type = p_type;
        this.udate = udate;
        this.status = status;
        this.p_cat = p_cat;
        this.p_image = p_image;
        this.p_nane = p_nane;
        this.selling_cost = selling_cost;
    }

    public void setid(Integer id){
        this.id=id;
    }
    public Integer getid(){
        return id;
    }
    public void setuid(String uid){
        this.uid=uid;
    }
    public String getuid(){
        return uid;
    }
    public void setpid(String pid){
        this.pid=pid;
    }
    public String getpid(){
        return pid;
    }
    public void setqty(String qty){
        this.qty=qty;
    }
    public String getqty(){
        return qty;
    }
    public void setunit_rate(String unit_rate){
        this.unit_rate=unit_rate;
    }
    public String getunit_rate(){
        return unit_rate;
    }
    public void settotal_rate(String total_rate){
        this.total_rate=total_rate;
    }
    public String gettotal_rate(){
        return total_rate;
    }
    public void setp_type(String p_type){
        this.p_type=p_type;
    }
    public String getp_type(){
        return p_type;
    }
    public void setudate(String udate){
        this.udate=udate;
    }
    public String getudate(){
        return udate;
    }
    public void setstatus(String status){
        this.status=status;
    }
    public String getstatus(){
        return status;
    }
    public void setp_cat(String p_cat){
        this.p_cat=p_cat;
    }
    public String getp_cat(){
        return p_cat;
    }
    public void setp_image(String p_image){
        this.p_image=p_image;
    }
    public String getp_image(){
        return p_image;
    }
    public void setp_nane(String p_nane){
        this.p_nane=p_nane;
    }
    public String getp_nane(){
        return p_nane;
    }
    public void setselling_cost(String selling_cost){
        this.selling_cost=selling_cost;
    }
    public String getselling_cost(){
        return selling_cost;
    }
}

