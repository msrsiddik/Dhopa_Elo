package com.robotechvalley.dhopaelo.ui.status;

import static com.robotechvalley.dhopaelo.ui.status.OrderStatusActivity.ORDER_DELIVERY_INFO;
import static com.robotechvalley.dhopaelo.ui.status.OrderStatusActivity.ORDER_INVOICE_DATA;
import static com.robotechvalley.dhopaelo.ui.status.OrderStatusActivity.ORDER_PAYMENT_INFO;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Map;

import com.robotechvalley.dhopaelo.R;
import com.robotechvalley.dhopaelo.databinding.FragmentInvoiceBinding;
import com.robotechvalley.dhopaelo.databinding.InvoiceItemViewBinding;
import com.robotechvalley.dhopaelo.domain.DeliveryInfo;
import com.robotechvalley.dhopaelo.domain.view.InvoiceItemModel;


public class InvoiceFragment extends Fragment {
    private FragmentInvoiceBinding binding;

    public InvoiceFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_invoice, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        Bundle bundle = getArguments();
        if (bundle != null) {
            String deliveryInfo = bundle.getString(ORDER_DELIVERY_INFO);
            String invoiceData = bundle.getString(ORDER_INVOICE_DATA);
            String paymentData = bundle.getString(ORDER_PAYMENT_INFO);

            Type type = new TypeToken<Map<String, InvoiceItemModel>>() {}.getType();
            Map<String, InvoiceItemModel> invoiceItemModelMap = new Gson().fromJson(invoiceData, type);

            double totalPrice = 0;
            for (InvoiceItemModel value : invoiceItemModelMap.values()) {
                addItemInInvoice(binding.invoice.itemContainer, value);
                totalPrice += value.gettPrice();
            }

            binding.invoice.totalPrice.setText(String.format("Tk %.2f", totalPrice));
            binding.totalPrice.setText(String.format("Total Price: ৳ %.2f", (totalPrice)));

            DeliveryInfo info = new Gson().fromJson(deliveryInfo, DeliveryInfo.class);
            binding.name.setText(info == null ? "null" : info.getUserInfo().getName());
            binding.address.setText(info == null ? "null" : info.getUserAddress().toString());
            binding.phone.setText(info == null ? "null" : info.getUserInfo().getPhoneNumber());

            ConfirmOrderFragment.PaymentInfo paymentInfo = new Gson().fromJson(paymentData, ConfirmOrderFragment.PaymentInfo.class);

            binding.paymentStatus.setText("Payment Status: "+paymentInfo.getPaymentStatus()+", Payment method: "+paymentInfo.getPaymentMethod());

        }

        binding.orderTrackingBtn.setOnClickListener(v -> {
            InvoiceFragmentToStatusTimeLine statusTimeLine = (InvoiceFragmentToStatusTimeLine) getActivity();
            statusTimeLine.goStatusTimeLine();
        });
    }

    private void addItemInInvoice(LinearLayout itemContainer, InvoiceItemModel itemModel) {
        InvoiceItemViewBinding itemViewBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.invoice_item_view, itemContainer, false);

        itemViewBinding.itemName.setText(itemModel.getItemName());
        itemViewBinding.itemQuantity.setText(itemModel.getItemQuantity() + "");
        itemViewBinding.price.setText(itemModel.gettPrice() + "");

        itemContainer.addView(itemViewBinding.getRoot());
    }

    public interface InvoiceFragmentToStatusTimeLine{
        void goStatusTimeLine();
    }
}