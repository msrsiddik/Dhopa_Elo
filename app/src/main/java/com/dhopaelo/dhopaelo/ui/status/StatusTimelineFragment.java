package com.dhopaelo.dhopaelo.ui.status;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import com.dhopaelo.dhopaelo.R;
import com.dhopaelo.dhopaelo.adapter.StatusTimelineRecyclerView;
import com.dhopaelo.dhopaelo.databinding.FragmentStatusTimelineBinding;
import com.dhopaelo.dhopaelo.domain.StatusTimelineDetailsModel;

public class StatusTimelineFragment extends Fragment {
    public static final String SERVICE_NAME = "serviceName";
    private FragmentStatusTimelineBinding binding;

    public StatusTimelineFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_status_timeline, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        String string = getArguments().getString(SERVICE_NAME);

//        binding.serviceTitle.setText(string);

        String serviceName = "wash";

        List<StatusTimelineDetailsModel> list = new ArrayList<>();
        list.add(new StatusTimelineDetailsModel("", "Order Placed", "Your order is placed successfully.", "At: 00:00 am", R.drawable.order_icon,true));
        list.add(new StatusTimelineDetailsModel("", "Pickup", "Your clothes will be picked up for "+serviceName+" within 3 hours.", "At: 00:00 am", R.drawable.pickup_icon));
        list.add(new StatusTimelineDetailsModel("", "On Process", "Your order is in process.", null, R.drawable.process_icon));
        list.add(new StatusTimelineDetailsModel("", "Ready to delivery", "Your product is ready to deliver. Hopefully you will get it within 3 hours", "At: 00:00 pm", R.drawable.ready_to_delivery_icon));
        list.add(new StatusTimelineDetailsModel("", "Delivered", "Delivery completed.", "At: 00:00 pm", R.drawable.deliverd_icon));
        StatusTimelineRecyclerView adapter = new StatusTimelineRecyclerView(list, getContext());
        binding.statusTimelineRecyclerView.setAdapter(adapter);
    }
}