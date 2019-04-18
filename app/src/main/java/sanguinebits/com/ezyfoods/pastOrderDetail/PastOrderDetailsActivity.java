package sanguinebits.com.ezyfoods.pastOrderDetail;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.Date;
import java.util.TimeZone;

import base.BaseActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import model.Dish;
import model.Order;
import model.Review;
import model.user.UserRepository;
import sanguinebits.com.ezyfoods.R;
import sanguinebits.com.ezyfoods.home.fragments.dishDetail.CommentsAdapter;
import sanguinebits.com.ezyfoods.orderDetailsNotification.OrderDetailsNotificationActivity;
import sanguinebits.com.ezyfoods.review.ReviewActivity;
import sanguinebits.com.ezyfoods.review.ReviewView;
import utils.AppConst;
import utils.DateTimeUtil;
import utils.SharedPrefs;

public class PastOrderDetailsActivity extends BaseActivity {

    private static final String KEY_ORDER = AppConst.PACKAGE + "key_order";
    private Order order;

    public static void start(Context context, Order order) {
        Intent starter = new Intent(context, PastOrderDetailsActivity.class);
        starter.putExtra(KEY_ORDER, order);
        context.startActivity(starter);
    }

    @BindView(R.id.textViewCookName)
    TextView textViewCookName;

    @BindView(R.id.textViewDishName)
    TextView textViewDishName;

    @BindView(R.id.textViewAvailableOn)
    TextView textViewAvailableOn;

    @BindView(R.id.textViewOrderOn)
    TextView textViewOrderOn;

    @BindView(R.id.textViewSpiceLevel)
    TextView textViewSpiceLevel;

    @BindView(R.id.textViewPrice)
    TextView textViewPrice;

    @BindView(R.id.textViewPickUp)
    TextView textViewPickUp;

    @BindView(R.id.textViewDelivery)
    TextView textViewDelivery;

    @BindView(R.id.textViewQuantity)
    TextView textViewQuantity;

    @BindView(R.id.textViewDescription)
    TextView textViewDescription;

//    @BindView(R.id.textViewComments)
//    TextView textViewComments;

    @BindView(R.id.textView)
    TextView textViewCommentLabel;

    @BindView(R.id.textViewNotRatedYet)
    TextView textViewNotRatedYet;

    @BindView(R.id.reviewOrder)
    Button reviewOrder;

    @BindView(R.id.imageView3)
    SimpleDraweeView imageViewDish;

    @BindView(R.id.imageView4)
    SimpleDraweeView imageViewCook;

    @BindView(R.id.ratingBar)
    RatingBar ratingBar;

    @BindView(R.id.recyclerComments)
    RecyclerView recyclerComments;

    private static int counter=0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_past_order_details);
        ButterKnife.bind(this);

         order = (Order) getIntent().getSerializableExtra(KEY_ORDER);

        try {
            setDetails(order);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setDetails(Order order) {

        counter= Integer.parseInt(order.getQuantity());

        Dish dish = order.getDish();
        textViewDishName.setText(TextUtils.concat(dish.getTitle(), " - ", dish.getCuisineType()));
        textViewCookName.setText(dish.getPreparedBy().getName());

        textViewPrice.setText(TextUtils.concat("Price: $", dish.getPrice()));
        textViewSpiceLevel.setText(TextUtils.concat("Spice Level: ", dish.getSpiceLevel()));
        textViewPickUp.setText(TextUtils.concat("Pick Up: ", dish.getPickUp()));
        textViewDelivery.setText(TextUtils.concat("Delivery: ", dish.getDelivery()));
            textViewQuantity.setText(TextUtils.concat("Order Quantity: ", order.getQuantity()));
        textViewDescription.setText(dish.getDescription());
        if (dish.getRating() != null) {
            ratingBar.setVisibility(View.VISIBLE);
            textViewNotRatedYet.setVisibility(View.GONE);
            ratingBar.setRating(dish.getRating());
        } else {
            ratingBar.setVisibility(View.GONE);
            textViewNotRatedYet.setVisibility(View.VISIBLE);
        }
        if (dish.getReviews() != null && !dish.getReviews().isEmpty()) {
            recyclerComments.setVisibility(View.VISIBLE);
            recyclerComments.setLayoutManager(new LinearLayoutManager(this));
            recyclerComments.setAdapter(new CommentsAdapter(dish.getReviews()));
//            for (int i = 0; i < dish.getReviews().size(); i++) {
//                textViewComments.append(TextUtils.concat(String.valueOf(i + 1), ". ", dish.getReviews().get(i).toString(), "\n"));
//            }
        } else {
            textViewCommentLabel.setVisibility(View.GONE);
            recyclerComments.setVisibility(View.GONE);
        }

        try {
//            Date date = new Date();
//            date.setTime(Long.parseLong(dish.getAvailableOn()));
//            textViewAvailableOn.setText(TextUtils.concat("Available On: ", dateFormatApi.format(date)));

             DateTimeUtil.formatTimeStamp.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date date = DateTimeUtil.formatTimeStamp.parse(order.getCreatedAt());

            textViewOrderOn.setText(TextUtils.concat("Order On: ", DateTimeUtil.FormatDateMonthNamwYear.format(date)));

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (dish.getDishImage() != null && !dish.getDishImage().isEmpty()) {
            String uri = AppConst.DISH_IMAGE_BASE_URL + dish.getDishImage();
            imageViewDish.setImageURI(uri);
        }
        if (dish.getPreparedBy().getProfilePic() != null && !dish.getPreparedBy().getProfilePic().isEmpty()) {
            String uri = AppConst.USER_IMAGE_BASE_URL + dish.getPreparedBy().getProfilePic();
            imageViewCook.setImageURI(uri);
        }

        boolean isRevied=false;
        for (Review review : order.getDish().getReviews()) {
            if (review.getReviewerId().equalsIgnoreCase(SharedPrefs.getInstance(getApplicationContext()).getString(UserRepository.KEY_USER_ID))) {
                isRevied=true;
                break;
            }
        }

        if(isRevied){
            reviewOrder.setText("Reviewed");
            reviewOrder.setEnabled(false);
        }else{
            reviewOrder.setText("Review Order");
            reviewOrder.setEnabled(true);
        }
    }

    @OnClick(R.id.imageView6)
    void back(){
        onBackPressed();
    }

    @OnClick(R.id.reviewOrder)
    void review(){
        ReviewActivity.start(this,order.getDishId());
    }


}
