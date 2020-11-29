package com.example.buchverwaltung;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class LendingAdapter extends RecyclerView.Adapter<LendingAdapter.LendingViewHolder> {
    ArrayList<Lending> lendingList = new ArrayList<>();

    public LendingAdapter(ArrayList<Lending> lendingList) {
        this.lendingList = lendingList;
    }

    @NonNull
    @Override
    public LendingAdapter.LendingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_detailbookslendings, parent, false);
        LendingAdapter.LendingViewHolder vh = new LendingAdapter.LendingViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull LendingAdapter.LendingViewHolder holder, int position) {
        Lending l = lendingList.get(position);

        if(l.getIsBack() == true) {
            holder.l_checkbox.setImageResource(R.drawable.ic_baseline_check_box_45);
        } else {
            holder.l_checkbox.setImageResource(R.drawable.ic_baseline_check_box_outline_blank_45);
        }

        holder.l_lender.setText(l.getLender());
        holder.l_plannedTimeBack.setText(l.getPlanned_end().toString());
    }

    @Override
    public int getItemCount() {
        return lendingList.size();
    }

    public static class LendingViewHolder extends RecyclerView.ViewHolder{
        ImageView l_checkbox;
        TextView l_lender;
        TextView l_plannedTimeBack;

        public LendingViewHolder(@NonNull View itemView) {
            super(itemView);
            l_checkbox = (ImageView) itemView.findViewById(R.id.detailBookLendingsCheckbox);
            l_lender = (TextView) itemView.findViewById(R.id.detailBookLendingsName);
            l_plannedTimeBack = (TextView) itemView.findViewById(R.id.detailBookLendingsPlannedTime);
        }
    }
}
