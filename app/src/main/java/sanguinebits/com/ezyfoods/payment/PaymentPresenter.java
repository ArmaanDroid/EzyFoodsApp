package sanguinebits.com.ezyfoods.payment;

import android.app.Activity;
import android.content.Intent;

import apis.RequestEndPoints;
import apis.WebRequestBody;
import base.BaseActivity;
import base.BasePresenter;
import model.ApiResponse;
import sanguinebits.com.ezyfoods.home.HomeActivity;

public class PaymentPresenter extends BasePresenter {
    private Activity activity;
    private PaymentView mView;
    public PaymentPresenter(Activity activity,PaymentView paymentView) {
        super(activity);
        this.activity=activity;
        this.mView=paymentView;
    }
    void buyDishes(String id,String[] countArry,String[] itemIds) {
        WebRequestBody requestBody = new WebRequestBody();
        requestBody.setStripeToken(id);
        requestBody.setCount(countArry);
        requestBody.setDishes(itemIds);

        requestBody.setRequestName(RequestEndPoints.buy_dishes);
        mView. showProgress("Please wait...");
        makeRequest(requestBody, new WeResponseCallback() {
            @Override
            public void onResponse(ApiResponse commonPojo) throws Exception {
                mView . hidePorgress();
                mView. showToast("Payment successfull");
                Intent intent=new Intent(activity, HomeActivity.class);
                intent.putExtra(HomeActivity.CLEAR_DATA,true);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                activity.startActivity(intent);
                activity.finish();
            }

            @Override
            public void failure(Exception e, String message) {
               mView. hidePorgress();
               mView. showToast("Payment Failed");
            }
        });
    }
}
