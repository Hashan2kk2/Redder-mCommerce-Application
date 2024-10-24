package lk.sky.redder;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import lk.sky.redder.Adapter.SingleProductViewImageAdapter;
import lk.sky.redder.model.Product;

public class SingleProductViewFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private Product product;

    public SingleProductViewFragment(Product product) {
        this.product = product;
    }

    public SingleProductViewFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_single_product_view, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View fragment, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(fragment, savedInstanceState);
        ArrayList<String> imageList = new ArrayList<>();

        TextView categoryField = fragment.findViewById(R.id.catTextField);
        TextView brandField = fragment.findViewById(R.id.brandField);
        TextView nameField = fragment.findViewById(R.id.productNameFieldS);
        TextView priceField = fragment.findViewById(R.id.priceFieldS);
        TextView descriptionField = fragment.findViewById(R.id.descrptionFieldS);
        TextView qtyField = fragment.findViewById(R.id.qtyTextS);


        for (int i = 0; i < product.getImageList().size(); i++) {
            imageList.add(product.getImageList().get(i));
        }
        recyclerView = fragment.findViewById(R.id.singleProductViewImageRecycler);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        adapter = new SingleProductViewImageAdapter(imageList);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        categoryField.setText(product.getCategory());
        brandField.setText(product.getBrand());
        nameField.setText(product.getName());
        priceField.setText("Rs." + product.getPrice().toString() + "0");
        descriptionField.setText(product.getDescription());
        qtyField.setText("Quantity : " + product.getQuantity());
    }
}