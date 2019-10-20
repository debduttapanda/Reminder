package com.coderusk.reminder;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import java.util.Calendar;

public class TesterActivity extends AppCompatActivity {

    /////////views///////////
    Button bt_execute;
    CheckBox cb_friday;
    CheckBox cb_monday;
    CheckBox cb_saturday;
    CheckBox cb_sunday;
    CheckBox cb_thursday;
    CheckBox cb_tuesday;
    CheckBox cb_wednesday;
    DatePicker dp_monthday;
    DatePicker dp_startdate;
    DatePicker dp_nowdate;
    NumberPicker np_day_count;
    NumberPicker np_month_count;
    Spinner sp_mode;
    TimePicker tp_timepoint;
    TimePicker tp_now_time;
    TextView tv_result;

    TextView tv_timepoint;
    TextView tv_monthday;
    TextView tv_startdate;
    TextView tv_monthcount;
    TextView tv_daycount;
    TextView tv_nowdate;
    TextView tv_nowtime;
    ////////////////////////////
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reminder_test_layout);
        initialize();
    }
    private void findViewsById(){
        //execute button
        bt_execute=findViewById(R.id.bt_execute);

        //result
        tv_result=findViewById(R.id.tv_result);


        //weekdays
        cb_friday=findViewById(R.id.cb_friday);
        cb_monday=findViewById(R.id.cb_monday);
        cb_saturday=findViewById(R.id.cb_saturday);
        cb_sunday=findViewById(R.id.cb_sunday);
        cb_thursday=findViewById(R.id.cb_thursday);
        cb_tuesday=findViewById(R.id.cb_tuesday);
        cb_wednesday=findViewById(R.id.cb_wednesday);


        //monthday
        dp_monthday=findViewById(R.id.dp_monthday);

        //startdate
        dp_startdate=findViewById(R.id.dp_startdate);

        //daycount
        np_day_count=findViewById(R.id.np_day_count);

        np_day_count.setMinValue(0);
        np_day_count.setMaxValue(30);

        //monthcount
        np_month_count=findViewById(R.id.np_month_count);

        np_month_count.setMinValue(0);
        np_month_count.setMaxValue(30);

        //mode
        sp_mode=findViewById(R.id.sp_mode);

        //timepoint
        tp_timepoint=findViewById(R.id.tp_timepoint);

        ///////////////////

        dp_nowdate=findViewById(R.id.dp_nowdate);
        tp_now_time=findViewById(R.id.tp_now_time);

        tv_timepoint=findViewById(R.id.tv_timepoint);
        tv_monthday=findViewById(R.id.tv_monthday);
        tv_startdate=findViewById(R.id.tv_startdate);
        tv_monthcount=findViewById(R.id.tv_month_count);
        tv_daycount=findViewById(R.id.tv_day_count);
        tv_nowdate=findViewById(R.id.tv_nowdate);
        tv_nowtime=findViewById(R.id.tv_nowtime);
    }

    private void setViewActions(){
        if(null!=bt_execute)
        {
            bt_execute.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onExecute();
                }
            });
        }

        tp_timepoint.setIs24HourView(true);
        tp_now_time.setIs24HourView(true);

        onChangeSetter(tv_timepoint,tp_timepoint);
        onChangeSetter(tv_monthday,dp_monthday);
        onChangeSetter(tv_startdate,dp_startdate);
        onChangeSetter(tv_monthcount,np_month_count);
        onChangeSetter(tv_daycount,np_day_count);
        onChangeSetter(tv_nowdate,dp_nowdate);
        onChangeSetter(tv_nowtime,tp_now_time);
    }

    private SingleTimeReminder.RepeatMode collectRepeaptModeFromUI()
    {
        int index=sp_mode.getSelectedItemPosition();
        return SingleTimeReminder.RepeatMode.values()[index% SingleTimeReminder.RepeatMode.values().length];
    }

    private TimePoint collectTimePointFromUI(){
        int api_level= Build.VERSION.SDK_INT;

        int hour=-1;
        int mint=-1;
        int sec=0;

        if(api_level>22)
        {
            hour=tp_timepoint.getHour();
            mint=tp_timepoint.getMinute();
        }
        else
        {
            hour=tp_timepoint.getCurrentHour();
            mint=tp_timepoint.getCurrentMinute();
        }

        TimePoint tp=new TimePoint();
        tp.setHour(hour);
        tp.setMinute(mint);
        tp.setSec(sec);

        return tp;
    }

    private boolean[] collectWeekDaysFromUI()
    {
        boolean[] ret= {
                cb_sunday.isChecked(),
                cb_monday.isChecked(),
                cb_tuesday.isChecked(),
                cb_wednesday.isChecked(),
                cb_thursday.isChecked(),
                cb_friday.isChecked(),
                cb_saturday.isChecked(),
        };
        return ret;
    }

    private int collectMonthDayFromUI()
    {
        return dp_monthday.getDayOfMonth();
    }

    private Calendar collectStartDateFromUI()
    {
        int day=dp_startdate.getDayOfMonth();
        int month=dp_startdate.getMonth();
        int year=dp_startdate.getYear();

        Calendar c=Calendar.getInstance();

        c.set(Calendar.DAY_OF_MONTH,day);
        c.set(Calendar.MONTH,month);
        c.set(Calendar.YEAR,year);

        return c;
    }

    private int collectMonthCountFromUI()
    {
        return np_month_count.getValue();
    }

    private int collectDayCountFromUI()
    {
        return np_day_count.getValue();
    }

    private Calendar collectNowFromUI()
    {
        int api_level= Build.VERSION.SDK_INT;

        int hour=-1;
        int mint=-1;
        int sec=0;

        if(api_level>22)
        {
            hour=tp_now_time.getHour();
            mint=tp_now_time.getMinute();
        }
        else
        {
            hour=tp_now_time.getCurrentHour();
            mint=tp_now_time.getCurrentMinute();
        }
        ///////////////////////

        int day=dp_nowdate.getDayOfMonth();
        int month=dp_nowdate.getMonth();
        int year=dp_nowdate.getYear();

        Calendar c=Calendar.getInstance();

        c.set(Calendar.DAY_OF_MONTH,day);
        c.set(Calendar.MONTH,month);
        c.set(Calendar.YEAR,year);

        c.set(Calendar.HOUR_OF_DAY,hour);
        c.set(Calendar.MINUTE,mint);
        c.set(Calendar.SECOND,sec);

        return c;
    }

    private SingleTimeReminder collectDataFromUI()
    {
        SingleTimeReminder.RepeatMode repeatMode=collectRepeaptModeFromUI();
        TimePoint timePoint=collectTimePointFromUI();
        boolean[] weekDays=collectWeekDaysFromUI();
        int monthDay=collectMonthDayFromUI();
        Calendar startDate=collectStartDateFromUI();
        int monthCount=collectMonthCountFromUI();
        int dayCount=collectDayCountFromUI();

        SingleTimeReminder str=new SingleTimeReminder();

        str.setRepeatrepeatMode(repeatMode);
        str.setTimePoint(timePoint);
        str.setWeekDays(weekDays);
        str.setMonthDay(monthDay);
        str.setStartDate(startDate);
        str.setMonth_count(monthCount);
        str.setDay_count(dayCount);

        str.reactUnreal();

        Calendar now=collectNowFromUI();

        str.setNowCalendar(now);

        return str;
    }

    private void setUIResult(String input)
    {
        tv_result.setText(input);
    }

    private void onExecute()
    {
        SingleTimeReminder str=collectDataFromUI();
        setUIResult(str.nextAlarmString());
    }

    private void initialize()
    {
        findViewsById();
        setViewActions();
    }

    private void toggleTimePoint(){
        visibilityToggle(tv_timepoint,tp_timepoint);
    }
    private void toggleMonthDay(){
        visibilityToggle(tv_monthday,dp_monthday);
    }
    private void toggleStartDate(){
        visibilityToggle(tv_startdate,dp_startdate);
    }
    private void toggleMonthCount(){
        visibilityToggle(tv_monthcount,np_month_count);
    }
    private void toggleDayCount(){
        visibilityToggle(tv_daycount,np_day_count);
    }
    private void toggleNowDate(){
        visibilityToggle(tv_nowdate,dp_nowdate);
    }
    private void toggleNowTime(){
        visibilityToggle(tv_nowtime,tp_now_time);
    }

    private void dateChangeSetter(final TextView value, View changer)
    {
        DatePicker datePicker = (DatePicker) changer;
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());

        int iyear=calendar.get(Calendar.YEAR);
        int imonth=calendar.get(Calendar.MONTH);
        int iday=calendar.get(Calendar.DAY_OF_MONTH);

        String val=iday+"-"+(imonth+1)+"-"+iyear;
        value.setText(val);

        datePicker.init(iyear, imonth, iday, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker datePicker, int year, int month, int dayOfMonth) {
                String val=dayOfMonth+"-"+(month+1)+"-"+year;
                value.setText(val);

            }
        });
    }

    private void timeChangeSetter(final TextView value, View changer)
    {
        TimePicker t=(TimePicker)changer;

        int api_level= Build.VERSION.SDK_INT;

        int hour=-1;
        int mint=-1;
        int sec=0;

        if(api_level>22)
        {
            hour=t.getHour();
            mint=t.getMinute();
        }
        else
        {
            hour=t.getCurrentHour();
            mint=t.getCurrentMinute();
        }

        String val=hour+"-"+mint;
        value.setText(val);

        t.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                String val=hourOfDay+"-"+minute;
                value.setText(val);
            }
        });
    }

    private void numberChangeSetter(final TextView value, View changer)
    {
        NumberPicker n=(NumberPicker)changer;

        int ival=n.getValue();
        value.setText(ival+"");

        n.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                String val=newVal+"";
                value.setText(val);
            }
        });
    }

    private void onChangeSetter(TextView value,View changer)
    {
        if(changer instanceof DatePicker)
        {
            dateChangeSetter(value,changer);
            return;
        }
        if(changer instanceof TimePicker)
        {
            timeChangeSetter(value,changer);
            return;
        }
        if(changer instanceof NumberPicker)
        {
            numberChangeSetter(value,changer);
            return;
        }

    }

    private void visibilityToggle(View v1,View v2)
    {
        if(v1.getVisibility()==View.VISIBLE)
        {
            v1.setVisibility(View.GONE);
        }
        else
        {
            v1.setVisibility(View.VISIBLE);
        }

        if(v2.getVisibility()==View.VISIBLE)
        {
            v2.setVisibility(View.GONE);
        }
        else
        {
            v2.setVisibility(View.VISIBLE);
        }
    }

    public void onClickToggler(View v)
    {
        int id=v.getId();
        switch (id)
        {
            case R.id.tv_label_time:toggleTimePoint();break;
            case R.id.tv_label_monthday:toggleMonthDay();break;
            case R.id.tv_label_startdate:toggleStartDate();break;
            case R.id.tv_label_month_count:toggleMonthCount();break;
            case R.id.tv_label_day_count:toggleDayCount();break;
            case R.id.tv_label_nowdate:toggleNowDate();break;
            case R.id.tv_label_nowtime:toggleNowTime();break;
            default:break;
        }
    }
}
