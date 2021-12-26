package com.como.service.receiveapprequests;

public class Item {
    private String itemName;
    private String itemId;
    private int itemPointCost;

    public String getItemName(){
        return itemName;
    }

    public String getItemId(){
        return itemId;
    }
    
    public int getItemPointCost(){
        return itemPointCost;
    }

    public void setItemName(String itemName){
        this.itemName = itemName;
    }

    public void setItemId(String itemId){
        this.itemId = itemId;
    }

    public void setItemPointCost(int itemPointCost){
        this.itemPointCost = itemPointCost;
    }    
}
