package com.example.lazabee.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.lazabee.R;
import com.example.lazabee.adapter.CheckoutItemAdapter;
import com.example.lazabee.data.model.address.AddressResponse;
import com.example.lazabee.data.model.CartItem;
import com.example.lazabee.utils.Constants;
import com.example.lazabee.viewmodel.AddressViewModel;
import com.example.lazabee.viewmodel.CartViewModel;
import com.example.lazabee.viewmodel.OrderViewModel;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class CheckoutActivity extends AppCompatActivity {

    private ImageView btnBack;
    private TextView tvShippingAddress, tvAddressDetails, tvChangeAddress;
    private RecyclerView rvCheckoutItems;
    private RadioGroup rgPaymentMethod;
    private RadioButton rbCOD, rbBankTransfer, rbCreditCard, rbEWallet;
    private EditText etOrderNote;
    private TextView tvSubtotal, tvShipping, tvTotal;
    private Button btnPlaceOrder;
    private ProgressBar progressBar;

    private CartViewModel cartViewModel;
    private AddressViewModel addressViewModel;
    private OrderViewModel orderViewModel;

    private CheckoutItemAdapter checkoutAdapter;
    private List<CartItem> cartItems = new ArrayList<>();
    private AddressResponse selectedAddress;

    private double subtotal = 0;
    private double shippingFee = 30000; // Fixed 30k

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        initViews();
        initViewModels();
        setupRecyclerView();
        loadCartItems();
        loadDefaultAddress();
        setupClickListeners();
    }

    private void initViews() {
        btnBack = findViewById(R.id.btnBack);
        tvShippingAddress = findViewById(R.id.tvShippingAddress);
        tvAddressDetails = findViewById(R.id.tvAddressDetails);
        tvChangeAddress = findViewById(R.id.tvChangeAddress);
        rvCheckoutItems = findViewById(R.id.rvCheckoutItems);
        rgPaymentMethod = findViewById(R.id.rgPaymentMethod);
        rbCOD = findViewById(R.id.rbCOD);
        rbBankTransfer = findViewById(R.id.rbBankTransfer);
        rbCreditCard = findViewById(R.id.rbCreditCard);
        rbEWallet = findViewById(R.id.rbEWallet);
        etOrderNote = findViewById(R.id.etOrderNote);
        tvSubtotal = findViewById(R.id.tvSubtotal);
        tvShipping = findViewById(R.id.tvShipping);
        tvTotal = findViewById(R.id.tvTotal);
        btnPlaceOrder = findViewById(R.id.btnPlaceOrder);
        progressBar = findViewById(R.id.progressBar);

        // Set default payment method
        rbCOD.setChecked(true);
    }

    private void initViewModels() {
        cartViewModel = new ViewModelProvider(this).get(CartViewModel.class);
        addressViewModel = new ViewModelProvider(this).get(AddressViewModel.class);
        orderViewModel = new ViewModelProvider(this).get(OrderViewModel.class);
    }

    private void setupRecyclerView() {
        checkoutAdapter = new CheckoutItemAdapter(this, cartItems);
        rvCheckoutItems.setLayoutManager(new LinearLayoutManager(this));
        rvCheckoutItems.setAdapter(checkoutAdapter);
    }

    private void loadCartItems() {
        progressBar.setVisibility(View.VISIBLE);

        cartViewModel.getCartItems().observe(this, response -> {
            progressBar.setVisibility(View.GONE);

            if (response != null && response.isSuccess() && response.getData() != null) {
                cartItems.clear();
                cartItems.addAll(response.getData());
                checkoutAdapter.notifyDataSetChanged();

                // Calculate subtotal
                calculateTotal();
            } else {
                Toast.makeText(this, "Failed to load cart items", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadDefaultAddress() {
        addressViewModel.getAddresses().observe(this, response -> {
            if (response != null && response.isSuccess() && response.getData() != null) {
                List<AddressResponse> addresses = response.getData();

                // Find default address
                for (AddressResponse address : addresses) {
                    if (address.isDefault()) {
                        selectedAddress = address;
                        displayAddress(address);
                        return;
                    }
                }

                // If no default, use first address
                if (!addresses.isEmpty()) {
                    selectedAddress = addresses.get(0);
                    displayAddress(selectedAddress);
                } else {
                    tvAddressDetails.setText("No address found. Please add an address.");
                    btnPlaceOrder.setEnabled(false);
                }
            }
        });
    }

    private void displayAddress(AddressResponse address) {
        if (address != null) {
            tvShippingAddress.setText(address.getFullName());
            tvAddressDetails.setText(address.getFullAddress() + "\n" + address.getPhoneNumber());
            btnPlaceOrder.setEnabled(true);
        }
    }

    private void calculateTotal() {
        subtotal = 0;
        for (CartItem item : cartItems) {
            subtotal += item.getPrice() * item.getQuantity();
        }

        double total = subtotal + shippingFee;

        DecimalFormat formatter = new DecimalFormat("#,###");
        tvSubtotal.setText(formatter.format(subtotal) + "đ");
        tvShipping.setText(formatter.format(shippingFee) + "đ");
        tvTotal.setText(formatter.format(total) + "đ");
    }

    private void setupClickListeners() {
        btnBack.setOnClickListener(v -> finish());

        tvChangeAddress.setOnClickListener(v -> {
            // TODO: Open address selection dialog
            Toast.makeText(this, "Address selection - Coming soon", Toast.LENGTH_SHORT).show();
        });

        btnPlaceOrder.setOnClickListener(v -> placeOrder());
    }

    private void placeOrder() {
        if (selectedAddress == null) {
            Toast.makeText(this, "Please select a shipping address", Toast.LENGTH_SHORT).show();
            return;
        }

        if (cartItems.isEmpty()) {
            Toast.makeText(this, "Your cart is empty", Toast.LENGTH_SHORT).show();
            return;
        }

        // Get selected payment method
        String paymentMethod = Constants.PAYMENT_COD;
        int selectedId = rgPaymentMethod.getCheckedRadioButtonId();
        if (selectedId == R.id.rbBankTransfer) {
            paymentMethod = Constants.PAYMENT_BANK_TRANSFER;
        } else if (selectedId == R.id.rbCreditCard) {
            paymentMethod = Constants.PAYMENT_CREDIT_CARD;
        } else if (selectedId == R.id.rbEWallet) {
            paymentMethod = Constants.PAYMENT_E_WALLET;
        }

        String note = etOrderNote.getText().toString().trim();

        progressBar.setVisibility(View.VISIBLE);
        btnPlaceOrder.setEnabled(false);

        orderViewModel.placeOrder(selectedAddress.getAddressId(), paymentMethod, note)
                .observe(this, response -> {
                    progressBar.setVisibility(View.GONE);
                    btnPlaceOrder.setEnabled(true);

                    if (response != null && response.isSuccess() && response.getData() != null) {
                        // Clear cart after successful order
                        cartViewModel.clearCart();

                        // Navigate to OrderSuccessActivity
                        Intent intent = new Intent(CheckoutActivity.this, OrderSuccessActivity.class);
                        intent.putExtra("orderId", response.getData().getOrderId());
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    } else {
                        String errorMessage = response != null ? response.getMessage() : "Failed to place order";
                        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
