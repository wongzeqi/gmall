package com.wjq.gmall.bean;

import java.io.Serializable;

public class PmsSearchParam implements Serializable{

    private String catalog3Id;

    private String keyword;



    //这里是 使用list 来绑定参数 valueid 的  所以 在前端传递参数的时候必须这样传递  skuAttrValueList.valueId = ?
    //改掉
    private String[] valueId;

    public String getCatalog3Id() {
        return catalog3Id;
    }

    public void setCatalog3Id(String catalog3Id) {
        this.catalog3Id = catalog3Id;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String[] getValueId() {
        return valueId;
    }

    public void setValueId(String[] valueId) {
        this.valueId = valueId;
    }
}
