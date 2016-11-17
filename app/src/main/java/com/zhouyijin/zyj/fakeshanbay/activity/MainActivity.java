package com.zhouyijin.zyj.fakeshanbay.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.zhouyijin.zyj.fakeshanbay.BaseActivity;
import com.zhouyijin.zyj.fakeshanbay.MyApplication;
import com.zhouyijin.zyj.fakeshanbay.R;
import com.zhouyijin.zyj.fakeshanbay.TodayTask.TodayTask;
import com.zhouyijin.zyj.fakeshanbay.activity.fragment_main_group.FragmentMainGroup;
import com.zhouyijin.zyj.fakeshanbay.activity.fragment_main_home.FragmentMainHome;
import com.zhouyijin.zyj.fakeshanbay.activity.fragment_main_setting.FragmentMainSetting;
import com.zhouyijin.zyj.fakeshanbay.network.NetworkConnection;
import com.zhouyijin.zyj.fakeshanbay.tools.CheckTokenExpired;

public class MainActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener, TodayTask.OnPrepareProgress {

    public static final String TAG = MainActivity.class.getSimpleName();

    private RadioGroup rg_shanbay_main;
    private RadioButton rb_shanbay_home;
    private RadioButton rb_shanbay_group;
    private RadioButton rb_shanbay_mine;

    private FragmentManager mFragmentManager;
    private FrameLayout fragmentcontainer_main_0, fragmentcontainer_main_1;

    private Fragment fragmentMainHome;
    private Fragment fragmentMainGroup;
    private Fragment fragmentMainMine;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MyApplication.getTodayTask().refreshWords();
        initView();
        testToken();
        if (NetworkConnection.isNetworkConnected()) {
            MyApplication.getTodayTask().prepareTodayTask(this);
        } else {
            Toast.makeText(this, "网络未连接,无法获取今日计划的内容", Toast.LENGTH_SHORT).show();
        }
    }

    //检查是否是最新的Token
    private void testToken() {
        CheckTokenExpired.checkTokenExpired(new CheckTokenExpired.OnResult() {
            @Override
            public void onResult(boolean isTokenUseful) {
                if (!isTokenUseful) {
                    getLatestToken();
                }
            }
        });
    }


    // TODO: 2016/8/24 如果token不是最新的则启动GetTokenActivity来获取最新的token
    private void getLatestToken() {
        Intent intent = new Intent(this, GetTokenActivity.class);
        intent.putExtra("get_token", GetTokenActivity.GET_TOKEN);
        startActivityForResult(intent, GetTokenActivity.GET_TOKEN);
    }


    private void initView() {
        //设置两个fragmentcontainer是为了不让viewpager重复加载,让viewpager常驻内存,不然来回切换会卡
        fragmentcontainer_main_0 = (FrameLayout) findViewById(R.id.fragmentcontainer_main_0);
        fragmentcontainer_main_1 = (FrameLayout) findViewById(R.id.fragmentcontainer_main_1);
        mFragmentManager = getSupportFragmentManager();
        rg_shanbay_main = (RadioGroup) findViewById(R.id.rg_shanbay_main);
        rg_shanbay_main.setOnCheckedChangeListener(this);
        rb_shanbay_home = (RadioButton) findViewById(R.id.rb_shanbay_home);
        rb_shanbay_group = (RadioButton) findViewById(R.id.rb_shanbay_group);
        rb_shanbay_mine = (RadioButton) findViewById(R.id.rb_shanbay_mine);

        rb_shanbay_home.setChecked(true);
        fragmentcontainer_main_0.setVisibility(View.VISIBLE);
        fragmentcontainer_main_1.setVisibility(View.GONE);
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        if (fragmentMainHome == null) {
            fragmentMainHome = new FragmentMainHome();
        }
        transaction.replace(R.id.fragmentcontainer_main_0, fragmentMainHome);
        transaction.commit();

    }


    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        switch (i) {
            case R.id.rb_shanbay_home: {
                fragmentcontainer_main_0.setVisibility(View.VISIBLE);
                fragmentcontainer_main_1.setVisibility(View.GONE);
                if (fragmentMainHome == null) {
                    fragmentMainHome = new FragmentMainHome();
                }
                FragmentTransaction transaction = mFragmentManager.beginTransaction();
                transaction.replace(R.id.fragmentcontainer_main_0, fragmentMainHome);
                transaction.commit();
                break;
            }

            case R.id.rb_shanbay_group: {
                fragmentcontainer_main_0.setVisibility(View.GONE);
                fragmentcontainer_main_1.setVisibility(View.VISIBLE);
                if (fragmentMainGroup == null) {
                    fragmentMainGroup = new FragmentMainGroup();
                }
                FragmentTransaction transaction = mFragmentManager.beginTransaction();
                transaction.replace(R.id.fragmentcontainer_main_1, fragmentMainGroup);
                transaction.commit();
                break;
            }
            case R.id.rb_shanbay_mine: {
                fragmentcontainer_main_0.setVisibility(View.GONE);
                fragmentcontainer_main_1.setVisibility(View.VISIBLE);
                if (fragmentMainMine == null) {
                    fragmentMainMine = new FragmentMainSetting();
                }
                FragmentTransaction transaction = mFragmentManager.beginTransaction();
                transaction.replace(R.id.fragmentcontainer_main_1, fragmentMainMine);
                transaction.commit();
                break;
            }
        }
    }


    //这里主要是接收最新的token后进行的操作
    // TODO: 2016/8/24 刷新了token后要,刷新计划,还要下载计划中的单词,有空再写把,先写别的逻辑
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {

        }


        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onPrepareProgress(int progress, int max) {
        Log.d("big_test", "onPrepareProgress: progress = " + progress);
        Log.d("big_test", "onPrepareProgress: maxProgress = " + max);
    }




/*
    请求权限的代码,看样子貌似用不上
 */
//    private static final int REQUEST_INTERNET_CODE = -3;

//    @Override
//    protected void onResume() {
//        super.onResume();
//        if (ContextCompat.checkSelfPermission(
//                this, Manifest.permission.INTERNET)
//                == PackageManager.PERMISSION_DENIED) {
//            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.INTERNET)) {
//                showRequestDialog(Manifest.permission.INTERNET);
//            } else {
//                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET}, REQUEST_INTERNET_CODE);
//            }
//        }
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        switch (requestCode) {
//            case REQUEST_INTERNET_CODE:
//                if (grantResults != null && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//
//                } else {
//                    showRequestFialedDialog(Manifest.permission.INTERNET);
//                }
//                break;
//            default:
//        }
//    }
//
//    private void showRequestDialog(final String permission) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setMessage("若要正常使用需要获取网络权限.")
//                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        ActivityCompat.requestPermissions(MainActivity.this, new String[]{permission}, REQUEST_INTERNET_CODE);
//                        dialog.dismiss();
//                    }
//                })
//                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                })
//                .setTitle("获取权限")
//                .show();
//    }
//
//    private void showRequestFialedDialog(String permission) {
//        new AlertDialog.Builder(this)
//                .setMessage("若要正常使用功能,需要您手动在设置中打开网络访问权限")
//                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                })
//                .show();
//    }
}
