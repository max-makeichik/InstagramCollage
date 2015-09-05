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
import android.widget.Button;

import com.maxmakeychik.instagramcollage.MediaActivity;
import com.maxmakeychik.instagramcollage.R;
import com.maxmakeychik.instagramcollage.adapters.MediaAdapter;
import com.maxmakeychik.instagramcollage.model.Media;

import java.util.ArrayList;

public class MediaFragment extends Fragment {

    private static final String MEDIA_LIST_KEY = "MEDIA_LIST";
    private RecyclerView recyclerView;
    private MediaAdapter mediaAdapter;

    private ArrayList<Media> mediaList;
    private static String TAG = "MediaFragment";

    public MediaFragment() {
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

        view = inflater.inflate(R.layout.media_list, container, false);

        mediaList = getArguments().getParcelableArrayList(MEDIA_LIST_KEY);
        Log.d(TAG, "here set mediaList " + mediaList);

        Button makeCollageButton = (Button) view.findViewById(R.id.makeCollageButton);
        makeCollageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MediaActivity) getActivity()).makeCollage();
            }
        });

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(itemAnimator);

        if (savedInstanceState != null)
            mediaList = savedInstanceState.getParcelableArrayList(MEDIA_LIST_KEY);

        mediaAdapter = new MediaAdapter(mediaList, getActivity(), new MediaAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Media media, boolean checked) {  //  check icon clicked
                Log.d(TAG, "position");
                ((MediaActivity) getActivity()).changeTitle(media, checked);
            }
        });
        recyclerView.setAdapter(mediaAdapter);

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
        recyclerView = null;
        mediaAdapter = null;
    }
}
