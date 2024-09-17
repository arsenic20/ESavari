package com.example.e_savari.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.e_savari.databinding.ItemProductBinding;
import com.example.e_savari.model.ProductModel;
import com.example.e_savari.utils.OnClickActions;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private List<ProductModel> productList;
    private final OnClickActions onProductClickListener;

    public ProductAdapter(List<ProductModel> productList, OnClickActions onClickActions) {
        this.productList = productList;
        this.onProductClickListener = onClickActions;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateProductList(List<ProductModel> newList) {
        productList = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemProductBinding binding = ItemProductBinding.inflate(layoutInflater, parent, false);
        return new ProductViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        ProductModel product = productList.get(position);
        holder.bind(product);

        holder.itemView.setOnClickListener(v -> {
            if (onProductClickListener != null) {
                onProductClickListener.onProductClick(product);
            }
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        private final ItemProductBinding binding;

        public ProductViewHolder(ItemProductBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        @SuppressLint("SetTextI18n")
        public void bind(ProductModel product) {
            int secondSpaceIndex = product.getTitle().indexOf(" ", product.getTitle().indexOf(" ") + 1);
            String result = (secondSpaceIndex != -1) ? product.getTitle().substring(0, secondSpaceIndex) : product.getTitle();
            if(secondSpaceIndex!= -1)
            binding.productName.setText(result+"...");
            else binding.productName.setText(result);
            binding.setProduct(product);
            binding.executePendingBindings();
        }
    }

}
