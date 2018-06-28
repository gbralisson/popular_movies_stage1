package com.example.android.popular_movies_1.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popular_movies_1.Model.Movie;
import com.example.android.popular_movies_1.R;
import com.squareup.picasso.Picasso;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieAdpaterViewHolder>{

    private static final String IMAGE_PATH = "http://image.tmdb.org/t/p/w185/";

    private MovieAdapterOnClickHandler handler;
    private Movie[] movies;
    private Context context;

    public MovieAdapter(Context context, MovieAdapterOnClickHandler movieAdapterOnClickHandler){
        this.context = context;
        this.handler = movieAdapterOnClickHandler;
    }

    public interface MovieAdapterOnClickHandler{
        void onClick(Movie movie);
    }

    @Override
    public MovieAdpaterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.movie_list_item, parent, false);

        return new MovieAdpaterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieAdpaterViewHolder holder, int position) {
        //holder.txtMovie.setText(movies[position].getTitle());
        loadImageMovie(context, movies[position].getPoster_path(), holder.imageView);
    }

    @Override
    public int getItemCount() {
        if(movies == null)
            return 0;

        return movies.length;
    }


    public class MovieAdpaterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView txtMovie;
        private ImageView imageView;

        public MovieAdpaterViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.img_item);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int id = getAdapterPosition();
            Movie movie = movies[id];
            handler.onClick(movie);
        }
    }

    public void setMovieData(Movie[] movies){
        this.movies = movies;
        notifyDataSetChanged();
    }

    public void loadImageMovie(Context context, String pathImage, ImageView imageView){
        Picasso.with(context).load(IMAGE_PATH + pathImage).into(imageView);
    }

}
