package com.example.lollipop;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.lollipop.ViewModel.FeedViewModelGetQuery;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import de.hdodenhof.circleimageview.CircleImageView;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{
    private static final String TAG = "RecyclerViewAdapter";
    private List<FeedViewModelGetQuery.User> mUsers;
    private ArrayList<String> mImages = new ArrayList<>();
    private ArrayList<String> mImageNames = new ArrayList<>();
    private ArrayList<String> mBio = new ArrayList<>();
    private ArrayList<String> mAge = new ArrayList<>();
    private ArrayList<String> mID = new ArrayList<>();
    private Context mContext;
    private AdapterCallback mAdapterCallback;
    public RecyclerViewAdapter(Context context, List<FeedViewModelGetQuery.User> users) {
        try {
            this.mAdapterCallback = ((AdapterCallback) context);
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement AdapterCallback.");
        }
        mContext = context;
        mUsers = users;
        for (int i = 0; i<mUsers.size(); i++){
            mImageNames.add(mUsers.get(i).name());
            mBio.add(mUsers.get(i).bio());
            mAge.add(Integer.toString(mUsers.get(i).age()));
            mID.add(mUsers.get(i).id());
            if (mUsers.get(i).media().size() != 0){
                mImages.add(mUsers.get(i).media().get(0).photoUrl());
                Log.d(TAG, "RecyclerViewAdapter: "+ mUsers.get(i).media().get(0).photoUrl());
            }
            else
                mImages.add("puto");
        }

    }

    public interface AdapterCallback {
        void onMethodCallback(Fragment yourValue);
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_listitem, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position0) {
        Log.d(TAG, "onBindViewHolder: Got it");
        for (int i=0; i<4; i++){
            final int position = position0*4+i;
            if (!mImageNames.get(position).equals("puto"))
                Glide.with(mContext)
                        .asBitmap()
                        .load(mImages.get(position))
                        .into(holder.imageViews.get(i));
            else
                holder.imageViews.get(i).setImageResource(R.mipmap.ic_launcher);
            holder.textViews.get(i).setText(mImageNames.get(position));
            holder.imageViews.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d(TAG, "onClick: Got Clicked");
                    Bundle bundle = new Bundle();
                    bundle.putString("image_url", mImages.get(position));
                    bundle.putString("image_name", mImageNames.get(position));
                    bundle.putString("image_bio", mBio.get(position));
                    bundle.putString("image_age", mAge.get(position));
                    bundle.putString("image_id", mID.get(position));
                    Log.d(TAG, "onClick: " + bundle.get("image_id"));
                    FragmentManager fm = ((AppCompatActivity)mContext).getSupportFragmentManager();
                    PersonFragment personFragment = new PersonFragment();
                    personFragment.setArguments(bundle);
                    fm.beginTransaction().add(R.id.fragment_container, personFragment, "3").addToBackStack(null).commit();
                    mAdapterCallback.onMethodCallback(personFragment);

                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return mImageNames.size()/4;
    }



    public class ViewHolder extends RecyclerView.ViewHolder {
        ArrayList<TextView> textViews = new ArrayList<>();
        ArrayList<ImageView> imageViews = new ArrayList<>();
        LinearLayout parentLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            textViews.add((TextView) itemView.findViewById(R.id.text1));
            textViews.add((TextView) itemView.findViewById(R.id.text2));
            textViews.add((TextView) itemView.findViewById(R.id.text3));
            textViews.add((TextView) itemView.findViewById(R.id.text4));

            imageViews.add((ImageView) itemView.findViewById(R.id.image1));
            imageViews.add((ImageView) itemView.findViewById(R.id.image2));
            imageViews.add((ImageView) itemView.findViewById(R.id.image3));
            imageViews.add((ImageView) itemView.findViewById(R.id.image4));

            parentLayout = itemView.findViewById(R.id.parent_layout);
        }

    }
}
