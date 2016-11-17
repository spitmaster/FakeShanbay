

package com.zhouyijin.zyj.fakeshanbay.activity.fragment_main_home.home_sub_fragment.progress_fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.zhouyijin.zyj.atable.ATable;
import com.zhouyijin.zyj.fakeshanbay.R;


/**
 * Created by Administrator on 2016/7/22.
 */
public class HomeSubFragmentProgress extends Fragment implements SeekBar.OnSeekBarChangeListener {

    private ATable atable;
    private SeekBar t1, t2, t3, t4;

    private TextView tv1, tv2, tv3, tv4;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_sub_fragment_progress, container, false);

        initView(view);
        return view;
    }

    private void initView(View view) {
        atable = (ATable) view.findViewById(R.id.at_table_test);
        atable.setData(new int[]{10, 20, 30, 40});
        atable.setXaxisNames(new String[]{"单词总数", "掌握单词", "正在学习", "新词"});
        t1 = (SeekBar) view.findViewById(R.id.sb_t1);
        t2 = (SeekBar) view.findViewById(R.id.sb_t2);
        t3 = (SeekBar) view.findViewById(R.id.sb_t3);
        t4 = (SeekBar) view.findViewById(R.id.sb_t4);
        t1.setOnSeekBarChangeListener(this);
        t2.setOnSeekBarChangeListener(this);
        t3.setOnSeekBarChangeListener(this);
        t4.setOnSeekBarChangeListener(this);
        tv1 = (TextView) view.findViewById(R.id.tv_home_progress_total_words);
        tv2 = (TextView) view.findViewById(R.id.tv_home_progress_mastered_words);
        tv3 = (TextView) view.findViewById(R.id.tv_home_progress_proceeding_words);
        tv4 = (TextView) view.findViewById(R.id.tv_home_progress_new_words);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        int[] a = atable.getData();
        switch (seekBar.getId()) {
            case R.id.sb_t1:
                a[0] = progress;
                tv1.setText(progress + "");
                break;
            case R.id.sb_t2:
                a[1] = progress;
                tv2.setText(progress + "");
                break;
            case R.id.sb_t3:
                a[2] = progress;
                tv3.setText(progress + "");
                break;
            case R.id.sb_t4:
                a[3] = progress;
                tv4.setText(progress + "");
                break;
        }
        atable.setData(a);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
