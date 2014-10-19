package com.triestpa.selfiefeed;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class SelfieAdapter extends RecyclerView.Adapter<SelfieAdapter.ViewHolder> {
    private List<Selfie> mSelfies;
    private SelfieFeedActivity mActivity;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public View mView;
        public ImageView mSelfieImage;

        public ViewHolder(View v) {
            super(v);
            mView = v;
            mSelfieImage = (ImageView) v.findViewById(R.id.selfie_image);
        }
    }

    public SelfieAdapter(List<Selfie> selfies, SelfieFeedActivity context) {
        mSelfies = selfies;
        mActivity = context;
    }

    @Override
    public SelfieAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_selfie_cell, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        StaggeredGridLayoutManager.LayoutParams layoutParams = new StaggeredGridLayoutManager.LayoutParams(100, 100);


        if ((position % 3) == 0) {
            layoutParams.setFullSpan(true);
        }
        else {
            layoutParams.setFullSpan(false);
        }
        holder.mView.setLayoutParams(layoutParams);

        Selfie thisSelfie = mSelfies.get(position);
        if (thisSelfie != null) {
            String imageURL = thisSelfie.images.standard_resolution.url;

            if ((position % 2) == 1) {
                Picasso.with(mActivity).load(imageURL).resize(500, 500).noFade().centerCrop().into(holder.mSelfieImage);
            }
            else {
                Picasso.with(mActivity).load(imageURL).resize(500, 500).noFade().centerCrop().into(holder.mSelfieImage);
            }
        }

    }

    @Override
    public int getItemCount() {
        return mSelfies.size();
    }

    public void addItem(Selfie selfie) {
        int position = mSelfies.size();
        mSelfies.add(selfie);
        notifyItemInserted(position);
    }

    public void removeItem(Selfie selfie) {
        int position = mSelfies.indexOf(selfie);
        mSelfies.remove(position);
        notifyItemRemoved(position);
    }

}