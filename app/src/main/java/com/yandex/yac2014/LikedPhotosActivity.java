package com.yandex.yac2014;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class LikedPhotosActivity extends Activity {

    public static void start(Activity caller) {
        Intent i = new Intent(caller, LikedPhotosActivity.class);
//        caller.overridePendingTransition(); TODO
        caller.startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liked_photos);

        if (savedInstanceState == null) {
            getFragmentManager()
                    .beginTransaction()
                    .add(R.id.content, LikedPhotosFragment.newInstance())
                    .commit();
        }
    }
}
