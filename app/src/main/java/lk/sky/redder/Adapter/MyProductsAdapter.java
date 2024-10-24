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

public class MyProductsAdapter extends RecyclerView.Adapter<MyProductsAdapter.ViewHolder> {
    private ArrayList<ProductItem> productItemsList;

    public MyProductsAdapter(ArrayList<ProductItem> productsList) {
        this.productItemsList = productsList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView name, brand, category, price;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.my_prod_img);
            name = itemView.findViewById(R.id.my_prod_name);
            brand = itemView.findViewById(R.id.my_prod_brand);
            category = itemView.findViewById(R.id.my_prod_category);
            price = itemView.findViewById(R.id.my_prod_category);
        }
    }

    @NonNull
    @Override
    public MyProductsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_products_card, parent, false);
        MyProductsAdapter.ViewHolder viewHolder = new MyProductsAdapter.ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyProductsAdapter.ViewHolder holder, int position) {
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
