package com.example.e_savari.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.example.e_savari.R;
import com.example.e_savari.adapter.ReviewAdapter;
import com.example.e_savari.databinding.DetailPageBinding;
import com.example.e_savari.viewModel.DetailPageViewModel;
import com.facebook.shimmer.ShimmerFrameLayout;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class DetailPageActivity extends AppCompatActivity  {
    private DetailPageViewModel detailPageViewModel;
    private DetailPageBinding binding;
    private Integer productId;
    private ReviewAdapter reviewAdapter;
    private ImageView iconClose;
    private ImageView navBack;
    private EditText searchItem;
    private TextView header;
    private ShimmerFrameLayout shimmerProductLayout;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DetailPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        detailPageViewModel = new ViewModelProvider(this).get(DetailPageViewModel.class);


        Intent intent = getIntent();
        productId = intent.getIntExtra("productId", 0); // 0 is default if no value is found

        iconClose = findViewById(R.id.close);
        searchItem = findViewById(R.id.searchEditText);
        header = findViewById(R.id.custom_toolbar_text);
        navBack = findViewById(R.id.backBtn);
        shimmerProductLayout = binding.detailShimmer;
        shimmerProductLayout.startShimmer();

        binding.main.setVisibility(View.GONE);


        navBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Go back to the previous activity or fragment
                onBackPressed();
            }
        });

        observeData();
    }


    private void observeData() {
        detailPageViewModel.getProduct(String.valueOf(productId)).observe(this, product -> {
            if (product != null) {
                shimmerProductLayout.stopShimmer();
                binding.detailShimmer.setVisibility(View.GONE);
                setToolBar();
                binding.main.setVisibility(View.VISIBLE);
                binding.setProduct(product);
                header.setText(product.getTitle());
                reviewAdapter = new ReviewAdapter(product.getReviews());
                binding.reviewList.setAdapter(reviewAdapter);
            } else {
                Toast.makeText(DetailPageActivity.this, "Failed to fetch Products", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setToolBar() {
        iconClose.setVisibility(View.GONE);
        searchItem.setVisibility(View.GONE);
        header.setVisibility(View.VISIBLE);
        navBack.setVisibility(View.VISIBLE);
    }
}
