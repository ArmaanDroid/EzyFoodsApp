package sanguinebits.com.ezyfoods.home.fragments.notifications;

import android.app.Activity;
import android.support.v4.widget.SwipeRefreshLayout;

import apis.RequestEndPoints;
import apis.WebRequestBody;
import base.BasePresenter;
import model.ApiResponse;

public class NotificationPresenter extends BasePresenter {
    NotificationView mView;
    Activity activity;


    public NotificationPresenter(Activity activity, NotificationView mView) {
        super(activity);
        this.activity = activity;
        this.mView = mView;
    }


    public void getNotificaitons(final SwipeRefreshLayout mySwipeRefreshLayout) {

        WebRequestBody requestBody = new WebRequestBody();
        requestBody.setRequestName(RequestEndPoints.get_notifications);

        mView.showProgress("Please wait...");
        mySwipeRefreshLayout.setRefreshing(true);
        makeRequest(requestBody, new WeResponseCallback() {
            @Override
            public void onResponse(ApiResponse commonPojo) throws Exception {
                mView.hidePorgress();
                mView.onNotificationsLoaded(commonPojo.getNotifications());
                mySwipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void failure(Exception e, String message) {
                mView.hidePorgress();
                mView.showToast(message);
                mySwipeRefreshLayout.setRefreshing(false);
            }
        });

    }
}
