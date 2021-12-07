package com.robotechvalley.dhopaelo.ui.order;

import static com.robotechvalley.dhopaelo.ui.order.OrderActivity.DELIVERY_INFO;
import static com.robotechvalley.dhopaelo.ui.order.OrderActivity.FROM_FRAGMENT_NAME;
import static com.robotechvalley.dhopaelo.ui.order.OrderActivity.INVOICE_DATA;
import static com.robotechvalley.dhopaelo.ui.order.OrderActivity.PENDING_ITEM_KEY;
import static com.robotechvalley.dhopaelo.ui.order.OrderActivity.SERVICE_NAME;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.robotechvalley.dhopaelo.R;
import com.robotechvalley.dhopaelo.databinding.AddressEditBinding;
import com.robotechvalley.dhopaelo.databinding.FragmentOrderSecondStageBinding;
import com.robotechvalley.dhopaelo.databinding.InvoiceItemViewBinding;
import com.robotechvalley.dhopaelo.domain.DeliveryInfo;
import com.robotechvalley.dhopaelo.domain.UserAddress;
import com.robotechvalley.dhopaelo.domain.UserInfo;
import com.robotechvalley.dhopaelo.domain.view.InvoiceItemModel;
import com.robotechvalley.dhopaelo.util.OrderModelConstant;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class OrderSecondStageFragment extends Fragment {
    private FragmentOrderSecondStageBinding binding;

    private String serviceName;
    private String pendingItemKey = "";

    private CollectionReference profileInfoDB;

    private Map<String, InvoiceItemModel> invoiceItemModelMap;
    private UserAddress userAddress;
    private UserInfo userInfo;

    public OrderSecondStageFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        profileInfoDB = FirebaseFirestore.getInstance().collection("app-user")
                .document(FirebaseAuth.getInstance().getUid()).collection("profile");
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_order_second_stage, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        Bundle bundle = getArguments();
        if (bundle != null) {
            String fromFragment = bundle.getString(FROM_FRAGMENT_NAME);
            String invoiceData = bundle.getString(INVOICE_DATA);
            serviceName = bundle.getString(SERVICE_NAME);
            pendingItemKey = bundle.getString(PENDING_ITEM_KEY);
            String dInfoData = bundle.getString(DELIVERY_INFO);

            DeliveryInfo deliveryInfo = new Gson().fromJson(dInfoData, DeliveryInfo.class);
            if (deliveryInfo != null) {
                binding.address.setText(deliveryInfo.getUserAddress().toString());
            } else {
                setDeliveryInfo();
            }

            Type type = new TypeToken<Map<String, InvoiceItemModel>>() {}.getType();
            invoiceItemModelMap = new Gson().fromJson(invoiceData, type);

            double totalPrice = 0;
            for (InvoiceItemModel value : invoiceItemModelMap.values()) {
                addItemInInvoice(binding.invoice.itemContainer, value);
                totalPrice += value.gettPrice();
            }

            binding.invoice.totalPrice.setText(String.format("Tk %.2f", totalPrice));
            binding.totalPrice.setText(String.format("Total Price : ৳ %.2f", (totalPrice)));

            backPressHandle(fromFragment);

        }

        SecondStageListener secondStageListener = (SecondStageListener) getActivity();

        binding.placeOrderBtn.setOnClickListener(v -> {
            if (pendingItemKey.equals("")) {
                addNameField();
                pendingOrderPlace(secondStageListener);
            } else {
                addNameField();
                secondStageListener.goSecondToThird(pendingItemKey);
            }
        });


//        setDeliveryInfo();
        binding.addressEditBtn.setOnClickListener(v -> {

            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            AddressEditBinding binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.address_edit, null, false);
            builder.setView(binding.getRoot());

            AlertDialog show = builder.show();

            binding.address.saveBtn.setOnClickListener(v1 -> {
                String name = binding.name.getEditText().getText().toString();
                String number = binding.phoneNumber.getEditText().getText().toString();
                String areaName = binding.address.areaName.getEditText().getText().toString();
                String sectorNo = binding.address.sectorNo.getEditText().getText().toString();
                String roadNo = binding.address.roadNo.getEditText().getText().toString();
                String houseNo = binding.address.houseNo.getEditText().getText().toString();
                String flatNo = binding.address.flatNo.getEditText().getText().toString();

                userInfo = new UserInfo(name, number, userInfo.getEmail());
                userAddress = new UserAddress(areaName, sectorNo, roadNo, houseNo, flatNo);

                this.binding.name.setText(userInfo.getName());
                this.binding.phone.setText(userInfo.getPhoneNumber());
                this.binding.address.setText(userAddress.toString());

                show.dismiss();
            });

        });
    }

    private void setDeliveryInfo(){
        binding.name.setText(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());

        profileInfoDB.document("user-info").get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                UserInfo userInfo = documentSnapshot.toObject(UserInfo.class);
                this.userInfo = userInfo;
                binding.phone.setText(userInfo.getPhoneNumber());
            }
        });

        profileInfoDB.document("address").get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                UserAddress userAddress = documentSnapshot.toObject(UserAddress.class);
                this.userAddress = userAddress;
                binding.address.setText(userAddress.toString());
            }
        });
    }

    private void pendingOrderPlace(SecondStageListener secondStageListener) {
        Map<String, Object> map = new HashMap<>();
        map.put(OrderModelConstant.INVOICE_ITEM_MODEL_MAP, invoiceItemModelMap);
        map.put(OrderModelConstant.TIMESTAMP, FieldValue.serverTimestamp());
        map.put(OrderModelConstant.ORDER_STATUS, "pending");
        map.put(OrderModelConstant.SERVICE_NAME, serviceName);
        map.put(OrderModelConstant.DELIVERY_INFO, new DeliveryInfo(userInfo, userAddress));

        if (userAddress != null && userInfo != null) {
            FirebaseFirestore.getInstance()
                    .collection("app-user")
                    .document(FirebaseAuth.getInstance().getUid())
                    .collection("order")
                    .add(map)
                    .addOnSuccessListener(documentReference -> {
                        if (secondStageListener != null) {
                            secondStageListener.goSecondToThird(documentReference.getId());
                        } else {
                            getActivity().finish();
                        }
                    }).addOnFailureListener(e -> {
                Toast.makeText(getContext(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
            });
        }
    }

    private void addItemInInvoice(LinearLayout itemContainer, InvoiceItemModel itemModel) {
        InvoiceItemViewBinding itemViewBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.invoice_item_view, itemContainer, false);

        itemViewBinding.itemName.setText(itemModel.getItemName());
        itemViewBinding.itemQuantity.setText(itemModel.getItemQuantity() + "");
        itemViewBinding.price.setText(itemModel.gettPrice() + "");

        itemContainer.addView(itemViewBinding.getRoot());
    }

    private void backPressHandle(String fragmentName) {
        getActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (fragmentName == "OrderFirstStageFragment") {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
                    dialog.setTitle("Save");
                    dialog.setMessage("Do you want to save your invoice?");

                    dialog.setPositiveButton("Yes", (dialog1, which) -> {
                        Toast.makeText(getContext(), "Your order is saved in pending order", Toast.LENGTH_LONG).show();
                        pendingOrderPlace(null);
                    }).setNegativeButton("No", (dialog1, which) -> {
                        Toast.makeText(getContext(), "Your order is canceled", Toast.LENGTH_LONG).show();
                        getActivity().finish();
                    }).show();
                } else if (fragmentName.equals("PendingOrderFragment")){
                    getActivity().finish();
                }

            }
        });
    }

    private void addNameField(){
        FirebaseFirestore.getInstance().collection("app-user").document(FirebaseAuth.getInstance().getUid())
                .set(new HashMap<String, String>(Collections.singletonMap("name",FirebaseAuth.getInstance().getCurrentUser().getDisplayName())));

    }

    public interface SecondStageListener {
        void goSecondToThird(String orderKey);
        void goSecondToEditAddress();
    }
}