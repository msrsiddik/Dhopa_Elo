package com.robotechvalley.dhopaelo.ui.order;

import static com.robotechvalley.dhopaelo.util.OrderModelConstant.*;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.robotechvalley.dhopaelo.R;
import com.robotechvalley.dhopaelo.databinding.FragmentOrderThirdStageBinding;
import com.robotechvalley.dhopaelo.domain.OrderModel;

import java.util.HashMap;
import java.util.Map;

public class OrderThirdStageFragment extends Fragment {

    private FragmentOrderThirdStageBinding binding;

    private double totalAmount = 0;

    private OrderModel orderModel;

    private String order_key;

    Dialog dialog;

    public OrderThirdStageFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Toast.makeText(getContext(), "Your Order Saved in Pending Order", Toast.LENGTH_LONG).show();
                getActivity().finish();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_order_third_stage, container, false);
        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        Bundle bundle = getArguments();

        dialog = new Dialog(getContext());

        if (bundle != null) {
            order_key = bundle.getString("order_key");

            FirebaseFirestore.getInstance()
                    .collection("app-user")
                    .document(FirebaseAuth.getInstance().getUid())
                    .collection("order")
                    .document(order_key)
                    .get().addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot.exists()) {
                    orderModel = documentSnapshot.toObject(OrderModel.class);
                }
            });

        }

        binding.confirmOrderBtn.setOnClickListener(v -> {
            int paymentMethod = binding.paymentGroup.getCheckedRadioButtonId();

            if (paymentMethod == binding.onlinePay.getId() && order_key != null) {
//                Toast.makeText(getContext(), "coming soon...", Toast.LENGTH_SHORT).show();

                dialog.setContentView(R.layout.coming_soon);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                ImageView imageView = dialog.findViewById(R.id.imageView);
                dialog.show();

//                AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
//                dialog.setTitle("Coming Soon");
//                //dialog.setMessage("Our Online Payment Service is Coming Soon");
//                dialog.setIcon(R.drawable.coming_soon_1);
//
//                dialog.setPositiveButton("Ok", (dialog1, which) -> {
//                    getActivity().finish();
//                }).show();

//                if (orderModel != null) {
//
//                    SSLCommerzInitialization sslCommerzInitialization = new SSLCommerzInitialization(
//                            getResources().getString(R.string.store_id), getResources().getString(R.string.store_password),
//                            totalAmount, SSLCCurrencyType.BDT, order_key,
//                            "laundry", SSLCSdkType.TESTBOX
//                    );
//
//                    IntegrateSSLCommerz.getInstance(getContext())
//                            .addSSLCommerzInitialization(sslCommerzInitialization)
//                            .buildApiCall(new SSLCTransactionResponseListener() {
//                                @Override
//                                public void transactionSuccess(SSLCTransactionInfoModel sslcTransactionInfoModel) {
//                                    confirmOrder("Paid", "SSL");
//                                    getActivity().finish();
//                                }
//
//                                @Override
//                                public void transactionFail(String s) {
//
//                                }
//
//                                @Override
//                                public void merchantValidationError(String s) {
//
//                                }
//                            });
//
//                }

            } else if (paymentMethod == binding.cashPay.getId()) {

                confirmOrder("Unpaid", "Cash");

            } else {
                throw new IllegalStateException("Unexpected value: " + paymentMethod);
            }
        });
    }

    private void confirmOrder(String paymentStatus, String paymentMethod) {
        Map<String, Object> map = new HashMap<>();
        map.put(INVOICE_ITEM_MODEL_MAP,orderModel.getInvoiceItemModelMap());
        map.put(TIMESTAMP, FieldValue.serverTimestamp());
        map.put(PAYMENT_STATUS, paymentStatus);
        map.put(PAYMENT_METHOD, paymentMethod);
        map.put(ORDER_STATUS, "Confirm");

        FirebaseFirestore.getInstance()
                .collection("app-user")
                .document(FirebaseAuth.getInstance().getUid())
                .collection("order")
                .document(order_key)
                .update(map).addOnSuccessListener(command -> {
            Toast.makeText(getContext(), "Successfully order complete", Toast.LENGTH_SHORT).show();
            getActivity().finish();
        });
    }
}