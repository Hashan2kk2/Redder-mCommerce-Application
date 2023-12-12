package lk.sky.redder.Adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import lk.sky.redder.R;

public class SingleProductViewImageAdapter extends RecyclerView.Adapter<SingleProductViewImageAdapter.ViewHolder>{

    ArrayList<String> productImageList;

    public SingleProductViewImageAdapter(ArrayList<String> productImageList) {
        this.productImageList = productImageList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.productImageSingleProductView);
        }
    }
    @NonNull
    @Override
    public SingleProductViewImageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull SingleProductViewImageAdapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
