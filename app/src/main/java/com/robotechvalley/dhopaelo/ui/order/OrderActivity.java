package com.robotechvalley.dhopaelo.ui.order;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.robotechvalley.dhopaelo.R;
import com.robotechvalley.dhopaelo.databinding.ActivityOrderBinding;
import com.robotechvalley.dhopaelo.domain.AllServiceOneInvoiceModel;
import com.robotechvalley.dhopaelo.domain.OrderModel;

import java.util.ArrayList;
import java.util.List;

public class OrderActivity extends AppCompatActivity implements
        OrderSecondStageFragment.SecondStageListener{

    public static final List<OrderActivity> oa = new ArrayList<>();

    private ActivityOrderBinding binding;

    public static final String FROM_FRAGMENT_NAME = "fragment_name";

    public static final String SERVICE_NAME = "serviceName";
    public static final String SELECT_POSITION = "select_position";
    public static final String INVOICE_DATA = "invoice_data";
    public static final String PENDING_ITEM_KEY = "pendingItemKey";
    public static final String DELIVERY_INFO = "deliveryInfo";

    public static boolean isOfferEligible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_order);
        oa.add(this);
        setSupportActionBar(binding.toolbar.toolbar);

        String fromFragName = getIntent().getStringExtra(FROM_FRAGMENT_NAME);
        String invoice_data = getIntent().getStringExtra(INVOICE_DATA);

        switch (fromFragName){
            case "HomeFragment":
                offerEligible();
                AllServiceFragment allServiceFragment = new AllServiceFragment();
                Bundle bundle = new Bundle();
                bundle.putString(SERVICE_NAME, getIntent().getStringExtra(SERVICE_NAME));
                bundle.putString("list", getIntent().getStringExtra("list"));
                bundle.putInt(SELECT_POSITION, getIntent().getIntExtra(SELECT_POSITION, 0));
                allServiceFragment.setArguments(bundle);
                gotoFragment(allServiceFragment, false);
                break;
            case "OrderFirstStageFragment":
                OrderSecondStageFragment orderSecondStageFragment = new OrderSecondStageFragment();
                Bundle bundle2 = new Bundle();
                bundle2.putString(SERVICE_NAME, getIntent().getStringExtra(SERVICE_NAME));
                bundle2.putString(FROM_FRAGMENT_NAME, "OrderFirstStageFragment");
                bundle2.putString(INVOICE_DATA, getIntent().getStringExtra(INVOICE_DATA));
                bundle2.putString(PENDING_ITEM_KEY, "");
                orderSecondStageFragment.setArguments(bundle2);
                gotoFragment(orderSecondStageFragment, false);
                break;
            case "PendingOrderFragment":
                offerEligible();
                OrderSecondStageFragment fragment = new OrderSecondStageFragment();
                Bundle bundle1 = new Bundle();
                bundle1.putString(INVOICE_DATA, invoice_data);
                bundle1.putString(SERVICE_NAME, getIntent().getStringExtra(SERVICE_NAME));
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

    private void offerEligible(){
        new Thread(() -> {
            FirebaseFirestore.getInstance()
                    .collection("app-user")
                    .document(FirebaseAuth.getInstance().getUid())
                    .collection("order")
                    .orderBy("timestamp", Query.Direction.DESCENDING)
                    .get().addOnSuccessListener(queryDocumentSnapshots -> {
                List<DocumentSnapshot> documents = queryDocumentSnapshots.getDocuments();

                final List<OrderModel> orderModels = new ArrayList<>();

                for (DocumentSnapshot document : documents) {
                    OrderModel orderModel = document.toObject(OrderModel.class);
                    if (orderModel.getOrderStatus().equals("Confirm")) {
                        orderModels.add(orderModel);
                    }
                }

                isOfferEligible = false;

                if (orderModels.isEmpty()){
                    isOfferEligible = true;
                }

            });
        }).start();
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
    public void goSecondToThird(String orderKey) {
        OrderThirdStageFragment orderThirdStageFragment = new OrderThirdStageFragment();
        Bundle bundle = new Bundle();
        bundle.putString("order_key", orderKey);
        orderThirdStageFragment.setArguments(bundle);
        gotoFragment(orderThirdStageFragment, true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AllServiceOneInvoiceModel.destroy();
    }
}