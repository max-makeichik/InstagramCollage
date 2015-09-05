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

    public interface OnItemClickListener {
        void onItemClick(Media media, boolean checked);
    }

    private List<Media> mediaList;
    private Context ctx;
    private OnItemClickListener mOnItemClickListener;

    private static final boolean DEBUG = false;
    final String TAG = "MediaAdapter";

    public MediaAdapter(List<Media> mediaList, Context ctx, OnItemClickListener onItemClickListener) {
        this.mediaList = mediaList;
        this.ctx = ctx;
        mOnItemClickListener = onItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.media_item, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Media media = mediaList.get(position);

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
                            mOnItemClickListener.onItemClick(media, true);
                        }
                        else{
                            holder.checkIcon.setImageDrawable(ctx.getResources().getDrawable(R.drawable.check_0_icon));
                            holder.checkIcon.setTag(0);
                            mOnItemClickListener.onItemClick(media, false);
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