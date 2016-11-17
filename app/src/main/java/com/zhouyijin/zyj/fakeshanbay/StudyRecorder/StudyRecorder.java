package com.zhouyijin.zyj.fakeshanbay.StudyRecorder;

import java.util.List;

/**
 * Created by zhouyijin on 2016/10/24.
 *
 */

public interface StudyRecorder {
    //把背过的单词保存到数据库中
    void recordWord(String word);
    void recordWords(List<String> words);
    //把指定单词标记为未背过
    void unRecordWord(String word);
    //检查是否背过这个单词
    boolean isWordRecorded(String word);
    //获取一定量的单词用来作为今日任务
    List<String>getTaskWords(int number);
    //获取计划单词的总数
    int getScheduleWordNumber();
    //获取已经掌握的单词总数
    int getMasteredWordNumber();


    //加入新单词
    void addNewWords(List<String> words);

}
