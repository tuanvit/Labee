package com.example.lazabee.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.lazabee.R;
import com.example.lazabee.data.model.cart.CartItemResponse;
import java.text.DecimalFormat;
import java.util.List;

public class CheckoutItemAdapter extends RecyclerView.Adapter<CheckoutItemAdapter.ViewHolder> {

    private Context context;
    private List<CartItemResponse> items;

    public CheckoutItemAdapter(Context context, List<CartItemResponse> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_checkout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CartItemResponse item = items.get(position);

        holder.tvProductName.setText(item.getProduct().getName());
        holder.tvQuantity.setText("x" + item.getQuantity());

        DecimalFormat formatter = new DecimalFormat("#,###");
        holder.tvPrice.setText(formatter.format(item.getPrice() * item.getQuantity()) + "đ");

        if (item.getProduct().getImageUrl() != null) {
            Glide.with(context)
                    .load(item.getProduct().getImageUrl())
                    .placeholder(R.drawable.img_shoes)
                    .into(holder.ivProductImage);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivProductImage;
        TextView tvProductName, tvQuantity, tvPrice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProductImage = itemView.findViewById(R.id.ivProductImage);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            tvPrice = itemView.findViewById(R.id.tvPrice);
        }
    }
}
