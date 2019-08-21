package com.example.lollipop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity implements RecyclerViewAdapter.AdapterCallback, PersonFragment.OnFragmentInteractionListener, PeopleFragment.OnFragmentInteractionListener, UserFragment.OnFragmentInteractionListener{
    private static final String TAG = "MainActivity";
    private final Fragment fragment1 = new PeopleFragment();
    private final Fragment fragment2 = new UserFragment();
    private final FragmentManager fm = getSupportFragmentManager();
    private int oldCount = 1;
    private Fragment active = fragment1;

//    public Fragment getActive(){
//        return active;
//    }
//    public void setActive(Fragment active){
//        this.active = active;
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        fm.beginTransaction().add(R.id.fragment_container, fragment1, "1").addToBackStack(null).commit();
        fm.beginTransaction().add(R.id.fragment_container, fragment2, "2").detach(fragment2).commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri){
        //you can leave it empty
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_people:
                    fm.beginTransaction().hide(active).show(fragment1).commit();
                    oldCount = Integer.valueOf(active.getTag());
                    active = fragment1;
                    return true;
                case R.id.navigation_user:
                    fm.beginTransaction().detach(active).attach(fragment2).commit();
                    oldCount = Integer.valueOf(active.getTag());
                    active = fragment2;
                    return true;
            }

            return false;
        }
    };

        @Override
    public void onBackPressed() {
//
        int count = Integer.valueOf(active.getTag());
        Log.d(TAG, "onBackPressed: " + count);
        if (count == 0 || count == 1) {
            super.onBackPressed();
            //additional code
        } else {
            Fragment fragment = fm.findFragmentByTag(Integer.toString(oldCount));
            fm.beginTransaction().hide(active).show(fragment).commit();
            active = fragment;
        }

    }

    @Override
    public void onMethodCallback(Fragment yourValue) {
        Log.d(TAG, "onMethodCallback: I got the active");
        active = yourValue;
        oldCount = 1;
    }
}
