# 🌦️💰 WeatherFinanceHelper - 天气理财助手


一款结合实时天气查询与个人财务管理的Android应用，帮助您轻松规划日常生活。

## ✨ 主要功能

### 天气服务
- 实时获取成都市天气数据
- 显示天气状况、温度及中文描述
- 自动匹配动态天气背景
- 手动刷新天气数据

### 财务管理
- **收支记录**：
  - 添加收入/支出记录
  - 自定义分类标签
  - 自动记录交易日期
- **预算管理**：
  - 设置月度预算
  - 实时预算进度条
  - 剩余预算/超支提醒

### 数据可视化
- 彩色区分收支记录（绿色收入/红色支出）
- 天气图标直观展示
- 预算消耗比例图形化显示

## 🛠️ 技术实现

```java
// 核心代码示例
public class MainActivity extends AppCompatActivity {
    private void fetchWeather() {
        String url = "https://api.openweathermap.org/data/2.5/weather?q=Chengdu&units=metric&lang=zh_cn&appid=API_KEY";
        
        JsonObjectRequest request = new JsonObjectRequest(
            Request.Method.GET, url, null,
            response -> {
                // 解析天气数据
                String weather = response.getJSONArray("weather").getJSONObject(0).getString("main");
                double temp = response.getJSONObject("main").getDouble("temp");
                updateUI(weather, temp);
            },
            error -> showError("网络请求失败")
        );
        Volley.newRequestQueue(this).add(request);
    }
}
