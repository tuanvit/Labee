package com.example.lazabee.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.lazabee.R;
import com.example.lazabee.data.model.order.OrderResponse;
import java.text.DecimalFormat;
import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {

    private Context context;
    private List<OrderResponse> orders;
    private OnOrderClickListener listener;

    public interface OnOrderClickListener {
        void onOrderClick(OrderResponse order);
    }

    public OrderAdapter(Context context, List<OrderResponse> orders, OnOrderClickListener listener) {
        this.context = context;
        this.orders = orders;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_order, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        OrderResponse order = orders.get(position);

        holder.tvOrderId.setText("Order #" + order.getOrderId());
        holder.tvOrderDate.setText(order.getCreatedAt());
        holder.tvOrderStatus.setText(order.getStatus());

        // Status color
        if ("PENDING".equals(order.getStatus())) {
            holder.tvOrderStatus.setTextColor(context.getResources().getColor(android.R.color.holo_orange_dark));
        } else if ("CONFIRMED".equals(order.getStatus())) {
            holder.tvOrderStatus.setTextColor(context.getResources().getColor(android.R.color.holo_blue_dark));
        } else if ("SHIPPING".equals(order.getStatus())) {
            holder.tvOrderStatus.setTextColor(context.getResources().getColor(android.R.color.holo_blue_light));
        } else if ("DELIVERED".equals(order.getStatus())) {
            holder.tvOrderStatus.setTextColor(context.getResources().getColor(android.R.color.holo_green_dark));
        } else if ("CANCELLED".equals(order.getStatus())) {
            holder.tvOrderStatus.setTextColor(context.getResources().getColor(android.R.color.holo_red_dark));
        }

        DecimalFormat formatter = new DecimalFormat("#,###");
        holder.tvTotalAmount.setText(formatter.format(order.getTotalAmount()) + "đ");

        holder.tvItemCount.setText(order.getItems() != null ? order.getItems().size() + " items" : "0 items");

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onOrderClick(order);
            }
        });
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvOrderId, tvOrderDate, tvOrderStatus, tvTotalAmount, tvItemCount;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvOrderId = itemView.findViewById(R.id.tvOrderId);
            tvOrderDate = itemView.findViewById(R.id.tvOrderDate);
            tvOrderStatus = itemView.findViewById(R.id.tvOrderStatus);
            tvTotalAmount = itemView.findViewById(R.id.tvTotalAmount);
            tvItemCount = itemView.findViewById(R.id.tvItemCount);
        }
    }
}
