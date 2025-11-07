package com.example.lazabee.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.example.lazabee.data.model.ApiResponse;
import com.example.lazabee.data.model.address.AddressRequest;
import com.example.lazabee.data.model.address.AddressResponse;
import com.example.lazabee.data.repository.AddressRepository;
import java.util.List;

public class AddressViewModel extends AndroidViewModel {
    private AddressRepository addressRepository;

    public AddressViewModel(@NonNull Application application) {
        super(application);
        addressRepository = new AddressRepository(application);
    }

    public LiveData<ApiResponse<List<AddressResponse>>> getAddresses() {
        return addressRepository.getAddresses();
    }

    public LiveData<ApiResponse<AddressResponse>> getAddressById(String addressId) {
        return addressRepository.getAddressById(addressId);
    }

    public LiveData<ApiResponse<AddressResponse>> createAddress(AddressRequest request) {
        return addressRepository.createAddress(request);
    }

    public LiveData<ApiResponse<AddressResponse>> updateAddress(String addressId, AddressRequest request) {
        return addressRepository.updateAddress(addressId, request);
    }

    public LiveData<ApiResponse<Void>> deleteAddress(String addressId) {
        return addressRepository.deleteAddress(addressId);
    }

    public LiveData<ApiResponse<AddressResponse>> setDefaultAddress(String addressId) {
        return addressRepository.setDefaultAddress(addressId);
    }
}
