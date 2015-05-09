/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.yandex.yac2014.tv;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.common.collect.Lists;
import com.squareup.picasso.Picasso;
import com.yandex.yac2014.R;
import com.yandex.yac2014.model.Photo;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/*
 * Details activity class that loads LeanbackDetailsFragment class
 */
public class DetailsActivity extends Activity {

    public static final String SHARED_ELEMENT_NAME = "hero";
    public static final String PHOTO = "photo";
    public static final String PHOTO_LIST = "photo.list";
    public static final String PHOTO_LIST_POSITION = "photo.list.position";

    @InjectView(R.id.pager)
    ViewPager mImagePager;

    private List<Photo> mPhotos;
    private int mPosition = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ButterKnife.inject(this);
        if (getIntent().hasExtra(PHOTO)) {
            mPhotos = Lists.newArrayList((Photo)getIntent().getSerializableExtra(PHOTO));
        } else {
            mPhotos = (List<Photo>) getIntent().getSerializableExtra(PHOTO_LIST);
            mPosition = getIntent().getIntExtra(PHOTO_LIST_POSITION, 0);
        }
        PagerPhotoAdapter adapter = new PagerPhotoAdapter();
        mImagePager.setAdapter(adapter);
        mImagePager.setCurrentItem(mPosition, false);
    }


    class PagerPhotoAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return mPhotos.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView iv = new ImageView(container.getContext());
            container.addView(iv);
            Photo photo = mPhotos.get(position);
            iv.setTag(photo);

            Picasso.with(DetailsActivity.this)
                    .load(photo.getFirstImageUri())
                    .into(iv);

            return iv;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

}
