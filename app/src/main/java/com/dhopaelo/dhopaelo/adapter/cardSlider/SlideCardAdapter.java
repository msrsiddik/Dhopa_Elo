package com.dhopaelo.dhopaelo.adapter.cardSlider;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.github.islamkhsh.CardSliderAdapter;
import com.dhopaelo.dhopaelo.R;
import com.dhopaelo.dhopaelo.databinding.CardSliderViewBinding;

import java.io.File;

public class SlideCardAdapter extends CardSliderAdapter<SlideCardAdapter.ViewHolder> {
    private final Context context;
    private final File[] list;

    public SlideCardAdapter(Context context, File[] list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public void bindVH(ViewHolder viewHolder, int i) {
        File sliderImage = list[i];
//        Glide.with(context).load(sliderImage).into(viewHolder.binding.image);

        Glide.with(context).load(sliderImage).into(viewHolder.binding.image);

//        sliderImage.getDownloadUrl().addOnSuccessListener(uri -> {
//            Glide.with(context.getApplicationContext()).load(uri).diskCacheStrategy(DiskCacheStrategy.ALL).into(viewHolder.binding.image);
//        });

        //viewHolder.binding.text.setText(sliderModel.getTitle());
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardSliderViewBinding dataBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.card_slider_view, parent, false);
        return new ViewHolder(dataBinding);
    }

    @Override
    public int getItemCount() {
        return list.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private CardSliderViewBinding binding;

        public ViewHolder(CardSliderViewBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

    }
}
