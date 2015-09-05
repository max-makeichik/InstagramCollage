package com.maxmakeychik.instagramcollage.fragments;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.maxmakeychik.instagramcollage.R;
import com.maxmakeychik.instagramcollage.model.Media;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;

public class CollageFragment extends Fragment {

    private static final String MEDIA_LIST_KEY = "MEDIA_LIST";
    private static final int COLLAGE_PHOTOS_MARGIN = 15;

    ImageView collage;

    private ArrayList<Media> checkedMediaList;
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
        view = inflater.inflate(R.layout.collage, container, false);

        checkedMediaList = getArguments().getParcelableArrayList(MEDIA_LIST_KEY);
        Log.d(TAG, "here set checkedMediaList " + checkedMediaList);

        if (savedInstanceState != null)
            checkedMediaList = savedInstanceState.getParcelableArrayList(MEDIA_LIST_KEY);
        collage = (ImageView) view.findViewById(R.id.collage);
        //Bitmap bitmap = makeCollage();
        Bitmap bitmap = combineImages();
        Log.d(TAG, "collage bitmap == null " + (bitmap == null));
        if (bitmap != null)
            collage.setImageBitmap(bitmap);
        else
            Toast.makeText(getActivity(), getString(R.string.error_collage), Toast.LENGTH_SHORT).show();

        return view;
    }

    private Bitmap combineImages() {
        final int width, height, imagesCount = checkedMediaList.size();

        width = checkedMediaList.get(0).getImageWidth();
        height = imagesCount * checkedMediaList.get(0).getImageHeight() + (imagesCount - 1) * COLLAGE_PHOTOS_MARGIN;

        final Bitmap cs = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        final Canvas comboImage = new Canvas(cs);

        // this is an extra bit I added, just incase you want to save the new image somewhere and then return the location
    /*String tmpImg = String.valueOf(System.currentTimeMillis()) + ".png";

    OutputStream os = null;
    try {
      os = new FileOutputStream(loc + tmpImg);
      cs.compress(CompressFormat.PNG, 100, os);
    } catch(IOException e) {
      Log.e("combineImages", "problem combining images", e);
    }*/
        for (final Media media : checkedMediaList) {
            Picasso.with(getActivity()).load(media.getImageUrl()).into(new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    int mediaIndex = checkedMediaList.indexOf(media);
                    float imageTopPosition = mediaIndex * (bitmap.getHeight() + COLLAGE_PHOTOS_MARGIN);
                    Log.d(TAG, "comboImage " + imageTopPosition);
                    comboImage.drawBitmap(bitmap, 0f, imageTopPosition, null);
                }

                @Override
                public void onBitmapFailed(final Drawable errorDrawable) {
                    Log.d("TAG", "FAILED");
                }

                @Override
                public void onPrepareLoad(final Drawable placeHolderDrawable) {
                    Log.d("TAG", "Prepare Load");
                }
            });
        }
        return cs;
    }

    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(TAG, "onSaveInstanceState");
        outState.putParcelableArrayList(MEDIA_LIST_KEY, checkedMediaList);
    }

    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG, "onDestroyView");
        view = null;
    }
}
