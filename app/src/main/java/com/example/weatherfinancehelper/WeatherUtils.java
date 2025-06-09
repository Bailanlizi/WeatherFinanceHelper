package com.example.weatherfinancehelper;

import android.content.Context;
import android.graphics.drawable.Drawable;
import androidx.annotation.DrawableRes;

import java.util.Locale;

public class WeatherUtils {
    // 成都市坐标常量
    public static final double CHENGDU_LAT = 30.5728;
    public static final double CHENGDU_LON = 104.0668;

    // 默认背景图名称
    private static final String DEFAULT_BG = "weather_bg_default";

    /**
     * 获取天气背景图Drawable
     */
    public static Drawable getWeatherDrawable(Context context, WeatherData data) {
        int resId = getDrawableResId(context, data.getBackgroundResourceName());
        return resId != 0 ? context.getDrawable(resId) : getDefaultDrawable(context);
    }

    /**
     * 获取温度显示文本（带℃符号）
     */
    public static String formatTemperature(double temp) {
        return String.format(Locale.CHINA, "%.1f℃", temp);
    }

    /**
     * 获取资源ID（内部使用）
     */
    @DrawableRes
    private static int getDrawableResId(Context context, String resName) {
        return context.getResources()
                .getIdentifier(resName, "drawable", context.getPackageName());
    }

    /**
     * 获取默认背景图（内部使用）
     */
    private static Drawable getDefaultDrawable(Context context) {
        int resId = getDrawableResId(context, DEFAULT_BG);
        return resId != 0 ? context.getDrawable(resId) : null;
    }
}