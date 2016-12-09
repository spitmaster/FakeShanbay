package com.zhouyijin.zyj.fakeshanbay.wechat;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;

import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXImageObject;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXTextObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.zhouyijin.zyj.fakeshanbay.activity.sharepictureactivity.SharePicActivity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

/**
 * Created by zhouyijin on 2016/12/7.
 */

public class WeChatModule {

    public static final String TAG = "WeChatModule";

    private static final String APP_ID = "wx91faa7904aa35143";

    private static final String APP_SECRET = "b2f7e41b1700ff95bf736c71952395a3";

    public IWXAPI api;

    public void registerWeChat(Context context) {
        api = WXAPIFactory.createWXAPI(context, APP_ID, true);
        api.registerApp(APP_ID);
    }


    public void sharePic(Bitmap bitmap, String tag) {
        if (bitmap == null) {
            return;
        }
        WXImageObject imgObj = new WXImageObject(bitmap);
        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = imgObj;

        Bitmap thumbBitmap = Bitmap.createScaledBitmap(bitmap, 150, 150, true);
        msg.thumbData = bitmap2ByteArray(thumbBitmap);

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = tag;  //这个标识了这个请求
        req.message = msg;  //这个保存了分享的图片
        req.scene = SendMessageToWX.Req.WXSceneTimeline;
        Log.d(TAG, "sharePic: 2");
        boolean a = api.sendReq(req);
        Log.d(TAG, "sendReq: " + a);
    }

    public void sharePicByFile(File picFile, String tag) {
        if (!picFile.exists()) {
            return;
        }
        Bitmap pic = BitmapFactory.decodeFile(picFile.toString());
        WXImageObject imageObject = new WXImageObject(pic); //这个构造方法中自动把传入的bitmap转化为2进制数据
        //imageObject.imageData = .....
        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = imageObject;
        Bitmap thumbBitmap = Bitmap.createScaledBitmap(pic, 150, 150, true);
        msg.thumbData = bitmap2ByteArray(thumbBitmap);  //这个bitmap不能超过32kb如果一个像素是8bit的话换算成正方形的bitmap则边长不超过181像素,变长设置成150是比较保险的
//        msg.setThumbImage(thumbBitmap);
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.message = msg;
        req.scene = SendMessageToWX.Req.WXSceneTimeline;
//        req.scene = SendMessageToWX.Req.WXSceneSession;
        req.transaction = tag;
        boolean b = api.sendReq(req);
    }

    public void shareText(String content) {
        if (content == null || content.equals("")) {
            return;
        }
        WXTextObject textObj = new WXTextObject(content);
        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = textObj;
        msg.description = content;

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.message = msg;
        req.transaction = String.valueOf(System.currentTimeMillis());
        req.scene = SendMessageToWX.Req.WXSceneTimeline;
        api.sendReq(req);

    }


    private byte[] bitmap2ByteArray(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        try {
            baos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return b;
    }

    private WeChatModule() {
    }

    public static WeChatModule getInstance() {
        return InstanceHolder.instance;
    }

    private static class InstanceHolder {
        private static WeChatModule instance = new WeChatModule();
    }

}
