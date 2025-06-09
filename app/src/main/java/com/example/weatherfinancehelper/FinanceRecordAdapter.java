package com.example.weatherfinancehelper;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class FinanceRecordAdapter extends RecyclerView.Adapter<FinanceRecordAdapter.ViewHolder> {
    private final List<FinanceRecord> records;

    public FinanceRecordAdapter(List<FinanceRecord> records) {
        this.records = records;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_record, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FinanceRecord record = records.get(position);

        if (holder.tvTag != null) {
            holder.tvTag.setText(record.getTag());
        }

        if (holder.tvAmount != null) {
            // 根据金额正负设置颜色和显示格式
            if (record.getAmount() >= 0) {
                // 收入：绿色显示，带+号
                holder.tvAmount.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.income_green));
                holder.tvAmount.setText(String.format("+¥%.2f", record.getAmount()));
            } else {
                // 支出：红色显示，带-号
                holder.tvAmount.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.expense_red));
                holder.tvAmount.setText(String.format("-¥%.2f", Math.abs(record.getAmount())));
            }
        }

        if (holder.tvDate != null) {
            holder.tvDate.setText(record.getDate());
        }
    }

    @Override
    public int getItemCount() {
        return records != null ? records.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView tvTag, tvAmount, tvDate;

        public ViewHolder(View itemView) {
            super(itemView);
            try {
                tvTag = itemView.findViewById(R.id.tv_tag);
                tvAmount = itemView.findViewById(R.id.tv_amount);
                tvDate = itemView.findViewById(R.id.tv_date);

                if (tvTag == null || tvAmount == null || tvDate == null) {
                    throw new IllegalStateException("Missing TextView in layout");
                }
            } catch (Exception e) {
                throw new RuntimeException("ViewHolder initialization failed", e);
            }
        }
    }

    public void updateData(List<FinanceRecord> newRecords) {
        this.records.clear();
        this.records.addAll(newRecords);
        notifyDataSetChanged();
    }
}