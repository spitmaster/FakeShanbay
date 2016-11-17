package com.zhouyijin.zyj.fakeshanbay.StudyRecorder;


import java.util.List;

/**
 * Created by zhouyijin on 2016/10/24.
 * <p>
 * 这个类用来保存一本书的计划
 * <p>
 * 此类所拥有的方法:
 * 1.获取一定数量的单词
 * 2.保存背过的单词(放入数据库)
 * 3.判断一个单词是否背过
 */

public class StudyRecorderIMP implements StudyRecorder {

    private static StudyRecorderIMP instance;
    private StudyRecordGetter getter;

    private StudyRecorderIMP() {
        getter = StudyRecordGetter.getInstance();
    }

    public static StudyRecorderIMP getStudyRecorder() {
        if (instance == null) {
            synchronized (StudyRecorderIMP.class) {
                if (instance == null) {
                    instance = new StudyRecorderIMP();
                }
            }
        }
        return instance;
    }


    @Override
    public void recordWord(String word) {
        getter.recordWord(word);
    }

    @Override
    public boolean isWordRecorded(String word) {
        return getter.isWordStudied(word);
    }

    @Override
    public void recordWords(List<String> words) {
        getter.recordWords(words);
    }

    @Override
    public void unRecordWord(String word) {

    }

    @Override
    public List<String> getTaskWords(int number) {
        return getter.getUnstudiedWords(number);
    }

    @Override
    public int getScheduleWordNumber() {
        return getter.getScheduleWordsNumber();
    }

    @Override
    public int getMasteredWordNumber() {
        return getter.getMasteredWordsNumber();
    }


    //加入新单词
    @Override
    public void addNewWords(List<String> words) {
        getter.insertWords(words);
    }
}
