package com.maxmakeychik.instagramcollage.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.maxmakeychik.instagramcollage.R;
import com.maxmakeychik.instagramcollage.model.Media;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MediaAdapter extends RecyclerView.Adapter<MediaAdapter.ViewHolder> {

    final String TAG = "MediaAdapter";
    private List<Media> mediaList;
    private Context ctx;

    private static final boolean DEBUG = false;

    public MediaAdapter(List<Media> mediaList, Context ctx) {
        this.mediaList = mediaList;
        this.ctx = ctx;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.media_item, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Media media = mediaList.get(position);

        Picasso.with(ctx).load(media.getImageUrl()).into(holder.image, new Callback() {
            @Override
            public void onSuccess() {
                holder.checkIcon.setImageDrawable(ctx.getResources().getDrawable(R.drawable.check_0_icon));
                holder.checkIcon.setVisibility(View.VISIBLE);
                holder.checkIcon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if((holder.checkIcon.getTag() == null) || (int) holder.checkIcon.getTag() == 0) {
                            holder.checkIcon.setImageDrawable(ctx.getResources().getDrawable(R.drawable.check_1_icon));
                            holder.checkIcon.setTag(1);
                        }
                        else{
                            holder.checkIcon.setImageDrawable(ctx.getResources().getDrawable(R.drawable.check_0_icon));
                            holder.checkIcon.setTag(0);
                        }
                    }
                });
            }

            @Override
            public void onError() {}
        });

        holder.date.setText(media.getDate());
        holder.text.setText(media.getText());
        holder.likes.setText(media.getLikesCountString());
        /*String marks = mediaList.get(position).getMarks();
        if (marks != null && !marks.equals("")) {
            holder.markIcon.setVisibility(View.VISIBLE);
            holder.markIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //showMarks(position);
                }
            });
        }*/
    }

    @Override
    public int getItemCount() {
        return mediaList.size();
    }

    protected class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView image;
        public TextView date, text, likes;
        public ImageButton checkIcon;

        public ViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.media_image);
            date = (TextView) itemView.findViewById(R.id.media_date);
            text = (TextView) itemView.findViewById(R.id.media_text);
            likes = (TextView) itemView.findViewById(R.id.media_likes);
            checkIcon = (ImageButton) itemView.findViewById(R.id.check_icon);
        }
    }
}