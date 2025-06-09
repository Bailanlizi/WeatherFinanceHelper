package com.example.weatherfinancehelper;

import java.util.Locale;

public class WeatherData {
    private final String weatherType; // 原始天气类型（如"Rain"）
    private final double temperature; // 温度（摄氏度）
    private final String city;       // 城市名（固定为"成都市"）
    private final String description; // 中文天气描述

    public WeatherData(String weatherType, double temperature, String city) {
        this.weatherType = weatherType;
        this.temperature = temperature;
        this.city = city;
        this.description = convertToChineseDescription(weatherType);
    }

    /**
     * 将OpenWeatherMap的天气类型转换为中文描述
     */
    private String convertToChineseDescription(String type) {
        switch (type.toLowerCase(Locale.ROOT)) {

            case "rain": return "下雨";
            case "clear": return "晴天";
            case "clouds": return "多云";
            default: return type;
        }
    }

    /**
     * 获取对应的背景图资源名称（匹配res/drawable中的文件名）
     */
    public String getBackgroundResourceName() {
        switch (weatherType.toLowerCase(Locale.ROOT)) {
            case "rain": return "weather_bg_rain";
            case "clear": return "weather_bg_sunny";
            case "clouds": return "weather_bg_cloudy";
            default: return "weather_bg_default";
        }
    }

    // Getter方法
    public String getWeatherType() { return weatherType; }
    public double getTemperature() { return temperature; }
    public String getCity() { return city; }
    public String getDescription() { return description; }
}