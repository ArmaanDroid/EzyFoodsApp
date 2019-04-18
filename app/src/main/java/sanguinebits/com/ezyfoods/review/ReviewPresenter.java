package sanguinebits.com.ezyfoods.review;

import android.app.Activity;

import apis.RequestEndPoints;
import apis.WebRequestBody;
import base.BasePresenter;
import model.ApiResponse;

public class ReviewPresenter extends BasePresenter {
    ReviewView mView;
    Activity activity;
    public ReviewPresenter(Activity activity,ReviewView reviewView) {
        super(activity);

        this.activity=activity;
        this.mView=reviewView;

    }


    public void submitReview(float rating, String comment, String dishId) {
        WebRequestBody requestBody=new WebRequestBody();
        requestBody.setRating(String.valueOf(rating));
        requestBody.setComment(comment);
        requestBody.setDishId(dishId);
        requestBody.setRequestName(RequestEndPoints.review_dish);

        mView.showProgress("Please wait...");
        makeRequest(requestBody, new WeResponseCallback() {
            @Override
            public void onResponse(ApiResponse commonPojo) throws Exception {
                mView.hidePorgress();
                mView.showToast("Review Submitted");
                mView.reviewSuccess();
            }

            @Override
            public void failure(Exception e, String message) {
                mView.hidePorgress();
                mView.showToast("Review Submission Failed. ");
                mView.reviewFail();

            }
        });

    }
}
