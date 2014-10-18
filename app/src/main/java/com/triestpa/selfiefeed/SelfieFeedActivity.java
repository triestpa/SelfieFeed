package com.triestpa.selfiefeed;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;

public class SelfieFeedActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selfie_feed);

        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new PictureGridFragment()).commit();

        String [] taskParams = {"https://api.instagram.com/v1/tags/selfie/media/recent?client_id=84368c072bf84e2c89e5265891985868"};

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
