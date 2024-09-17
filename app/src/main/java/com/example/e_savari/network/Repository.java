package com.example.e_savari.network;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.e_savari.model.CategoryModel;
import com.example.e_savari.model.ProductModel;
import com.example.e_savari.model.ProductResponse;
import java.util.List;
import javax.inject.Inject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Repository {
    private final ApiService apiService;

    @Inject
    public Repository(ApiService apiService) {
        this.apiService = apiService;
    }

    public LiveData<List<CategoryModel>> getProductCategories() {
        MutableLiveData<List<CategoryModel>> categoriesData = new MutableLiveData<>();

        apiService.getProductCategories().enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<List<CategoryModel>> call, @NonNull Response<List<CategoryModel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    categoriesData.setValue(response.body());
                } else {
                    categoriesData.setValue(null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<CategoryModel>> call, @NonNull Throwable t) {
                categoriesData.setValue(null);
            }
        });

        return categoriesData;
    }

    public LiveData<List<ProductModel>> getProducts() {
        MutableLiveData<List<ProductModel>> productsData = new MutableLiveData<>();

        apiService.getProducts().enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<ProductResponse> call, @NonNull Response<ProductResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<ProductModel> products = response.body().getProducts();
                    productsData.setValue(products);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ProductResponse> call, @NonNull Throwable t) {
                // Handle the error
                productsData.setValue(null);
            }
        });
       return productsData;
    }

    public LiveData<ProductModel> getProduct(String productId) {
        MutableLiveData<ProductModel> productsData = new MutableLiveData<>();

        apiService.getProduct(productId).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<ProductModel> call, @NonNull Response<ProductModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ProductModel products = response.body();
                    productsData.setValue(products);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ProductModel> call, @NonNull Throwable t) {
                // Handle the error
                productsData.setValue(null);
            }
        });
       return productsData;
    }
}
