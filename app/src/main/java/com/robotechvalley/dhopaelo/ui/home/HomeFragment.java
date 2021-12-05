package com.robotechvalley.dhopaelo.ui.home;

import static com.robotechvalley.dhopaelo.ui.order.OrderActivity.SERVICE_NAME;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.collection.LruCache;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.denzcoskun.imageslider.ImageSlider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.robotechvalley.dhopaelo.R;
import com.robotechvalley.dhopaelo.adapter.OrderItemRecyclerView;
import com.robotechvalley.dhopaelo.adapter.cardSlider.SlideCardAdapter;
import com.robotechvalley.dhopaelo.databinding.FragmentHomeBinding;
import com.robotechvalley.dhopaelo.domain.view.OrderViewItemModel;
import com.robotechvalley.dhopaelo.ui.ToolBarSetup;
import com.robotechvalley.dhopaelo.ui.order.OrderActivity;

public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;

    private ImageSlider imageSlider;

    private DatabaseReference databaseReference;

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

        List<OrderViewItemModel> list = new ArrayList<>();
        list.add(new OrderViewItemModel(R.drawable.wash, "Wet Wash"));
        list.add(new OrderViewItemModel(R.drawable.iron, "Wet Iron"));
        list.add(new OrderViewItemModel(R.drawable.wash_iron, "Wet Wash & Iron"));

        binding.orderRecyclerView.setAdapter(new OrderItemRecyclerView(list, getContext(), position -> {
            Intent intent = new Intent(getContext(), OrderActivity.class);
            intent.putExtra(OrderActivity.FROM_FRAGMENT_NAME, "HomeFragment");
            intent.putExtra(SERVICE_NAME, list.get(position).getTitle());
            startActivity(intent);
        }));

        List<OrderViewItemModel> dryList = new ArrayList<>();
//        dryList.add(new OrderViewItemModel(R.drawable.dry_wash, "Dry Wash"));
        dryList.add(new OrderViewItemModel(R.drawable.dry_wash_iron, "Dry Wash & Iron"));

        binding.orderRecyclerViewDry.setAdapter(new OrderItemRecyclerView(dryList, getContext(), position -> {
            Intent intent = new Intent(getContext(), OrderActivity.class);
            intent.putExtra(OrderActivity.FROM_FRAGMENT_NAME, "HomeFragment");
            intent.putExtra(SERVICE_NAME, dryList.get(position).getTitle());
            startActivity(intent);
        }));
    }

    private void setupBanner() {
//        FirebaseStorage storage = FirebaseStorage.getInstance();
//        StorageReference storageRef = storage.getReference("banner");
//        storageRef.listAll().addOnSuccessListener(listResult -> {
//            List<StorageReference> items = listResult.getItems();
//            SlideCardAdapter adapter = new SlideCardAdapter(getContext(), items);
//            binding.sliderViewPager.setAdapter(adapter);
//        });

        File[] files = getContext().getCacheDir().listFiles(file -> file.getName().startsWith("banner"));
        if (files != null) {
            SlideCardAdapter adapter = new SlideCardAdapter(getContext(), files);
            binding.sliderViewPager.setAdapter(adapter);
        }

    }
}