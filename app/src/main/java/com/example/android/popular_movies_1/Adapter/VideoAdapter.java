package com.example.android.popular_movies_1.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popular_movies_1.Model.Video;
import com.example.android.popular_movies_1.R;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoAdapterViewHolder>{

    private Video[] videos;
    private Context context;
    private VideoAdapterOnClickHandler handler;

    public VideoAdapter(Context context, VideoAdapterOnClickHandler videoAdapterOnClickHandler){
        this.context = context;
        this.handler = videoAdapterOnClickHandler;
    }

    public interface VideoAdapterOnClickHandler{
        void onClick(Video video);
    }

    @NonNull
    @Override
    public VideoAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.video_list_item, parent, false);

        return new VideoAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoAdapterViewHolder holder, int position) {
        holder.txtName.setText(videos[position].getName());
    }

    @Override
    public int getItemCount() {
        if (videos == null)
            return 0;

        return videos.length;
    }

    public class VideoAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView txtName;

        private VideoAdapterViewHolder(View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.textName);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int id = getAdapterPosition();
            Video video = videos[id];
            handler.onClick(video);
        }
    }

    public void setVideos(Video[] videos){
        this.videos = videos;
        notifyDataSetChanged();
    }
}
