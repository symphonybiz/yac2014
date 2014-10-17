package com.yandex.yac2014.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yandex.yac2014.R;
import com.yandex.yac2014.model.Photo;

import butterknife.ButterKnife;
import butterknife.InjectView;
import timber.log.Timber;

/**
 * Created by 7times6 on 17.10.14.
 */
public class PhotoListItemView extends RelativeLayout {

    Photo photo;

    @InjectView(R.id.image)
    ImageView image;

    @InjectView(R.id.photo_name)
    TextView  textName;

    @InjectView(R.id.photo_user)
    TextView textUser;

    public PhotoListItemView(Context context) {
        super(context);
        createView();
    }

    public void setPhoto(Photo photo) {
        this.photo = photo;

        textName.setText(photo.name);
        textUser.setText(photo.user.fullname);

        final String url = photo.images.get(0).url;
        Timber.d("url: %s", url);

        Glide.with(getContext())
                .load(url)
                .crossFade()
                .into(image);
    }

    private void createView() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_photo_list_item, this);
        ButterKnife.inject(this);
    }
}
