package com.example.lollipop;

import android.content.Intent;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;

public class CarouselActivity extends AppCompatActivity {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private CarouselFragment.OnFragmentInteractionListener mListener;
    private ArrayList<String> mURLs = new ArrayList<>();

    CarouselView carouselView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carousel);
        getIncomingIntent();
        carouselView = (CarouselView) findViewById(R.id.carouselView);
        carouselView.setPageCount(mURLs.size());
        carouselView.setImageListener(imageListener);
    }

    private void getIncomingIntent() {
        Intent intent = getIntent();
        if (intent != null)
            mURLs = intent.getStringArrayListExtra("user_URLs");

    }
    ImageListener imageListener = new ImageListener() {
        @Override
        public void setImageForPosition(int position, ImageView imageView) {
            Glide.with(CarouselActivity.this)
                    .asBitmap()
                    .load(mURLs.get(position))
                    .into(imageView);
        }
    };

}
