package com.zhouyijin.zyj.fakeshanbay.Beans;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.TextAppearanceSpan;

import com.zhouyijin.zyj.fakeshanbay.MyApplication;
import com.zhouyijin.zyj.fakeshanbay.R;

import org.xmlpull.v1.XmlPullParser;

import java.util.List;

/**
 * Created by zhouyijin on 2016/11/9.
 */

public class ExampleSentenceBean {

/*
        API：获取单词的例句
URL： https://api.shanbay.com/bdc/example/?vocabulary_id={id}&type={type}

请求方式: GET

参数： {id} ，必须，单词id ; {type} 可选， 如果指定type＝sys，则返回系统例句

返回示例：
{
    "msg": "SUCCESS",
    "status_code": 0,
    "data": [
        {
            id: 443808,
            "user": {
                "username": "username",
                "nickname": "nickanme",
                "id": 72196,
                "avatar": "http://qstatic.shanbay.com/team/media_store/d99aa28621d300c251c67d4c2ede08e8_1.jpg"
            },
            "unlikes": 0,
            "likes": 3,
            "translation": "跟大家打个招呼",
            "annotation": "say <vocab>hello</vocab> to everybody",
            "version": 0,
        },
         {
            id: 443808,
            "user": {
                "username": "username",
                "nickname": "nickanme",
                "id": 72198,
                "avatar": "http://qstatic.shanbay.com/team/media_store/d99aa28621d300c251c67d4c2ede08e8_1.jpg"
            },
            "unlikes": 0,
            "likes": 3,
            "translation": "跟大家打个招呼",
            "annotation": "say <vocab>hello</vocab> to everybody",
            "version": 1
        }
    }
}
    返回字段说明:

    id           int           例句的id
    user         object        用户的json对象
    likes        int           喜欢该例句的用户数量
    unlikes      int           不喜欢该例句的用户数量
    translation  int           例句的翻译
    annotation   string        例句原文，vocab括起来的内容是匹配的单词，用来高亮
 */

    public static final String SYSTEN_USER_NAME = "Shanbay";

    private String msg;
    private int status_code;
    /**
     * username : leiyuxuan
     * audio_addresses : {}
     * audio_name :
     * unlikes : 0
     * user : {"username":"leiyuxuan","nickname":"leiyuxuan","id":7567,"avatar":"https://static.baydn.com/static/img/icon_head.png"}
     * last : .
     * translation : 我是单身。
     * nickname : leiyuxuan
     * id : 63964
     * word : single
     * userid : 7567
     * mid : single
     * annotation : I'm a <vocab>single</vocab>.
     * version : 9
     * first : I'm a
     * likes : 40
     */

    private List<DataBean> data;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getStatus_code() {
        return status_code;
    }

    public void setStatus_code(int status_code) {
        this.status_code = status_code;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        private String username;
        private String audio_name;
        private int unlikes;
        /**
         * username : leiyuxuan
         * nickname : leiyuxuan
         * id : 7567
         * avatar : https://static.baydn.com/static/img/icon_head.png
         */

        private UserBean user;
        private String last;
        private String translation;
        private String nickname;
        private int id;
        private String word;
        private int userid;
        private String mid;
        private String annotation;
        private int version;
        private String first;
        private int likes;

        //调用ridTag()方法后keywordInSentence才会被赋值
        private String keywordInSentence;

        /*
            这个方法返回一个去掉tag的String
            如:<vocab>single</vocab>
                会把"<vocab>"和"</vocab>"去掉而返回,同时把标签中间的单词复制给keywordInSentence
         */
        public String ridTag() {
            if (annotation == null && annotation.equals("")) return null;

            int indexBegin = annotation.indexOf("<vocab>") + "<vocab>".length(); //标签的开头就是这个
            int indexEnd = annotation.indexOf("</vocab>");
            keywordInSentence = annotation.substring(indexBegin, indexEnd);

            return annotation.replace("<vocab>", "").replace("</vocab>", "");
        }

        public SpannableString getColoredEnglishSentence() {
            if (annotation == null && annotation.equals("")) return null;

            int indexBegin = annotation.indexOf("<vocab>") + "<vocab>".length(); //标签的开头就是这个
            int indexEnd = annotation.indexOf("</vocab>");
            keywordInSentence = annotation.substring(indexBegin, indexEnd);

            String sentence = annotation.replace("<vocab>", "").replace("</vocab>", "");

            int wordBegin = sentence.indexOf(keywordInSentence);
            int wordEnd = wordBegin + keywordInSentence.length();
            SpannableString ss = new SpannableString(sentence);
            ss.setSpan(new TextAppearanceSpan(MyApplication.getContext(), R.style.KeyWord), wordBegin, wordEnd, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            return ss;
        }


        public String getKeywordInSentence() {
            return keywordInSentence;
        }


        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getAudio_name() {
            return audio_name;
        }

        public void setAudio_name(String audio_name) {
            this.audio_name = audio_name;
        }

        public int getUnlikes() {
            return unlikes;
        }

        public void setUnlikes(int unlikes) {
            this.unlikes = unlikes;
        }

        public UserBean getUser() {
            return user;
        }

        public void setUser(UserBean user) {
            this.user = user;
        }

        public String getLast() {
            return last;
        }

        public void setLast(String last) {
            this.last = last;
        }

        public String getTranslation() {
            return translation;
        }

        public void setTranslation(String translation) {
            this.translation = translation;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getWord() {
            return word;
        }

        public void setWord(String word) {
            this.word = word;
        }

        public int getUserid() {
            return userid;
        }

        public void setUserid(int userid) {
            this.userid = userid;
        }

        public String getMid() {
            return mid;
        }

        public void setMid(String mid) {
            this.mid = mid;
        }

        public String getAnnotation() {
            return annotation;
        }

        public void setAnnotation(String annotation) {
            this.annotation = annotation;
        }

        public int getVersion() {
            return version;
        }

        public void setVersion(int version) {
            this.version = version;
        }

        public String getFirst() {
            return first;
        }

        public void setFirst(String first) {
            this.first = first;
        }

        public int getLikes() {
            return likes;
        }

        public void setLikes(int likes) {
            this.likes = likes;
        }

        public static class UserBean {
            private String username;
            private String nickname;
            private int id;
            private String avatar;

            public String getUsername() {
                return username;
            }

            public void setUsername(String username) {
                this.username = username;
            }

            public String getNickname() {
                return nickname;
            }

            public void setNickname(String nickname) {
                this.nickname = nickname;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getAvatar() {
                return avatar;
            }

            public void setAvatar(String avatar) {
                this.avatar = avatar;
            }
        }
    }
}
