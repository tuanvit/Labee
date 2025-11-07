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
import com.example.lazabee.data.model.Product;
import java.text.DecimalFormat;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private Context context;
    private List<Product> productList;
    private OnProductClickListener listener;

    public interface OnProductClickListener {
        void onProductClick(Product product);
    }

    public ProductAdapter(Context context, List<Product> productList, OnProductClickListener listener) {
        this.context = context;
        this.productList = productList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);

        holder.tvProductName.setText(product.getName());

        // Format price
        DecimalFormat formatter = new DecimalFormat("#,###");
        holder.tvPrice.setText(formatter.format(product.getPrice()) + "đ");

        // Show original price if different
        if (product.getOriginalPrice() > product.getPrice()) {
            holder.tvOriginalPrice.setVisibility(View.VISIBLE);
            holder.tvOriginalPrice.setText(formatter.format(product.getOriginalPrice()) + "đ");
        } else {
            holder.tvOriginalPrice.setVisibility(View.GONE);
        }

        // Rating
        if (product.getRating() > 0) {
            holder.tvRating.setVisibility(View.VISIBLE);
            holder.tvRating.setText(String.format("%.1f ⭐", product.getRating()));
        } else {
            holder.tvRating.setVisibility(View.GONE);
        }

        // Load image with Glide
        if (product.getImageUrl() != null && !product.getImageUrl().isEmpty()) {
            Glide.with(context)
                    .load(product.getImageUrl())
                    .placeholder(R.drawable.img_shoes)
                    .error(R.drawable.img_shoes)
                    .centerCrop()
                    .into(holder.ivProductImage);
        } else {
            holder.ivProductImage.setImageResource(R.drawable.img_shoes);
        }

        // Click listener
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onProductClick(product);
            }
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    static class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView ivProductImage;
        TextView tvProductName;
        TextView tvPrice;
        TextView tvOriginalPrice;
        TextView tvRating;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProductImage = itemView.findViewById(R.id.ivProductImage);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvOriginalPrice = itemView.findViewById(R.id.tvOriginalPrice);
            tvRating = itemView.findViewById(R.id.tvRating);
        }
    }
}
