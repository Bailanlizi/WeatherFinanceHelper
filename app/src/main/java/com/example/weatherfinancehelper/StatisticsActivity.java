package com.example.weatherfinancehelper;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import java.util.ArrayList;
import java.util.List;

public class StatisticsActivity extends AppCompatActivity {
    private PieChart pieChart;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        dbHelper = new DatabaseHelper(this);
        pieChart = findViewById(R.id.pie_chart);
        setupPieChart();
    }

    private void setupPieChart() {
        List<FinanceRecord> records = dbHelper.getAllRecords();
        float income = 0, expense = 0;

        for (FinanceRecord record : records) {
            if (record.getAmount() > 0) income += record.getAmount();
            else expense += Math.abs(record.getAmount());
        }

        List<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(income, "收入"));
        entries.add(new PieEntry(expense, "支出"));

        PieDataSet dataSet = new PieDataSet(entries, "收支统计");
        dataSet.setColors(new int[]{0xFF4CAF50, 0xFFF44336}); // 绿/红

        pieChart.setData(new PieData(dataSet));
        pieChart.getDescription().setEnabled(false);
        pieChart.animateY(1000);
    }
}