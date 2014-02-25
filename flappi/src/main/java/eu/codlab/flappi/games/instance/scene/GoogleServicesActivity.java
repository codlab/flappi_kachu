/* Copyright (C) 2013 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package eu.codlab.flappi.games.instance.scene;

import android.os.Bundle;
import android.view.View;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.games.achievement.OnAchievementUpdatedListener;
import com.inmobi.monetization.IMBanner;

import eu.codlab.flappi.R;
import eu.codlab.flappi.games.basegameutils.BaseGameActivityAndEngine;

public abstract class GoogleServicesActivity extends BaseGameActivityAndEngine implements View.OnClickListener, OnAchievementUpdatedListener {
    protected boolean _using_ads;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        _using_ads = getSharedPreferences("GAME", 0).getBoolean("ads", true);

        findViewById(R.id.button_sign_in).setOnClickListener(this);
        findViewById(R.id.button_sign_out).setOnClickListener(this);
        hideAds();

        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());
        if (status != ConnectionResult.SUCCESS) {
            googleServicesError();
        }
    }

    private void ads(final int state) {
        runOnUiThread(new Runnable() {
            public void run() {
                if (_using_ads && findViewById(R.id.adview) != null) {
                    findViewById(R.id.adview).setVisibility(state);
                    IMBanner banner = (IMBanner) findViewById(R.id.banner);
                    if (banner != null && state == View.VISIBLE) {
                        banner.setRefreshInterval(60);
                        banner.loadBanner();
                    }
                }
            }
        });
    }

    protected void showAds() {
        ads(View.VISIBLE);
    }

    protected void hideAds() {
        ads(View.GONE);
    }

    // Shows the "sign in" bar (explanation and button).
    private void showSignInBar() {

        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());
        if (status != ConnectionResult.SUCCESS) {
            googleServicesError();
        } else {
            findViewById(R.id.sign_in_bar).setVisibility(View.VISIBLE);
            findViewById(R.id.sign_out_bar).setVisibility(View.GONE);
        }
    }

    // Shows the "sign out" bar (explanation and button).
    private void showSignOutBar() {
        findViewById(R.id.sign_in_bar).setVisibility(View.GONE);
        findViewById(R.id.sign_out_bar).setVisibility(View.VISIBLE);
    }

    private void googleServicesError() {
        findViewById(R.id.sign_in_bar).setVisibility(View.GONE);
        findViewById(R.id.sign_out_bar).setVisibility(View.GONE);
    }

    /**
     * Called to notify us that sign in failed. Notice that a failure in sign in is not
     * necessarily due to an error; it might be that the user never signed in, so our
     * attempt to automatically sign in fails because the user has not gone through
     * the authorization flow. So our reaction to sign in failure is to show the sign in
     * button. When the user clicks that button, the sign in process will start/resume.
     */
    @Override
    public void onSignInFailed() {
        // Sign-in has failed. So show the user the sign-in button
        // so they can click the "Sign-in" button.
        showSignInBar();
    }

    @Override
    public void onErrorServices() {
        googleServicesError();
    }

    /**
     * Called to notify us that sign in succeeded. We react by loading the loot from the
     * cloud and updating the UI to show a sign-out button.
     */
    @Override
    public void onSignInSucceeded() {
        // Sign-in worked!
        showSignOutBar();
        if (!getGamesClient().isConnected())
            getGamesClient().connect();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_sign_in:

                // start the sign-in flow
                beginUserInitiatedSignIn();
                break;
            case R.id.button_sign_out:
                // sign out.
                signOut();
                showSignInBar();
                break;
        }
    }

    @Override
    protected int getLayoutID() {
        return _using_ads ? R.layout.activity_andengine_ads : R.layout.activity_andengine;
    }

    @Override
    protected int getRenderSurfaceViewID() {
        return R.id.container;
    }
    public void unlockAchievement(int resultat) {
        if (getGamesClient().isConnected()) {
            if (resultat > 5) {
                getGamesClient().unlockAchievementImmediate(this, getString(R.string.achievement_1));
            }
            if (resultat > 15) {
                getGamesClient().unlockAchievementImmediate(this, getString(R.string.achievement_2));
            }
            if (resultat > 25) {
                getGamesClient().unlockAchievementImmediate(this, getString(R.string.achievement_3));
            }
            if (resultat > 50) {
                getGamesClient().unlockAchievementImmediate(this, getString(R.string.achievement_4));
            }
            if (resultat > 40) {
                getGamesClient().revealAchievement(getString(R.string.achievement_pikachu));
                getGamesClient().unlockAchievementImmediate(this, getString(R.string.achievement_pikachu));
            }
            if (resultat > 60) {
                getGamesClient().revealAchievement(getString(R.string.achievement_keldeo));
                getGamesClient().unlockAchievementImmediate(this, getString(R.string.achievement_keldeo));
            }
            getGamesClient().submitScore(getString(R.string.leaderboard_max), resultat);
        }

    }

    @Override
    public void onAchievementUpdated(final int i, final String s) {
    }
}
