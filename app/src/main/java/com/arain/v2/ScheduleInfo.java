package com.arain.v2;

public class ScheduleInfo {
    public String time;
    public String duration;
    public Boolean pumpOn;
    public Long timeInMilliSeconds;

    public ScheduleInfo(String time, String duration, Boolean pumpOn, Long timeInMilliSeconds) {
        this.time = time;
        this.duration = duration;
        this.pumpOn = pumpOn;
        this.timeInMilliSeconds = timeInMilliSeconds;
    }
}





