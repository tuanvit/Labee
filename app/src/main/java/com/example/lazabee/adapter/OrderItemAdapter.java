package com.example.lazabee.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.lazabee.R;
import com.example.lazabee.model.OrderItemDetail;
import java.text.DecimalFormat;
import java.util.List;

public class OrderItemAdapter extends RecyclerView.Adapter<OrderItemAdapter.ViewHolder> {

    private Context context;
    private List<OrderItemDetail> items;

    public OrderItemAdapter(Context context, List<OrderItemDetail> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_order_product, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        OrderItemDetail item = items.get(position);

        holder.tvProductName.setText(item.productName);
        holder.tvQuantity.setText("x" + item.quantity);

        DecimalFormat formatter = new DecimalFormat("#,###");
        holder.tvPrice.setText(formatter.format(item.price) + "đ");
        holder.tvSubtotal.setText(formatter.format((long) item.price * item.quantity) + "đ");

        // Load image from resources
        if (item.productImage != null) {
            int resId = context.getResources().getIdentifier(item.productImage, "drawable", context.getPackageName());
            if (resId != 0) {
                holder.ivProductImage.setImageResource(resId);
            } else {
                holder.ivProductImage.setImageResource(R.drawable.ic_launcher_foreground);
            }
        } else {
            holder.ivProductImage.setImageResource(R.drawable.ic_launcher_foreground);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivProductImage;
        TextView tvProductName, tvQuantity, tvPrice, tvSubtotal;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProductImage = itemView.findViewById(R.id.ivProductImage);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvSubtotal = itemView.findViewById(R.id.tvSubtotal);
        }
    }
}
