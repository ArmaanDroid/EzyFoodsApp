package sanguinebits.com.ezyfoods.pastOrders;

import android.app.Activity;
import android.support.v4.widget.SwipeRefreshLayout;

import apis.RequestEndPoints;
import apis.WebRequestBody;
import base.BasePresenter;
import model.ApiResponse;

public class PastOrdersPresenter extends BasePresenter {
   private PastOrdersView mView;
    private Activity activity;
    public PastOrdersPresenter(Activity activity,PastOrdersView mView) {
        super(activity);
        this.activity=activity;
        this.mView=mView;
    }

    void getPastOrders(final SwipeRefreshLayout mySwipeRefreshLayout){
        WebRequestBody requestBody=new WebRequestBody();
        requestBody.setRequestName(RequestEndPoints.get_orders);

        mView.showProgress("Please wait...");
        mySwipeRefreshLayout.setRefreshing(true);
        makeRequest(requestBody, new WeResponseCallback() {
            @Override
            public void onResponse(ApiResponse commonPojo) throws Exception {
                mView.hidePorgress();
                mView.onOrdersLoaded(commonPojo.getOrders());
                mySwipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void failure(Exception e, String message) {
                mView.hidePorgress();
                mySwipeRefreshLayout.setRefreshing(false);

            }
        });
    }
}
