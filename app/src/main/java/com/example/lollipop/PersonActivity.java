package com.example.lollipop;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.bumptech.glide.Glide;
import com.example.lollipop.ViewModel.UserProfileViewModelGetQuery;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class PersonActivity extends AppCompatActivity implements CarouselFragment.OnFragmentInteractionListener {

    private static final String TAG = "PersonActivity";
    private MyApolloClient myApolloClient;
    Handler handler;

    private ArrayList<String> mURLs = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);
        handler=new Handler();
        myApolloClient = (MyApolloClient)getApplication();
        getIncomingIntent();
    }

    private void getIncomingIntent() {
        Log.d(TAG, "getIncomingIntent: check for incoming intents Puto.");
        Intent intent = getIntent();
        String imageURL = intent.getStringExtra("image_url");
        String imageName = intent.getStringExtra("image_name");
        String imageBio = intent.getStringExtra("image_bio");
        String imageAge = intent.getStringExtra("image_age");
        String imageID = intent.getStringExtra("image_id");
        setImage(imageURL, imageName, imageBio, imageAge);
        getPosts(imageID);
    }

    private void setImage(String imageURL, String imageName, String imageBio, String imageAge) {
        Log.d(TAG, "setImage: Setting the image and name");

        TextView name = findViewById(R.id.image_description);
        TextView bio = findViewById(R.id.user_description);
        TextView titleName = findViewById(R.id.title_name);
        ImageView imageView = findViewById(R.id.image);

        name.setText(imageName.concat(", ").concat(imageAge));
        bio.setText(imageBio);
        titleName.setText(imageName);
        Glide.with(this)
                .asBitmap()
                .load(imageURL.replace("small", "large"))
                .into(imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getSupportFragmentManager();
                Intent intent = new Intent(getBaseContext(), CarouselActivity.class);
                intent.putStringArrayListExtra("user_URLs", mURLs);
                startActivity(intent);
//                Bundle bundle = new Bundle();
//                bundle.putStringArrayList("user_URLs", mURLs);
//                CarouselFragment carouselFragment = new CarouselFragment();
//                carouselFragment.setArguments(bundle);
//                fm.beginTransaction().add(R.id.fragment_container, carouselFragment).addToBackStack(null).commit();
            }
        });

    }

    private void getPosts(final String imageID) {
        Log.d(TAG, "getPosts: Second Query");
        myApolloClient.apolloClient().query(
                UserProfileViewModelGetQuery.builder().userId(imageID).build()).enqueue(new ApolloCall.Callback<UserProfileViewModelGetQuery.Data>() {
            @Override
            public void onResponse(@NotNull Response<UserProfileViewModelGetQuery.Data> response) {
                Log.d(TAG, "onResponse: Successful Query");
                UserProfileViewModelGetQuery.User1 user = response.data().user().user();
                for (int i = 0; i<user.media().size(); i++)
                    mURLs.add(user.media().get(i).photoUrl());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setOtherImages(mURLs);
                    }

                });
            }

            @Override
            public void onFailure(@NotNull ApolloException e) {
                Log.e(TAG, e.getMessage(), e);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Log.d(TAG, "run: Ya estoy esperando 1 sec");
                        getPosts(imageID);
                    }
                }, 1000);
            }
        });
    }

    private void setOtherImages(ArrayList<String> imagesURLs){
        Log.d(TAG, "setOtherImages: New Query");
        ImageView imageView1 = (ImageView) findViewById(R.id.subImage1);
        ImageView imageView2 = (ImageView) findViewById(R.id.subImage2);
        ImageView imageView3 = (ImageView) findViewById(R.id.subImage3);
        ArrayList<ImageView> imageViews = new ArrayList<>();
        imageViews.add(imageView1);
        imageViews.add(imageView2);
        imageViews.add(imageView3);
        if (imagesURLs.size()>1){
            for (int i = 1; i<Math.min(4, imagesURLs.size()); i++){
                Glide.with(this)
                        .asBitmap()
                        .load(imagesURLs.get(i).replace("medium", "large"))
                        .into(imageViews.get(i-1));
            }}

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
