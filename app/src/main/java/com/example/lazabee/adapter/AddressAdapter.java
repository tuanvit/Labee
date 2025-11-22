package com.example.lazabee.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lazabee.R;
import com.example.lazabee.model.Address;

import java.util.List;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.AddressViewHolder> {

    private Context context;
    private List<Address> addressList;
    private OnAddressActionListener listener;

    public interface OnAddressActionListener {
        void onSetDefault(Address address);

        void onEdit(Address address);

        void onDelete(Address address);
    }

    public AddressAdapter(Context context, List<Address> addressList, OnAddressActionListener listener) {
        this.context = context;
        this.addressList = addressList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public AddressViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_address, parent, false);
        return new AddressViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AddressViewHolder holder, int position) {
        Address address = addressList.get(position);

        holder.tvRecipientName.setText(address.name);
        holder.tvPhone.setText(address.phone);

        // Build full address
        holder.tvAddress.setText(address.address);

        // Show/hide default badge
        if (address.isDefault) {
            holder.tvDefaultBadge.setVisibility(View.VISIBLE);
            holder.btnSetDefault.setVisibility(View.GONE);
        } else {
            holder.tvDefaultBadge.setVisibility(View.GONE);
            holder.btnSetDefault.setVisibility(View.VISIBLE);
        } // Action buttons
        holder.btnSetDefault.setOnClickListener(v -> {
            if (listener != null)
                listener.onSetDefault(address);
        });

        holder.btnEdit.setOnClickListener(v -> {
            if (listener != null)
                listener.onEdit(address);
        });

        holder.btnDelete.setOnClickListener(v -> {
            if (listener != null)
                listener.onDelete(address);
        });
    }

    @Override
    public int getItemCount() {
        return addressList.size();
    }

    public void updateAddresses(List<Address> newAddresses) {
        this.addressList = newAddresses;
        notifyDataSetChanged();
    }

    static class AddressViewHolder extends RecyclerView.ViewHolder {
        TextView tvRecipientName, tvPhone, tvAddress, tvDefaultBadge;
        TextView btnSetDefault, btnEdit, btnDelete;

        public AddressViewHolder(@NonNull View itemView) {
            super(itemView);
            tvRecipientName = itemView.findViewById(R.id.tvRecipientName);
            tvPhone = itemView.findViewById(R.id.tvPhone);
            tvAddress = itemView.findViewById(R.id.tvAddress);
            tvDefaultBadge = itemView.findViewById(R.id.tvDefaultBadge);
            btnSetDefault = itemView.findViewById(R.id.btnSetDefault);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
