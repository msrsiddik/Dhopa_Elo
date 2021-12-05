package com.robotechvalley.dhopaelo.ui.order;

import static com.robotechvalley.dhopaelo.ui.order.OrderActivity.SERVICE_NAME;
import static com.robotechvalley.dhopaelo.util.ServicePriceConstant.DRY_WASH_AND_IRON_PRICE;
import static com.robotechvalley.dhopaelo.util.ServicePriceConstant.DRY_WASH_PRICE;
import static com.robotechvalley.dhopaelo.util.ServicePriceConstant.IRON_PRICE;
import static com.robotechvalley.dhopaelo.util.ServicePriceConstant.ITEM_NAME;
import static com.robotechvalley.dhopaelo.util.ServicePriceConstant.WASH_PRICE;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.robotechvalley.dhopaelo.R;
import com.robotechvalley.dhopaelo.databinding.FragmentOrderFirstStageBinding;
import com.robotechvalley.dhopaelo.databinding.ListItemBinding;
import com.robotechvalley.dhopaelo.databinding.OrderItemBinding;
import com.robotechvalley.dhopaelo.domain.ProductInfoModel;
import com.robotechvalley.dhopaelo.domain.view.InvoiceItemModel;
import com.robotechvalley.dhopaelo.text.drawable.ColorGenerator;
import com.robotechvalley.dhopaelo.text.drawable.TextDrawable;
import com.robotechvalley.dhopaelo.ui.ToolBarSetup;

public class OrderFirstStageFragment extends Fragment {
    private FragmentOrderFirstStageBinding binding;

    private FirebaseFirestore firestore;

    private List<ProductInfoModel> productInfoModels = new ArrayList<>();

    private int counter = 0;
    private int totalProduct = 0;
    private double totalPrice = 0;

    public OrderFirstStageFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        firestore = FirebaseFirestore.getInstance();
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_order_first_stage, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        String serviceName = getArguments().getString(SERVICE_NAME);

        binding.serviceTitle.setVisibility(View.GONE);
        binding.serviceTitle.setText(serviceName);

        ToolBarSetup.setTitle(getActivity(), serviceName, false);

        firestore.collection("service-price").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                productInfoModels.clear();
                for (QueryDocumentSnapshot snapshot : task.getResult()) {
                    Map<String, Object> data = snapshot.getData();
                    ProductInfoModel productInfoModel = null;
                    switch (serviceName) {
                        case "Wet Wash":
                            if (!data.get(WASH_PRICE).equals("")) {
                                productInfoModel = new ProductInfoModel(data.get(ITEM_NAME).toString(),
                                        Double.parseDouble(data.get(WASH_PRICE).toString()));
                            }
                            break;
                        case "Wet Iron":
                            if (!data.get(IRON_PRICE).equals("")) {
                                productInfoModel = new ProductInfoModel(data.get(ITEM_NAME).toString(),
                                        Double.parseDouble(data.get(IRON_PRICE).toString()));
                            }
                            break;
                        case "Wet Wash & Iron":
                            if (!data.get(WASH_PRICE).equals("") && !data.get(IRON_PRICE).equals("")) {
                                productInfoModel = new ProductInfoModel(data.get(ITEM_NAME).toString(),
                                        Double.parseDouble(data.get(WASH_PRICE).toString()) + Double.parseDouble(data.get(IRON_PRICE).toString()));
                            }
                            break;
                        case "Dry Wash":
                            if (!data.get(DRY_WASH_PRICE).equals("")) {
                                productInfoModel = new ProductInfoModel(data.get(ITEM_NAME).toString(),
                                        Double.parseDouble(data.get(DRY_WASH_PRICE).toString()));
                            }
                            break;
                        case "Dry Wash & Iron":
                            if (!data.get(DRY_WASH_AND_IRON_PRICE).equals("")) {
                                productInfoModel = new ProductInfoModel(data.get(ITEM_NAME).toString(),
                                        Double.parseDouble(data.get(DRY_WASH_AND_IRON_PRICE).toString()));
                            }
                            break;

                    }
                    if (productInfoModel != null) {
                        productInfoModels.add(productInfoModel);
                    }
                }
            }
        });

        addItemToContainer(binding.ItemContainer);

        binding.addItemBtn.setOnClickListener(v -> {
            addItemToContainer(binding.ItemContainer);
        });

        binding.totalQuantity.getEditText().setText("00");
        binding.totalAmount.getEditText().setText("00.00");

        binding.confirmBtn.setOnClickListener(v -> {
            FirstStageListener firstStageListener = (FirstStageListener) getActivity();

            Map<String, InvoiceItemModel> invoiceItemModelMap = new HashMap<>();

            for (int i = 1; i < binding.ItemContainer.getChildCount(); i++) {
                AutoCompleteTextView itemName = binding.ItemContainer.getChildAt(i).findViewById(R.id.productList);
                TextInputEditText quantity = binding.ItemContainer.getChildAt(i).findViewById(R.id.quantity);

                for (ProductInfoModel infoModel : productInfoModels) {
                    if (infoModel.getName().equals(itemName.getText().toString())) {
                        if (quantity.getText().toString() != null && !quantity.getText().toString().isEmpty()) {
                            int itemQty = Integer.parseInt(quantity.getText().toString());
                            double unitPrice = infoModel.getPrice();

                            invoiceItemModelMap.put(i + "",
                                    new InvoiceItemModel(
                                            itemName.getText().toString(),
                                            itemQty,
                                            unitPrice,
                                            itemQty * unitPrice));
                        } else {
                            Toast.makeText(getContext(), "insert quantity", Toast.LENGTH_SHORT).show();
                            return;
                        }

                    }
                }
            }

            if (!invoiceItemModelMap.isEmpty()) {
                if(totalPrice != 0.0) {
                    firstStageListener.goFirstToSecond(invoiceItemModelMap, serviceName);
                }
            } else {
                Toast.makeText(getContext(), "Please add minimum 1 item", Toast.LENGTH_SHORT).show();
            }

        });
    }

    private void addItemToContainer(LinearLayout itemContainer) {
        OrderItemBinding itemBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.order_item, itemContainer, false);

        itemBinding.counter.setText(++counter + "");

        final ProductInfoModel[] product = new ProductInfoModel[1];

        itemBinding.closeCard.setOnClickListener(v -> {
            String s1 = itemBinding.quantity.getText().toString();
            if (!s1.isEmpty()) {
                int i = Integer.parseInt(s1);
                totalProduct -= i;
                totalPrice -= i * product[0].getPrice();
                binding.totalQuantity.getEditText().setText(totalProduct + "");
                binding.totalAmount.getEditText().setText(totalPrice + "");
            }
            itemContainer.removeView(itemBinding.getRoot());
            --counter;
        });

        List<String> list = new ArrayList<>();
        for (ProductInfoModel infoModel : productInfoModels) {
            list.add(infoModel.getName());
        }

        ProductListAdapter listAdapter = new ProductListAdapter(getContext(), R.layout.list_item, productInfoModels);
        itemBinding.productList.setAdapter(listAdapter);

        itemBinding.quantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (!s.toString().isEmpty()) {
                    int i = Integer.parseInt(s.toString());
                    totalProduct -= i;
                    totalPrice -= i * product[0].getPrice();
                    binding.totalQuantity.getEditText().setText(totalProduct + "");
                    binding.totalAmount.getEditText().setText(totalPrice + "");
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().isEmpty()) {
                    int i = Integer.parseInt(s.toString());
                    totalProduct += i;
                    totalPrice += i * product[0].getPrice();
                    binding.totalQuantity.getEditText().setText(totalProduct + "");
                    binding.totalAmount.getEditText().setText(totalPrice + "");
                }
            }
        });

        itemBinding.productList.setOnItemClickListener((parent, view, position, id) -> {
            product[0] = productInfoModels.get(position);
            itemBinding.perItemRate.setText(String.format("%s per item %s tk", product[0].getName(), product[0].getPrice()));
            itemBinding.quantity.setEnabled(true);
        });
        itemContainer.addView(itemBinding.getRoot());
    }

    private class ProductListAdapter extends ArrayAdapter<ProductInfoModel> {
        private Context context;
        private int resource;
        private List<ProductInfoModel> list;

        public ProductListAdapter(@NonNull Context context, int resource, @NonNull List<ProductInfoModel> objects) {
            super(context, resource, objects);
            this.context = context;
            this.resource = resource;
            this.list = objects;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            ListItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), resource, parent, false);
            ProductInfoModel productInfoModel = list.get(position);
            TextDrawable textDrawable = TextDrawable.builder()
                    .buildRoundRect(productInfoModel.getName().substring(0, 2),
                            ColorGenerator.MATERIAL.getRandomColor(),
                            30);
            binding.imageView.setImageDrawable(textDrawable);
            binding.textView.setText(productInfoModel.getName());
            return binding.getRoot();
        }
    }

    public interface FirstStageListener {
        void goFirstToSecond(Map<String, InvoiceItemModel> invoiceItemModelMap, String serviceName);
    }
}