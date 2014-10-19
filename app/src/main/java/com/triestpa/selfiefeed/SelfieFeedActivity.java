package com.triestpa.selfiefeed;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public class SelfieFeedActivity extends FragmentActivity {

    PictureGridFragment mGridFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selfie_feed);

        mGridFragment = new PictureGridFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, mGridFragment).commit();
    }

}
