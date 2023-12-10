package lk.sky.redder;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, NavigationBarView.OnItemSelectedListener {


    private BottomNavigationView bottomNavigationView;
    private DrawerLayout drawerLayout;
    private NavigationView navigation;
    private MaterialToolbar toolbar;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.mainDrawerLayout);
        navigation = findViewById(R.id.mainSideNavigation);
        toolbar = findViewById(R.id.materialToolBar);
        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();

//        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(MainActivity.this, drawerLayout, R.string.open_navigation_drawer, R.string.close_navigation_drawer);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        findViewById(R.id.imageMenuButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.open();
            }
        });

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setItemIconTintList(null);

        navigation.setNavigationItemSelectedListener(this);
        bottomNavigationView.setOnItemSelectedListener(this);


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

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        DrawerLayout dl = findViewById(R.id.mainDrawerLayout);
        if (item.getItemId() == R.id.home_nav||item.getItemId() == R.id.sideNavHome) {
            loadFragment(new HomeFragment());
            dl.close();
            return true;
        } else if (item.getItemId() == R.id.notification_nav) {
            loadFragment(new NotificationFragment());
            return true;
        } else if (item.getItemId() == R.id.orders_nav) {
            loadFragment(new OrderFragment());
            return true;
        } else if (item.getItemId() == R.id.profile_nav) {
            loadFragment(new UserProfileFragment());
            return true;
        }else if (item.getItemId() == R.id.sideNavLog) {
            Intent intent = new Intent(MainActivity.this, SignInActivity.class);
            startActivity(intent);
        } else if (item.getItemId() == R.id.sideNavSellerProds) {
            loadFragment(new MyProductsFragment());
            dl.close();
            return true;
        } else if (item.getItemId()==R.id.sideNavCart) {
            loadFragment(new CartFragment());
            dl.close();
            return true;
        }else if (item.getItemId()==R.id.sideNavWishlist) {
            loadFragment(new WishlistFragment());
            dl.close();
            return true;
        }else if (item.getItemId()==R.id.sideNavPrivacyPolicy) {
            loadFragment(new PrivacyPolicyFragment());
            dl.close();
            return true;
        }else if (item.getItemId()==R.id.sideNavProdReg) {
            loadFragment(new AddProductFragment());
            dl.close();
            return true;
        } else if (item.getItemId()==R.id.sideNavLogOut) {
            firebaseAuth.signOut();
            Toast.makeText(MainActivity.this, "Successfully Signed Out", Toast.LENGTH_SHORT).show();
        }
        dl.close();
        return true;
    }

    private void loadFragment(Fragment fragment) {
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment);
        fragmentTransaction.commit();
    }
}