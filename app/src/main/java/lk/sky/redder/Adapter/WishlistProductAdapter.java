package lk.sky.redder.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import lk.sky.redder.R;
import lk.sky.redder.model.ProductItem;

public class WishlistProductAdapter extends RecyclerView.Adapter<WishlistProductAdapter.ViewHolder>{
    private ArrayList<ProductItem> productItemsList;

    public WishlistProductAdapter(ArrayList<ProductItem> productsList) {
        this.productItemsList = productsList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView name, brand, category, price;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.productImgWishlist);
            name = itemView.findViewById(R.id.productNameText);
            brand = itemView.findViewById(R.id.brandText);
            category = itemView.findViewById(R.id.categoryText);
            price = itemView.findViewById(R.id.priceText);
        }
    }

    @NonNull
    @Override
    public WishlistProductAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.wishlist_prod_card, parent, false);
        WishlistProductAdapter.ViewHolder viewHolder = new WishlistProductAdapter.ViewHolder(view);

        return viewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull WishlistProductAdapter.ViewHolder holder, int position) {
        ProductItem currentItem = productItemsList.get(position);
        holder.imageView.setImageResource(currentItem.getImageSource());
        holder.name.setText(currentItem.getName());
        holder.brand.setText(currentItem.getBrand());
        holder.category.setText(currentItem.getCategory());
        holder.price.setText(currentItem.getPrice().toString());
    }

    @Override
    public int getItemCount() {
        return productItemsList.size();
    }
}
