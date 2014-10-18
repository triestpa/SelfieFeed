package com.triestpa.selfiefeed;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;


public class PictureGridFragment extends Fragment {
    SelfieFeedActivity mActivity;
    View mView;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = (SelfieFeedActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_picture_grid, container, false);

        //Test code
        Selfie testSelfie = new Selfie("randomURL");
        ArrayList<Selfie> selfieList = new ArrayList<Selfie>();
        selfieList.add(testSelfie);

        GridView gridview = (GridView) mView.findViewById(R.id.gridview);
        gridview.setAdapter(new SelfieAdapter(selfieList));

        return mView;
    }

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
           /*
            if (mSelfies.get(position))
                return LIST_ITEM_TYPE_BIG_PIC;
            else
                return LIST_ITEM_TYPE_SMALL_PIC;
                */
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
                        convertView = mInflater.inflate(R.layout.adapter_big_picture, parent, false);
                        holder.selfieImage = (ImageView) convertView.findViewById(R.id.selfie_image);
                        break;
                    case LIST_ITEM_TYPE_SMALL_PIC:
                        convertView = mInflater.inflate(R.layout.adapter_big_picture, parent, false);
                        holder.selfieImage = (ImageView) convertView.findViewById(R.id.selfie_image);
                        break;
                }
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            ImageView selfieImage = (ImageView) holder.selfieImage;

            Selfie thisMessage = getItem(position);


            return convertView;
        }
    }
}
