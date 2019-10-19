package com.coderusk.reminder;

public class AlarmData {
    private String id;
    private String second;

    AlarmData(String id, String second)
    {
        this.id=id;
        this.second=second;
    }

    public String getId()
    {
        return id;
    }
    public void setId(String id)
    {
        this.id=id;
    }

    public String getSecond()
    {
        return second;
    }

    public void setSecond(String second)
    {
        this.second=second;
    }
}
