package lk.sky.redder;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import lk.sky.redder.Adapter.HomeProductAdapter;
import lk.sky.redder.model.Product;
import lk.sky.redder.model.ProductItem;


public class HomeFragment extends Fragment {
    private RecyclerView.Adapter newArrivalAdapter, trendingAdapter, moreToLoveAdapter;

    private RecyclerView recyclerView;
    private RecyclerView recyclerView2;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private GridLayoutManager gridLayoutManager;

    private FirebaseDatabase firebaseDatabase;

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

        firebaseDatabase = FirebaseDatabase.getInstance();

//        New Arrival Start
        //New Arrival Start ///////////////////////////////////////////////////////////////////////////////////////////////////////////

        ArrayList<Product> newArrivalsProducts = new ArrayList<>();

        firebaseDatabase.getReference("Products").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Product product = dataSnapshot.getValue(Product.class);
                    newArrivalsProducts.add(product);
                }
                newArrivalAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Db data load fail", Toast.LENGTH_LONG).show();
            }
        });


//        New Arrival End



        recyclerView = fragment.findViewById(R.id.homeRecyclerView);
        recyclerView.setHasFixedSize(true);
//        layoutManager = new LinearLayoutManager(getContext());

        gridLayoutManager = new GridLayoutManager(getContext(), 2);
        newArrivalAdapter = new HomeProductAdapter(newArrivalsProducts);

        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(newArrivalAdapter);

//        ==================== recycler view 2 =========================
//        recyclerView2 = fragment.findViewById(R.id.homeRecyclerView2);
//        recyclerView2.setHasFixedSize(true);
//        layoutManager = new LinearLayoutManager(getContext());

//        gridLayoutManager = new GridLayoutManager(getContext(), 2);
//        adapter = new HomeProductAdapter(productsList);
//
//        recyclerView2.setLayoutManager(gridLayoutManager);
//        recyclerView2.setAdapter(adapter);


    }
}