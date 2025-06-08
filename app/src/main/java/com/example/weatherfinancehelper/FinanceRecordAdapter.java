package com.example.weatherfinancehelper;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class FinanceRecordAdapter extends RecyclerView.Adapter<FinanceRecordAdapter.ViewHolder> {
    private List<FinanceRecord> records;

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
        holder.tvTag.setText(record.getTag());
        holder.tvAmount.setText(String.format("%.2f", record.getAmount()));
        holder.tvDate.setText(record.getDate());
    }

    @Override
    public int getItemCount() {
        return records.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTag, tvAmount, tvDate;

        public ViewHolder(View itemView) {
            super(itemView);
            tvTag = itemView.findViewById(R.id.tv_tag);
            tvAmount = itemView.findViewById(R.id.tv_amount);
            tvDate = itemView.findViewById(R.id.tv_date);
        }
    }
}