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
            case "thunderstorm": return "雷雨";
            case "drizzle": return "毛毛雨";
            case "rain": return "下雨";
            case "snow": return "下雪";
            case "clear": return "晴天";
            case "clouds": return "多云";
            case "mist": return "薄雾";
            case "smoke": return "雾霾";
            case "haze": return "雾霾";
            case "dust": return "浮尘";
            case "fog": return "浓雾";
            case "sand": return "沙尘";
            case "ash": return "火山灰";
            case "squall": return "狂风";
            case "tornado": return "龙卷风";
            default: return type;
        }
    }

    /**
     * 获取对应的背景图资源名称（匹配res/drawable中的文件名）
     */
    public String getBackgroundResourceName() {
        switch (weatherType.toLowerCase(Locale.ROOT)) {
            case "rain": return "weather_bg_heavy_rain";
            case "snow": return "weather_bg_heavy_snow";
            case "clear": return "weather_bg_sunny";
            case "clouds": return "weather_bg_cloudy";
            case "thunderstorm": return "weather_bg_storm";
            case "fog": case "mist": return "weather_bg_fog";
            case "haze": case "smoke": return "weather_bg_haze";
            case "dust": case "sand": return "weather_bg_dust_in_suspension";
            default: return "weather_bg_default";
        }
    }

    // Getter方法
    public String getWeatherType() { return weatherType; }
    public double getTemperature() { return temperature; }
    public String getCity() { return city; }
    public String getDescription() { return description; }
}