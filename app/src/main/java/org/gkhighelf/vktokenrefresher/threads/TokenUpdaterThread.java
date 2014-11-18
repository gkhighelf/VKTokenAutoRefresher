package org.gkhighelf.vktokenrefresher.threads;

import android.util.Log;

import org.gkhighelf.vktokenrefresher.network.NetworkUtils;

/**
 * Created by gkhighelf on 18.11.14.
 */
public class TokenUpdaterThread extends Thread {
    private static final String LOG_KEY = "TokenUpdaterThread";

    private final String tokenUpdateUrl;

    public TokenUpdaterThread(String tokenUpdateUrl) {
        super();
        this.tokenUpdateUrl = tokenUpdateUrl;
    }

    @Override
    public void run() {
        Log.d(LOG_KEY, "TokenUpdateUrl: " + tokenUpdateUrl);
        NetworkUtils.Response response = NetworkUtils.LoadUrl(tokenUpdateUrl);
        if(response != null && response.responseCode == 200) {
            Log.d(LOG_KEY, "Response: " + response.body);
            Log.d(LOG_KEY, "Token updated successfully");
        } else {
            if(response!=null) {
                Log.d(LOG_KEY, "Response: " + response.responseCode);
            }
            Log.d(LOG_KEY, "Failed to load");
        }
    }
}
