package com.dhopaelo.dhopaelo.ui.status;

import static com.dhopaelo.dhopaelo.ui.status.OrderStatusActivity.FROM_FRAGMENT_NAME;
import static com.dhopaelo.dhopaelo.ui.status.OrderStatusActivity.ORDER_DELIVERY_INFO;
import static com.dhopaelo.dhopaelo.ui.status.OrderStatusActivity.ORDER_INVOICE_DATA;
import static com.dhopaelo.dhopaelo.ui.status.OrderStatusActivity.ORDER_PAYMENT_INFO;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.gson.Gson;
import com.dhopaelo.dhopaelo.R;
import com.dhopaelo.dhopaelo.adapter.StatusItemRecyclerView;
import com.dhopaelo.dhopaelo.databinding.FragmentConfirmOrderBinding;
import com.dhopaelo.dhopaelo.domain.DeliveryInfo;
import com.dhopaelo.dhopaelo.domain.OrderModel;
import com.dhopaelo.dhopaelo.domain.view.InvoiceItemModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ConfirmOrderFragment extends Fragment {
    private FragmentConfirmOrderBinding binding;

    private List<String> confirmItemKey;
    private List<DeliveryInfo> deliveryInfoList;

    private List<PaymentInfo> paymentInfoList;

    private ProgressDialog progressDialog;

    public ConfirmOrderFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_confirm_order, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Please wait...");
        progressDialog.show();

        setupData();

        binding.refresh.setOnRefreshListener(() -> {
            setupData();
            binding.refresh.setRefreshing(false);
        });

    }

    private void setupData() {
        FirebaseFirestore.getInstance()
                .collection("app-user")
                .document(FirebaseAuth.getInstance().getUid())
                .collection("order")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .get().addOnSuccessListener(queryDocumentSnapshots -> {
            List<DocumentSnapshot> documents = queryDocumentSnapshots.getDocuments();

            confirmItemKey = new ArrayList<>();
            deliveryInfoList = new ArrayList<>();
            List<Map<String, InvoiceItemModel>> list = new ArrayList<>();
            List<Long> timestamp = new ArrayList<>();
            List<String> serviceName = new ArrayList<>();

            paymentInfoList = new ArrayList<>();
            for (DocumentSnapshot document : documents) {
                OrderModel orderModel = document.toObject(OrderModel.class);
                if (orderModel.getOrderStatus().equals("Confirm")) {
                    confirmItemKey.add(document.getId());
                    deliveryInfoList.add(orderModel.getDeliveryInfo());
                    list.add(orderModel.getInvoiceItemModelMap());
                    timestamp.add(orderModel.getTimestamp().toDate().getTime());
                    serviceName.add(orderModel.getServiceName());

                    paymentInfoList.add(new PaymentInfo(
                            orderModel.getServiceName(),
                            orderModel.getOrderStatus(),
                            orderModel.getPaymentMethod(),
                            orderModel.getPaymentStatus(),
                            orderModel.getTimestamp()));
                }
            }

            adapterSetup(list, timestamp, serviceName);

        });

        progressDialog.dismiss();
    }

    private void adapterSetup(List<Map<String, InvoiceItemModel>> list, List<Long> timestamp, List<String> serviceName) {
        binding.statusItemRecyclerView.setAdapter(new StatusItemRecyclerView(getContext(), list, timestamp, serviceName, position -> {
            Intent intent = new Intent(getContext(), OrderStatusActivity.class);
            intent.putExtra(FROM_FRAGMENT_NAME, "ConfirmOrderFragment");
            intent.putExtra(ORDER_DELIVERY_INFO, new Gson().toJson(deliveryInfoList.get(position)));
            intent.putExtra(ORDER_INVOICE_DATA, new Gson().toJson(list.get(position)));
            intent.putExtra(ORDER_PAYMENT_INFO, new Gson().toJson(paymentInfoList.get(position)));
            startActivity(intent);
        }));
    }

    public class PaymentInfo{
        private String serviceName;
        private String orderStatus;
        private String paymentMethod;
        private String paymentStatus;
        private Timestamp orderTimeStamp;

        public PaymentInfo(String serviceName, String orderStatus, String paymentMethod, String paymentStatus, Timestamp orderTimeStamp) {
            this.serviceName = serviceName;
            this.orderStatus = orderStatus;
            this.paymentMethod = paymentMethod;
            this.paymentStatus = paymentStatus;
            this.orderTimeStamp = orderTimeStamp;
        }

        public String getServiceName() {
            return serviceName;
        }

        public String getOrderStatus() {
            return orderStatus;
        }

        public String getPaymentMethod() {
            return paymentMethod;
        }

        public String getPaymentStatus() {
            return paymentStatus;
        }

        public Timestamp getOrderTimeStamp() {
            return orderTimeStamp;
        }
    }

}