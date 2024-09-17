package com.example.e_savari.network;

import com.example.e_savari.model.CategoryModel;
import com.example.e_savari.model.ProductModel;
import com.example.e_savari.model.ProductResponse;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiService {
    @GET("products/categories")
    Call<List<CategoryModel>> getProductCategories();

    @GET("products?limit=200")
    Call<ProductResponse> getProducts();

    @GET("products/{productId}")
    Call<ProductModel> getProduct(@Path("productId") String productId);
}
