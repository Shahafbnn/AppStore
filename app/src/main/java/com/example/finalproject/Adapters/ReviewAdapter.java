package com.example.finalproject.Adapters;

import android.content.Context;
import android.media.Rating;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.finalproject.Classes.Review;
import com.example.finalproject.Classes.StorageFunctions;
import com.example.finalproject.Classes.User.User;
import com.example.finalproject.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class ReviewAdapter extends BaseAdapter {
    private Context context;
    private List<Review> reviews;


    public ReviewAdapter(Context context, List<Review> reviews) {
        this.context = context;
        this.reviews = reviews;
    }

    @Override
    public int getCount() {
        return reviews.size();
    }

    @Override
    public Object getItem(int position) {
        return reviews.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View reviewView = inflater.inflate(R.layout.review_adapter, parent, false);
        Review review = reviews.get(position);
        User reviewer = review.getReviewReviewer();


        ImageView ivUserImage = reviewView.findViewById(R.id.ivUserImage);
        StorageFunctions.setImage(context, ivUserImage, reviewer.getUserImagePath());

        TextView tvUserName = reviewView.findViewById(R.id.tvUserName);
        tvUserName.setText(reviewer.getFullNameAdmin());

        RadioButton rbReviewLike = reviewView.findViewById(R.id.rbReviewLike);
        RadioButton rbReviewDislike = reviewView.findViewById(R.id.rbReviewDislike);


        RatingBar rbRating = reviewView.findViewById(R.id.rbRating);
        rbRating.setRating(review.getReviewAppScore());
        rbRating.setOnTouchListener((v, event) -> true);


        TextView tvReview = reviewView.findViewById(R.id.tvReview);
        tvReview.setText(review.getReviewText());


        return reviewView;
    }

}
