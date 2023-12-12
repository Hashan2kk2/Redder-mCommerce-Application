

//public class HomeProductAdapter {
package lk.sky.redder.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import lk.sky.redder.R;
import lk.sky.redder.SingleProductViewFragment;
import lk.sky.redder.model.Product;

public class HomeProductAdapter extends RecyclerView.Adapter<HomeProductAdapter.ViewHolder> {
    private ArrayList<Product> productItemsList;
    private Fragment transactionFragment;


    public HomeProductAdapter(ArrayList<Product> productsList, Fragment fragment) {
        this.productItemsList = productsList;
        this.transactionFragment = fragment;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView name, brand, category, price;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.productImg);
            name = itemView.findViewById(R.id.prod_name);
            brand = itemView.findViewById(R.id.prod_brand);
            category = itemView.findViewById(R.id.prod_category);
            price = itemView.findViewById(R.id.prod_price);


        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_prod_card, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product currentItem = productItemsList.get(position);
        Picasso.get().load(currentItem.getImageList().get(0)).into(holder.imageView);
        holder.name.setText(currentItem.getName());
        holder.brand.setText(currentItem.getBrand());
        holder.category.setText(currentItem.getCategory());
        holder.price.setText(currentItem.getPrice().toString());

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new SingleProductViewFragment(currentItem));
            }
        });
    }

    @Override
    public int getItemCount() {
        return productItemsList.size();
    }
    public void loadFragment(Fragment fragment) {
        FragmentManager supportFragmentManager = transactionFragment.getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment);
        fragmentTransaction.commit();
    }
}
