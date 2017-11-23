package com.example.bagguo.dreamalarm;
/*
 * @author 陈湛蓝
 * @version 1.0
 *
 * 创建时间  2015/8/15
 *
 * 蓝宝闹钟1.0在实现基础的闹钟响铃、设置时间的基础上，增加了变换闹钟北京的功能。
 */


import android.content.Context;
import android.content.Intent;
import android.content.BroadcastReceiver;
import android.os.Bundle;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context, AlertActivity.class);
        Bundle bundleRet = new Bundle();
        bundleRet.putString("STR_CALLER", "");
        i.putExtras(bundleRet);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }
}  