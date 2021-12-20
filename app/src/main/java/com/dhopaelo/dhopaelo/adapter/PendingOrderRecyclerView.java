package com.dhopaelo.dhopaelo.adapter;

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
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.dhopaelo.dhopaelo.R;
import com.dhopaelo.dhopaelo.databinding.StatusItemBinding;
import com.dhopaelo.dhopaelo.domain.view.InvoiceItemModel;

public class PendingOrderRecyclerView extends RecyclerView.Adapter<PendingOrderRecyclerView.ViewHolder> {
    private final Context context;
    private final List<Map<String, InvoiceItemModel>> list;
    private final List<Long> timestamp;
    private final PendingItemClickListener clickListener;
    private final List<String> serviceName;

    private DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    public PendingOrderRecyclerView(Context context, List<Map<String, InvoiceItemModel>> list, List<Long> timestamp, List<String> serviceName, PendingItemClickListener clickListener) {
        this.context = context;
        this.list = list;
        this.timestamp = timestamp;
        this.clickListener = clickListener;
        this.serviceName = serviceName;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.status_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Map<String, InvoiceItemModel> invoiceItemModelMap = list.get(position);

        double totalPrice = 0;
        for (Map.Entry<String, InvoiceItemModel> itemModelEntry : invoiceItemModelMap.entrySet()) {
            totalPrice+=itemModelEntry.getValue().gettPrice();
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

    public void removedItem(int l) {
        list.remove(l);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        StatusItemBinding binding;

        public ViewHolder(@NonNull StatusItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.getRoot().setOnClickListener(this);
            binding.getRoot().setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            clickListener.oneClickListenerItemPosition(getAdapterPosition());
        }

        @Override
        public boolean onLongClick(View v) {
            clickListener.longClickListenerItemPosition(getAdapterPosition());
            return true;
        }
    }

    public interface PendingItemClickListener {
        void oneClickListenerItemPosition(int i);
        void longClickListenerItemPosition(int l);
    }
}
