package com.zhouyijin.zyj.fakeshanbay.queryword.dictionary;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.zhouyijin.zyj.fakeshanbay.Beans.WordBean;
import com.zhouyijin.zyj.fakeshanbay.MyApplication;

/**
 * Created by zhouyijin on 2016/11/7.
 * <p>
 * * 这个类实现存取单词的信息from database
 * <p>
 * 保存的内容有:  1.单词
 * 2.音标
 * 3.单词的类型(短语还是单词)
 * 4.单词的编号(扇贝网标记的编号)
 * 5.单词的名词性英文解释
 * 6.单词的形容词性英文解释
 * 7.单词的动词的英文解释
 * 8.单词的副词的英文解释
 * 9.单词的连词的英文解释
 * 10.单词的介词的英文解释
 * 11.音频的url地址
 * 12.单词的中文释义
 */

public class LocalDictionaryImp implements LocalDictionary {

    public static final String DB_NAME = "dictionary.db";

    public static final String TABLE_NAME = "local_dictionary";
    public static final String WORD = "word";   //这个应该是唯一的 且非空
    public static final String PRONUNCIATION = "pronunciation";
    public static final String CONTENT_TYPE = "content_type";
    public static final String ID = "ID";       //这个应该是唯一的
    public static final String N_EN = "noun_en";
    public static final String ADJ_EN = "adjective_en";
    public static final String V_EN = "verb_en";
    public static final String ADV_EN = "adverb_en";
    public static final String CONJ_EN = "conjunction_en";
    public static final String PREP_EN = "preposition_en";
    public static final String AUDIO_URL = "audio_url";
    public static final String DEFINITION_CN = "definition_cn";

    private DictionarySQLiteOpenHelper sqLiteOpenHelper;

    @Override
    public void putWord(WordBean bean) {
        if (bean==null){
            return;
        }
        SQLiteDatabase db = sqLiteOpenHelper.getWritableDatabase();
        ContentValues contentValues = packContentValue(bean);
        db.insertWithOnConflict(TABLE_NAME, null, contentValues, SQLiteDatabase.CONFLICT_IGNORE);
        sqLiteOpenHelper.close();
        contentValues.clear();
    }

    private static class InstanceHolder {
        private static LocalDictionaryImp instance = new LocalDictionaryImp();
    }

    public static LocalDictionaryImp getInstance() {
        return InstanceHolder.instance;
    }

    private LocalDictionaryImp() {
        sqLiteOpenHelper = new DictionarySQLiteOpenHelper(MyApplication.getContext(), DB_NAME, null, 1);
    }

    @Override
    public WordBean queryWord(String word) {
        SQLiteDatabase db = sqLiteOpenHelper.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null,
                WORD + " = ?",
                new String[]{word},
                null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            WordBean bean = packWordBean(cursor);
            cursor.close();
            sqLiteOpenHelper.close();
            return bean;
        }
        sqLiteOpenHelper.close();
        return null;
    }


    private WordBean packWordBean(Cursor cursor) {
        WordBean bean = new WordBean();

        String content = cursor.getString(cursor.getColumnIndex(WORD));
        bean.setContent(content);
        String content_type = cursor.getString(cursor.getColumnIndex(CONTENT_TYPE));
        bean.setContent_type(content_type);
        int id = cursor.getInt(cursor.getColumnIndex(ID));
        bean.setId(id);
        String pronunciation = cursor.getString(cursor.getColumnIndex(PRONUNCIATION));
        bean.setPronunciation(pronunciation);
        String audioUrl = cursor.getString(cursor.getColumnIndex(AUDIO_URL));
        bean.setAudioUrl(audioUrl);
        String definition_cn = cursor.getString(cursor.getColumnIndex(DEFINITION_CN));
        bean.setChineseDefinition(definition_cn);
        String adj_cn = cursor.getString(cursor.getColumnIndex(ADJ_EN));
        bean.setEnglishDefinitioinOfAdj(adj_cn);
        String adv_en = cursor.getString(cursor.getColumnIndex(ADV_EN));
        bean.setEnglishDefinitioinOfAdv(adv_en);
        String conj_cn = cursor.getString(cursor.getColumnIndex(CONJ_EN));
        bean.setEnglishDefinitioinOfConj(conj_cn);
        String n_en = cursor.getString(cursor.getColumnIndex(N_EN));
        bean.setEnglishDefinitioinOfN(n_en);
        String prep_en = cursor.getString(cursor.getColumnIndex(PREP_EN));
        bean.setEnglishDefinitioinOfPrep(prep_en);
        String v_en = cursor.getString(cursor.getColumnIndex(V_EN));
        bean.setEnglishDefinitioinOfV(v_en);


        return bean;
    }

    private ContentValues packContentValue(WordBean bean) {
        ContentValues contentValues = new ContentValues();

        String word = bean.getContent();
        contentValues.put(WORD, word);
        String pronunciation = bean.getPronunciation();
        contentValues.put(PRONUNCIATION, pronunciation);
        String content_type = bean.getContent_type();
        contentValues.put(CONTENT_TYPE, content_type);
        int id = bean.getId();
        contentValues.put(ID, id);
        String n_en = bean.getEnglishDefinitionOfN();
        contentValues.put(N_EN, n_en);
        String adj_en = bean.getEnglishDefinitionOfAdj();
        contentValues.put(ADJ_EN, adj_en);
        String v_en = bean.getEnglishDefinitionOfV();
        contentValues.put(V_EN, v_en);
        String adv_en = bean.getEnglishDefinitionOfAdv();
        contentValues.put(ADV_EN, adv_en);
        String conj_en = bean.getEnglishDefinitionOfConj();
        contentValues.put(CONJ_EN, conj_en);
        String prep_en = bean.getEnglishDefinitionOfPrep();
        contentValues.put(PREP_EN, prep_en);
        String audio_url = bean.getAudioUrl();
        contentValues.put(AUDIO_URL, audio_url);
        String definition_cn = bean.getChineseDefinition();
        contentValues.put(DEFINITION_CN, definition_cn);

        return contentValues;
    }

}
