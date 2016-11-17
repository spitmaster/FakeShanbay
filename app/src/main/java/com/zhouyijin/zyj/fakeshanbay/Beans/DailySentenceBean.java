package com.zhouyijin.zyj.fakeshanbay.Beans;

/**
 * Created by zhouyijin on 2016/10/23.
 */

public class DailySentenceBean {

    public static final String DAILY_SENTENCE_API = "http://open.iciba.com/dsapi/";


    /**
     * sid : 2391
     * tts : http://news.iciba.com/admin/tts/2016-10-23-day.mp3
     * content : And glory like the phoenix midst her fires,.Exhales her odours, blazes, and expires.
     * note : "荣耀之光如凤凰浴火重生， 呼吸，绽放光芒，渐渐逝去。 "
     * love : 3718
     * translation : 词霸小编：选自拜伦诗集，投稿来自神之子牛顿。这个投稿的小伙子呀！可以看得出来有很高的文学素养。拜伦是英国19世纪的浪漫主义诗人，100多年前（准确的说是1824年去世）的一位投身革命的诗人。他最重要的一组诗是《唐璜》，笔调有庄有谐，有叙有议，轻松又讽刺。其实小编觉得某种程度上有点像中国的李白。大家有兴趣的可以去看一看~

     * picture : http://cdn.iciba.com/news/word/20161023.jpg
     * picture2 : http://cdn.iciba.com/news/word/big_20161023b.jpg
     * caption : 词霸每日一句
     * dateline : 2016-10-23
     * s_pv : 0
     * sp_pv : 0
     * tags : [{"id":null,"name":null}]
     * fenxiang_img : http://cdn.iciba.com/web/news/longweibo/imag/2016-10-23.jpg
     */

    //金山词霸编的序号
    private int sid;
    //句子的音频url
    private String tts;
    //句子的英文
    private String content;
    //句子的中文
    private String note;

    //词霸小编给的评价
    private String translation;
    //句子的配图(小)
    private String picture;
    //句子的配图(大)
    private String picture2;
    //每日一句的日期
    private String dateline;
    //分享图片的地址
    private String fenxiang_img;

    public int getSid() {
        return sid;
    }

    public void setSid(int sid) {
        this.sid = sid;
    }

    public String getTts() {
        return tts;
    }

    public void setTts(String tts) {
        this.tts = tts;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getTranslation() {
        return translation;
    }

    public void setTranslation(String translation) {
        this.translation = translation;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getPicture2() {
        return picture2;
    }

    public void setPicture2(String picture2) {
        this.picture2 = picture2;
    }

    public String getDateline() {
        return dateline;
    }

    public void setDateline(String dateline) {
        this.dateline = dateline;
    }

    public String getFenxiang_img() {
        return fenxiang_img;
    }

    public void setFenxiang_img(String fenxiang_img) {
        this.fenxiang_img = fenxiang_img;
    }
}
