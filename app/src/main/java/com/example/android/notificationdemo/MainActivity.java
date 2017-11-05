package com.example.android.notificationdemo;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.view.View;
import android.widget.Button;
import android.widget.RemoteViews;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = (Button)findViewById(R.id.send_button);
        button.setOnClickListener(this);
    }
        public void onClick(View v){
            switch (v.getId()){
                case R.id.send_button:
                    showNormalProgress(this);
                    showNotificationProgress(this);
                    ShowRemoteViewsProgress(this);
            }
        }
    public static void showNormalProgress(Context context){
        Notification notification = new NotificationCompat.Builder(context)
                //设置通知左边的大图标
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(),R.mipmap.ic_launcher))
                //设置通知右边的小图标
                .setSmallIcon(R.mipmap.ic_launcher)
                //通知首次出现在通知栏，带上升动画效果
                .setTicker("通知来了")
                //设置通知的图标
                .setContentTitle("普通通知样式")
                //设置通知的内容
                .setContentText("普通通知的内容")
                //通知产生的时间，会在通知信息里显示
                .setWhen(System.currentTimeMillis())
                //设置该通知的优先级
                .setPriority(Notification.PRIORITY_DEFAULT)
                //设置点击一次后消失（如果没有点击事件，则该方法无效。）
                .setAutoCancel(true)
                //设置是否可清除 false为可清除
                .setOngoing(false)
                //向通知添加声音、闪光和震动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性
                .setDefaults(Notification.DEFAULT_VIBRATE | Notification.DEFAULT_SOUND)
                .setContentIntent(PendingIntent.getActivity(context,1,
                        new Intent(context,MainActivity.class),PendingIntent.FLAG_CANCEL_CURRENT))
                .build();
       final NotificationManager manager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        //notification.setLatestEventInfo(this,"this is content title","this is content text",null); 已经废弃
        //第一个参数ID不能重复，否则新通知会覆盖旧通知
        manager.notify(0,notification);
    }
    public static void showNotificationProgress(Context context){
        final NotificationCompat.Builder builderProgress = new NotificationCompat.Builder(context);
        final NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        builderProgress.setContentTitle("进度条通知")
                       .setContentText("下载中")
                       .setSmallIcon(R.mipmap.ic_launcher)
                       .setTicker("进度条通知");
        new Thread(new Runnable() {
            @Override
            public void run() {
                for(int progress=0;progress<=100;progress++){
                    builderProgress.setProgress(100,progress,false);
                    notificationManager.notify(2,builderProgress.build());
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                builderProgress.setContentText("下载完成");
                builderProgress.setProgress(0,0,false);
                notificationManager.notify(2,builderProgress.build());
                notificationManager.cancel(2);
            }
        }).start();
    }
    public static void ShowRemoteViewsProgress(Context context){
        Intent mIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://blog.csdn.net/itachi85/"));
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, mIntent, 0);
        Notification.Builder builder = new Notification.Builder(context);
        builder.setContentIntent(pendingIntent)
               //.setContentIntent(PendingIntent.getActivity(context,3,new Intent(context,MainActivity.class),PendingIntent.FLAG_CANCEL_CURRENT))
               .setWhen(System.currentTimeMillis())
               .setTicker("有新资讯")
               .setContentTitle("折叠式通知")
               .setPriority(Notification.PRIORITY_HIGH)
               .setAutoCancel(true)
               .setOngoing(false)
               .setSmallIcon(R.drawable.toutiao_32);
        Notification notification = builder.build();
        //创建RemoteViews实例
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(),R.layout.remote_layout);
        remoteViews.setImageViewResource(R.id.custom_icon, R.drawable.toutiao);
        remoteViews.setTextViewText(R.id.remote_title,"今日头条");
        remoteViews.setTextViewText(R.id.remote_content,"锤子新品坚果Pro2外形曝光，这外形，你怎么看？");
        notification.bigContentView = remoteViews;
        final NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(3,notification);
    }
}
