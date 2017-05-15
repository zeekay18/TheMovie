package com.zeeice.themovie.ui.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zeeice.themovie.Data.Model.Video;
import com.zeeice.themovie.R;

import java.util.List;

/**
 * Created by Oriaje on 12/05/2017.
 */

public class VideosAdapter extends RecyclerView.Adapter<VideosAdapter.VideosViewHolder> {


    private List<Video> mVideos;
    private Context mContext;

    public VideosAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public VideosViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.movie_item_layout, parent, false);

        return new VideosViewHolder(view);
    }

    @Override
    public void onBindViewHolder(VideosViewHolder holder, int position) {

        Video video = mVideos.get(position);
        holder.videoName.setText(video.getName());

        holder.itemView.setTag(video);

    }

    public void setData(List<Video> videos) {
        if (videos == null)
            return;

        mVideos = videos;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (mVideos == null)
            return 0;

        return mVideos.size();
    }

    class VideosViewHolder extends RecyclerView.ViewHolder {

        public TextView videoName;

        public VideosViewHolder(View itemView) {
            super(itemView);
            videoName = (TextView) itemView.findViewById(R.id.movie_name);
        }
    }
}
