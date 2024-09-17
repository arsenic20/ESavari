package com.example.e_savari.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import com.example.e_savari.model.ProductModel;
import com.example.e_savari.network.Repository;
import javax.inject.Inject;
import dagger.hilt.android.lifecycle.HiltViewModel;


@HiltViewModel
public class DetailPageViewModel extends ViewModel {
    private final Repository categoryRepository;

    @Inject
    public DetailPageViewModel(Repository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public LiveData<ProductModel> getProduct(String productId) {
        return categoryRepository.getProduct(productId);
    }
}
