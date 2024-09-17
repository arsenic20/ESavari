package com.example.e_savari.utils;


import com.example.e_savari.model.CategoryModel;
import com.example.e_savari.model.ProductModel;

public interface OnClickActions {
    void onProductClick(ProductModel product);

    void onCategoryClick(CategoryModel category);
}
