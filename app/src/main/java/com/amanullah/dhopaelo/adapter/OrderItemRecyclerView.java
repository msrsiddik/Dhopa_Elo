package com.amanullah.dhopaelo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import com.amanullah.dhopaelo.R;
import com.amanullah.dhopaelo.domain.view.OrderViewItemModel;

public class OrderItemRecyclerView extends RecyclerView.Adapter<OrderItemRecyclerView.ViewHolder> {
    private final List<OrderViewItemModel> list;
    private final Context context;
    private final OrderItemClickListener clickListener;

    public OrderItemRecyclerView(List<OrderViewItemModel> list, Context context, OrderItemClickListener clickListener) {
        this.list = list;
        this.context = context;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.order_service_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.imageView.setImageResource(list.get(position).getImage());
        holder.title.setText(list.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private CardView orderItemCardView;
        private ImageView imageView;
        private TextView title;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.orderItemImage);
            title = itemView.findViewById(R.id.orderItemName);

            orderItemCardView = itemView.findViewById(R.id.orderItemCardView);
            orderItemCardView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            clickListener.onItemClick(getAdapterPosition());
        }
    }

    public interface OrderItemClickListener{
        void onItemClick(int position);
    }
}