package com.coderusk.reminder;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;


public class MainActivity extends AppCompatActivity {

    private static int count=0;
    ArrayList<AlarmData> dataModels;
    public ListView listView;
    private static AlarmListAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);
        /*//setAlarm(3);
        listView = (ListView) findViewById(R.id.lv_alarm_list);
        dataModels = new ArrayList<>();
        //dataModels.add("123456789");
        //dataModels.add("123456780");
        adapter = new AlarmListAdapter(dataModels, getApplicationContext(),listView);

        listView.setAdapter(adapter);*/
        //checkApi1();
        openTester();
    }

    private void openTester()
    {
        Intent myIntent = new Intent(this, TesterActivity.class);
        startActivity(myIntent);
    }



    public void addItem(View v)
    {
        EditText et_second=findViewById(R.id.et_second);
        String second=et_second.getText().toString();
        et_second.setText("");
        int val=10;
        if(second.isEmpty()){return;}
        try {
            val=Integer.valueOf(second);
        } catch (NumberFormatException e) {
            //
            return;
        }
        String id=getNewAlarmId();
        AlarmData ad=new AlarmData(id,second);
        addItemToList(ad);
        addAlarm(ad);
    }

    private String getNewAlarmId()
    {
        return String.valueOf(++count);
    }

    private void addItemToList(AlarmData ad)
    {
        ArrayList<AlarmData> al=((AlarmListAdapter)(listView.getAdapter())).getList();
        al.add(ad);
        AlarmListAdapter aa=new AlarmListAdapter(al,this,listView);
        listView.setAdapter(aa);
    }

    private void addAlarm(AlarmData ad)
    {
        setAlarm(ad);
    }

    public void onDeleteItem(String id)
    {
        cancelAlarm(Integer.valueOf(id));
    }

    private void cancelAlarm(int id)
    {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(MainActivity.this, AlarmReceiver.class);
        /*PendingIntent pendingIntent = PendingIntent.getBroadcast(
                getApplicationContext(), 1, myIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);*/

        PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, id,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager.cancel(pendingIntent);
    }

    public final void setAlarm(AlarmData ad) {
        Intent intent = new Intent(MainActivity.this,
                AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this,
                Integer.valueOf(ad.getId()),
                intent, 0);
        ((AlarmManager) getSystemService(ALARM_SERVICE)).set(
                AlarmManager.RTC, System.currentTimeMillis() + Long.valueOf(ad.getSecond())
                        * 1000, pendingIntent);
        Toast.makeText(MainActivity.this, "Timer set to " + ad.getSecond() + " seconds.",
                Toast.LENGTH_SHORT).show();
    }

    public static class AlarmReceiver extends BroadcastReceiver {
        public void onReceive(Context context, Intent intent) {
            Log.d("123456789", "Receiver3");
            ///////////////
            Intent i = new Intent();
            i.setClassName("com.coderusk.reminder", "com.coderusk.reminder.FullscreenActivity");
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        }
    }

    private Calendar stringToCalendar(String input)
    {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy_HH:mm:ss", Locale.ENGLISH);
        try {
            cal.setTime(sdf.parse(input));
        } catch (ParseException e) {
            Log.d("123456789",e.toString());
            return null;
        }
        return cal;
    }
    private String message="";
    private void checkApi1()
    {
        SingleTimeReminder str=new SingleTimeReminder();
        str.setStartDate(stringToCalendar("15-7-2019_10:10:10"));//startdate
        str.setTimePoint(new TimePoint("08:20:11"));//alarmtime
        str.reactUnreal();
        str.setRepeatrepeatMode(SingleTimeReminder.RepeatMode.ONCE_A_MONTH);
        str.setMonth_count(2);
        str.setDay_count(0);
        str.setMonthDay(15);
        Calendar now=stringToCalendar("16-09-2019_08:20:10");//now
        str.setNowCalendar(now);
        msg(str.toString());
        msg("now="+dsfc(now));
        msg(dsfc(str.nextAlarm()));//next
    }

    private void msg(String s)
    {
        //Log.d("123456789","\n"+
        TextView tv=findViewById(R.id.tv_message);
        message+="\n\n"+s;
        tv.setText(message);
    }

    private String dsfc(Calendar calendar)
    {
        if(calendar==null)
        {
            return "calendar==null";
        }
        int d=calendar.get(Calendar.DATE);
        int M=calendar.get(Calendar.MONTH)+1;
        int y=calendar.get(Calendar.YEAR);
        int h=calendar.get(Calendar.HOUR_OF_DAY);
        int m=calendar.get(Calendar.MINUTE);
        int s=calendar.get(Calendar.SECOND);

        String day=String.valueOf(d);
        String month=String.valueOf(M);
        String year=String.valueOf(y);

        String hour=String.valueOf(h);
        String min=String.valueOf(m);
        String sec=String.valueOf(s);

        String ret=day+"-"+month+"-"+year+"_"+hour+":"+min+":"+sec;

        return ret;
    }
}
