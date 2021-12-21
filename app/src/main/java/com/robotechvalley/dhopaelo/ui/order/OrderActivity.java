package com.robotechvalley.dhopaelo.ui.order;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.widget.Toast;

import com.google.gson.Gson;

import java.lang.reflect.Constructor;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import com.google.gson.reflect.TypeToken;
import com.robotechvalley.dhopaelo.R;
import com.robotechvalley.dhopaelo.adapter.order.AllService;
import com.robotechvalley.dhopaelo.databinding.ActivityOrderBinding;
import com.robotechvalley.dhopaelo.domain.AllServiceOneInvoiceModel;
import com.robotechvalley.dhopaelo.domain.view.InvoiceItemModel;
import com.robotechvalley.dhopaelo.domain.view.OrderViewItemModel;

public class OrderActivity extends AppCompatActivity implements
        OrderFirstStageFragment.FirstStageListener,
        OrderSecondStageFragment.SecondStageListener {

    private ActivityOrderBinding binding;

    public static final String FROM_FRAGMENT_NAME = "fragment_name";

    public static final String SERVICE_NAME = "serviceName";
    public static final String SELECT_POSITION = "select_position";
    public static final String INVOICE_DATA = "invoice_data";
    public static final String PENDING_ITEM_KEY = "pendingItemKey";
    public static final String DELIVERY_INFO = "deliveryInfo";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_order);

        setSupportActionBar(binding.toolbar.toolbar);

        String fromFragName = getIntent().getStringExtra(FROM_FRAGMENT_NAME);
        String invoice_data = getIntent().getStringExtra(INVOICE_DATA);

        switch (fromFragName){
            case "HomeFragment":
//                OrderFirstStageFragment orderFirstStageFragment = new OrderFirstStageFragment();
                Bundle bundle = new Bundle();
                bundle.putString(SERVICE_NAME, getIntent().getStringExtra(SERVICE_NAME));
//                orderFirstStageFragment.setArguments(bundle);
//                gotoFragment(orderFirstStageFragment, false);

                AllServiceFragment allServiceFragment = new AllServiceFragment();
                bundle.putString("list", getIntent().getStringExtra("list"));
                bundle.putInt(SELECT_POSITION, getIntent().getIntExtra(SELECT_POSITION, 0));
                allServiceFragment.setArguments(bundle);
                gotoFragment(allServiceFragment, false);
                break;
            case "PendingOrderFragment":
                OrderSecondStageFragment fragment = new OrderSecondStageFragment();
                Bundle bundle1 = new Bundle();
                bundle1.putString(INVOICE_DATA, invoice_data);
                bundle1.putString(FROM_FRAGMENT_NAME, "PendingOrderFragment");
                bundle1.putString(PENDING_ITEM_KEY, getIntent().getStringExtra(PENDING_ITEM_KEY));
                bundle1.putString(DELIVERY_INFO, getIntent().getStringExtra(DELIVERY_INFO));
                fragment.setArguments(bundle1);
                gotoFragment(fragment, false);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + fromFragName);
        }

    }

    private void gotoFragment(Fragment fragment, boolean addBackStack) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction()
                .replace(R.id.orderFragmentContainer, fragment);
        if (!addBackStack) {
            transaction.commit();
        } else {
            transaction.addToBackStack(null).commit();
        }
    }

    @Override
    public void goFirstToSecond(Map<String, InvoiceItemModel> invoiceItemModelMap, String serviceName) {
        OrderSecondStageFragment orderSecondStageFragment = new OrderSecondStageFragment();
        Bundle bundle = new Bundle();
        bundle.putString(SERVICE_NAME, serviceName);
        bundle.putString(FROM_FRAGMENT_NAME, "OrderFirstStageFragment");
        bundle.putString(INVOICE_DATA, new Gson().toJson(invoiceItemModelMap));
        bundle.putString(PENDING_ITEM_KEY, "");
        orderSecondStageFragment.setArguments(bundle);
        gotoFragment(orderSecondStageFragment, true);
    }

    @Override
    public void goSecondToThird(String orderKey) {
        OrderThirdStageFragment orderThirdStageFragment = new OrderThirdStageFragment();
        Bundle bundle = new Bundle();
        bundle.putString("order_key", orderKey);
        orderThirdStageFragment.setArguments(bundle);
        gotoFragment(orderThirdStageFragment, true);
    }

    @Override
    public void goSecondToEditAddress() {
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AllServiceOneInvoiceModel.destroy();
    }
}