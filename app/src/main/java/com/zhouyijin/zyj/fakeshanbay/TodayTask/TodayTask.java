package com.zhouyijin.zyj.fakeshanbay.TodayTask;


import java.util.List;

/**
 * Created by zhouyijin on 2016/10/24.
 * <p>
 * 获取今天的任务
 */

public interface TodayTask {
    //设置今天的任务量,并同时刷新单词
    void setTaskWordsNumber(int number);

    //把今天的单词换成新的一批,只换未完成的单词
    void refreshWords();

    //获取今天背诵失败的单词
    List<String> getTodayFailedWords();

    //获取今天未完成的单词
    List<String> getTodayUnfinishedWords();

    //获取今天已完成的单词
    List<String> getTodayFinishedWords();

    //设置某个单词已完成,单词必须是今天计划中的
    void setFinishedWord(String word);

    //设置某个单词未完成,单词必须是今天计划中的
    void setFailedWord(String word);

    //返回失败的单词个数
    int getTodayFailedWordsNumber();

    //返回未背单词的个数
    int getTodayUnfinishedWordsNumber();

    //返回已背单词的个数
    int getTodayFinishedWordsNumber();

    //获取task的所有单词数
    int getTodayTaskWordsNumber();

    String getFirstUnfinishedOrFailedWord();

    //准备今天的任务,在任务进度有更新的时候会回调onPrepareProgress方法
    void prepareTodayTask(OnPrepareProgress listener);

    //检查今天是不是新的一天
    boolean checkIsNewDay();

    interface OnPrepareProgress {
        void onPrepareProgress(int progress, int max);
    }
}
