package lk.sky.redder;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;

import java.util.ArrayList;

import lk.sky.redder.Adapter.CartProductAdapter;
import lk.sky.redder.Adapter.HomeProductAdapter;
import lk.sky.redder.model.ProductItem;


public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView recyclerView2;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private GridLayoutManager gridLayoutManager;

    public HomeFragment() {
        // Required empty public constructor
    }
   public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View fragment, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(fragment, savedInstanceState);

        ArrayList<ProductItem> productsList = new ArrayList<>();

        for (int i = 0; i < 20; i++) {
            productsList.add(new ProductItem(
                    R.drawable.macbook,
                    "Mac book Pro 2022",
                    "Apple",
                    "Laptop",
                    300000.00
            ));
        }

        recyclerView = fragment.findViewById(R.id.homeRecyclerView);
        recyclerView.setHasFixedSize(true);
//        layoutManager = new LinearLayoutManager(getContext());

        gridLayoutManager = new GridLayoutManager(getContext(),2);
        adapter = new HomeProductAdapter(productsList);

        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(adapter);

//        ==================== recycler view 2 =========================
        recyclerView2 = fragment.findViewById(R.id.homeRecyclerView2);
        recyclerView2.setHasFixedSize(true);
//        layoutManager = new LinearLayoutManager(getContext());

        gridLayoutManager = new GridLayoutManager(getContext(),2);
        adapter = new HomeProductAdapter(productsList);

        recyclerView2.setLayoutManager(gridLayoutManager);
        recyclerView2.setAdapter(adapter);

    }
}