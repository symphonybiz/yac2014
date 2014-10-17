package com.yandex.yac2014;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.yandex.yac2014.api.Api500pxFacade;
import com.yandex.yac2014.api.response.PhotosResponse;
import com.yandex.yac2014.model.Photo;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;


public class AppEntryActivity extends Activity {

    Api500pxFacade api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_entry);

        Timber.plant(new Timber.DebugTree());

        api = new Api500pxFacade();

        api.popularPhotos()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<PhotosResponse>() {
                    @Override
                    public void onCompleted() {
                        Timber.d("Completed");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.e(e, "Failed to load photos.");
                    }

                    @Override
                    public void onNext(PhotosResponse photosResponse) {
                        for (Photo p : photosResponse.photos) {
                            Timber.d("load: %s", p.name);
                        }
                    }
                });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.app_entry, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
