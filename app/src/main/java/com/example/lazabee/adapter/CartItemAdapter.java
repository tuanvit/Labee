package com.example.lazabee.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.lazabee.R;
import com.example.lazabee.data.model.CartItem;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class CartItemAdapter extends RecyclerView.Adapter<CartItemAdapter.CartItemViewHolder> {

    private List<CartItem> cartItems;
    private OnCartItemListener listener;

    public interface OnCartItemListener {
        void onQuantityChanged(CartItem cartItem, int newQuantity);

        void onDeleteItem(CartItem cartItem);
    }

    public CartItemAdapter(OnCartItemListener listener) {
        this.cartItems = new ArrayList<>();
        this.listener = listener;
    }

    public void setCartItems(List<CartItem> cartItems) {
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
        CartItem cartItem = cartItems.get(position);
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

        public void bind(CartItem cartItem) {
            // Set product name
            tvProductName.setText(cartItem.getProductName());

            // Format and display price
            DecimalFormat formatter = new DecimalFormat("#,###");
            tvProductPrice.setText(formatter.format(cartItem.getPrice()) + "đ");

            // Set quantity
            tvQuantity.setText(String.valueOf(cartItem.getQuantity()));

            // Calculate and display subtotal
            double subtotal = cartItem.getPrice() * cartItem.getQuantity();
            tvSubtotal.setText(formatter.format(subtotal) + "đ");

            // Load product image
            if (cartItem.getProductImageUrl() != null && !cartItem.getProductImageUrl().isEmpty()) {
                String imageUrl = cartItem.getProductImageUrl();
                if (!imageUrl.startsWith("http")) {
                    imageUrl = "http://localhost:8080" + imageUrl;
                }

                Glide.with(itemView.getContext())
                        .load(imageUrl)
                        .placeholder(R.drawable.placeholder_image)
                        .error(R.drawable.error_image)
                        .into(ivProductImage);
            }

            // Decrease quantity button
            btnDecrease.setOnClickListener(v -> {
                int currentQuantity = cartItem.getQuantity();
                if (currentQuantity > 1) {
                    int newQuantity = currentQuantity - 1;
                    cartItem.setQuantity(newQuantity);
                    tvQuantity.setText(String.valueOf(newQuantity));

                    // Update subtotal
                    double newSubtotal = cartItem.getPrice() * newQuantity;
                    tvSubtotal.setText(formatter.format(newSubtotal) + "đ");

                    if (listener != null) {
                        listener.onQuantityChanged(cartItem, newQuantity);
                    }
                }
            });

            // Increase quantity button
            btnIncrease.setOnClickListener(v -> {
                int currentQuantity = cartItem.getQuantity();
                int newQuantity = currentQuantity + 1;
                cartItem.setQuantity(newQuantity);
                tvQuantity.setText(String.valueOf(newQuantity));

                // Update subtotal
                double newSubtotal = cartItem.getPrice() * newQuantity;
                tvSubtotal.setText(formatter.format(newSubtotal) + "đ");

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
