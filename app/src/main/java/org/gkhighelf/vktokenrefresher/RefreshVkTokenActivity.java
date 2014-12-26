package org.gkhighelf.vktokenrefresher;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import org.gkhighelf.vktokenrefresher.threads.TokenUpdaterThread;

public class RefreshVkTokenActivity extends Activity {
    private static final String LOG_KEY = "VKTokenRefresher";
    private WebView _webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String vk_app_id = VKTokenRefresherApplication.instance().getPreferences()
                .getString(SettingsActivity.KEY_VK_APP_ID, null);
        final String vk_callback_url = VKTokenRefresherApplication.instance().getPreferences()
                .getString(SettingsActivity.KEY_TOKEN_CALLBACK_URL, null);
        boolean started_by_alarm_manager = false;

        if(vk_app_id == null || vk_callback_url == null) {
            showSettings();
            finish();
        } else {
            Bundle bundle = this.getIntent().getExtras();
            if(bundle != null) {
                started_by_alarm_manager = bundle.getBoolean("started_by_alarm_manager", false);
            }
            if(!started_by_alarm_manager) {
                VKTokenRefresherApplication.instance().setActivityStartedManually(true);
            } else if(VKTokenRefresherApplication.instance().isActivityStartedManually()) {
                finish();
                return;
            }
            if(VKTokenRefresherApplication.isNetworkAvailable(this)) {
                setContentView(R.layout.activity_refresh_vk_token);
                _webView = (WebView) findViewById(R.id.refresherWebView);
                _webView.loadUrl("https://oauth.vk.com/authorize?client_id=" + vk_app_id +
                        "&scope=video,email,profile,offline&redirect_uri=https://oauth.vk.com/blank.html&display=page&response_type=token");
                final Boolean finalStarted_by_alarm_manager = started_by_alarm_manager;
                _webView.setWebViewClient(new WebViewClient() {
                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
                        int tokenPos = findTokenPosition(url);
                        if(tokenPos != -1) {
                            Log.d(LOG_KEY, url);
                            String token = url.substring(tokenPos + 6, url.indexOf("&", tokenPos));
                            Log.d(LOG_KEY, "Token: " + token);
                            String tokenUpdateUrl = vk_callback_url + "?token=" + token;
                            TokenUpdaterThread updater = new TokenUpdaterThread(tokenUpdateUrl);
                            updater.start();
                            if(finalStarted_by_alarm_manager) {
                                finish();
                            }
                        }
                        return false;
                    }
                });
            } else {
                Log.d(LOG_KEY, "No internet connection available");
                finish();
            }
        }
    }

    @Override
    protected void onStop() {
        VKTokenRefresherApplication.instance().setActivityStartedManually(false);
        super.onStop();
    }

    private int findTokenPosition(String data) {
        return data.indexOf("token=");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.refresh_vk_token, menu);
        return true;
    }

    private void showSettings() {
        Intent newIntent = new Intent(this, SettingsActivity.class);
        newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(newIntent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            showSettings();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
