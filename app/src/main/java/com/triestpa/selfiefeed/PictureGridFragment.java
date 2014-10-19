package com.triestpa.selfiefeed;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Point;
import android.graphics.Rect;
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
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

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

    private Animator mCurrentAnimator;
    private int mShortAnimationDuration = 500;

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

        //Load the next page of results on click
        mLoadMoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isDownloading && mNextPage != null) {
                    isDownloading = true;
                    mLoadMoreButton.setText("Downloading");
                    String apiCall = mNextPage;
                    Log.d(TAG, apiCall);
                    String[] taskParams = {apiCall};
                    SelfiePullTask pullTask = new SelfiePullTask(mActivity);

                    pullTask.execute(taskParams);
                }
            }
        });

        mRecyclerView.setHasFixedSize(false);
        mLayoutManager = new StaggeredGridLayoutManager(2, 1);
        mRecyclerView.setLayoutManager(mLayoutManager);

        //Find most recent selfies on swipe up
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

        //Animate Button Visibility on Scroll
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
        String[] taskParams = {apiCall};
        SelfiePullTask pullTask = new SelfiePullTask(mActivity);
        pullTask.execute(taskParams);

        return mView;
    }

    public void populateList(Response response) {
        // If it is not 'refreshing' then it is loading an older response page
        if (!mSwipeRefreshLayout.isRefreshing()) {
            for (Selfie selfie : response.selfies) {
                mAdapter.addItemToBack(selfie);
            }
            mNextPage = response.pagination.nextURL;
            mLoadMoreButton.setText(getString(R.string.load_more));
            isDownloading = false;
        } else {

            for (Selfie selfie : response.selfies) {
                mAdapter.addItemToFront(selfie);

                mMaxId = response.selfies.get(0).id;
                mRecyclerView.scrollToPosition(response.selfies.size());
            }
            mSwipeRefreshLayout.setRefreshing(false);
            isDownloading = false;
        }
    }

    // Zoom the image to full size on tap
    // From https://developer.android.com/training/animation/zoom.html
    void zoomImageFromThumb(final View thumbView, String url) {
        // If there's an animation in progress, cancel it
        // immediately and proceed with this one.
        if (mCurrentAnimator != null) {
            mCurrentAnimator.cancel();
        }

        // Load the high-resolution "zoomed-in" image.
        final ImageView expandedImageView = (ImageView) mView.findViewById(
                R.id.expanded_image);
        Picasso.with(mActivity).load(url).fit().noFade().centerCrop().into(expandedImageView);

        // Calculate the starting and ending bounds for the zoomed-in image.
        // This step involves lots of math. Yay, math.
        final Rect startBounds = new Rect();
        final Rect finalBounds = new Rect();
        final Point globalOffset = new Point();

        // The start bounds are the global visible rectangle of the thumbnail,
        // and the final bounds are the global visible rectangle of the container
        // view. Also set the container view's offset as the origin for the
        // bounds, since that's the origin for the positioning animation
        // properties (X, Y).
        thumbView.getGlobalVisibleRect(startBounds);
        mView.findViewById(R.id.grid_layout)
                .getGlobalVisibleRect(finalBounds, globalOffset);
        startBounds.offset(-globalOffset.x, -globalOffset.y);
        finalBounds.offset(-globalOffset.x, -globalOffset.y);

        // Adjust the start bounds to be the same aspect ratio as the final
        // bounds using the "center crop" technique. This prevents undesirable
        // stretching during the animation. Also calculate the start scaling
        // factor (the end scaling factor is always 1.0).
        float startScale;
        if ((float) finalBounds.width() / finalBounds.height()
                > (float) startBounds.width() / startBounds.height()) {
            // Extend start bounds horizontally
            startScale = (float) startBounds.height() / finalBounds.height();
            float startWidth = startScale * finalBounds.width();
            float deltaWidth = (startWidth - startBounds.width()) / 2;
            startBounds.left -= deltaWidth;
            startBounds.right += deltaWidth;
        } else {
            // Extend start bounds vertically
            startScale = (float) startBounds.width() / finalBounds.width();
            float startHeight = startScale * finalBounds.height();
            float deltaHeight = (startHeight - startBounds.height()) / 2;
            startBounds.top -= deltaHeight;
            startBounds.bottom += deltaHeight;
        }

        // Hide the thumbnail and show the zoomed-in view. When the animation
        // begins, it will position the zoomed-in view in the place of the
        // thumbnail.
        //thumbView.setAlpha(0f);
        expandedImageView.setVisibility(View.VISIBLE);

        // Set the pivot point for SCALE_X and SCALE_Y transformations
        // to the top-left corner of the zoomed-in view (the default
        // is the center of the view).
        expandedImageView.setPivotX(0f);
        expandedImageView.setPivotY(0f);

        // Construct and run the parallel animation of the four translation and
        // scale properties (X, Y, SCALE_X, and SCALE_Y).
        AnimatorSet set = new AnimatorSet();
        set
                .play(ObjectAnimator.ofFloat(expandedImageView, View.X,
                        startBounds.left, finalBounds.left))
                .with(ObjectAnimator.ofFloat(expandedImageView, View.Y,
                        startBounds.top, finalBounds.top))
                .with(ObjectAnimator.ofFloat(expandedImageView, View.SCALE_X,
                        startScale, 1f)).with(ObjectAnimator.ofFloat(expandedImageView,
                View.SCALE_Y, startScale, 1f));
        set.setDuration(mShortAnimationDuration);
        set.setInterpolator(new DecelerateInterpolator());
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mCurrentAnimator = null;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                mCurrentAnimator = null;
            }
        });
        set.start();
        mCurrentAnimator = set;

        // Upon clicking the zoomed-in image, it should zoom back down
        // to the original bounds and show the thumbnail instead of
        // the expanded image.
        final float startScaleFinal = startScale;
        expandedImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCurrentAnimator != null) {
                    mCurrentAnimator.cancel();
                }

                // Animate the four positioning/sizing properties in parallel,
                // back to their original values.
                AnimatorSet set = new AnimatorSet();
                set.play(ObjectAnimator
                        .ofFloat(expandedImageView, View.X, startBounds.left))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageView,
                                        View.Y, startBounds.top))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageView,
                                        View.SCALE_X, startScaleFinal))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageView,
                                        View.SCALE_Y, startScaleFinal));
                set.setDuration(mShortAnimationDuration);
                set.setInterpolator(new DecelerateInterpolator());
                set.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        thumbView.setAlpha(1f);
                        expandedImageView.setVisibility(View.GONE);
                        mCurrentAnimator = null;
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                        thumbView.setAlpha(1f);
                        expandedImageView.setVisibility(View.GONE);
                        mCurrentAnimator = null;
                    }
                });
                set.start();
                mCurrentAnimator = set;
            }
        });
    }

}
