package com.mfashiongallery.emag.express.push.model;

import java.io.Serializable;

/**
 * Created by liuchonghui on 16/7/6.
 */
public class Extra implements Serializable {
    private static final long serialVersionUID = 1L;

    private String title;
    private String display;
    private String mpsContent;

    private String type;
    private String content;
    private String c;
    private String url;
    private String newsid;
    private String ch;
    private String time;
    private String handle_by_app;
    private String param;
    private String mark;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    public String getMpsContent() {
        return mpsContent;
    }

    public void setMpsContent(String mpsContent) {
        this.mpsContent = mpsContent;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getC() {
        return c;
    }

    public void setC(String c) {
        this.c = c;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getNewsid() {
        return newsid;
    }

    public void setNewsid(String newsid) {
        this.newsid = newsid;
    }

    public String getCh() {
        return ch;
    }

    public void setCh(String ch) {
        this.ch = ch;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getHandle_by_app() {
        return handle_by_app;
    }

    public void setHandle_by_app(String handle_by_app) {
        this.handle_by_app = handle_by_app;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }
}
