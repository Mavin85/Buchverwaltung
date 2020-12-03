package com.example.buchverwaltung;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class LendingAdapter extends RecyclerView.Adapter<LendingAdapter.LendingViewHolder> {
    List<Lending> lendingList;
    Context con;

    public LendingAdapter(List<Lending> lendingList, Context con) {
        this.lendingList = lendingList;
        this.con = con;
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

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(con, DetailActivityLending.class);
                i.putExtra("lendingId", l.getId());
                con.startActivity(i);
            }
        });


        if(l.getIsBack()) {
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
