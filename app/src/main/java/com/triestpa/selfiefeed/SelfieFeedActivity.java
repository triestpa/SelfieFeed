package com.triestpa.selfiefeed;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;

public class SelfieFeedActivity extends FragmentActivity {

    PictureGridFragment mGridFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selfie_feed);

        mGridFragment = new PictureGridFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, mGridFragment).commit();

        String apiCall = "https://api.instagram.com/v1/tags/selfie/media/recent?client_id=" + getResources().getString(R.string.client_id);
        String [] taskParams = {apiCall};

        SelfiePullTask pullTask = new SelfiePullTask(this);
        pullTask.execute(taskParams);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.selfie_feed, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
