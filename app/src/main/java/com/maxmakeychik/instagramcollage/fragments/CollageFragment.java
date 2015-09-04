package com.maxmakeychik.instagramcollage.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.maxmakeychik.instagramcollage.R;
import com.maxmakeychik.instagramcollage.adapters.MediaAdapter;
import com.maxmakeychik.instagramcollage.model.Media;

import java.util.ArrayList;

public class CollageFragment extends Fragment {

    private static final String MEDIA_LIST_KEY = "MEDIA_LIST";

    private ArrayList<Media> mediaList;
    private static String TAG = "CollageFragment";

    public CollageFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        setHasOptionsMenu(true);
        setRetainInstance(true);
    }

    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");

        //((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getResources().getString(R.string.favorites));
        view = inflater.inflate(R.layout.media_list, container, false);

        mediaList = getArguments().getParcelableArrayList(MEDIA_LIST_KEY);
        Log.d(TAG, "here set mediaList " + mediaList);

        if (savedInstanceState != null)
            mediaList = savedInstanceState.getParcelableArrayList(MEDIA_LIST_KEY);

        return view;
    }

    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(TAG, "onSaveInstanceState");
        outState.putParcelableArrayList(MEDIA_LIST_KEY, mediaList);
    }

    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG, "onDestroyView");
        view = null;
    }
}
