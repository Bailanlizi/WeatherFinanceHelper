package com.example.weatherfinancehelper;

public class FinanceRecord {
    private final String tag;
    private final double amount;
    private final String date;

    public FinanceRecord(String tag, double amount, String date) {
        this.tag = tag;
        this.amount = amount;
        this.date = date;
    }

    // Getter方法（必须添加）
    public String getTag() {
        return tag;
    }

    public double getAmount() {
        return amount;
    }

    public String getDate() {
        return date;
    }

    // 可选：添加toString()方便调试
    @Override
    public String toString() {
        return "FinanceRecord{" +
                "tag='" + tag + '\'' +
                ", amount=" + amount +
                ", date='" + date + '\'' +
                '}';
    }
}