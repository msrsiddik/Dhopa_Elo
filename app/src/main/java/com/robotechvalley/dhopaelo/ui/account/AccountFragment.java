package com.robotechvalley.dhopaelo.ui.account;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.robotechvalley.dhopaelo.R;
import com.robotechvalley.dhopaelo.databinding.AddressInputBinding;
import com.robotechvalley.dhopaelo.databinding.FragmentAccountBinding;
import com.robotechvalley.dhopaelo.domain.UserAddress;
import com.robotechvalley.dhopaelo.domain.UserInfo;
import com.robotechvalley.dhopaelo.ui.ToolBarSetup;

public class AccountFragment extends Fragment {
    private FragmentAccountBinding binding;

    private CollectionReference profileInfoDB;

    public AccountFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        profileInfoDB = FirebaseFirestore.getInstance().collection("app-user")
                .document(FirebaseAuth.getInstance().getUid()).collection("profile");
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_account, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        ToolBarSetup.setTitle(getActivity(), "Profile", true);
        setHasOptionsMenu(true);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        assert currentUser != null;

        Glide.with(getContext())
                .load(currentUser.getPhotoUrl())
                .override(100)
                .circleCrop()
                .into(binding.profileImage);

        setProfileInfo();

        binding.userName.getEditText().setText(currentUser.getDisplayName());
        binding.userEmail.getEditText().setText(currentUser.getEmail());
        binding.userPhone.getEditText().setText(currentUser.getPhoneNumber());

        binding.userPhone.getEditText().setOnClickListener(v -> {
            AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
            TextInputLayout inputLayout = new TextInputLayout(dialog.getContext());
            TextInputEditText editText = new TextInputEditText(dialog.getContext());
            editText.setInputType(InputType.TYPE_CLASS_PHONE);
            editText.setText(binding.userPhone.getEditText().getText());
            inputLayout.addView(editText);
            inputLayout.setStartIconDrawable(R.drawable.phone_icon);
            dialog.setView(inputLayout);
            dialog.setTitle("Phone Number");
            dialog.setCancelable(false);


            dialog.setPositiveButton("Save", (dialog1, which) -> {

                profileInfoDB.document("user-info")
                        .set(new UserInfo(
                                currentUser.getDisplayName(),
                                inputLayout.getEditText().getText().toString(),
                                currentUser.getEmail())
                        );

                binding.userPhone.getEditText().setText(editText.getText().toString());
            }).setNegativeButton("Cancel", (dialog1, which) -> {

            }).show();
        });

        binding.userAddress.getEditText().setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            AddressInputBinding addressInputBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.address_input, null, false);
            builder.setView(addressInputBinding.getRoot());

            builder.setTitle("Address");
            AlertDialog dialog = builder.show();

            addressInputBinding.saveBtn.setOnClickListener(v1 -> {
                String areaName = addressInputBinding.areaName.getEditText().getText().toString();
                String sectorNo = addressInputBinding.sectorNo.getEditText().getText().toString();
                String roadNo = addressInputBinding.roadNo.getEditText().getText().toString();
                String houseNo = addressInputBinding.houseNo.getEditText().getText().toString();
                String flatNo = addressInputBinding.flatNo.getEditText().getText().toString();

                profileInfoDB.document("address").set(new UserAddress(
                        areaName, sectorNo, roadNo, houseNo, flatNo
                ));

                setProfileInfo();
                dialog.dismiss();
            });

        });

    }

    private void setProfileInfo() {
        profileInfoDB.document("user-info").get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                UserInfo userInfo = documentSnapshot.toObject(UserInfo.class);
                binding.userPhone.getEditText().setText(userInfo.getPhoneNumber());
            }
        });

        profileInfoDB.document("address").get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                UserAddress userAddress = documentSnapshot.toObject(UserAddress.class);
                binding.userAddress.getEditText().setText(userAddress.toString());
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.add("Logout");
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            getActivity().onBackPressed();
            return true;
        }
        if (item.getTitle().equals("Logout")) {
            FirebaseAuth.getInstance().signOut();
            ActivityManager systemService = (ActivityManager) getContext().getSystemService(Context.ACTIVITY_SERVICE);
            systemService.clearApplicationUserData();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateButtonEnable(TextInputLayout inputLayout) {
        inputLayout.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.updateButton.setVisibility(View.VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

}