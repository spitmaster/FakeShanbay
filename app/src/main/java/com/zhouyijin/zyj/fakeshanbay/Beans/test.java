package com.zhouyijin.zyj.fakeshanbay.Beans;

import java.util.List;

/**
 * Created by zhouyijin on 2016/11/8.
 */

public class test {


    /**
     * msg : SUCCESS
     * status_code : 0
     * data : [{"username":"Shanbay","likes":212,"last":" the invaders.","userid":0,"mid":"drive out","audio_name":"","annotation":"After a prolonged battle, they managed to <vocab>drive<\/vocab> <vocab>out<\/vocab> the invaders.","audio_addresses":{},"version":90,"unlikes":21,"user":{"username":"Shanbay","nickname":"Shanbay","id":1,"avatar":"https://static.baydn.com/avatar/media_store/0ef69ded3383a1cffcdeb71280ddbc7e.png?imageView/1/w/128/h/128/"},"word":"drive out","translation":"经过长期的战争，他们终于赶走了侵略者。","nickname":"Shanbay","id":5745,"first":"After a prolonged battle, they managed to "},{"username":"Shanbay","likes":326,"last":"","userid":0,"mid":"","audio_name":"","annotation":"We <vocab>drove<\/vocab> <vocab>out<\/vocab> to a little place in the country for a picnic.","audio_addresses":{},"version":135,"unlikes":18,"user":{"username":"Shanbay","nickname":"Shanbay","id":1,"avatar":"https://static.baydn.com/avatar/media_store/0ef69ded3383a1cffcdeb71280ddbc7e.png?imageView/1/w/128/h/128/"},"word":"drive out","translation":"我们开车去一个小地方野餐。","nickname":"Shanbay","id":5746,"first":"We drove out to a little place in the country for a picnic."}]
     */

    private String msg;
    private int status_code;
    /**
     * username : Shanbay
     * likes : 212
     * last :  the invaders.
     * userid : 0
     * mid : drive out
     * audio_name :
     * annotation : After a prolonged battle, they managed to <vocab>drive</vocab> <vocab>out</vocab> the invaders.
     * audio_addresses : {}
     * version : 90
     * unlikes : 21
     * user : {"username":"Shanbay","nickname":"Shanbay","id":1,"avatar":"https://static.baydn.com/avatar/media_store/0ef69ded3383a1cffcdeb71280ddbc7e.png?imageView/1/w/128/h/128/"}
     * word : drive out
     * translation : 经过长期的战争，他们终于赶走了侵略者。
     * nickname : Shanbay
     * id : 5745
     * first : After a prolonged battle, they managed to
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
        private int likes;
        private String last;
        private int userid;
        private String mid;
        private String audio_name;
        private String annotation;
        private int version;
        private int unlikes;
        /**
         * username : Shanbay
         * nickname : Shanbay
         * id : 1
         * avatar : https://static.baydn.com/avatar/media_store/0ef69ded3383a1cffcdeb71280ddbc7e.png?imageView/1/w/128/h/128/
         */

        private UserBean user;
        private String word;
        private String translation;
        private String nickname;
        private int id;
        private String first;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public int getLikes() {
            return likes;
        }

        public void setLikes(int likes) {
            this.likes = likes;
        }

        public String getLast() {
            return last;
        }

        public void setLast(String last) {
            this.last = last;
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

        public String getAudio_name() {
            return audio_name;
        }

        public void setAudio_name(String audio_name) {
            this.audio_name = audio_name;
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

        public String getWord() {
            return word;
        }

        public void setWord(String word) {
            this.word = word;
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

        public String getFirst() {
            return first;
        }

        public void setFirst(String first) {
            this.first = first;
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
