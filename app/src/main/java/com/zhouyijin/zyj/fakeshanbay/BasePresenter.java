package com.zhouyijin.zyj.fakeshanbay;

import java.lang.ref.WeakReference;

/**
 * Created by zhouyijin on 2016/10/24.
 *
 * 这个类提供了一个presenter共有的方法来获取fragment或者activity而且还不会造成内存泄漏,使用了弱引用
 *  使用getView()则可以获得fragment或者activity
 */

public abstract class BasePresenter<T> {

    private WeakReference<T> view;

    protected BasePresenter(T view) {
        this.view = new WeakReference(view);
    }

    protected T getView(){
        return view.get();
    }
}
