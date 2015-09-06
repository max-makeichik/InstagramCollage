package com.maxmakeychik.instagramcollage.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.maxmakeychik.instagramcollage.R;
import com.maxmakeychik.instagramcollage.model.Media;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CollageFragment extends Fragment {

    private static final String MEDIA_LIST_KEY = "MEDIA_LIST", USERNAME_KEY = "USERNAME";;
    private static final int COLLAGE_PHOTOS_MARGIN = 15;
    private String userName = "";

    private ArrayList<Media> checkedMediaList;
    Bitmap collageBitmap;
    private static String TAG = "CollageFragment";

    public CollageFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        setHasOptionsMenu(true);
        setRetainInstance(true);
        if(savedInstanceState == null){
            checkedMediaList = getArguments().getParcelableArrayList(MEDIA_LIST_KEY);
            userName = getArguments().getString(USERNAME_KEY);
        }
        else {
            checkedMediaList = savedInstanceState.getParcelableArrayList(MEDIA_LIST_KEY);
            userName = savedInstanceState.getString(USERNAME_KEY);
        }
    }

    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");

        //((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getResources().getString(R.string.favorites));
        view = inflater.inflate(R.layout.collage, container, false);

        Log.d(TAG, "here set checkedMediaList " + checkedMediaList);

        ImageView collage = (ImageView) view.findViewById(R.id.collage);
        Button printButton = (Button) view.findViewById(R.id.printButton);

        makeCollage();
        if (collageBitmap != null) {
            collage.setImageBitmap(collageBitmap);
            printButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    printBitmap(bitmapToUri(collageBitmap));
                }
            });
        } else
            Toast.makeText(getActivity(), getString(R.string.error_collage), Toast.LENGTH_SHORT).show();

        return view;
    }

    private Uri bitmapToUri(Bitmap bitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(getActivity().getContentResolver(), bitmap, "Title", null);
        return Uri.parse(path);
    }

    private void printBitmap(Uri uri) { //  send intent to printing apps
        Intent printIntent = new Intent(Intent.ACTION_SEND);
        printIntent.setType("image/*");
        printIntent.putExtra(Intent.EXTRA_TITLE, userName + "_" + DateFormat.getDateTimeInstance().format(new Date()));
        Log.d(TAG, userName + "_" + DateFormat.getDateTimeInstance().format(new Date()));
        printIntent.putExtra(Intent.EXTRA_STREAM, uri);
        startActivity(printIntent);
    }

    private void makeCollage() {
        final int width, height, imagesCount = checkedMediaList.size();

        width = checkedMediaList.get(0).getImageWidth();
        height = imagesCount * checkedMediaList.get(0).getImageHeight() + (imagesCount - 1) * COLLAGE_PHOTOS_MARGIN;

        collageBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(collageBitmap);

        for (final Media media : checkedMediaList) {
            Picasso.with(getActivity()).load(media.getImageUrl()).into(new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    int mediaIndex = checkedMediaList.indexOf(media);
                    float imageTopPosition = mediaIndex * (bitmap.getHeight() + COLLAGE_PHOTOS_MARGIN);
                    canvas.drawBitmap(bitmap, 0f, imageTopPosition, null);
                }

                @Override
                public void onBitmapFailed(final Drawable errorDrawable) {
                    Toast.makeText(getActivity(), getString(R.string.error_loading_image), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onPrepareLoad(final Drawable placeHolderDrawable) {
                    Log.d("TAG", "Prepare Load");
                }
            });
        }
    }

    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(TAG, "onSaveInstanceState");
        outState.putParcelableArrayList(MEDIA_LIST_KEY, checkedMediaList);
        outState.putString(USERNAME_KEY, userName);
    }

    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG, "onDestroyView");
        view = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(collageBitmap != null) {
            collageBitmap.recycle();
            collageBitmap = null;
        }
    }
}
