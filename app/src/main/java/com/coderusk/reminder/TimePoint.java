package com.coderusk.reminder;

import java.util.Calendar;

public class TimePoint {
    private int hour;//24 hour format
    private int min;
    private int sec;

    public TimePoint(){
        Calendar c=Calendar.getInstance();
        hour=c.get(Calendar.HOUR_OF_DAY);
        min=c.get(Calendar.MINUTE);
        sec=c.get(Calendar.SECOND);
    }

    public static TimePoint now()
    {
        TimePoint tp=new TimePoint();
        Calendar nowTime=Calendar.getInstance();

        int h=nowTime.get(Calendar.HOUR_OF_DAY);
        int m=nowTime.get(Calendar.MINUTE);
        int s=nowTime.get(Calendar.SECOND);

        tp.setHour(h);
        tp.setHour(m);
        tp.setHour(s);

        return tp;
    }

    public boolean isEqual(TimePoint other)
    {
        return (hour==other.hour&&min==other.min&&sec==other.sec);
    }

    public boolean after(TimePoint other)
    {
        int h=hour;
        int m=min;
        int s=sec;

        int h1=other.hour;
        int m1=other.min;
        int s1=other.sec;

        if(h>h1){return true;}
        if(h<h1){return false;}
        //that means h==h1
        if(m>m1){return true;}
        if(m<m1){return false;}
        //that means m==m1
        if(s>s1){return true;}
        if(s<s1){return false;}
        //that means s==s1
        //so h==h1, m==m1 and s==s1
        //that means both are same
        //that means 'after' should return false
        return false;
    }

    public boolean before(TimePoint other)
    {
        int h=hour;
        int m=min;
        int s=sec;

        int h1=other.hour;
        int m1=other.min;
        int s1=other.sec;

        if(h>h1){return false;}
        if(h<h1){return true;}
        //that means h==h1
        if(m>m1){return false;}
        if(m<m1){return true;}
        //that means m==m1
        if(s>s1){return false;}
        if(s<s1){return true;}
        //that means s==s1
        //so h==h1, m==m1 and s==s1
        //that means both are same
        //that means after should return false
        return false;
    }

    public static boolean isStringOk(String input)
    {
        return input==new TimePoint(input).toString();
    }

    public TimePoint(String input)
    {
        this();
        if(input==null){return;}
        if(input.isEmpty()){return;}
        if(input.length()==0){return;}
        String[] params=input.split(":");
        if(params.length!=3){return;}

        String h=params[0];
        String m=params[1];
        String s=params[2];

        if(!h.matches("\\d+")){return;}
        if(!m.matches("\\d+")){return;}
        if(!s.matches("\\d+")){return;}

        int ihour=Integer.valueOf(h);
        int imin=Integer.valueOf(m);
        int isec=Integer.valueOf(s);

        TimePoint temp=new TimePoint();
        if(!temp.setHour(ihour)){return;}
        if(!temp.setMinute(imin)){return;}
        if(!temp.setSec(isec)){return;}

        this.hour=temp.hour;
        this.min=temp.min;
        this.sec=temp.sec;
    }

    private boolean inRange(int input, int min, int max){
        return (input>=min&&input<=max);
    }

    public boolean setHour(int input){
        boolean ret=inRange(input,0,23);
        if(ret) {
            hour = input;
        }
        return ret;
    }
    public boolean setMinute(int input){
        boolean ret=inRange(input,0,59);
        if(ret) {
            min = input;
        }
        return ret;
    }
    public boolean setSec(int input){
        boolean ret=inRange(input,0,59);
        if(ret) {
            sec = input;
        }
        return ret;
    }

    //////////////////////////

    public int getHour(){
        return hour;
    }
    public int getMin(){
        return min;
    }
    public int getSec() {
        return sec;
    }

    public String toString()
    {
        String ret="";
        ret+=String.valueOf(hour);ret+=":";
        ret+=String.valueOf(min);ret+=":";
        ret+=String.valueOf(sec);
        return ret;
    }
}
