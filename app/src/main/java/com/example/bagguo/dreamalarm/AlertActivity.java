package com.example.bagguo.dreamalarm;


import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.alarm1.R;

import java.io.File;

public class AlertActivity extends Activity {
    MediaPlayer mediaPlayer1 = null;
    VideoView videoView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /**
         * 音频闹钟
         */
//        mediaPlayer1 = MediaPlayer.create(this, R.raw.music);
//        mediaPlayer1.start();
        /**
         * 视频闹钟
         */
        setContentView(R.layout.activity_alert);
        videoView = (VideoView) findViewById(R.id.videoView);
        //6.0动态权限申请
        if (ContextCompat.checkSelfPermission(AlertActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(AlertActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        } else {
            initVideoPath();
        }
        videoView.start(); // 开始播放
        new AlertDialog.Builder(AlertActivity.this)
                //.setIcon(R.drawable.icon)
//                .setTitle("闹钟响了!!")
//                .setMessage("小懒猪，快起床！！！")
                .setCancelable(false)
                .setPositiveButton("再睡会~~~",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
//                                mediaPlayer1.stop();
                                videoView.stopPlayback();
                                finish();
                            }
                        })
                .setNegativeButton("关闭", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        videoView.stopPlayback();
                        finish();
                    }
                }).show();

        //AlertActivity.PlaySound(getApplicationContext());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    initVideoPath();
                } else {
                    Toast.makeText(this, "拒绝权限将无法使用程序", Toast.LENGTH_SHORT).show();
                    finish();
                }
        }

    }

    private void initVideoPath() {
        File file = new File(Environment.getExternalStorageDirectory(), "movie.mp4");
        videoView.setVideoPath(file.getPath());
    }

	/*public static int PlaySound(final Context context) { 
        NotificationManager mgr = (NotificationManager) context 
                .getSystemService(Context.NOTIFICATION_SERVICE); 
        Notification nt = new Notification(); 
        nt.sound=Uri.parse("android.resource://" +"/" +R.raw.music); 
        nt.defaults = Notification.DEFAULT_SOUND; 
        int soundId = new Random(System.currentTimeMillis()) 
                .nextInt(Integer.MAX_VALUE); 
        mgr.notify(soundId, nt); 
        return soundId; 
    }*/

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (videoView!=null) {
            videoView.suspend();
        }
    }
}
