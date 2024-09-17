package com.example.e_savari.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.e_savari.databinding.ItemReviewsBinding;
import com.example.e_savari.model.ProductModel;

import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {

    private final List<ProductModel.Review> reviewList;

    public ReviewAdapter(List<ProductModel.Review> reviewList) {
        this.reviewList = reviewList;
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemReviewsBinding binding = ItemReviewsBinding.inflate(layoutInflater, parent, false);
        return new ReviewViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        ProductModel.Review review = reviewList.get(position);
        holder.bind(review);
    }

    @Override
    public int getItemCount() {
        return reviewList.size();
    }

    public static class ReviewViewHolder extends RecyclerView.ViewHolder {
        private final ItemReviewsBinding binding;

        public ReviewViewHolder(ItemReviewsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(ProductModel.Review review) {
            String rating = String.valueOf(review.getRating());
            binding.setReview(review);
            binding.rating.setText(rating);
            binding.executePendingBindings();
        }
    }

}
