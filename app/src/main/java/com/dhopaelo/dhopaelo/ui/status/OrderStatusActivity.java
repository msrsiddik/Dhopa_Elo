package com.dhopaelo.dhopaelo.ui.status;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.dhopaelo.dhopaelo.R;

public class OrderStatusActivity extends AppCompatActivity implements InvoiceFragment.InvoiceFragmentToStatusTimeLine {
    public static final String FROM_FRAGMENT_NAME = "fragment_name";
    public static final String ORDER_INVOICE_DATA = "invoice_data";
    public static final String ORDER_DELIVERY_INFO = "deliveryInfo";
    public static final String ORDER_PAYMENT_INFO = "orderPaymentInfo";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_status);

        String fromFragName = getIntent().getStringExtra(FROM_FRAGMENT_NAME);

        switch (fromFragName){
            case "ConfirmOrderFragment" :
                InvoiceFragment invoiceFragment = new InvoiceFragment();
                Bundle bundle = new Bundle();
                bundle.putString(ORDER_DELIVERY_INFO, getIntent().getStringExtra(ORDER_DELIVERY_INFO));
                bundle.putString(ORDER_INVOICE_DATA, getIntent().getStringExtra(ORDER_INVOICE_DATA));
                bundle.putString(ORDER_PAYMENT_INFO, getIntent().getStringExtra(ORDER_PAYMENT_INFO));
                invoiceFragment.setArguments(bundle);
                gotoFragment(invoiceFragment, false);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + fromFragName);
        }

    }

    private void gotoFragment(Fragment fragment, boolean setBackStack){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction()
                .replace(R.id.statusFragmentContainer, fragment);
        if (!setBackStack){
            transaction.commit();
        } else {
            transaction.addToBackStack(null)
                    .commit();
        }


    }

    @Override
    public void goStatusTimeLine() {
        StatusTimelineFragment statusTimelineFragment = new StatusTimelineFragment();
        gotoFragment(statusTimelineFragment, true);
    }
}