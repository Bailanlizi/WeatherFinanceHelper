package com.example.weatherfinancehelper;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ImageView ivWeatherBg;
    private TextView tvWeather, tvTemp;
    private ProgressBar pbBudget;
    private RecyclerView rvRecords;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 初始化视图
        ivWeatherBg = findViewById(R.id.iv_weather_bg);
        tvWeather = findViewById(R.id.tv_weather);
        tvTemp = findViewById(R.id.tv_temp);
        pbBudget = findViewById(R.id.pb_budget);
        rvRecords = findViewById(R.id.rv_records);

        // 设置RecyclerView
        rvRecords.setLayoutManager(new LinearLayoutManager(this));
        rvRecords.setAdapter(new FinanceRecordAdapter(getDummyData()));

        // 模拟从网络获取天气数据
        fetchWeatherData();
    }

    private void fetchWeatherData() {
        // 这里替换为真实的API请求
        String weatherType = "sunny"; // 模拟晴天
        int temp = 26; // 模拟温度

        // 更新UI
        tvWeather.setText(weatherType);
        tvTemp.setText(temp + "°C");

        // 动态设置背景图
        int resId = getResources().getIdentifier(
                "weather_bg_" + weatherType,
                "drawable",
                getPackageName()
        );
        ivWeatherBg.setImageResource(resId);
    }

    private List<FinanceRecord> getDummyData() {
        // 返回测试数据
        return Arrays.asList(
                new FinanceRecord("餐饮", -50.0, "2023-11-01"),
                new FinanceRecord("工资", 5000.0, "2023-11-01")
        );
    }
}