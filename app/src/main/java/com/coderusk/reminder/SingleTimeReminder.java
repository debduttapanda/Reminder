package com.coderusk.reminder;


import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class SingleTimeReminder {

    public enum RepeatMode {NUMBER_OF_DAYS, EVERYDAY, DAYS_IN_WEEK, ONCE_A_WEEK, ONCE_IN_TWO_WEEKS, ONCE_A_MONTH, ONCE_IN_TWO_MONTHS, ONCE_IN_THREE_MONTHS};

    private TimePoint timePoint;
    private RepeatMode repeatrepeatMode;
    private boolean[] weekDays={false,false,false,false,false,false,false};
    private int monthDay;
    private Calendar startDate;
    private int month_count;
    private int day_count;

    private boolean real=true;

    private Calendar unrealNow;

    private void msg(String s)
    {
        Log.d("123456789",s);
    }

    public void reactUnreal()
    {
        real=false;
    }

    public void reactReal()
    {
        real=true;
    }

    public Calendar getUnrealNow()
    {
        return (Calendar) unrealNow.clone();
    }

    private Calendar getNow()
    {
        if(real)
        {
            return Calendar.getInstance();
        }
        else
        {
            return getUnrealNow();
        }
    }

    private Calendar getStart()
    {
        Calendar start=(Calendar) startDate.clone();
        start.set(Calendar.HOUR_OF_DAY,timePoint.getHour());
        start.set(Calendar.MINUTE,timePoint.getMin());
        start.set(Calendar.SECOND,timePoint.getSec());
        return start;
    }

    private Calendar endOfCalendar()
    {
        Calendar c=Calendar.getInstance();
        c.set(Calendar.YEAR,2100);
        c.set(Calendar.MONTH,11);
        c.set(Calendar.DAY_OF_MONTH,31);
        c.set(Calendar.HOUR_OF_DAY,11);
        c.set(Calendar.MINUTE,59);
        c.set(Calendar.SECOND,59);
        c.set(Calendar.MILLISECOND,999);
        return c;
    }

    private Calendar getLast(){
        Calendar last=getStart();
        switch (repeatrepeatMode) {
            case NUMBER_OF_DAYS:
                last.add(Calendar.DAY_OF_MONTH,day_count);
                return last;
            case EVERYDAY:
                return endOfCalendar();
            case ONCE_A_WEEK:
            case DAYS_IN_WEEK:
            case ONCE_A_MONTH:
            case ONCE_IN_TWO_WEEKS:
            case ONCE_IN_TWO_MONTHS:
            case ONCE_IN_THREE_MONTHS:
                last.add(Calendar.DAY_OF_MONTH,day_count);
                last.add(Calendar.MONTH,month_count);
                return last;
            default:
                return null;
        }
    }

    private Calendar nextCalendar_mode_numberOfDays(){//Quality Passed

        if(day_count<=0){return null;}

        Calendar start=getStart();
        Calendar now=getNow();
        Calendar last=getLast();
        Calendar first=(Calendar) start.clone();
        Calendar next=(Calendar) first.clone();

        if(now.after(last)){return null;}

        for(;;)
        {
            if(next.after(last))
            {
                next=null;
                break;
            }
            if(next.after(now))
            {
                break;
            }
            next.add(Calendar.DAY_OF_MONTH,1);
        }
        return next;
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

        String weekDayString=calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault());

        String ret=weekDayString+":"+day+"-"+month+"-"+year+"_"+hour+":"+min+":"+sec;

        return ret;
    }

    private Calendar nextCalendar_mode_everyday(){//Quality Passed

        Calendar start=getStart();
            String ss=dsfc(start);
        Calendar now=getNow();
            String sn=dsfc(now);
        Calendar last=getLast();
            String sl=dsfc(last);
        Calendar first=start;
            String sf=dsfc(first);
        Calendar next=first;
            String sne=dsfc(next);

        for(;;)
        {
            String ss1=dsfc(start);
            String sn1=dsfc(now);
            String sl1=dsfc(last);
            String sf1=dsfc(first);
            String sne1=dsfc(next);
            if(next.after(last))
            {
                next=null;
                break;
            }
            if(next.after(now))
            {
                break;
            }
            next.add(Calendar.DAY_OF_MONTH,1);
        }
        return next;
    }

    private int nextWeekDay(int today){
        int ret=-1;
        int temp=-1;
        if(today>6){return ret;}
        for(;;)
        {
            if(weekDays[temp=getWeekDayCyclicMode(++today)])
            {
                ret=temp;
                break;
            }
        }
        return ret;
    }

    private int getWeekDayCyclicMode(int input)
    {
        return input%7;
    }

    private int nextAlarmWeekDayOffset(){
        //get today is which day of week
        int currentWeekDay=Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
        //check today is a alarm day or not
        boolean todayAlarmDay=weekDays[currentWeekDay];
        if(todayAlarmDay)
        {
            TimePoint alarmTime=timePoint;
            TimePoint now=TimePoint.now();
            //check wheather we already passed todays alarm time
            boolean passed=now.after(alarmTime);
            if(passed)
            {
                //check if today is only alarm day
                boolean onlyToday=weekDays.length==1;
                if(onlyToday)
                {
                    return 7;
                }
                else
                {
                    int nextAlarmDay=nextWeekDay(currentWeekDay);
                    int offset=nextAlarmDay-currentWeekDay;
                    if(offset<0){++offset;}
                    return offset;
                }
            }
            else
            {
                return 0;
            }
        }
        else
        {
            int nextAlarmDay=nextWeekDay(currentWeekDay);
            int offset=nextAlarmDay-currentWeekDay;
            if(offset<0){++offset;}
            return offset;
        }
    }

    private int countAlarmDays()
    {
        int count=0;
        for(int i=0;i<7;++i)
        {
            if(weekDays[i]){++count;}
        }
        return count;
    }

    private int nextAlarmDayForDaysInweek(int input)
    {
        int ret=-1;
        if(input<0){return ret;}
        if(countAlarmDays()==0){return ret;}
        for(;;)
        {
            if(weekDays[input%7])
            {
                ret=input;
                break;
            }
            ++input;
        }
        return input%7;
    }

    private Calendar getFirstForDaysInWeek()
    {
        Calendar start=getStart();
        int startWeekDay=start.get(Calendar.DAY_OF_WEEK)-1;
        boolean startIsAlarmDay=weekDays[startWeekDay];
        if(startIsAlarmDay){return start;}
        int nextAlarmWeekDay=nextAlarmDayForDaysInweek(startWeekDay);
        int offset=nextAlarmWeekDay-startWeekDay;
        if(offset<0){offset+=7;}
        start.add(Calendar.DAY_OF_MONTH,offset);
        return start;
    }

    private Calendar getNextForDaysInWeek(Calendar input)
    {
        int inputWeekDay=input.get(Calendar.DAY_OF_WEEK)-1;
        boolean inputIsAlarmDay=weekDays[inputWeekDay];
        if(inputIsAlarmDay){
            Calendar inputOnTime=(Calendar) input.clone();
            inputOnTime.set(Calendar.HOUR,timePoint.getHour());
            inputOnTime.set(Calendar.MINUTE,timePoint.getMin());
            inputOnTime.set(Calendar.SECOND,timePoint.getSec());

            final Calendar ret=(Calendar) inputOnTime.clone();

            boolean before=input.before(inputOnTime);
            if(before) {
                return ret;
            }
        }
        int nextAlarmWeekDay=nextAlarmDayForDaysInweek(inputWeekDay+1);
        int offset=nextAlarmWeekDay-inputWeekDay;
        if(offset<0){offset+=7;}
        input.add(Calendar.DAY_OF_MONTH,offset);
        return input;
    }

    private Calendar nextCalendar_mode_daysInWeek(){//Quality Passed
        if(month_count==0&&day_count==0)
        {
            return null;
        }
        if(countAlarmDays()==0){return null;}
        Calendar now=getNow();
        Calendar last=getLast();
        Calendar first=getFirstForDaysInWeek();
        if(first.after(last)){return null;}
        if(now.before(first))
        {
            return first;
        }
        final Calendar next=getNextForDaysInWeek(now);
        if(next.after(last)){return null;}
        return next;
    }

    private Calendar nextCalendar_mode_OnceAWeek(){
        if(countAlarmDays()!=1){return null;}
        return nextCalendar_mode_daysInWeek();
    }

    private int firstAlarmWeekDay(){
        int ret=-1;
        for(int i=0;i<weekDays.length;++i)
        {
            if(weekDays[i])
            {
                ret=i;
                break;
            }
        }
        return ret;
    }

    private Calendar nextCalendar_mode_OnceInTwoWeek(){
        //1)    check weekDays contain only one and must one alarmday
        if(weekDays.length!=1){return null;}
        //2)    get first alarm date
        Calendar firstAlarmDate=null;
        int startDateWeekDay=startDate.get(Calendar.DAY_OF_WEEK);
        int alarmWeekDay=firstAlarmWeekDay();
        if(startDateWeekDay==alarmWeekDay)
        {
            firstAlarmDate=startDate;

            firstAlarmDate.set(Calendar.HOUR_OF_DAY,timePoint.getHour());
            firstAlarmDate.set(Calendar.MINUTE,timePoint.getMin());
            firstAlarmDate.set(Calendar.SECOND,timePoint.getSec());
        }
        else
        {
            int offset=alarmWeekDay-startDateWeekDay;
            if(offset<0){offset+=7;}

            firstAlarmDate=startDate;

            firstAlarmDate.set(Calendar.HOUR_OF_DAY,timePoint.getHour());
            firstAlarmDate.set(Calendar.MINUTE,timePoint.getMin());
            firstAlarmDate.set(Calendar.SECOND,timePoint.getSec());

            firstAlarmDate.add(Calendar.DAY_OF_MONTH,offset);
        }
        //3)    return proper
        Calendar now=getNow();

        Calendar last=startDate;
        last.set(Calendar.HOUR_OF_DAY,timePoint.getHour());
        last.set(Calendar.MINUTE,timePoint.getMin());
        last.set(Calendar.SECOND,timePoint.getSec());

        last.add(Calendar.MONTH,month_count);
        last.add(Calendar.DAY_OF_MONTH,day_count);

        Calendar next=firstAlarmDate;
        for(;;)
        {
            if(next.after(last))
            {
                next=null;
                break;
            }
            if(next.after(now))
            {
                break;
            }
            next.add(Calendar.DAY_OF_MONTH,14);
        }
        return next;
    }

    private Calendar nextCalendar_mode_OnceAMonth(){//Quality Passed
        Calendar alarmDate=getStart();

        alarmDate.set(Calendar.HOUR_OF_DAY,timePoint.getHour());
        alarmDate.set(Calendar.MINUTE,timePoint.getMin());
        alarmDate.set(Calendar.SECOND,timePoint.getSec());

        alarmDate.set(Calendar.DAY_OF_MONTH,monthDay);

        Calendar now=getNow();

        Calendar last=getLast();

        for(;;)
        {
            if(alarmDate.after(last))
            {
                alarmDate=null;
                break;
            }
            if(alarmDate.after(now))
            {
                break;
            }
            alarmDate.add(Calendar.MONTH,1);
        }

        return alarmDate;
    }

    private Calendar nextCalendar_mode_OnceInTwoMonth(){
        Calendar alarmDate=getStart();

        alarmDate.set(Calendar.HOUR_OF_DAY,timePoint.getHour());
        alarmDate.set(Calendar.MINUTE,timePoint.getMin());
        alarmDate.set(Calendar.SECOND,timePoint.getSec());

        alarmDate.set(Calendar.DAY_OF_MONTH,monthDay);

        Calendar now=getNow();

        Calendar last=getLast();

        for(;;)
        {
            if(alarmDate.after(last))
            {
                alarmDate=null;
                break;
            }
            if(alarmDate.after(now))
            {
                break;
            }
            alarmDate.add(Calendar.MONTH,2);
        }

        return alarmDate;
    }

    private Calendar nextCalendar_mode_OnceInThreeMonth(){
        Calendar alarmDate=getStart();

        alarmDate.set(Calendar.HOUR_OF_DAY,timePoint.getHour());
        alarmDate.set(Calendar.MINUTE,timePoint.getMin());
        alarmDate.set(Calendar.SECOND,timePoint.getSec());

        alarmDate.set(Calendar.DAY_OF_MONTH,monthDay);

        Calendar now=getNow();

        Calendar last=getLast();

        for(;;)
        {
            if(alarmDate.after(last))
            {
                alarmDate=null;
                break;
            }
            if(alarmDate.after(now))
            {
                break;
            }
            alarmDate.add(Calendar.MONTH,3);
        }

        return alarmDate;
    }

    public Calendar nextAlarm(){
        switch (repeatrepeatMode)
        {
            case NUMBER_OF_DAYS:return nextCalendar_mode_numberOfDays();//ok
            case EVERYDAY:return nextCalendar_mode_everyday();//ok
            case ONCE_A_WEEK:return nextCalendar_mode_OnceAWeek();//ok
            case DAYS_IN_WEEK:return nextCalendar_mode_daysInWeek();//ok
            case ONCE_A_MONTH:return nextCalendar_mode_OnceAMonth();//ok
            case ONCE_IN_TWO_WEEKS:return nextCalendar_mode_OnceInTwoWeek();//ok
            case ONCE_IN_TWO_MONTHS:return nextCalendar_mode_OnceInTwoMonth();//ok
            case ONCE_IN_THREE_MONTHS:return nextCalendar_mode_OnceInThreeMonth();//ok
            default:return null;
        }
    }

    public SingleTimeReminder(){
        timePoint=new TimePoint();
        repeatrepeatMode = RepeatMode.EVERYDAY;
        //weekDays already initialized
        monthDay=1;
        startDate=Calendar.getInstance();
        month_count=1;
        day_count=1;
        real=true;
        unrealNow=Calendar.getInstance();
    }

    public SingleTimeReminder(String input){
        this();
        if(input==null){return;}
        if(input.isEmpty()){return;}
        if(input.length()==0){return;}
        String[] params=input.split("\n");
        if(params.length!=7){return;}
        //////////////////////////////////////////////////
        timePoint=new TimePoint(params[0]);
        //////////////////////////////////////////////////
        String rm=params[1];
        try {
            repeatrepeatMode = RepeatMode.valueOf(rm);
        } catch (IllegalArgumentException e) {
            return;
        }
        /////////////////////////////////////////////////
        //weekDays
        weekDays=booleanArrayFromString(params[2]);
        ////////////////////////////////////
        String md=params[3];
        if(!md.matches("\\d+")){return;}
        monthDay=Integer.valueOf(md);
        ////////////////////////////////////
        startDate=dateStringToCalendar(params[4]);
        ////////////////////////////////////
        String mc=params[5];
        if(!mc.matches("\\d+")){return;}
        month_count=Integer.valueOf(mc);
        ////////////////////////////////////
        String dc=params[5];
        if(!dc.matches("\\d+")){return;}
        day_count=Integer.valueOf(dc);
    }

    public boolean isStringOk(String input){
        return input==new SingleTimeReminder(input).toString();
    }

    public TimePoint getTimePoint(){
        return timePoint;
    }
    public void setTimePoint(TimePoint input)
    {
        timePoint=input;
    }
    public RepeatMode getRepeatrepeatMode(){
        return repeatrepeatMode;
    }
    public void setRepeatrepeatMode(RepeatMode input)
    {
        repeatrepeatMode =input;
    }
    public boolean[] getWeekDays()
    {
        return weekDays;
    }
    public boolean setWeekDays(boolean[] input)
    {
        if(input==null){return false;}
        if(input.length!=7){return false;}
        weekDays=input;
        return true;
    }
    public int getMonthDay()
    {
        return monthDay;
    }
    public boolean setMonthDay(int input)
    {
        if(input>31){return false;}
        monthDay=input;
        return true;
    }
    public Calendar getStartDate()
    {
        return startDate;
    }
    public void setStartDate(Calendar input)
    {
        startDate=input;
    }
    public int getMonth_count()
    {
        return month_count;
    }
    public void setMonth_count(int input)
    {
        month_count=input;
    }
    public int getDay_count()
    {
        return day_count;
    }
    public void setDay_count(int input)
    {
        day_count=input;
    }

    public String toString()
    {
        String ret="";
        ret+=timePoint.toString();ret+="\n";
        ret+=String.valueOf(repeatrepeatMode);ret+="\n";
        ret+=booleanArrayToString(weekDays);ret+="\n";
        ret+=String.valueOf(monthDay);ret+="\n";
        ret+=dateStringFromCalendar(startDate);ret+="\n";
        ret+=String.valueOf(month_count);ret+="\n";
        ret+=String.valueOf(day_count);
        return ret;
    }
    private String booleanArrayToString(boolean[] input)
    {
        String ret="";
        for(int i=0;i<input.length;++i)
        {
            ret+=String.valueOf(input[i]?1:0);
        }
        return ret;
    }

    private boolean[] booleanArrayFromString(String input)
    {
        if(input==null){return null;}
        if(input.isEmpty()){return null;}
        if(input.length()==7){return null;}
        boolean[] temp={false,false,false,false,false,false,false};
        for(int i=0;i<7;++i)
        {
            char c=input.charAt(i);
            if(c!='0'||c!='1')
            {
                temp=null;
                break;
            }
            else
            {
                temp[i]=c=='0'?false:true;
            }
        }
        return temp;
    }

    public boolean setStartDate(String input)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

        try {
            startDate.setTime(sdf.parse(input));
        } catch (ParseException e) {
            msg("Wrong format/value");
            return false;
        }
        return true;
    }

    private String dateStringFromCalendar(Calendar calendar)
    {
        int d=calendar.get(Calendar.DATE);
        int m=calendar.get(Calendar.MONTH)+1;
        int y=calendar.get(Calendar.YEAR);

        String day=String.valueOf(d);
        String month=String.valueOf(m);
        String year=String.valueOf(y);

        String ret=day+"-"+month+"-"+year;

        return ret;
    }
    private Calendar dateStringToCalendar(String input)
    {
        Calendar c=Calendar.getInstance();
        ////////////////////////
        if(input==null){return c;}
        if(input.isEmpty()){return c;}
        if(input.length()==0){return c;}

        String params[]=input.split("-");
        if(params.length!=3){return c;}

        String d=params[0];
        String m=params[1];
        String y=params[2];

        if(!d.matches("\\d+")){return c;}
        if(!m.matches("\\d+")){return c;}
        if(!y.matches("\\d+")){return c;}

        int day=Integer.valueOf(d);
        int month=Integer.valueOf(m);
        int year=Integer.valueOf(y);

        c.set(Calendar.DATE,day);
        c.set(Calendar.MONTH,month);
        c.set(Calendar.YEAR,year);
        ////////////////////////
        return c;
    }

    public void setNowCalendar(Calendar input)
    {
        unrealNow=input;
    }

    public String nextAlarmString()
    {
        Calendar c=nextAlarm();
        return dsfc(c);
    }
}
