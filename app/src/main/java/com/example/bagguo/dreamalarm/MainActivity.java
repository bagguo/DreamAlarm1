package com.example.bagguo.dreamalarm;

/*
 * @author 陈湛蓝
 * @version 1.0
 *
 * 创建时间  2015/8/15
 *
 * 蓝宝闹钟1.0在实现基础的闹钟响铃、设置时间的基础上，增加了变换闹钟北京的功能。
 */

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.alarm1.R;

import java.util.Calendar;

import static com.example.alarm1.R.id.alarm;


public class MainActivity extends Activity {
    public static final String WALLPAPER_FILE = "wallpaper_file";
    private static int RESULT_LOAD_IMAGE = 1;

    private LinearLayout layout;
    private Button Cancel;
    private TextView alarmTime, SetBackground;
    private LinearLayout AlarmOne;
    private CheckBox RepeatingCheck;
    boolean isClicked = false, isLate = false;

    int count = 1, option = 0;
    ;

    private Calendar c = Calendar.getInstance();
    AlertDialog builder = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        layout = (LinearLayout) findViewById(R.id.layout);

        SetBackground = (TextView) findViewById(R.id.setBackground);
        SetBackground.setOnClickListener(new TextView.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                final String[] PictureList = {"系统图片", "自定义图片"};
                AlertDialog.Builder listDia = new AlertDialog.Builder(MainActivity.this);
                listDia.setTitle("背景设置");
                listDia.setItems(PictureList, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                         /*下标是从0开始的*/
                        switch (which) {
                            case 0:
                                final String[] SystemPicture = {"小黄人", "海绵宝宝", "樱桃小丸子", "哆啦a梦"};
                                AlertDialog.Builder SystemDia = new AlertDialog.Builder(MainActivity.this);
                                SystemDia.setTitle("取消闹钟");
                                SystemDia.setItems(SystemPicture, new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        // TODO Auto-generated method stub
		    		                 /*下标是从0开始的*/
                                        SharedPreferences.Editor editor = getSharedPreferences(WALLPAPER_FILE, MODE_PRIVATE).edit();
                                        switch (which) {
                                            case 0:
                                                layout.setBackgroundResource(R.drawable.xiaohuangren);
                                                editor.putInt("wellpaper", R.drawable.xiaohuangren);
                                                editor.commit();
                                                break;

                                            case 1:
                                                layout.setBackgroundResource(R.drawable.background_a);
                                                break;

                                            case 2:
                                                layout.setBackgroundResource(R.drawable.xiaowanzi);
                                                break;

                                            case 3:
                                                layout.setBackgroundResource(R.drawable.duola);
                                                break;

                                            default:
                                                break;
                                        }
                                    }
                                });
                                SystemDia.create().show();
                                break;

                            case 1:
                                Intent i = new Intent(
                                        Intent.ACTION_PICK,
                                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);//设置系统图片有问题

                                startActivityForResult(i, RESULT_LOAD_IMAGE);
                        }
                    }
                });
                listDia.create().show();
            }

        });

        RepeatingCheck = (CheckBox) findViewById(R.id.repeatingCheck);
        RepeatingCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                // TODO Auto-generated method stub
                //isChecked = true;
            	/*count++;
            	if(count % 2 == 0){
            		option = 0;
            		isClicked = true;
            	} else {
            		option = 1;
            		isClicked = false;
            	}*/
                isClicked = true;
            }
        });

        Cancel = (Button) findViewById(R.id.cancel);
        Cancel.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(MainActivity.this, AlarmReceiver.class);
                PendingIntent pi = PendingIntent.getBroadcast(MainActivity.this, 0,
                        intent, 0);
                AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
                //取消警报
                am.cancel(pi);
                alarmTime.setText("闹钟取消");
                //取消闹钟的同时取消音乐
                stopService(new Intent("com.example.bagguo.alarmtest.MUSIC"));
            }

        });

        alarmTime = (TextView) findViewById(alarm);

        alarmTime.setOnClickListener(new LinearLayout.OnClickListener() {
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                c.setTimeInMillis(System.currentTimeMillis());
                int mHour_0 = c.get(Calendar.HOUR_OF_DAY);
                int mMinute_0 = c.get(Calendar.MINUTE);


                new TimePickerDialog(MainActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {
                                c.setTimeInMillis(System.currentTimeMillis());

                                c.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                c.set(Calendar.MINUTE, minute);
                                c.set(Calendar.SECOND, 0);
                                c.set(Calendar.MILLISECOND, 0);

                                long date = c.getTimeInMillis();

                                if (c.getTimeInMillis() < System.currentTimeMillis()) {
                                    c.set(Calendar.DAY_OF_YEAR, c.get(Calendar.DAY_OF_YEAR) + 1);
                                }

                                Intent intent = new Intent(MainActivity.this, AlarmReceiver.class);
                                PendingIntent pendIntent = PendingIntent.getBroadcast(
                                        MainActivity.this, 0, intent, 0);
                                AlarmManager am;
                                am = (AlarmManager) getSystemService(ALARM_SERVICE);
                                if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M)
                                {
                                    am.setExactAndAllowWhileIdle(AlarmManager.ELAPSED_REALTIME_WAKEUP,c.getTimeInMillis(), pendIntent);
                                }else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
                                    am.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP,c.getTimeInMillis(), pendIntent);
                                }else{
                                    am.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,c.getTimeInMillis(), pendIntent);
                                }
//                                am.set(AlarmManager.RTC_WAKEUP,
//                                        c.getTimeInMillis(),
//                                        pendIntent
//                                );

                                String str = format(hourOfDay) + ":" + format(minute);
                                alarmTime.setText(str);
                                SharedPreferences textChange = getPreferences(0);
                                SharedPreferences.Editor editor_1 = textChange.edit();
                                editor_1.putString("TIME1", str);
                                editor_1.commit();
                                //times[0].replaceAll(times[0].toString(),tmpS);

                                //SharedPreferences保存数据，并提交
                                SharedPreferences timeShare = getPreferences(0);
                                SharedPreferences.Editor editor = timeShare.edit();
                                editor.putString("TIME1", str);
                                editor.commit();

                                Toast.makeText(MainActivity.this, "宝宝的闹钟时间为" + str,
                                        Toast.LENGTH_SHORT)
                                        .show();

                                if (!isClicked) {
							 /*Intent mIntent = new Intent();
		                	 PendingIntent pending = PendingIntent.getBroadcast(
			                		 MainActivity.this,0, mIntent, 0);*/
                                    am.setRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(),
                                            8 * 60 * 1000, pendIntent);
                                    isClicked = false;
                                }
                            }
                        }, mHour_0, mMinute_0, true).show();
            }
        });

    }


    private String format(int x) {
        String s = "" + x;
        if (s.length() == 1) s = "0" + s;
        return s;
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            builder = new AlertDialog.Builder(MainActivity.this)
//                    .setIcon(R.drawable.icon)
                    .setTitle("叮咚！")
                    .setMessage("真的要离开宝宝吗？")
                    .setPositiveButton("确定",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int whichButton) {
                                    Intent intent = new Intent();
                                    intent.setAction(Intent.ACTION_MAIN);
                                    intent.addCategory(Intent.CATEGORY_HOME);
                                    startActivity(intent);
                                }
                            })
                    .setNegativeButton("取消",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int whichButton) {
                                    builder.dismiss();
                                }
                            }).show();
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

	/*BroadcastReceiver broadcastReceiver = new BroadcastReceiver() { 
		   
        @Override 
        public void onReceive(Context context, Intent intent) { 
            // TODO Auto-generated method stub 
            changeNickname.setText(intent.getExtras().getString("data")); 
        } 
    };
   
    protected void onDestroy() { 
        unregisterReceiver(broadcastReceiver); 
    }; */

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            //layout.setBackground(BitmapFactory.decodeFile(picturePath));
            //touxiangPicture.setImageBitmap(BitmapFactory.decodeFile(picturePath));
            layout.setBackgroundResource(columnIndex);
            
            /*LayoutInflater inflater = getLayoutInflater();      
            View view = inflater.inflate(R.layout.settingactivity, null);
            ImageButton image = (ImageButton)view.findViewById(R.id.laner);
            image.setImageBitmap(BitmapFactory.decodeFile(picturePath));
            image.setBackgroundResource(columnIndex);*/

        }
    }

}

