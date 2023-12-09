package lk.sky.redder;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import lk.sky.redder.Adapter.HomeProductAdapter;
import lk.sky.redder.Adapter.NotificationAdapter;
import lk.sky.redder.model.NotificationItems;
import lk.sky.redder.model.ProductItem;

public class NotificationFragment extends Fragment {

    private RecyclerView recyclerRecyclerView;
    private RecyclerView.Adapter recyclerAdapter;
//    private ArrayList<Tags> tagsArrayList;
    private RecyclerView.LayoutManager horizontalLayoutManager;
    GridLayoutManager gridLayoutManager;
    private String[] tagsName;

    public NotificationFragment() {
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
        return inflater.inflate(R.layout.fragment_notification, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View fragment, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(fragment, savedInstanceState);

        // Notification Start //////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        ArrayList<NotificationItems> notificationItemsList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            notificationItemsList.add(new NotificationItems(
                    "Become a personal customer",
                    "Lorem Ipsum is simply dummy text of the printing and typesetting industry.",
                    "10 Min Ago",
                    R.drawable.notification));
        }

        RecyclerView recyclerRecyclerView = fragment.findViewById(R.id.notificationRecyclerView);
        recyclerRecyclerView.setHasFixedSize(true);
        horizontalLayoutManager = new LinearLayoutManager(getContext());
        recyclerAdapter = new NotificationAdapter(notificationItemsList);
        recyclerRecyclerView.setLayoutManager(horizontalLayoutManager);
        recyclerRecyclerView.setAdapter(recyclerAdapter);

        // Notification End ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    }
}