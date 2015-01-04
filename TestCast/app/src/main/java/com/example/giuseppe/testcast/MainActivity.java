package com.example.giuseppe.testcast;

import android.app.Activity;
import android.os.Bundle;

import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.cast.CastMediaControlIntent;
import com.google.android.gms.cast.MediaInfo;
import com.google.android.gms.cast.MediaMetadata;
import com.google.sample.castcompanionlibrary.cast.BaseCastManager;
import com.google.sample.castcompanionlibrary.cast.VideoCastManager;
import com.google.sample.castcompanionlibrary.cast.exceptions.NoConnectionException;
import com.google.sample.castcompanionlibrary.cast.exceptions.TransientNetworkDisconnectionException;
import com.google.sample.castcompanionlibrary.widgets.MiniController;


public class MainActivity extends ActionBarActivity {
    private static VideoCastManager sCastMgr;
    private MiniController mMini;
    private Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeCastManager();
        mMini = (MiniController) findViewById(R.id.miniController1);
        getCastManager().addMiniController(mMini);

        btn = (Button) findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MediaMetadata metadata = new MediaMetadata();
                MediaInfo mediaInfo = new MediaInfo.Builder(
                        "http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4")
                        .setContentType("video/mp4")
                        .setStreamType(MediaInfo.STREAM_TYPE_BUFFERED)
                        .setMetadata(metadata)
                        .build();
                try {
                    getCastManager().loadMedia(mediaInfo, true, 0);
                } catch (TransientNetworkDisconnectionException e) {
                    e.printStackTrace();
                } catch (NoConnectionException e) {
                    e.printStackTrace();
                }
            }
        });
     



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_main, menu);
        sCastMgr.addMediaRouterButton(menu,
                R.id.media_route_menu_item);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void initializeCastManager() {
        sCastMgr = VideoCastManager.initialize
                (getApplicationContext(), CastMediaControlIntent.DEFAULT_MEDIA_RECEIVER_APPLICATION_ID, null,
                null);
        sCastMgr.enableFeatures(
                VideoCastManager.FEATURE_NOTIFICATION |
                        VideoCastManager.FEATURE_LOCKSCREEN |
                        VideoCastManager.FEATURE_WIFI_RECONNECT |
                        VideoCastManager.FEATURE_CAPTIONS_PREFERENCE |
                        VideoCastManager.FEATURE_DEBUGGING);
    }
    public static VideoCastManager getCastManager() {
        if (sCastMgr == null) {
            throw new IllegalStateException("Application has not been started");
        }
        return sCastMgr;
    }

    @Override
    protected void onResume() {
        super.onResume();
        sCastMgr = getCastManager();
        sCastMgr.incrementUiCounter();
    }
}
