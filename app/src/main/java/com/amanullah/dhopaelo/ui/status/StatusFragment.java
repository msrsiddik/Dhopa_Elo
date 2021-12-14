package com.amanullah.dhopaelo.ui.status;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.jem.fliptabs.FlipTab;

import com.amanullah.dhopaelo.R;
import com.amanullah.dhopaelo.databinding.FragmentStatusBinding;
import com.amanullah.dhopaelo.ui.ToolBarSetup;

public class StatusFragment extends Fragment {
    private FragmentStatusBinding binding;
    public StatusFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_status, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        ToolBarSetup.setTitle(getActivity(), "My Order", false);
        setHasOptionsMenu(true);

        changeInnerFrag(new ConfirmOrderFragment());

        binding.flipTab.setTabSelectedListener(new FlipTab.TabSelectedListener() {
            @Override
            public void onTabSelected(boolean b, String s) {
                if (b){
                    changeInnerFrag(new ConfirmOrderFragment());
                } else {
                    changeInnerFrag(new PendingOrderFragment());
                }
            }

            @Override
            public void onTabReselected(boolean b, String s) {
                if (b){
                    changeInnerFrag(new ConfirmOrderFragment());
                } else {
                    changeInnerFrag(new PendingOrderFragment());
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            getActivity().onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void changeInnerFrag(Fragment fragment){
        getChildFragmentManager().beginTransaction()
                .replace(binding.orderFragContainerInFragment.getId(), fragment)
                .commit();
    }
}