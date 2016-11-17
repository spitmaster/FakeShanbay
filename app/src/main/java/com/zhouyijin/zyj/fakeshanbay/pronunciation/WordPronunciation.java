package com.zhouyijin.zyj.fakeshanbay.pronunciation;

import android.media.SoundPool;

import com.zhouyijin.zyj.fakeshanbay.Beans.WordBean;
import com.zhouyijin.zyj.fakeshanbay.MyApplication;
import com.zhouyijin.zyj.fakeshanbay.network.NetworkConnection;
import com.zhouyijin.zyj.fakeshanbay.queryword.queryword.IQueryWord;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by zhouyijin on 2016/11/10.
 * Pronunciation的实现类,实现了播放单词声音的功能
 */

public class WordPronunciation implements Pronunciation, SoundPool.OnLoadCompleteListener {
    public static final String TAG = "WordPronunciation";

    private SoundPool soundPool;
    //这个file是所有音频文件的保存路径;
    private File audioPath = MyApplication.getContext().getFilesDir();


    private WordPronunciation() {
        soundPool = new SoundPool.Builder().setMaxStreams(1).build();
        soundPool.setOnLoadCompleteListener(this);
    }

    @Override
    public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
        soundPool.play(sampleId, 1, 1, 1, 0, 1);
    }


    private static class InstanceHolder {
        private static WordPronunciation instance = new WordPronunciation();
    }

    public static WordPronunciation getInstance() {
        return InstanceHolder.instance;
    }


    /**
     * 播放对应单词的声音 , 只能同时播放一个文件
     * 首先从本地文件获取,如果没有本地文件,则从网络下载!
     *
     * @param word 需要播放的单词
     */

    @Override
    public void playAudio(String word) {
        //如果没有加载,则要先看是否有这个单词的文件
        File wordFile = getFile(word);
        if (wordFile.exists()) {
            soundPool.load(wordFile.toString(), 1);
            return;
        }
        playAudioOnLine(word);
    }

    public void playAudioOnLine(final String word) {
        IQueryWord queryWord = MyApplication.getQueryWord();
        queryWord.queryWord(word, new IQueryWord.OnQuerySucceedListener() {
            @Override
            public void onQuerySucceed(WordBean wordBean) { //得到单词的url
                if (wordBean == null) {
                    return;
                }
                String downloadUrl = wordBean.getAudioUrl();
                if (downloadUrl == null && downloadUrl.equals("")) {
                    return;
                }
                //异步加载网络上的音频文件
                NetworkConnection.getInstance().getByteArray(downloadUrl, new NetworkConnection.OnByteArrayResponded() {
                    @Override
                    public void onByteArrayResponded(byte[] result) {
                        File file = getFile(word);
                        writeByteArray2File(file, result); //把网上下载下来的内容保存到文件中
                        soundPool.load(file.toString(), 1);
                    }
                });
            }
        });
    }


    public File getFile(String word) {
        return new File(audioPath, word + ".mp3");
    }

    void writeByteArray2File(File file, byte[] byteArray) {
        if (file.exists()) {
            file.delete();
        }
        try {
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            bos.write(byteArray);
            bos.flush();
            bos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
