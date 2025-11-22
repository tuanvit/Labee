package com.example.lazabee.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.lazabee.R;
import com.example.lazabee.model.Order;
import java.text.DecimalFormat;
import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {

    private Context context;
    private List<Order> orders;
    private OnOrderClickListener listener;

    public interface OnOrderClickListener {
        void onOrderClick(Order order);
    }

    public OrderAdapter(Context context, List<Order> orders, OnOrderClickListener listener) {
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
        Order order = orders.get(position);

        holder.tvOrderId.setText("Order #" + order.id);
        holder.tvOrderDate.setText(order.date);
        holder.tvOrderStatus.setText(order.status);

        // Status color
        if ("Pending".equals(order.status)) {
            holder.tvOrderStatus.setTextColor(context.getResources().getColor(android.R.color.holo_orange_dark));
        } else if ("Completed".equals(order.status)) {
            holder.tvOrderStatus.setTextColor(context.getResources().getColor(android.R.color.holo_green_dark));
        }

        DecimalFormat formatter = new DecimalFormat("#,###");
        holder.tvTotalAmount.setText(formatter.format(order.totalPrice) + "đ");

        // holder.tvItemCount.setText(order.getItems() != null ? order.getItems().size() + " items" : "0 items");
        holder.tvItemCount.setVisibility(View.GONE); // Hide item count for now as we don't join tables here

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
