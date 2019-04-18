package sanguinebits.com.ezyfoods.pastOrderDetail;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import model.Review;
import sanguinebits.com.ezyfoods.R;
import utils.AppConst;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.ViewHolder> {

    List<Review> reviews;

    public CommentsAdapter(List<Review> reviews) {
        this.reviews = reviews;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_comment,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.setData(reviews.get(i));
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textViewComments;
        private final TextView textViewDate;
        private final SimpleDraweeView imageView9;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewComments=itemView.findViewById(R.id.textViewComments);
            textViewDate=itemView.findViewById(R.id.textViewDate);
            imageView9=itemView.findViewById(R.id.imageView9);
        }

        public void setData(Review review) {
            String uri = AppConst.USER_IMAGE_BASE_URL + review.getReviewedBy().getProfilePic();
            imageView9.setImageURI(uri);

            textViewComments.setText(review.getComment());
//            Date date = new Date();
////            textViewDate.setText(DateTimeUtil.FormatDateMonthNamwYear.format(date));
////            textViewDate.setText();
        }
    }
}
