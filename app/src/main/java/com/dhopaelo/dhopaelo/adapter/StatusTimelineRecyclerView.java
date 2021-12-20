package com.dhopaelo.dhopaelo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.dhopaelo.dhopaelo.R;
import com.dhopaelo.dhopaelo.databinding.TrackingTimelineItemBinding;
import com.dhopaelo.dhopaelo.domain.StatusTimelineDetailsModel;

import java.util.List;

public class StatusTimelineRecyclerView extends RecyclerView.Adapter<StatusTimelineRecyclerView.ViewHolder> {
    private List<StatusTimelineDetailsModel> list;
    private Context context;

    public StatusTimelineRecyclerView(List<StatusTimelineDetailsModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        TrackingTimelineItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.tracking_timeline_item, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (position == 0){
            holder.binding.topDivider.setBackgroundResource(R.color.color5);
        }
        if (position == list.size()-1) {
            holder.binding.bottomDivider.setBackgroundResource(R.color.color5);
        }
        StatusTimelineDetailsModel detailsModel = list.get(position);

        if (position == 0) {
            if (detailsModel.isStep1Complete()) {
                holder.binding.leftNumberAndTick.setText("✔");
                holder.binding.leftNumberAndTick.setBackground(context.getDrawable(R.drawable.round_boarder_timeline_1));
            } else {
                holder.binding.leftNumberAndTick.setText("1");
            }
        }
        if (position == 1) {
            if (detailsModel.isStep2Complete()) {
                holder.binding.leftNumberAndTick.setText("✔");
                holder.binding.leftNumberAndTick.setBackground(context.getDrawable(R.drawable.round_boarder_timeline_1));
            } else {
                holder.binding.leftNumberAndTick.setText("2");
            }
        }
        if (position == 2) {
            if (detailsModel.isStep3Complete()) {
                holder.binding.leftNumberAndTick.setText("✔");
                holder.binding.leftNumberAndTick.setBackground(context.getDrawable(R.drawable.round_boarder_timeline_1));
            } else {
                holder.binding.leftNumberAndTick.setText("3");
            }
        }
        if (position == 3) {
            if (detailsModel.isStep4Complete()) {
                holder.binding.leftNumberAndTick.setText("✔");
                holder.binding.leftNumberAndTick.setBackground(context.getDrawable(R.drawable.round_boarder_timeline_1));
            } else {
                holder.binding.leftNumberAndTick.setText("4");
            }
        }
        if (position == 4) {
            if (detailsModel.isStep5Complete()) {
                holder.binding.leftNumberAndTick.setText("✔");
                holder.binding.leftNumberAndTick.setBackground(context.getDrawable(R.drawable.round_boarder_timeline_1));
            } else {
                holder.binding.leftNumberAndTick.setText("5");
            }
        }

        holder.binding.title.setText(detailsModel.getTitle());
        holder.binding.message.setText(detailsModel.getMessage());
        holder.binding.time.setText(detailsModel.getEstimateTime());
        holder.binding.image.setImageResource(detailsModel.getImage());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TrackingTimelineItemBinding binding;
        public ViewHolder(@NonNull TrackingTimelineItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
