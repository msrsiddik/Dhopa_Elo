package com.robotechvalley.dhopaelo.ui.order;

import static com.robotechvalley.dhopaelo.ui.order.OrderActivity.SELECT_POSITION;
import static com.robotechvalley.dhopaelo.ui.order.OrderActivity.SERVICE_NAME;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.robotechvalley.dhopaelo.R;
import com.robotechvalley.dhopaelo.adapter.order.AllService;
import com.robotechvalley.dhopaelo.databinding.FragmentAllServiceBinding;
import com.robotechvalley.dhopaelo.domain.AllServiceOneInvoiceModel;
import com.robotechvalley.dhopaelo.domain.ProductInfoModel;
import com.robotechvalley.dhopaelo.domain.view.OrderViewItemModel;
import com.robotechvalley.dhopaelo.listener.InvoiceListener;
import com.robotechvalley.dhopaelo.ui.ToolBarSetup;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class AllServiceFragment extends Fragment implements InvoiceListener {
    private FragmentAllServiceBinding binding;
    private AllServiceOneInvoiceModel oneInvoiceModel;

    public AllServiceFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        oneInvoiceModel = AllServiceOneInvoiceModel.getInstance();
        oneInvoiceModel.addListener(this);
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_all_service, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Bundle arguments = getArguments();

        if (arguments != null) {
            Type type = new TypeToken<List<OrderViewItemModel>>() {}.getType();
            String listJson = arguments.getString("list");
            List<OrderViewItemModel> orderViewItemModels = new Gson().fromJson(listJson, type);
            int selectPosition = arguments.getInt(SELECT_POSITION);

            AllService adapter = new AllService(this, orderViewItemModels);

            binding.viewPager.setAdapter(adapter);
            new TabLayoutMediator(binding.tabLayout, binding.viewPager, (tab, position) -> {
                tab.setText(orderViewItemModels.get(position).getTitle());
            }).attach();
            binding.tabLayout.getTabAt(selectPosition).select();
            binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    ToolBarSetup.setTitle(getActivity(), orderViewItemModels.get(tab.getPosition()).getTitle(), false);
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {

                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }
            });
        }
    }

    @Override
    public void update() {
        binding.totalQuantity.getEditText().setText(oneInvoiceModel.getTotalProduct() + "");
        binding.totalAmount.getEditText().setText(oneInvoiceModel.getTotalPrice() + "");
    }
}