package com.triestpa.selfiefeed;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;


public class PictureGridFragment extends Fragment {
    final String TAG = PictureGridFragment.class.getSimpleName();
    SelfieFeedActivity mActivity;
    View mView;

    String mMaxId;

    RecyclerView mRecyclerView;
    private SelfieAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    SwipeRefreshLayout mSwipeRefreshLayout;

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
        mRecyclerView.setHasFixedSize(false);
        mLayoutManager = new StaggeredGridLayoutManager(2,1);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mSwipeRefreshLayout = (SwipeRefreshLayout) mView.findViewById(R.id.swipe_refresh);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefreshLayout.setRefreshing(true);
                String apiCall = "https://api.instagram.com/v1/tags/selfie/media/recent?client_id=" + getResources().getString(R.string.client_id);
                        if (mMaxId != null) {
                            apiCall = apiCall + "&max_tag_id=" + mMaxId;
                        }
                String [] taskParams = {apiCall};

                SelfiePullTask pullTask = new SelfiePullTask(mActivity);
                pullTask.execute(taskParams);
            }
        });

        mAdapter = new SelfieAdapter(new ArrayList<Selfie>(), mActivity);
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        return mView;
    }

    public void populateList(List<Selfie> selfieList) {
       mRecyclerView.removeAllViews();

       for (Selfie selfie : selfieList) {
           mAdapter.addItemToFront(selfie);
       }
    }


}
