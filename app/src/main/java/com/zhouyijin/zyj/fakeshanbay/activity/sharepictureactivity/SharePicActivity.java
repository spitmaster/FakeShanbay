package com.zhouyijin.zyj.fakeshanbay.activity.sharepictureactivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;
import com.zhouyijin.zyj.fakeshanbay.BaseActivity;
import com.zhouyijin.zyj.fakeshanbay.Beans.DailySentenceBean;
import com.zhouyijin.zyj.fakeshanbay.MyApplication;
import com.zhouyijin.zyj.fakeshanbay.R;
import com.zhouyijin.zyj.fakeshanbay.dailysentence.DailySentence;
import com.zhouyijin.zyj.fakeshanbay.network.NetworkConnection;
import com.zhouyijin.zyj.fakeshanbay.tools.DateLine;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by zhouyijin on 2016/11/13.
 */

public class SharePicActivity extends BaseActivity implements View.OnClickListener {

    private File picFile = new File(MyApplication.getContext().getFilesDir(), DateLine.getTodayDateLine() + ".jpg");

    ImageView iv_shared_picture;

    ImageButton ib_back, ib_share_picture;

    SwipeLayout sl_share_pic;

    TextView tv_draw_view;

    FrameLayout fl_draw_container;


    Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {

        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_picture);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        initView();
        getSharedPic();
        getSentence();
    }

    private void getSentence() {

        DailySentence.getInstance().getDailySentence(new DailySentence.OnDailySentenceUpdate() {
            @Override
            public void onDailySentenceUpdate(DailySentenceBean bean) {
                final String sentence = bean.getDateline() + "\n" + bean.getContent() + "\n" + bean.getNote();
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        tv_draw_view.setText(sentence);
                    }
                });
            }
        });


    }

    private void initView() {
        iv_shared_picture = (ImageView) findViewById(R.id.iv_shared_picture);
        iv_shared_picture.setOnClickListener(this);
        ib_back = (ImageButton) findViewById(R.id.ib_back);
        ib_back.setOnClickListener(this);
        ib_share_picture = (ImageButton) findViewById(R.id.ib_share_picture);
        ib_share_picture.setOnClickListener(this);
        fl_draw_container = (FrameLayout) findViewById(R.id.fl_draw_container);
        fl_draw_container.setOnClickListener(this);
        sl_share_pic = (SwipeLayout) findViewById(R.id.sl_share_pic);
        sl_share_pic.setOnClickListener(this);
        tv_draw_view = (TextView) findViewById(R.id.tv_draw_view);
        sl_share_pic.setDrag(SwipeLayout.DragEdge.Bottom, fl_draw_container);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_shared_picture:
                sl_share_pic.toggle();
                break;
            case R.id.ib_back:
                finish();
                break;
            case R.id.ib_share_picture:

                break;
            case R.id.fl_draw_container:
                sl_share_pic.toggle();
                break;
            case R.id.sl_share_pic:
                sl_share_pic.toggle();
                break;
        }

    }


    private void getSharedPic() {
        if (picFile.exists()) {
            Bitmap bm = BitmapFactory.decodeFile(picFile.toString());
            iv_shared_picture.setImageBitmap(bm);
            iv_shared_picture.setScaleType(ImageView.ScaleType.CENTER_CROP);
            return;
        }
        DailySentence.getInstance().getDailySentence(new DailySentence.OnDailySentenceUpdate() {
            @Override
            public void onDailySentenceUpdate(DailySentenceBean bean) {
                String url = bean.getFenxiang_img();
                NetworkConnection.getInstance().getByteArray(url, new NetworkConnection.OnByteArrayResponded() {
                    @Override
                    public void onByteArrayResponded(byte[] result) {
                        try {
                            FileOutputStream fos = new FileOutputStream(picFile);
                            BufferedOutputStream bos = new BufferedOutputStream(fos);
                            bos.write(result);
                            bos.flush();
                            bos.close();
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    Bitmap bm = BitmapFactory.decodeFile(picFile.toString());
                                    iv_shared_picture.setImageBitmap(bm);
                                    iv_shared_picture.setScaleType(ImageView.ScaleType.CENTER_CROP);
                                }
                            });
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }
}
