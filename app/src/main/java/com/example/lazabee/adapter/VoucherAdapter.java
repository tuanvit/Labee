package com.example.lazabee.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lazabee.R;
import com.example.lazabee.model.Voucher;

import java.util.List;

public class VoucherAdapter extends RecyclerView.Adapter<VoucherAdapter.VoucherViewHolder> {

    private Context context;
    private List<Voucher> voucherList;

    public VoucherAdapter(Context context, List<Voucher> voucherList) {
        this.context = context;
        this.voucherList = voucherList;
    }

    @NonNull
    @Override
    public VoucherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_voucher, parent, false);
        return new VoucherViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VoucherViewHolder holder, int position) {
        Voucher voucher = voucherList.get(position);
        holder.tvTitle.setText(voucher.title);
        holder.tvDesc.setText("HSD: " + voucher.expiryDate);
        holder.tvQuantity.setText("Số lượng: " + voucher.quantity);

        if (voucher.isCollected) {
            holder.btnCollect.setText("Đã lưu");
            holder.btnCollect.setEnabled(false);
            holder.btnCollect.setBackgroundColor(context.getResources().getColor(android.R.color.darker_gray));
        } else {
            holder.btnCollect.setText("Lưu");
            holder.btnCollect.setEnabled(true);
            holder.btnCollect.setOnClickListener(v -> {
                if (voucher.quantity > 0) {
                    voucher.isCollected = true;
                    voucher.quantity--;
                    notifyItemChanged(position);
                    Toast.makeText(context, "Đã lưu mã giảm giá!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Mã giảm giá đã hết!", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return voucherList.size();
    }

    public static class VoucherViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvDesc, tvQuantity;
        Button btnCollect;

        public VoucherViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvVoucherTitle);
            tvDesc = itemView.findViewById(R.id.tvVoucherDesc);
            tvQuantity = itemView.findViewById(R.id.tvVoucherQuantity);
            btnCollect = itemView.findViewById(R.id.btnCollect);
        }
    }
}
