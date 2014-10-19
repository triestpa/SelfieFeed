package com.triestpa.selfiefeed;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;

import java.util.ArrayList;


public class PictureGridFragment extends Fragment {
    final String TAG = PictureGridFragment.class.getSimpleName();
    SelfieFeedActivity mActivity;
    boolean isDownloading = false;
    boolean buttonAnimating = false;

    String mMaxId;
    String mNextPage;

    View mView;
    RecyclerView mRecyclerView;
    private SelfieAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    SwipeRefreshLayout mSwipeRefreshLayout;
    Button mLoadMoreButton;

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
        mLoadMoreButton = (Button) mView.findViewById(R.id.load_more);

        mRecyclerView.setHasFixedSize(false);
        mLayoutManager = new StaggeredGridLayoutManager(2,1);
        mRecyclerView.setLayoutManager(mLayoutManager);

        //Animate Button Visibility on Scroll
        mSwipeRefreshLayout = (SwipeRefreshLayout) mView.findViewById(R.id.swipe_refresh);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (!isDownloading) {
                    isDownloading = true;
                    mSwipeRefreshLayout.setRefreshing(true);
                    String apiCall = "https://api.instagram.com/v1/tags/selfie/media/recent?client_id=" + getResources().getString(R.string.client_id);
                    if (mMaxId != null) {
                        apiCall = apiCall + "&max_tag_id=" + mMaxId;
                    }
                    String[] taskParams = {apiCall};

                    SelfiePullTask pullTask = new SelfiePullTask(mActivity);

                    try {
                        pullTask.execute(taskParams);
                    } catch (ArrayIndexOutOfBoundsException e) {
                        Log.e(TAG, e.getMessage());
                    }
                }
            }
        });

        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int scrollState) {
                if ((scrollState == RecyclerView.SCROLL_STATE_SETTLING || scrollState == RecyclerView.SCROLL_STATE_IDLE) && buttonAnimating == false && mLoadMoreButton.getVisibility() == View.INVISIBLE) {
                    buttonAnimating = true;
                    AlphaAnimation anim = new AlphaAnimation(0f, 1.0f);
                    anim.setDuration(500);
                    anim.setRepeatMode(Animation.REVERSE);
                    anim.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            mLoadMoreButton.setVisibility(View.VISIBLE);
                            buttonAnimating = false;
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {
                        }
                    });
                    mLoadMoreButton.startAnimation(anim);

                } else if (scrollState == RecyclerView.SCROLL_STATE_DRAGGING && mLoadMoreButton.getVisibility() == View.VISIBLE && !buttonAnimating) {
                    buttonAnimating = true;
                    AlphaAnimation anim = new AlphaAnimation(1.0f, 0.0f);
                    anim.setDuration(500);
                    anim.setRepeatCount(0);
                    anim.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            mLoadMoreButton.setVisibility(View.INVISIBLE);
                            buttonAnimating = false;
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {
                        }
                    });
                    mLoadMoreButton.startAnimation(anim);
                }
            }
        });

        mAdapter = new SelfieAdapter(new ArrayList<Selfie>(), mActivity);
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        //Download initial data
        String apiCall = "https://api.instagram.com/v1/tags/selfie/media/recent?client_id=" + getResources().getString(R.string.client_id);
        String [] taskParams = {apiCall};
        SelfiePullTask pullTask = new SelfiePullTask(mActivity);
        pullTask.execute(taskParams);
        mSwipeRefreshLayout.setRefreshing(true);

        return mView;
    }

    public void populateList(Response response) {
        // If it is not 'refreshing' then it is loading older posts
        if (!mSwipeRefreshLayout.isRefreshing()) {
            for (Selfie selfie : response.selfies) {
                mAdapter.addItemToBack(selfie);

            }
            mNextPage = response.pagination.nextURL;
        }
        else {
            for (Selfie selfie : response.selfies) {
                mAdapter.addItemToFront(selfie);
            }
            mMaxId = response.selfies.get(0).id;
            mRecyclerView.scrollToPosition(response.selfies.size());
        }
        mSwipeRefreshLayout.setRefreshing(false);
        isDownloading = false;
    }



}
