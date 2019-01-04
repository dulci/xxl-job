package com.xxl.job.admin.core.model;

/**
 * Created by zhangjianlong on 2019/1/3.
 */
public class GoJsNodeData {

    private Integer key;
    private String text;

    public Integer getKey() {
        return key;
    }

    public void setKey(Integer key) {
        this.key = key;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "{" +
                "'key':" + key +
                ", 'text':'" + text +
                "'}";
    }
}