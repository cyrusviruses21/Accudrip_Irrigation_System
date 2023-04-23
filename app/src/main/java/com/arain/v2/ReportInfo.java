package com.arain.v2;

public class ReportInfo {
    private String waterConsumption;
    private String date;
    private String time;
    private String duration;
    private String humidity;
    private String soilMoisture;
    private String temperature;
    private String waterLevel;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getSoilMoisture() {
        return soilMoisture;
    }

    public void setSoilMoisture(String soulMoisture) {
        this.soilMoisture = soulMoisture;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getWaterLevel() {
        return waterLevel;
    }

    public void setWaterLevel(String waterLevel) {
        this.waterLevel = waterLevel;
    }

    public String getWaterConsumption() {
        return waterConsumption;
    }

    public void setWaterConsumption(String waterConsumption) {
        this.waterConsumption = waterConsumption;
    }
    public ReportInfo(String date, String time, String duration, String humidity,
                      String soilMoisture, String temperature, String waterLevel, String waterConsumption) {
        this.date = date;
        this.time = time;
        this.duration = duration;
        this.humidity = humidity;
        this.soilMoisture = soilMoisture;
        this.temperature = temperature;
        this.waterLevel = waterLevel;
        this.waterConsumption = waterConsumption;
    }
}





