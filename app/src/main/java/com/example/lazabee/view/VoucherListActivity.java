package com.example.lazabee.view;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lazabee.R;
import com.example.lazabee.adapter.VoucherAdapter;
import com.example.lazabee.model.Voucher;

import java.util.ArrayList;
import java.util.List;

public class VoucherListActivity extends AppCompatActivity {

    private RecyclerView rvVouchers;
    private VoucherAdapter voucherAdapter;
    private List<Voucher> voucherList;
    private ImageView btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voucher_list);

        rvVouchers = findViewById(R.id.rvVouchers);
        btnBack = findViewById(R.id.btnBack);

        btnBack.setOnClickListener(v -> finish());

        setupRecyclerView();
        loadVouchers();
    }

    private void setupRecyclerView() {
        voucherList = new ArrayList<>();
        voucherAdapter = new VoucherAdapter(this, voucherList);
        rvVouchers.setLayoutManager(new LinearLayoutManager(this));
        rvVouchers.setAdapter(voucherAdapter);
    }

    private void loadVouchers() {
        // Dummy data
        voucherList.add(
                new Voucher("1", "FREESHIP", "Miễn phí vận chuyển", "Cho đơn hàng từ 0đ", 15000, 100, "31/12/2025"));
        voucherList.add(new Voucher("2", "GIAM50K", "Giảm 50k", "Cho đơn hàng từ 200k", 50000, 50, "31/12/2025"));
        voucherList.add(new Voucher("3", "GIAM100K", "Giảm 100k", "Cho đơn hàng từ 500k", 100000, 20, "31/12/2025"));
        voucherList.add(new Voucher("4", "LABEE20", "Giảm 20%", "Tối đa 50k", 50000, 200, "31/12/2025"));
        voucherList.add(new Voucher("5", "XMAS2025", "Giảm 30k", "Mừng giáng sinh", 30000, 1000, "25/12/2025"));

        voucherAdapter.notifyDataSetChanged();
    }
}
