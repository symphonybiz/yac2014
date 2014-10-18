package com.yandex.yac2014.view;

import android.app.Fragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.yandex.yac2014.model.Photo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
* Created by 7times6 on 18.10.14.
*/
public class PhotosAdapter extends BaseAdapter {
    final List<Photo> photos = new ArrayList<Photo>();
    final Fragment hostFragment;

    public PhotosAdapter(Fragment hostFragment) {
        this.hostFragment = hostFragment;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        PhotoListItemView itemView;
        if (convertView == null) {
            itemView = new PhotoListItemView(hostFragment.getActivity());
        } else {
            itemView = (PhotoListItemView) convertView;
        }

        itemView.setPhoto(getItem(position));

        return itemView;
    }

    @Override
    public Photo getItem(int position) {
        return photos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void addPhotos(Collection<Photo> newPhotos) {
        photos.addAll(newPhotos);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return photos.size();
    }
}
