package sanguinebits.com.ezyfoods.orderDetailsNotification;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.Date;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import model.Order;
import sanguinebits.com.ezyfoods.R;
import utils.AppConst;
import utils.DateTimeUtil;

public class OrderDetailsNotificationActivity extends AppCompatActivity {

    private static final String KEY_ORDER = AppConst.PACKAGE + "key_order";

    public static void start(Context context, Order order) {
        Intent starter = new Intent(context, OrderDetailsNotificationActivity.class);
        starter.putExtra(KEY_ORDER, order);
        context.startActivity(starter);
    }


    @BindView(R.id.textViewDishName)
    TextView textViewDishName;

    @BindView(R.id.textViewOrderOn)
    TextView textViewOrderOn;

    @BindView(R.id.textViewOrderBy)
    TextView textViewOrderBy;

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


    @BindView(R.id.imageView3)
    SimpleDraweeView imageViewDish;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);
        ButterKnife.bind(this);
        Order order = (Order) getIntent().getSerializableExtra(KEY_ORDER);
        try {
            setDetails(order);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setDetails(Order order) throws Exception {
        textViewOrderBy.setText(TextUtils.concat("Order By: ", order.getBuyer().getName()));
        textViewQuantity.setText(TextUtils.concat("Order Quantity: ", order.getQuantity()));
        textViewDishName.setText(TextUtils.concat(order.getDish().getTitle(), " - ", order.getDish().getCuisineType()));
        if (order.getDish().getDishImage() != null && !order.getDish().getDishImage().isEmpty()) {
            String uri = AppConst.DISH_IMAGE_BASE_URL + order.getDish().getDishImage();
            imageViewDish.setImageURI(uri);
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
    }

    @OnClick(R.id.imageView6)
    void back() {
        onBackPressed();
    }
}
