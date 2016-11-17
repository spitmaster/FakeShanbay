package com.zhouyijin.zyj.fakeshanbay.Beans;

import java.util.ArrayList;
import java.util.List;

public class WordBean {

    public static final String QUERY_SUCCESS = "SUCCESS";
    /**
     * msg : SUCCESS
     * status_code : 0
     * data : {"pronunciations":{"uk":"'sɪŋɡl","us":"'sɪŋɡl"},"en_definitions":{"n":["a base hit on which the batter stops safely at first base","the smallest whole number or a numeral representing this number"],"adj":["being or characteristic of a single thing or person","used of flowers having usually only one row or whorl of petals","existing alone or consisting of one entity or part or aspect or individual","characteristic of or meant for a single person or thing","having uniform application","not divided among or brought to bear on more than one object or objective"],"v":["hit a single"]},"content_id":5514,"audio_addresses":{"uk":["https://words-audio.baydn.com/uk%2Fs%2Fsi%2Fsingle.mp3","http://words-audio.cdn.shanbay.com/uk/s/si/single.mp3"],"us":["https://words-audio.baydn.com/us%2Fs%2Fsi%2Fsingle.mp3","http://words-audio.cdn.shanbay.com/us/s/si/single.mp3"]},"en_definition":{"pos":"n","defn":"a base hit on which the batter stops safely at first base; the smallest whole number or a numeral representing this number"},"uk_audio":"http://media.shanbay.com/audio/uk/single.mp3","has_audio":true,"conent_id":5514,"pronunciation":"'sɪŋɡl","audio_name":"single","content":"single","pron":"'sɪŋɡl","num_sense":1,"object_id":5514,"content_type":"vocabulary","definition":" adj. 单身的,单纯的,单一的 n. 单程票,单曲,单人房,单身者 vt. 单独挑出 vi. (棒球)作一垒打","sense_id":0,"audio":"http://media.shanbay.com/audio/us/single.mp3","id":5514,"cn_definition":{"pos":"","defn":"adj. 单身的,单纯的,单一的 n. 单程票,单曲,单人房,单身者 vt. 单独挑出 vi. (棒球)作一垒打"},"us_audio":"http://media.shanbay.com/audio/us/single.mp3"}
     */

    private String msg;
    private int status_code;

    //这是个例子
    /**
     * pronunciations : {"uk":"'sɪŋɡl","us":"'sɪŋɡl"}
     * en_definitions : {"n":["a base hit on which the batter stops safely at first base","the smallest whole number or a numeral representing this number"],"adj":["being or characteristic of a single thing or person","used of flowers having usually only one row or whorl of petals","existing alone or consisting of one entity or part or aspect or individual","characteristic of or meant for a single person or thing","having uniform application","not divided among or brought to bear on more than one object or objective"],"v":["hit a single"]}
     * content_id : 5514
     * audio_addresses : {"uk":["https://words-audio.baydn.com/uk%2Fs%2Fsi%2Fsingle.mp3","http://words-audio.cdn.shanbay.com/uk/s/si/single.mp3"],"us":["https://words-audio.baydn.com/us%2Fs%2Fsi%2Fsingle.mp3","http://words-audio.cdn.shanbay.com/us/s/si/single.mp3"]}
     * en_definition : {"pos":"n","defn":"a base hit on which the batter stops safely at first base; the smallest whole number or a numeral representing this number"}
     * uk_audio : http://media.shanbay.com/audio/uk/single.mp3
     * has_audio : true
     * conent_id : 5514
     * pronunciation : 'sɪŋɡl
     * audio_name : single
     * content : single
     * pron : 'sɪŋɡl
     * num_sense : 1
     * object_id : 5514
     * content_type : vocabulary
     * definition :  adj. 单身的,单纯的,单一的 n. 单程票,单曲,单人房,单身者 vt. 单独挑出 vi. (棒球)作一垒打
     * sense_id : 0
     * audio : http://media.shanbay.com/audio/us/single.mp3
     * id : 5514
     * cn_definition : {"pos":"","defn":"adj. 单身的,单纯的,单一的 n. 单程票,单曲,单人房,单身者 vt. 单独挑出 vi. (棒球)作一垒打"}
     * us_audio : http://media.shanbay.com/audio/us/single.mp3
     */


    private DataBean data;


    public WordBean() {
        data = new DataBean();
    }


    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setStatus_code(int status_code) {
        this.status_code = status_code;
    }


    public void setData(DataBean data) {
        this.data = data;
    }


    /**
     * ----------------------------------------------------------------------------------------------------------------------------------------
     * 以下全是get和set方法
     */


    public DataBean getData() {
        return data;
    }


    public int getStatus_code() {
        return status_code;
    }

    //获取json数据成功则返回true
    public String getMsg() {
        return msg;
    }

    //如果有音频则返回true
    public boolean isHas_audio() {
        return getData().isHas_audio();
    }

    //获取单词的音标
    public String getPronunciation() {
        return getData().getPronunciation();
    }

    public void setPronunciation(String pronunciation) {
        getData().setPronunciation(pronunciation);
    }


    /**
     * 获取查询的是哪个单词
     *
     * @return the specific word in English
     */
    public String getContent() {
        return getData().getContent();
    }

    public void setContent(String word) {
        getData().setContent(word);
    }

    //返回查询的是那种类型的,是单词(vocabulary)还是短语(phrase?)
    public String getContent_type() {
        return getData().getContent_type();
    }

    public void setContent_type(String type) {
        getData().setContent_type(type);
    }

    //获取音频的url地址
    public String getAudioUrl() {
        if(getData()==null){
            return null;
        }
        return getData().getAudio();
    }

    public void setAudioUrl(String url) {
        getData().setAudio(url);
    }

    //获取单词的编号,由扇贝网确定
    public int getId() {
        return getData().getId();
    }

    public void setId(int id) {
        getData().setId(id);
    }

    //获取单词的名词性的英文解释
    public String getEnglishDefinitionOfN() {
        if (getData().getEn_definitions().getN() != null) {
            return getData().getEn_definitions().getN().get(0);
        }
        return null;
    }

    public void setEnglishDefinitioinOfN(String definition) {
        getData().getEn_definitions().putN(definition);
    }


    //获取单词的形容词性的英文解释
    public String getEnglishDefinitionOfAdj() {
        if (getData().getEn_definitions().getAdj() != null) {
            return getData().getEn_definitions().getAdj().get(0);
        }
        return null;
    }

    public void setEnglishDefinitioinOfAdj(String definition) {
        getData().getEn_definitions().putAdj(definition);
    }

    //获取单词的动词的英文解释
    public String getEnglishDefinitionOfV() {
        if (getData().getEn_definitions().getV() != null) {
            return getData().getEn_definitions().getV().get(0);
        }
        return null;
    }

    public void setEnglishDefinitioinOfV(String definition) {
        getData().getEn_definitions().putV(definition);
    }

    //获取单词的副词的英文解释
    public String getEnglishDefinitionOfAdv() {
        if (getData().getEn_definitions().getAdv() != null) {
            return getData().getEn_definitions().getAdv().get(0);
        }
        return null;
    }

    public void setEnglishDefinitioinOfAdv(String definition) {
        getData().getEn_definitions().putAdv(definition);
    }

    //获取单词的连词的英文解释
    public String getEnglishDefinitionOfConj() {
        if (getData().getEn_definitions().getConj() != null) {
            getData().getEn_definitions().getConj().get(0);
        }
        return null;
    }

    public void setEnglishDefinitioinOfConj(String definition) {
        getData().getEn_definitions().putConj(definition);
    }

    //获取单词的介词的英文解释
    public String getEnglishDefinitionOfPrep() {
        if (getData().getEn_definitions().getPrep() != null) {
            return getData().getEn_definitions().getPrep().get(0);
        }
        return null;
    }

    public void setEnglishDefinitioinOfPrep(String definition) {
        getData().getEn_definitions().putPrep(definition);
    }

    public String getChineseDefinition() {
        return getData().getDefinition();
    }

    public void setChineseDefinition(String definition) {
        getData().setDefinition(definition);
    }

    public static class DataBean {
        private EnDefinitionsBean en_definitions;
        private boolean has_audio;
        private String pronunciation;
        private String content;
        private String content_type;
        private String audio;
        private int id;

        //这个是中文的释义
        private String definition;

        public DataBean() {
            en_definitions = new EnDefinitionsBean();
        }


        public String getDefinition() {
            return definition;
        }

        public void setDefinition(String definition) {
            this.definition = definition;
        }

        public EnDefinitionsBean getEn_definitions() {
            return en_definitions;
        }

        public void setEn_definitions(EnDefinitionsBean en_definitions) {
            this.en_definitions = en_definitions;
        }

        public boolean isHas_audio() {
            return has_audio;
        }

        public void setHas_audio(boolean has_audio) {
            this.has_audio = has_audio;
        }


        public String getPronunciation() {
            return pronunciation;
        }

        public void setPronunciation(String pronunciation) {
            this.pronunciation = pronunciation;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getContent_type() {
            return content_type;
        }

        public void setContent_type(String content_type) {
            this.content_type = content_type;
        }

        public String getAudio() {
            return audio;
        }

        public void setAudio(String audio) {
            this.audio = audio;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public static class EnDefinitionsBean {
            private List<String> n;
            private List<String> adj;
            private List<String> v;
            private List<String> adv;
            private List<String> prep;
            private List<String> conj;


            public List<String> getN() {
                return n;
            }

            public void setN(List<String> n) {
                this.n = n;
            }

            public void putN(String noun) {
                if (n == null) {
                    n = new ArrayList<>();
                }
                n.add(noun);
            }

            public List<String> getAdj() {
                return adj;
            }

            public void setAdj(List<String> adj) {
                this.adj = adj;
            }

            public void putAdj(String adjective) {
                if (adj == null) {
                    adj = new ArrayList<>();
                }
                adj.add(adjective);
            }

            public List<String> getV() {
                return v;
            }

            public void setV(List<String> v) {
                this.v = v;
            }

            public void putV(String verb) {
                if (v == null) {
                    v = new ArrayList<>();
                }
                v.add(verb);
            }


            public List<String> getAdv() {
                return adv;
            }

            public void setAdv(List<String> adv) {
                this.adv = adv;
            }

            public void putAdv(String adverb) {
                if (adv == null) {
                    adv = new ArrayList<>();
                }
                adv.add(adverb);
            }

            public List<String> getConj() {
                return conj;
            }

            public void setConj(List<String> conj) {
                this.conj = conj;
            }

            public void putConj(String conjunction) {
                if (conj == null) {
                    conj = new ArrayList<>();
                }
                conj.add(conjunction);
            }

            public List<String> getPrep() {
                return prep;
            }

            public void setPrep(List<String> prep) {
                this.prep = prep;
            }

            public void putPrep(String preposition) {
                if (prep == null) {
                    prep = new ArrayList<>();
                }
                prep.add(preposition);
            }

        }
    }


    //判断是否查询成功的方法
    public boolean isQuerySuccessful() {
        return getMsg().equals(QUERY_SUCCESS);
    }


    /*
        通过一个wordBean获取一个String放置在显示单词的英文释义的位置
     */
    public String getWordAllExplanationEn() {
        List<String> Strings = new ArrayList<>(8);
        Strings.add(getEnglishDefinitionOfAdj());
        Strings.add(getEnglishDefinitionOfAdv());
        Strings.add(getEnglishDefinitionOfN());
        Strings.add(getEnglishDefinitionOfConj());
        Strings.add(getEnglishDefinitionOfPrep());
        Strings.add(getEnglishDefinitionOfV());
        String result = "";
        for (String w : Strings) {
            result = appendString(result, w);
        }
        return result;
    }

    private String appendString(String originalString, String appendString) {
        if (appendString == null || appendString.equals("")) {
            return originalString;
        }
        return originalString + "\n" + appendString;
    }


    @Override
    public String toString() {
        String s = "WordBean{" +
                "data=" + data +
                ", msg='" + msg + '\'' +
                ", status_code=" + status_code +
                ", content= " + getContent() +
                ", audioUrl= " + getAudioUrl();

        if (getEnglishDefinitionOfAdj() != null) {
            s = s + ",EnglishDefinitionOfAdj= " + getEnglishDefinitionOfAdj();
        }
        if (getEnglishDefinitionOfAdv() != null) {
            s = s + ",EnglishDefinitionOfAdv= " + getEnglishDefinitionOfAdv();
        }
        if (getEnglishDefinitionOfConj() != null) {
            s = s + ",EnglishDefinitionOfConj= " + getEnglishDefinitionOfConj();
        }
        if (getEnglishDefinitionOfN() != null) {
            s = s + ",EnglishDefinitionOfN= " + getEnglishDefinitionOfN();
        }
        if (getEnglishDefinitionOfPrep() != null) {
            s = s + ",EnglishDefinitionOfPrep= " + getEnglishDefinitionOfPrep();
        }
        if (getEnglishDefinitionOfV() != null) {
            s = s + ",EnglishDefinitionOfV= " + getEnglishDefinitionOfV();
        }

        return s;


    }
}
