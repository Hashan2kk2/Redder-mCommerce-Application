package lk.sky.redder;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setItemIconTintList(null);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.home_nav) {
                    loadFragment(new HomeFragment());
                    return true;
                } else if (item.getItemId() == R.id.notification_nav) {
                    loadFragment(new NotificationFragment());
                    return true;
                } else if (item.getItemId() == R.id.orders_nav) {
                    loadFragment(new OrderFragment());
                    return true;
                }else if (item.getItemId() == R.id.profile_nav) {
                    loadFragment(new UserProfileFragment());
                    return true;
                }
                return false;
            }
        });

        ImageButton cart = findViewById(R.id.cart_home);
        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new CartFragment());
            }

        });

        ImageButton wishList = findViewById(R.id.wishList_home);
        wishList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new WishlistFragment());
            }
        });
        // initial fragment
        loadFragment(new HomeFragment());


    }

    private void loadFragment(Fragment fragment) {
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment);
        fragmentTransaction.commit();
    }
}