package com.coderusk.reminder;

public class ReminderOnce {

    public enum RepeatMode {NUMBER_OF_DAYS, EVERYDAY, DAYS_IN_WEEK, ONCE_A_WEEK, ONCE_IN_TWO_WEEKS, ONCE_A_MONTH, ONCE_IN_TWO_MONTHS, ONCE_IN_THREE_MONTHS};
    RepeatMode repeatMode;
    Caltim from;
    WeekDays weekDays;
    int dayOfMonth;
    Tim on;
    int months;
    int days;

    private boolean validAlarmDate(Cal date)
    {
        if(repeatMode==RepeatMode.NUMBER_OF_DAYS)
        {

        }
    }

    public Caltim nextOf(Caltim input)
    {
        if(input.before(from))
        {
            input.setDate(from.getDate());
            input.addDate(-1);
        }
        Caltim end=getEnd();
        if(!input.before(end)){return null;}
        Cal date=input.getDate();
        boolean dateValid=validAlarmDate(date);
        if(dateValid)
        {
            Tim time=input.getTime();
            boolean hasAChance=time.before(on);
            if(hasAChance)
            {
                Caltim ret=input;
                ret.setTime(time);
                return ret;
            }
        }
        Cal d=getNextDate(date);
        if(d==null){return null;}
        Caltim next=new Caltim();
        next.setDate(d);
        next.setTime(on);
        if(!next.after(end)){return next;}
        return null;
    }

    private Cal getNextDate(Cal date)
    {
        if(repeatMode==RepeatMode.EVERYDAY)
        {
            date.addDay(1);
            return date;
        }
        if(repeatMode==RepeatMode.NUMBER_OF_DAYS)
        {
            date.addDay(1);
            return date;
        }
        if(repeatMode==RepeatMode.DAYS_IN_WEEK)
        {
            int weekDay=date.getWeekDay();
            int nextWeekDay=getNextAlarmWeekDay(weekDay);
            int offset=nextWeekDay-weekDay;
            if(offset<0)
            {
                offset+=7;
            }
            date.addDay(offset);
            return date;
        }
        if(repeatMode==RepeatMode.ONCE_A_WEEK)
        {
            date.addDay(7);
            return date;
        }
        if(repeatMode==RepeatMode.ONCE_IN_TWO_WEEKS)
        {
            int alarmWeekDay=weekDays.getFirst();
            if(alarmWeekDay==-1){return null;}
            Cal first=from.getDate();
            int weekDay=first.getWeekDay();
            int nextWeekDay=getNextAlarmWeekDay(weekDay);
            int offset=nextWeekDay-weekDay;
            if(offset<0)
            {
                offset+=7;
            }
            first.addDay(offset);

            for(;;)
            {
                if(date.before(first))
                {
                    date=first;
                    break;
                }
                else
                {
                    first.addDay(14);
                }
            }

            return date;
        }
        if(repeatMode==RepeatMode.ONCE_A_MONTH)
        {
            int day=date.getDay();
            if(dayOfMonth>day)
            {
                date.setDay(dayOfMonth);
            }
            else
            {
                date.addMonth(1);
            }
            return date;
        }
        if(repeatMode==RepeatMode.ONCE_IN_TWO_MONTHS)
        {
            Cal first=from.getDate();
            if(first.getDay()<dayOfMonth)
            {
                first.setDay(dayOfMonth);
            }
            if(first.getDay()>dayOfMonth)
            {
                first.setDay(dayOfMonth);
                first.addMonth(1);
            }
            for(;;)
            {
                if(date.before(first))
                {
                    return first;
                }
                first.addMonth(2);
            }
        }
        if(repeatMode==RepeatMode.ONCE_IN_THREE_MONTHS)
        {
            Cal first=from.getDate();
            if(first.getDay()<dayOfMonth)
            {
                first.setDay(dayOfMonth);
            }
            if(first.getDay()>dayOfMonth)
            {
                first.setDay(dayOfMonth);
                first.addMonth(1);
            }
            for(;;)
            {
                if(date.before(first))
                {
                    return first;
                }
                first.addMonth(3);
            }
        }
        return null;
    }

}
