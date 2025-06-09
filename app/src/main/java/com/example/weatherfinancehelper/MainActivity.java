package com.example.weatherfinancehelper;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    // SharedPreferences配置
    private static final String PREFS_NAME = "BudgetPrefs";
    private static final String BUDGET_KEY = "current_budget";
    private static final String MONTH_KEY = "current_month";

    // 天气UI组件
    private TextView tvWeather, tvTemp, tvCity;
    private ProgressBar pbWeatherLoading;
    private Button btnRefresh;

    // 预算UI组件
    private EditText etBudget;
    private TextView tvRemaining;
    private ProgressBar pbBudget;
    private double currentBudget = 0;
    private double totalExpense = 0;
    private String currentMonth;

    // 财务记录
    private RecyclerView rvRecords;
    private DatabaseHelper dbHelper;
    private ExecutorService executorService;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 初始化SharedPreferences
        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        currentMonth = new SimpleDateFormat("yyyy-MM", Locale.getDefault()).format(new Date());

        // 初始化组件
        initWeatherViews();
        initBudgetViews();
        initFinanceViews();

        // 加载数据
        fetchChengduWeather();
        loadFinancialData();
    }

    private void initWeatherViews() {
        tvWeather = findViewById(R.id.tv_weather);
        tvTemp = findViewById(R.id.tv_temp);
        tvCity = findViewById(R.id.tv_city);
        pbWeatherLoading = findViewById(R.id.pb_weather_loading);
        btnRefresh = findViewById(R.id.btn_refresh);

        btnRefresh.setOnClickListener(v -> {
            tvWeather.setText("更新中...");
            pbWeatherLoading.setVisibility(View.VISIBLE);
            fetchChengduWeather();
        });
    }

    private void initBudgetViews() {
        etBudget = findViewById(R.id.et_budget);
        tvRemaining = findViewById(R.id.tv_remaining);
        pbBudget = findViewById(R.id.pb_budget);
        Button btnSetBudget = findViewById(R.id.btn_set_budget);

        // 加载保存的预算
        loadBudget();

        btnSetBudget.setOnClickListener(v -> setBudget());
    }

    private void loadBudget() {
        String savedMonth = sharedPreferences.getString(MONTH_KEY, "");
        if (currentMonth.equals(savedMonth)) {
            currentBudget = sharedPreferences.getFloat(BUDGET_KEY, 0);
            if (currentBudget > 0) {
                etBudget.setText(String.valueOf(currentBudget));
                tvRemaining.setText(String.format("剩余预算：¥%.2f", currentBudget - totalExpense));
            }
        } else {
            clearPreviousMonthBudget();
        }
    }

    private void clearPreviousMonthBudget() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(BUDGET_KEY);
        editor.apply();
        currentBudget = 0;
        etBudget.setText("");
        tvRemaining.setText("本月预算：未设置");
    }

    private void setBudget() {
        try {
            currentBudget = Double.parseDouble(etBudget.getText().toString());
            saveBudget();
            updateBudgetDisplay();
            Toast.makeText(this, "预算设置成功", Toast.LENGTH_SHORT).show();
        } catch (NumberFormatException e) {
            Toast.makeText(this, "请输入有效数字", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveBudget() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat(BUDGET_KEY, (float) currentBudget);
        editor.putString(MONTH_KEY, currentMonth);
        editor.apply();
    }

    private void updateBudgetDisplay() {
        if (currentBudget > 0) {
            double remaining = currentBudget - totalExpense;
            String status = remaining >= 0 ?
                    String.format("剩余预算：¥%.2f", remaining) :
                    String.format("超支：¥%.2f", Math.abs(remaining));

            int progress = (int) ((totalExpense / currentBudget) * 100);
            pbBudget.setProgress(Math.min(progress, 100));
            tvRemaining.setText(status);
        }
    }

    private void initFinanceViews() {
        rvRecords = findViewById(R.id.rv_records);
        rvRecords.setLayoutManager(new LinearLayoutManager(this));

        Button btnIncome = findViewById(R.id.btn_income);
        Button btnExpense = findViewById(R.id.btn_expense);

        btnIncome.setOnClickListener(v -> showInputDialog(true));
        btnExpense.setOnClickListener(v -> showInputDialog(false));

        executorService = Executors.newSingleThreadExecutor();
        dbHelper = new DatabaseHelper(this);
    }

    private void fetchChengduWeather() {
        String url = "https://api.openweathermap.org/data/2.5/weather?" +
                "q=Chengdu&units=metric&lang=zh_cn&appid=" + BuildConfig.WEATHER_API_KEY;

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET, url, null,
                response -> {
                    try {
                        String weather = response.getJSONArray("weather")
                                .getJSONObject(0)
                                .getString("main");
                        double temp = response.getJSONObject("main")
                                .getDouble("temp");

                        runOnUiThread(() -> {
                            tvWeather.setText(weather);
                            tvTemp.setText(String.format(Locale.CHINA, "%.1f℃", temp));
                            tvCity.setText("成都市");
                            pbWeatherLoading.setVisibility(View.GONE);
                        });
                    } catch (JSONException e) {
                        showWeatherError("数据解析失败");
                    }
                },
                error -> showWeatherError("网络请求失败")
        );

        Volley.newRequestQueue(this).add(request);
    }

    private void showWeatherError(String message) {
        runOnUiThread(() -> {
            tvWeather.setText(message);
            pbWeatherLoading.setVisibility(View.GONE);
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        });
    }

    private void loadFinancialData() {
        executorService.execute(() -> {
            List<FinanceRecord> records = dbHelper.getAllRecords();
            totalExpense = calculateTotalExpense(records);

            runOnUiThread(() -> {
                rvRecords.setAdapter(new FinanceRecordAdapter(records));
                updateBudgetDisplay();
            });
        });
    }

    private double calculateTotalExpense(List<FinanceRecord> records) {
        double total = 0;
        for (FinanceRecord record : records) {
            if (record.getAmount() < 0) {
                total += Math.abs(record.getAmount());
            }
        }
        return total;
    }

    private void showInputDialog(boolean isIncome) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(isIncome ? "添加收入" : "添加支出");

        View view = LayoutInflater.from(this).inflate(R.layout.dialog_input, null);
        EditText etAmount = view.findViewById(R.id.et_amount);
        Spinner spCategory = view.findViewById(R.id.sp_category);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                isIncome ? R.array.income_tags : R.array.expense_tags,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCategory.setAdapter(adapter);

        builder.setView(view)
                .setPositiveButton("保存", (dialog, which) -> {
                    try {
                        double amount = Double.parseDouble(etAmount.getText().toString());
                        String category = spCategory.getSelectedItem().toString();
                        saveRecord(isIncome, amount, category);
                    } catch (NumberFormatException e) {
                        Toast.makeText(this, "请输入有效金额", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("取消", null)
                .show();
    }

    private void saveRecord(boolean isIncome, double amount, String category) {
        executorService.execute(() -> {
            String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    .format(new Date());
            double signedAmount = isIncome ? amount : -amount;

            dbHelper.addRecord(new FinanceRecord(category, signedAmount, date));
            loadFinancialData();
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executorService.shutdown();
        dbHelper.close();
    }
}