package com.example.android.popular_movies_1.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.popular_movies_1.Model.Review;
import com.example.android.popular_movies_1.R;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewAdapterViewHolder>{

    private Review[] reviews;
    private Context context;
    private ReviewAdapterOnClickHandler reviewAdapterOnClickHandler;

    public ReviewAdapter(Context context, ReviewAdapterOnClickHandler reviewAdapterOnClickHandler){
        this.context = context;
        this.reviewAdapterOnClickHandler = reviewAdapterOnClickHandler;
    }

    public interface ReviewAdapterOnClickHandler{
        void onClick(Review review);
    }

    @NonNull
    @Override
    public ReviewAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.review_list_item, parent, false);

        return new ReviewAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewAdapterViewHolder holder, int position) {
        holder.txtAuthor.setText(reviews[position].getAuthor());
        holder.txtContent.setText(reviews[position].getContent());
    }

    @Override
    public int getItemCount() {
        if(reviews == null)
            return 0;

        return reviews.length;
    }

    public class ReviewAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView txtAuthor;
        private TextView txtContent;

        public ReviewAdapterViewHolder(View itemView) {
            super(itemView);
            txtAuthor = itemView.findViewById(R.id.textAuthor);
            txtContent = itemView.findViewById(R.id.textContent);
        }

        @Override
        public void onClick(View v) {

        }
    }

    public void setReviews(Review[] reviews){
        this.reviews = reviews;
        notifyDataSetChanged();
    }

}
