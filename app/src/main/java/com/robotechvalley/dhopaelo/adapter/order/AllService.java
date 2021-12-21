package com.robotechvalley.dhopaelo.adapter.order;

import static com.robotechvalley.dhopaelo.ui.order.OrderActivity.SERVICE_NAME;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.robotechvalley.dhopaelo.domain.view.OrderViewItemModel;
import com.robotechvalley.dhopaelo.ui.order.OrderFirstStageFragment;

import java.util.ArrayList;
import java.util.List;

public class AllService extends FragmentStateAdapter {
    private final List<Fragment> fragmentList = new ArrayList<>();

    public AllService(@NonNull Fragment fragment, List<OrderViewItemModel> orderViewItemModels) {
        super(fragment);
        for (OrderViewItemModel orderViewItemModel : orderViewItemModels) {
            OrderFirstStageFragment orderFirstStageFragment = new OrderFirstStageFragment();
            Bundle bundle = new Bundle();
            bundle.putString(SERVICE_NAME, orderViewItemModel.getTitle());
            orderFirstStageFragment.setArguments(bundle);
            fragmentList.add(orderFirstStageFragment);
        }
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getItemCount() {
        return fragmentList.size();
    }
}
