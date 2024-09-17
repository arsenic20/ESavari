package com.example.e_savari.viewModel;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import com.example.e_savari.model.CategoryModel;
import com.example.e_savari.model.ProductModel;
import com.example.e_savari.network.Repository;
import java.util.List;
import javax.inject.Inject;
import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class ListPageViewModel extends ViewModel {
    private final Repository categoryRepository;

    @Inject
    public ListPageViewModel(Repository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public LiveData<List<CategoryModel>> getProductCategories() {
        return categoryRepository.getProductCategories();
    }
    public LiveData<List<ProductModel>> getProducts() {
        return categoryRepository.getProducts();
    }
}
