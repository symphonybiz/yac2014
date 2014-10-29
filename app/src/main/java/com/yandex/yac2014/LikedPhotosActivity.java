package com.yandex.yac2014;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;


public class LikedPhotosActivity extends Activity {

    public static void start(Context context) {
        Intent i = new Intent(context, LikedPhotosActivity.class);
        context.startActivity(i);
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
