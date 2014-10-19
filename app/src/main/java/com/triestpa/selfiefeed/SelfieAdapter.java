package com.triestpa.selfiefeed;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class SelfieAdapter extends RecyclerView.Adapter<SelfieAdapter.ViewHolder> {
    private List<Selfie> mSelfies;
    private SelfieFeedActivity mActivity;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView mSelfieImage;

        public ViewHolder(View v) {
            super(v);
            mSelfieImage = (ImageView) v.findViewById(R.id.selfie_image);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public SelfieAdapter(List<Selfie> selfies, SelfieFeedActivity context) {
        mSelfies = selfies;
        mActivity = context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public SelfieAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_selfie_cell, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        Selfie thisSelfie = mSelfies.get(position);
        if (thisSelfie != null) {
            String imageURL = thisSelfie.images.standard_resolution.url;
            Picasso.with(mActivity).load(imageURL).resize(500, 500).noFade().centerCrop().into(holder.mSelfieImage);
        }

    }

    // Return the size of your dataset (invoked by the layout manager)
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
    /*
    public static class ViewHolder {
        public ImageView selfieImage;
    }

    private class SelfieAdapter extends BaseAdapter {
        private static final int LIST_ITEM_TYPE_BIG_PIC = 0;
        private static final int LIST_ITEM_TYPE_SMALL_PIC = 1;

        private List<Selfie> mSelfies;

        private LayoutInflater mInflater;

        public SelfieAdapter(List<Selfie> selfies) {
            mInflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mSelfies = selfies;
        }

        @Override
        public int getItemViewType(int position) {
            return LIST_ITEM_TYPE_BIG_PIC;
        }

        @Override
        public int getViewTypeCount() {
            return 2;
        }

        @Override
        public int getCount() {
            return mSelfies.size();
        }

        @Override
        public Selfie getItem(int position) {
            return mSelfies.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            int type = getItemViewType(position);
            if (convertView == null) {
                holder = new ViewHolder();
                switch (type) {
                    case LIST_ITEM_TYPE_BIG_PIC:
                        convertView = mInflater.inflate(R.layout.adapter_selfie_cell, parent, false);
                        holder.selfieImage = (ImageView) convertView.findViewById(R.id.selfie_image);
                        break;
                    case LIST_ITEM_TYPE_SMALL_PIC:
                        convertView = mInflater.inflate(R.layout.adapter_selfie_cell, parent, false);
                        holder.selfieImage = (ImageView) convertView.findViewById(R.id.selfie_image);
                        break;
                }

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }


            Selfie thisSelfie = mSelfies.get(position);
            if (thisSelfie != null) {
                String imageURL = thisSelfie.images.standard_resolution.url;
                Picasso.with(mActivity).load(imageURL).resize(500, 500).noFade().centerCrop().into(holder.selfieImage);
            }

            return convertView;
        }
    }
    */
