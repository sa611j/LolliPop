package com.example.lollipop;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.bumptech.glide.Glide;
import com.example.lollipop.ViewModel.UserProfileViewModelGetQuery;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PersonFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PersonFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PersonFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = "PersonFragment";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private View RootView;
    private MyApolloClient myApolloClient;
    Handler handler;

    private ArrayList<String> mURLs = new ArrayList<>();

    public PersonFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PersonFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PersonFragment newInstance(String param1, String param2) {
        PersonFragment fragment = new PersonFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        RootView = inflater.inflate(R.layout.fragment_person, container, false);
        handler=new Handler();
        myApolloClient = (MyApolloClient) getActivity().getApplication();

        getIncomingIntent();
        return RootView;
    }

    private void getIncomingIntent() {
        Log.d(TAG, "getIncomingIntent: check for incoming intents Puto.");
        Bundle bundle = getArguments();
        if (bundle != null){
            Log.d(TAG, "getIncomingIntent: Found intent extras");
            String imageURL = bundle.getString("image_url");
            String imageName = bundle.getString("image_name");
            String imageBio = bundle.getString("image_bio");
            String imageAge = bundle.getString("image_age");
            String imageID = bundle.getString("image_id");
            Toast.makeText(getActivity(), imageID, Toast.LENGTH_SHORT).show();
            setImage(imageURL, imageName, imageBio, imageAge);
            getPosts(imageID);

        }}

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
                getActivity().runOnUiThread(new Runnable() {
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
        ImageView imageView1 = (ImageView) RootView.findViewById(R.id.subImage1);
        ImageView imageView2 = (ImageView) RootView.findViewById(R.id.subImage2);
        ImageView imageView3 = (ImageView) RootView.findViewById(R.id.subImage3);
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

    private void setImage(String imageURL, String imageName, String imageBio, String imageAge) {
        Log.d(TAG, "setImage: Setting the image and name");

        TextView name = RootView.findViewById(R.id.image_description);
        TextView bio = RootView.findViewById(R.id.user_description);
        TextView titleName = RootView.findViewById(R.id.title_name);
        ImageView imageView = RootView.findViewById(R.id.image);

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
                FragmentManager fm = getFragmentManager();
                Bundle bundle = new Bundle();
                bundle.putStringArrayList("user_URLs", mURLs);
                CarouselFragment carouselFragment = new CarouselFragment();
                carouselFragment.setArguments(bundle);
                fm.beginTransaction().add(R.id.fragment_container, carouselFragment).addToBackStack(null).commit();
            }
        });
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
