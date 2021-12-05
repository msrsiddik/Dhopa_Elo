package com.robotechvalley.dhopaelo.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.robotechvalley.dhopaelo.R;
import com.robotechvalley.dhopaelo.databinding.StatusItemBinding;
import com.robotechvalley.dhopaelo.domain.view.InvoiceItemModel;

public class StatusItemRecyclerView extends RecyclerView.Adapter<StatusItemRecyclerView.ViewHolder> {
    private final Context context;
    private final List<Map<String, InvoiceItemModel>> list;
    private final StatusItemClick click;
    private final List<Long> timestamp;
    private final List<String> serviceName;

    private DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");


    public StatusItemRecyclerView(Context context, List<Map<String, InvoiceItemModel>> list, List<Long> timestamp, List<String> serviceName, StatusItemClick itemClick) {
        this.context = context;
        this.list = list;
        this.click = itemClick;
        this.timestamp = timestamp;
        this.serviceName = serviceName;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        StatusItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.status_item, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Map<String, InvoiceItemModel> invoiceItemModelMap = list.get(position);

        double totalPrice = 0;
        for (Map.Entry<String, InvoiceItemModel> itemModelEntry : invoiceItemModelMap.entrySet()) {
            totalPrice+=itemModelEntry.getValue().gettPrice();
        }

        switch (serviceName.get(position).toLowerCase()){
            case "wet wash":
                holder.binding.imageView.setImageDrawable(context.getDrawable(R.drawable.wash));
                break;
            case "wet iron":
                holder.binding.imageView.setImageDrawable(context.getDrawable(R.drawable.iron));
                break;
            case "wet wash & iron":
                holder.binding.imageView.setImageDrawable(context.getDrawable(R.drawable.wash_iron));
                break;
            case "dry wash":
                holder.binding.imageView.setImageDrawable(context.getDrawable(R.drawable.dry_wash));
                break;
            case "dry wash & iron":
                holder.binding.imageView.setImageDrawable(context.getDrawable(R.drawable.dry_wash_iron));
                break;
            default:
                holder.binding.imageView.setImageDrawable(context.getDrawable(R.drawable.laundry_icon));
                break;

        }


        holder.binding.serviceName.setText(serviceName.get(position));
        holder.binding.price.setText("💸 "+totalPrice);
        holder.binding.serviceOrderDate.setText(dateFormat.format(new Date(timestamp.get(position))));
        holder.binding.statusItem.setBackgroundColor(Color.red(5));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        StatusItemBinding binding;
        public ViewHolder(@NonNull StatusItemBinding binding) {
            super(binding.getRoot());

            this.binding = binding;

            binding.statusItem.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            click.StatusItemClickListener(getAdapterPosition());
        }
    }

    public interface StatusItemClick{
        void StatusItemClickListener(int position);
    }
}
