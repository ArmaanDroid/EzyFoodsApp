package sanguinebits.com.ezyfoods.pastOrders;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.view.SimpleDraweeView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import model.Order;
import model.Review;
import sanguinebits.com.ezyfoods.R;
import utils.AppConst;

public class PastOrderAdapter extends RecyclerView.Adapter<PastOrderAdapter.VHolder> {
    private PastOrderAdapterListner listner;
    private List<Order> orderList;
    protected DateFormat FormatDateMonthNamwYear = new SimpleDateFormat("E, MMM dd yyyy");
    protected DateFormat formatTimeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    String currentUserID;

    public PastOrderAdapter(List<Order> orderList, PastOrderAdapterListner listner, String currentUserID) {
        this.orderList = orderList;
        this.listner = listner;
        this.currentUserID = currentUserID;


    }

    @NonNull
    @Override
    public VHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.view_order_item, viewGroup, false);
        return new VHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VHolder vHolder, int i) {
        try {
            vHolder.setData(orderList.get(i));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public class VHolder extends RecyclerView.ViewHolder {
        private final SimpleDraweeView imageView;
        private final TextView textViewCount;
        private final TextView textViewItemName;
        private final TextView textViewPrice;
        private final TextView textViewCookName;
        private final TextView textViewOrderDate;
        private final TextView btnReview;
        private final RatingBar ratingBar;
        private final ImageView imageViewCancel;

        public VHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView1);
            textViewCount = itemView.findViewById(R.id.textViewCount);
            textViewItemName = itemView.findViewById(R.id.textView1);
            textViewPrice = itemView.findViewById(R.id.textViewPrice);
            imageViewCancel = itemView.findViewById(R.id.imageView8);
            textViewCookName = itemView.findViewById(R.id.textViewCookName);
            textViewOrderDate = itemView.findViewById(R.id.textViewOrderDate);
            btnReview = itemView.findViewById(R.id.btnReview);
            ratingBar = itemView.findViewById(R.id.ratingBar);

            btnReview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listner.reviewItem(getAdapterPosition());
                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listner.openDetails(getAdapterPosition());
                }
            });
            GenericDraweeHierarchy hierarchy = new GenericDraweeHierarchyBuilder(itemView.getResources())
                    .setActualImageScaleType(ScalingUtils.ScaleType.FOCUS_CROP)
                    .setProgressBarImageScaleType(ScalingUtils.ScaleType.CENTER)
                    .setProgressBarImage(R.drawable.icon_large_process)
                    .setFailureImage(R.color.gradientLast)
                    .build();
            imageView.setHierarchy(hierarchy);
        }

        public void setData(Order order) throws Exception {
            textViewItemName.setText(order.getDish().getTitle());
            textViewCount.setText(String.valueOf(order.getQuantity()));
            textViewCookName.setText(order.getDish().getPreparedBy().getName());

            if (!order.getDish().getDishImage().isEmpty()) {
                String uri = AppConst.DISH_IMAGE_BASE_URL + order.getDish().getDishImage();
                imageView.setImageURI(uri);
            } else {
                imageView.setImageResource(R.color.gradientLast);
            }

            Float price = Float.valueOf(order.getDish().getPrice()) * Integer.valueOf(order.getQuantity());
            textViewPrice.setText(String.valueOf(price));

            Date date = formatTimeStamp.parse(order.getCreatedAt());

            textViewOrderDate.setText(FormatDateMonthNamwYear.format(date));
            boolean isRevied=false;
            int rating = 0;
            for (Review review : order.getDish().getReviews()) {
                if (review.getReviewerId().equalsIgnoreCase(currentUserID)) {
                    isRevied=true;
                    rating= Integer.parseInt(review.getRating());
                    break;
                }
            }

            if(isRevied){
                btnReview.setText("Reviewed");
                btnReview.setEnabled(false);
                ratingBar.setVisibility(View.VISIBLE);
                ratingBar.setRating(rating);
            } else{
                btnReview.setText("Review");
                btnReview.setEnabled(true);
                ratingBar.setVisibility(View.GONE);
                ratingBar.setRating(rating);
            }
        }
    }

    public interface PastOrderAdapterListner {
        void reviewItem(int position);

        void openDetails(int position);
    }
}
