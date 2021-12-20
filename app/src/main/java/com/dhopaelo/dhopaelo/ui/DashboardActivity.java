package com.dhopaelo.dhopaelo.ui;

import static androidx.fragment.app.FragmentManager.POP_BACK_STACK_INCLUSIVE;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationBarView;
import com.dhopaelo.dhopaelo.R;
import com.dhopaelo.dhopaelo.databinding.ActivityDashboardBinding;
import com.dhopaelo.dhopaelo.ui.account.AccountFragment;
import com.dhopaelo.dhopaelo.ui.home.HomeFragment;
import com.dhopaelo.dhopaelo.ui.status.StatusFragment;

public class DashboardActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener, HomeFragment.AccountFragmentListener {

    private ActivityDashboardBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_dashboard);

        setSupportActionBar(binding.dashToolbar.toolbar);
        getSupportActionBar().setTitle("Home");

        binding.bottomNavigation.setOnItemSelectedListener(this);
        binding.bottomNavigation.setSelectedItemId(R.id.order_nav);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.status_nav:
                gotoFragment(new StatusFragment(), true);
                return true;
            case R.id.order_nav:
                gotoFragment(new HomeFragment(), false);
                return true;
            case R.id.account_nav:
                gotoFragment(new AccountFragment(), true);
                return true;
        }
        return false;
    }

    private void gotoFragment(Fragment fragment, boolean backStack){
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragment != null){
            FragmentTransaction transaction = fragmentManager.beginTransaction().replace(binding.fragmentContainerDashboard.getId(), fragment);
            final int count = fragmentManager.getBackStackEntryCount();
            if (backStack) {
            transaction.addToBackStack("FRAGMENT_OTHER").commit();
            }else {
                transaction.commit();
            }
            getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
                @Override
                public void onBackStackChanged() {
                    if( fragmentManager.getBackStackEntryCount() <= count){
                        // pop all the fragment and remove the listener
                        fragmentManager.popBackStack("FRAGMENT_OTHER", POP_BACK_STACK_INCLUSIVE);
                        fragmentManager.removeOnBackStackChangedListener(this);
                        // set the home button selected
                        binding.bottomNavigation.getMenu().getItem(0).setChecked(true);
                    }
                }
            });
        }
    }

    @Override
    public void gotoAccountFragment() {
        gotoFragment(new AccountFragment(), true);
        binding.bottomNavigation.getMenu().getItem(1).setChecked(true);
        Toast.makeText(this, "Please add mobile number & Address", Toast.LENGTH_SHORT).show();
    }
}