package com.triestpa.selfiefeed;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

/*
Adapter for the core RecyclerView. This will recieve data on each selfie, create(or recycle) a cardview, and download the image to fill it
 */
public class SelfieAdapter extends RecyclerView.Adapter<SelfieAdapter.ViewHolder> {
    final String TAG = SelfieAdapter.class.getSimpleName();

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
    public SelfieAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_selfie_cell, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        StaggeredGridLayoutManager.LayoutParams layoutParams = new StaggeredGridLayoutManager.LayoutParams(100, 100);
        ViewGroup.LayoutParams imageParams = holder.mSelfieImage.getLayoutParams();

        //Position every third picture in the center spot
        if ((position % 3) == 0) {
            layoutParams.setFullSpan(true);
            imageParams.height = 600;
            imageParams.width = 600;

        } else {
            layoutParams.setFullSpan(false);
            imageParams.height = 300;
            imageParams.width = 300;
        }

        holder.mSelfieImage.setLayoutParams(imageParams);
        holder.mView.setLayoutParams(layoutParams);

        final Selfie thisSelfie = mSelfies.get(position);
        if (thisSelfie != null) {
            final String imageURL = thisSelfie.getImages().getStandard_resolution().getUrl();

            // Zoom the image when taped
            holder.mSelfieImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mActivity.mGridFragment.zoomImageFromThumb(holder.mSelfieImage, imageURL);
                }
            });

            // Load and scale the image from the given url using Picasso
            Picasso.with(mActivity).load(imageURL).fit().noFade().centerCrop().into(holder.mSelfieImage);
        }
    }

    @Override
    public int getItemCount() {
        return mSelfies.size();
    }

    public void addItemToBack(Selfie selfie) throws IndexOutOfBoundsException {
        int position = mSelfies.size();
        mSelfies.add(position, selfie);
        notifyItemInserted(position);
    }

    public void addItemToFront(Selfie selfie) {
        int position = 0;
        mSelfies.add(position, selfie);
        notifyItemInserted(position);
    }

    public void removeItem(Selfie selfie) {
        int position = mSelfies.indexOf(selfie);
        mSelfies.remove(position);
        notifyItemRemoved(position);
    }

}