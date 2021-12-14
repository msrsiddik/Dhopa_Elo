package com.amanullah.dhopaelo.ui.status;

import static com.amanullah.dhopaelo.ui.order.OrderActivity.DELIVERY_INFO;
import static com.amanullah.dhopaelo.ui.order.OrderActivity.INVOICE_DATA;
import static com.amanullah.dhopaelo.ui.order.OrderActivity.PENDING_ITEM_KEY;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.gson.Gson;
import com.amanullah.dhopaelo.R;
import com.amanullah.dhopaelo.adapter.PendingOrderRecyclerView;
import com.amanullah.dhopaelo.databinding.FragmentPendingOrderBinding;
import com.amanullah.dhopaelo.domain.DeliveryInfo;
import com.amanullah.dhopaelo.domain.OrderModel;
import com.amanullah.dhopaelo.domain.view.InvoiceItemModel;
import com.amanullah.dhopaelo.ui.order.OrderActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PendingOrderFragment extends Fragment {
    private FragmentPendingOrderBinding binding;

    private List<String> pendingItemKey;
    private List<Map<String, InvoiceItemModel>> list;

    private List<DeliveryInfo> deliveryInfoList;

    private ProgressDialog progressDialog;

    public PendingOrderFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_pending_order, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Please wait...");

        setupData();

        binding.refresh.setOnRefreshListener(() -> {
            setupData();
            binding.refresh.setRefreshing(false);
        });

    }

    private void setupData(){
        progressDialog.show();

        FirebaseFirestore.getInstance()
                .collection("app-user")
                .document(FirebaseAuth.getInstance().getUid())
                .collection("order")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .get().addOnSuccessListener(queryDocumentSnapshots -> {
            List<DocumentSnapshot> documents = queryDocumentSnapshots.getDocuments();

            pendingItemKey = new ArrayList<>();
            list = new ArrayList<>();
            List<Long> timestamp = new ArrayList<>();
            List<String> serviceName = new ArrayList<>();

            deliveryInfoList = new ArrayList<>();

            for (DocumentSnapshot document : documents) {
                OrderModel orderModel = document.toObject(OrderModel.class);
                if (orderModel.getOrderStatus().equals("pending")) {
                    pendingItemKey.add(document.getId());
                    list.add(orderModel.getInvoiceItemModelMap());
                    timestamp.add(orderModel.getTimestamp().toDate().getTime());
                    serviceName.add(orderModel.getServiceName());

                    deliveryInfoList.add(orderModel.getDeliveryInfo());
                    Log.d("TAG", "setupData: "+orderModel.getDeliveryInfo().getUserAddress().toString());
                }
            }

            adapterSetup(list, timestamp, serviceName);
        });

        progressDialog.dismiss();

    }

    private PendingOrderRecyclerView adapter;

    private void adapterSetup(List<Map<String, InvoiceItemModel>> list, List<Long> timestamp, List<String> serviceName) {
        adapter = new PendingOrderRecyclerView(getContext(), list, timestamp, serviceName, new PendingOrderRecyclerView.PendingItemClickListener() {
            @Override
            public void oneClickListenerItemPosition(int i) {
                Intent intent = new Intent(getContext(), OrderActivity.class);
                intent.putExtra(OrderActivity.FROM_FRAGMENT_NAME, "PendingOrderFragment");
                intent.putExtra(INVOICE_DATA, new Gson().toJson(list.get(i)));
                intent.putExtra(PENDING_ITEM_KEY, pendingItemKey.get(i));
                intent.putExtra(DELIVERY_INFO, new Gson().toJson(deliveryInfoList.get(i)));
                startActivity(intent);
            }

            @Override
            public void longClickListenerItemPosition(int l) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Delete");
                //builder.setIcon(R.drawable.alert);
                builder.setMessage("Do you want to delete this pending order?");
                builder.setPositiveButton("Yes", (dialog, which) -> {
                    Toast.makeText(getContext(), "Your Order is Successfully Deleted", Toast.LENGTH_LONG).show();
                    FirebaseFirestore.getInstance()
                            .collection("app-user")
                            .document(FirebaseAuth.getInstance().getUid())
                            .collection("order")
                            .document(pendingItemKey.get(l)).delete();

                    clearDeleteData(l);

                }).setNegativeButton("No", (dialog, which) -> {
                    Toast.makeText(getContext(), "Your Order is in Pending Order", Toast.LENGTH_LONG).show();
                }).show();
            }
        });

        if (getActivity()!=null) {
            binding.pendingOrderRecyclerView.setAdapter(adapter);
        }
    }

    private void clearDeleteData(int l){
        adapter.removedItem(l);
    }

}