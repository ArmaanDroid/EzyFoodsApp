package sanguinebits.com.ezyfoods.review;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.RatingBar;

import base.BaseActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import sanguinebits.com.ezyfoods.R;
import utils.AppConst;

public class ReviewActivity extends BaseActivity implements ReviewView {

    private static final String KEY_DISH_ID = AppConst.PACKAGE+"dishId";
    private ReviewPresenter presenter;
    private String dishId;

    public static void start(Context context,String dishId) {
        Intent starter = new Intent(context, ReviewActivity.class);
        starter.putExtra(KEY_DISH_ID,dishId);
        context.startActivity(starter);
    }

    @BindView(R.id.ratingBarReview)
    RatingBar ratingBarReview;

    @BindView(R.id.editTextDescription)
    EditText editTextDescription;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        ButterKnife.bind(this);
        presenter=new ReviewPresenter(this,this);
        dishId=getIntent().getStringExtra(KEY_DISH_ID);
    }

    @OnClick(R.id.submit)
    void submit(){
        float rating = ratingBarReview.getRating();
        String comment = editTextDescription.getText().toString().trim();
        presenter.submitReview(rating,comment,dishId);
    }
    @OnClick(R.id.imageView6)
    void back(){
        onBackPressed();
    }




    @Override
    public void reviewSuccess() {
        this.finish();
    }

    @Override
    public void reviewFail() {

    }
}
