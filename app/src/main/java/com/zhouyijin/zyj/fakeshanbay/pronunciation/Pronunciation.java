package com.zhouyijin.zyj.fakeshanbay.pronunciation;

/**
 * Created by zhouyijin on 2016/11/10.
 * 这个接口用来播放单词的声音
 */

public interface Pronunciation {

    //根据输入的单词来播放对应的声音
    void playAudio(String word);
}
