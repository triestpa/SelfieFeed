package com.triestpa.selfiefeed;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.triestpa.selfiefeed.SelfiePullTask.NetworkRequestResult;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;

/* Download and parse data from the Instragram api */
public class SelfiePullTask extends AsyncTask<String, Void, NetworkRequestResult> {
    static final String TAG = SelfiePullTask.class.getSimpleName();
    SelfieFeedActivity mContext;

    public SelfiePullTask (Context context) {
        super();
        mContext = (SelfieFeedActivity) context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected NetworkRequestResult doInBackground(String... apiCall) {
        ConnectivityManager cm = (ConnectivityManager)
                mContext.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkRequestResult result = new NetworkRequestResult();
        if (!networkEnabled(cm))
            return (result.setCode(NetworkRequestResult.NO_NETWORK));

        InputStream stream = downloadDataFromServer(apiCall[0]);
        if (stream == null) {
            return result.setCode(NetworkRequestResult.NO_DATA);
        }

        result.setStream(stream);

        try {
           result.setResponse(readSelfieStream(stream));
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }

        if (!result.getResponse().meta.code.equals("200")) {
            result.setCode(NetworkRequestResult.HTTP_ERROR);
        }

        return result.setCode(NetworkRequestResult.SUCCESS);
    }

    // Handle the returned data from the api query
    @Override
    protected void onPostExecute(NetworkRequestResult result) {
        super.onPostExecute(result);
        if (result.getCode() == NetworkRequestResult.SUCCESS) {
            if (result.getResponse().selfies != null) {
                mContext.mGridFragment.populateList(result.getResponse());
            }
        }
        else {
            Toast.makeText(mContext, "Error Loading Selfies", Toast.LENGTH_SHORT).show();
            mContext.mGridFragment.mSwipeRefreshLayout.setRefreshing(false);
            mContext.mGridFragment.isDownloading = false;
        }
    }

    // Confirm that the device has an active network connection
    protected static boolean networkEnabled(ConnectivityManager cm) {
        NetworkInfo n = cm.getActiveNetworkInfo();
        return (n != null) && n.isConnectedOrConnecting();
    }

    // Make an HTTP GET request to the Instagram api
    protected static InputStream downloadDataFromServer(String urlstr) {
        InputStream stream = null;
        try {
            URL url = new URL(urlstr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.connect();
            stream = conn.getInputStream();
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }
        return stream;
    }

    // Parse the returned JSON feed
    public Response readSelfieStream(InputStream in) throws IOException {
        Reader inputReader = new InputStreamReader(in);
        Gson gson = new Gson();
        Response response = gson.fromJson(inputReader, Response.class);
        return response;
    }

    /* Store the results of the data download */
    public class NetworkRequestResult {
        /* Result Code Constants */
        public static final int UNKNOWN = -1;
        public static final int SUCCESS = 0;
        public static final int NO_NETWORK = 1;
        public static final int NO_ROUTE = 2;
        public static final int HTTP_ERROR = 3;
        public static final int NO_DATA = 4;

        private int code;
        private InputStream stream;
        private Response response;

        public NetworkRequestResult (int resultCode, InputStream resultStream) {
            code = resultCode;
            stream = resultStream;
        }
        public NetworkRequestResult () {this(-1, null);}
        public int getCode() {return code;}
        public InputStream getStream() {return stream;}
        public NetworkRequestResult setCode(int c) {code = c; return this;}
        public NetworkRequestResult setStream(InputStream s) {stream = s; return this;}

        public Response getResponse() {return response;}
        public void setResponse(Response response) {this.response = response;}
    }
}
