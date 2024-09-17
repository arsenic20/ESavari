package com.example.e_savari.view;


import static com.google.android.material.internal.ViewUtils.hideKeyboard;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.example.e_savari.R;
import com.example.e_savari.adapter.CategoryAdapter;
import com.example.e_savari.adapter.ProductAdapter;
import com.example.e_savari.databinding.ListPageBinding;
import com.example.e_savari.model.CategoryModel;
import com.example.e_savari.model.ProductModel;
import com.example.e_savari.utils.OnClickActions;
import com.example.e_savari.viewModel.ListPageViewModel;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;
import java.util.List;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ListPageActivity extends AppCompatActivity implements OnClickActions {
    private ListPageViewModel listPageViewModel;
    private ListPageBinding binding;
    private CategoryAdapter categoryAdapter;
    private ProductAdapter productAdapter;
    private final List<ProductModel> searchList = new ArrayList<>();
    private final List<ProductModel> productList = new ArrayList<>();
    private EditText searchItem;
    private TextView header;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ListPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        listPageViewModel = new ViewModelProvider(this).get(ListPageViewModel.class);

        searchItem = findViewById(R.id.searchEditText);
        ImageView iconClose = findViewById(R.id.close);
        header = findViewById(R.id.custom_toolbar_text);


        ShimmerFrameLayout shimmerListLayout = binding.listShimmer;
        shimmerListLayout.startShimmer();
        binding.categoryList.setVisibility(View.GONE);
        binding.productList.setVisibility(View.GONE);
        binding.toolbar.getRoot().setVisibility(View.GONE);

        onSearch();

        searchItem.setOnClickListener(v -> {
            searchItem.requestFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(searchItem, InputMethodManager.SHOW_IMPLICIT);
            binding.categoryList.setVisibility(View.GONE);
            productAdapter.updateProductList(productList);
            filterProducts(searchItem.getText().toString());
    });

        iconClose.setOnClickListener(v -> onClose());
        onSearchAction();
        observeData(this);
    }

    private void onSearchAction() {
        searchItem.setOnEditorActionListener((v, actionId, event) -> {
            // Check if the action is the Search action
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                // Perform the search action or handle it
               filterProducts(searchItem.getText().toString());
                // Hide the keyboard after search action
                hideKeyboard();

                return true;  // Return true to indicate action has been handled
            }
            return false;  // Return false to let the system handle other actions
        });
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null && getCurrentFocus() != null) {
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    private void onClose() {
        if (binding.categoryList.getVisibility() == View.VISIBLE) {
            super.onBackPressed();
        }
        searchItem.setVisibility(View.VISIBLE);
        header.setVisibility(View.GONE);
        binding.categoryList.setVisibility(View.VISIBLE);
        binding.noData.setVisibility(View.GONE);
        productAdapter.updateProductList(productList.subList(0,20));
        searchList.clear();
    }

    private void onSearch() {
        searchItem.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Filter the list when the user types 3 or more characters
                filterProducts(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) { }
        });
    }


    private void filterProducts(String query) {
        binding.categoryList.setVisibility(View.GONE);
        if (query.length() >= 3) {
            filterProductsByName(query);
        }
        else {
            binding.noData.setVisibility(View.GONE);
            productAdapter.updateProductList(productList);
        }
    }


    private void observeData(ListPageActivity listPageActivity) {
        listPageViewModel.getProductCategories().observe(this, categories -> {
            if (categories != null) {
                binding.listShimmer.stopShimmer();
                binding.categoryList.setVisibility(View.VISIBLE);
                binding.productList.setVisibility(View.VISIBLE);
                binding.toolbar.getRoot().setVisibility(View.VISIBLE);
                binding.listShimmer.setVisibility(View.GONE);


                categoryAdapter = new CategoryAdapter(categories,listPageActivity);
                binding.categoryList.setAdapter(categoryAdapter);
            } else {
                Toast.makeText(ListPageActivity.this, "Failed to fetch categories", Toast.LENGTH_SHORT).show();
            }
        });

        listPageViewModel.getProducts().observe(this, products -> {
            if (products != null) {
                productList.addAll(products);

                //Display first 20 products
                productAdapter = new ProductAdapter(products.subList(0,20),listPageActivity);
                binding.productList.setAdapter(productAdapter);
            } else {
                Toast.makeText(ListPageActivity.this, "Failed to fetch Products", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (binding.categoryList.getVisibility() == View.GONE) {
            searchItem.setText("");
            searchItem.clearFocus();
            onClose();
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public void onProductClick(ProductModel product) {
        Intent intent = new Intent(ListPageActivity.this, DetailPageActivity.class);
        intent.putExtra("productId", product.getId());
        startActivity(intent);
    }

    @Override
    public void onCategoryClick(CategoryModel category) {
       searchItem.setVisibility(View.GONE);
       header.setVisibility(View.VISIBLE);
       header.setText(category.getName());
       binding.categoryList.setVisibility(View.GONE);
       filterProductByCategory(category.getName());
    }

    private void filterProductByCategory(String name) {
        searchList.clear();
        for (ProductModel product : productList) {
            if (product.getCategory().equalsIgnoreCase(name)) {
                searchList.add(product);
            }
        }
        if(searchList.isEmpty()) binding.noData.setVisibility(View.VISIBLE);
        else binding.noData.setVisibility(View.GONE);
        productAdapter.updateProductList(searchList);
    }

    private void filterProductsByName(String name) {
        searchList.clear();
        for (ProductModel product : productList) {
            if (product.getTitle().toLowerCase().contains(name.toLowerCase())) {
                searchList.add(product);
            }
        }
        if(searchList.isEmpty()) binding.noData.setVisibility(View.VISIBLE);
        else binding.noData.setVisibility(View.GONE);
        productAdapter.updateProductList(searchList);
    }
}
