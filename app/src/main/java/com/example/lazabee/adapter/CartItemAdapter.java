package com.example.lazabee.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lazabee.R;
import com.example.lazabee.model.CartItemDetail;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class CartItemAdapter extends RecyclerView.Adapter<CartItemAdapter.CartItemViewHolder> {

    private List<CartItemDetail> cartItems;
    private OnCartItemListener listener;

    public interface OnCartItemListener {
        void onQuantityChanged(CartItemDetail cartItem, int newQuantity);

        void onDeleteItem(CartItemDetail cartItem);
    }

    public CartItemAdapter(OnCartItemListener listener) {
        this.cartItems = new ArrayList<>();
        this.listener = listener;
    }

    public void setCartItems(List<CartItemDetail> cartItems) {
        this.cartItems = cartItems != null ? cartItems : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CartItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_cart, parent, false);
        return new CartItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartItemViewHolder holder, int position) {
        CartItemDetail cartItem = cartItems.get(position);
        holder.bind(cartItem);
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    class CartItemViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivProductImage;
        private TextView tvProductName;
        private TextView tvProductPrice;
        private TextView tvQuantity;
        private TextView tvSubtotal;
        private ImageView btnDecrease;
        private ImageView btnIncrease;
        private ImageView btnDelete;

        public CartItemViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProductImage = itemView.findViewById(R.id.ivProductImage);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvProductPrice = itemView.findViewById(R.id.tvProductPrice);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            tvSubtotal = itemView.findViewById(R.id.tvSubtotal);
            btnDecrease = itemView.findViewById(R.id.btnDecrease);
            btnIncrease = itemView.findViewById(R.id.btnIncrease);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }

        public void bind(CartItemDetail cartItem) {
            // Set product name
            tvProductName.setText(cartItem.productName);

            // Format and display price
            DecimalFormat formatter = new DecimalFormat("#,###");
            tvProductPrice.setText(formatter.format(cartItem.price) + "đ");

            // Set quantity
            tvQuantity.setText(String.valueOf(cartItem.quantity));

            // Calculate and display subtotal
            double subtotal = cartItem.price * cartItem.quantity;
            tvSubtotal.setText(formatter.format(subtotal) + "đ");

            // Load product image
            int resId = 0;
            if (cartItem.imageResName != null) {
                resId = itemView.getContext().getResources().getIdentifier(cartItem.imageResName, "drawable",
                        itemView.getContext().getPackageName());
            }

            if (resId != 0) {
                ivProductImage.setImageResource(resId);
            } else {
                ivProductImage.setImageResource(R.drawable.ic_launcher_foreground);
            }

            // Decrease quantity button
            btnDecrease.setOnClickListener(v -> {
                int currentQuantity = cartItem.quantity;
                if (currentQuantity > 1) {
                    int newQuantity = currentQuantity - 1;
                    if (listener != null) {
                        listener.onQuantityChanged(cartItem, newQuantity);
                    }
                }
            });

            // Increase quantity button
            btnIncrease.setOnClickListener(v -> {
                int currentQuantity = cartItem.quantity;
                int newQuantity = currentQuantity + 1;
                if (listener != null) {
                    listener.onQuantityChanged(cartItem, newQuantity);
                }
            });

            // Delete button
            btnDelete.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onDeleteItem(cartItem);
                }
            });
        }
    }
}
