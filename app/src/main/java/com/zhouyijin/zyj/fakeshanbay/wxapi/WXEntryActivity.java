package com.zhouyijin.zyj.fakeshanbay.wxapi;


import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.zhouyijin.zyj.fakeshanbay.R;
import com.zhouyijin.zyj.fakeshanbay.wechat.WeChatModule;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

public class WXEntryActivity extends AppCompatActivity implements IWXAPIEventHandler {

    public static final String TAG = WeChatModule.TAG;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.entry);
        WeChatModule.getInstance().api.handleIntent(getIntent(), this);

    }

    @Override
    public void onResp(BaseResp resp) {
        String result;
        Log.d(TAG, "onResp: errStr" + resp.errStr);
        Log.d(TAG, "onResp: openId" + resp.openId);
        Log.d(TAG, "onResp: transaction" + resp.transaction);
        switch (resp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                result = "分享成功!";
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                Log.d(TAG, "onResp: ErrCode" + " ERR_USER_CANCEL");
                result = "用户取消";
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                Log.d(TAG, "onResp: ErrCode" + " ERR_AUTH_DENIED");
                result = "没有权限";
                break;
            default:
                Log.d(TAG, "onResp: ErrCode" + " errcode_unknown");
                result = "错误";
                break;
        }
        Toast.makeText(this, result, Toast.LENGTH_LONG).show();
        finish();
    }


    @Override
    public void onReq(BaseReq req) {
        Log.d(TAG, "onReq: ");
        switch (req.getType()) {
            case ConstantsAPI.COMMAND_GETMESSAGE_FROM_WX:
                Log.d(TAG, "onReq: " + "ConstantsAPI.COMMAND_GETMESSAGE_FROM_WX");
                break;
            case ConstantsAPI.COMMAND_SHOWMESSAGE_FROM_WX:
                Log.d(TAG, "onReq: " + "ConstantsAPI.COMMAND_SHOWMESSAGE_FROM_WX");
                break;
            default:
                break;
        }
    }



}