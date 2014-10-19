package com.triestpa.selfiefeed;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;


public class PictureGridFragment extends Fragment {
    SelfieFeedActivity mActivity;
    View mView;

    private RecyclerView mRecyclerView;
 //   private StaggeredGridLayoutManager;
    private SelfieAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    List<Selfie> mSelfieList;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = (SelfieFeedActivity) getActivity();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_picture_grid, container, false);

        mRecyclerView = (RecyclerView) mView.findViewById(R.id.gridview);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new StaggeredGridLayoutManager(2,1
        );//new LinearLayoutManager(mActivity);
        mRecyclerView.setLayoutManager(mLayoutManager);


        mSelfieList = new ArrayList<Selfie>();
        mAdapter = new SelfieAdapter(mSelfieList, mActivity);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        return mView;
    }

    public void populateList(List<Selfie> selfieList) {
       mSelfieList.addAll(selfieList);

        mRecyclerView.removeAllViews();

       for (Selfie selfie : selfieList) {
           mAdapter.addItem(selfie);
       }
    }


}
