package com.amanullah.dhopaelo.ui.home;

import static com.amanullah.dhopaelo.ui.order.OrderActivity.SERVICE_NAME;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.denzcoskun.imageslider.ImageSlider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.amanullah.dhopaelo.R;
import com.amanullah.dhopaelo.adapter.OrderItemRecyclerView;
import com.amanullah.dhopaelo.adapter.cardSlider.SlideCardAdapter;
import com.amanullah.dhopaelo.databinding.FragmentHomeBinding;
import com.amanullah.dhopaelo.domain.view.OrderViewItemModel;
import com.amanullah.dhopaelo.ui.ToolBarSetup;
import com.amanullah.dhopaelo.ui.order.OrderActivity;

public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;

    private ImageSlider imageSlider;

    private DatabaseReference databaseReference;

    private List<OrderViewItemModel> list;
    private List<OrderViewItemModel> dryList;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        ToolBarSetup.setTitle(getActivity(), "Home", false);

        binding.refresh.setOnRefreshListener(() -> {
            setupBanner();
            binding.refresh.setRefreshing(false);
        });

        setupBanner();

        list = new ArrayList<>();
        list.add(new OrderViewItemModel(R.drawable.wash, "Wet Wash"));
        list.add(new OrderViewItemModel(R.drawable.iron, "Wet Iron"));
        list.add(new OrderViewItemModel(R.drawable.wash_iron, "Wet Wash & Iron"));

        binding.orderRecyclerView.setAdapter(new OrderItemRecyclerView(list, getContext(), position -> {
            profileCheck(position, "wet");

        }));

        dryList = new ArrayList<>();
//        dryList.add(new OrderViewItemModel(R.drawable.dry_wash, "Dry Wash"));
        dryList.add(new OrderViewItemModel(R.drawable.dry_wash_iron, "Dry Wash & Iron"));

        binding.orderRecyclerViewDry.setAdapter(new OrderItemRecyclerView(dryList, getContext(), position -> {
            profileCheck(position, "dry");
        }));
    }

    private void profileCheck(int position, String serviceType){
        FirebaseFirestore.getInstance().collection("app-user")
                .document(FirebaseAuth.getInstance().getUid()).collection("profile")
                .document("address").get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                if (serviceType.equals("wet")) {
                    Intent intent = new Intent(getContext(), OrderActivity.class);
                    intent.putExtra(OrderActivity.FROM_FRAGMENT_NAME, "HomeFragment");
                    intent.putExtra(SERVICE_NAME, list.get(position).getTitle());
                    startActivity(intent);
                }
                else if (serviceType.equals("dry")) {
                    Intent intent = new Intent(getContext(), OrderActivity.class);
                    intent.putExtra(OrderActivity.FROM_FRAGMENT_NAME, "HomeFragment");
                    intent.putExtra(SERVICE_NAME, dryList.get(position).getTitle());
                    startActivity(intent);
                }

            } else {
                AccountFragmentListener listener = (AccountFragmentListener) getActivity();
                listener.gotoAccountFragment();
            }
        });
    }

    private void setupBanner() {
        File[] files = getContext().getCacheDir().listFiles(file -> file.getName().startsWith("banner"));
        if (files != null) {
            SlideCardAdapter adapter = new SlideCardAdapter(getContext(), files);
            binding.sliderViewPager.setAdapter(adapter);
        }
    }

    public interface AccountFragmentListener {
        void gotoAccountFragment();
    }
}