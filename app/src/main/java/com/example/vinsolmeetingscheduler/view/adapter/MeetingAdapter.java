package com.example.vinsolmeetingscheduler.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.vinsolmeetingscheduler.R;
import com.example.vinsolmeetingscheduler.service.model.ScheduleMeetingResponse;

import java.util.List;

public class MeetingAdapter extends RecyclerView.Adapter<MeetingAdapter.ViewHolder> {

    private List<ScheduleMeetingResponse> items;
    private Context context;


    public MeetingAdapter(List<ScheduleMeetingResponse> items, Context context) {
        this.items = items;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_layout_schedule_portrait, parent, false);
        return new ViewHolder(v);
    }

    public void remove(ScheduleMeetingResponse item) {
        int position = items.indexOf(item);
        items.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final ScheduleMeetingResponse item = items.get(position);
        holder.tvStartDateEndTime.setText(item.getStartTime() +"-"+ item.getEndTime());
        holder.tvDescription.setText(item.getDescription());
        holder.itemView.setTag(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvStartDateEndTime;
        public TextView tvDescription;

        public ViewHolder(View itemView) {
            super(itemView);
            tvStartDateEndTime = (TextView) itemView.findViewById(R.id.start_end_time);
            tvDescription = (TextView) itemView.findViewById(R.id.meeting_description);
        }
    }
}