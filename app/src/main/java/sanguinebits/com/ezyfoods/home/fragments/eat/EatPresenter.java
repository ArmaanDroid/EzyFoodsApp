package sanguinebits.com.ezyfoods.home.fragments.eat;

import android.app.Activity;
import android.support.v4.widget.SwipeRefreshLayout;

import apis.RequestEndPoints;
import apis.WebRequestBody;
import base.BasePresenter;
import model.ApiResponse;
import sanguinebits.com.ezyfoods.R;
import utils.AppConst;
import utils.SharedPrefs;

class EatPresenter extends BasePresenter {
    private Activity activity;
    private EatView mView;

    EatPresenter(Activity activity, EatView mView) {
        super(activity);
        this.activity=activity;
        this.mView=mView;
    }

    void getDishes(final SwipeRefreshLayout mySwipeRefreshLayout) {
        WebRequestBody requestBody=new WebRequestBody();
        requestBody.setLongitude(SharedPrefs.getInstance(activity).getString(AppConst.TAG_USER_LONGITUDE));
        requestBody.setLatitude(SharedPrefs.getInstance(activity).getString(AppConst.TAG_USER_LATITUDE));

        requestBody.setRequestName(RequestEndPoints.get_dishes);
        mView.showProgress(activity.getString(R.string.please_wait));
        mySwipeRefreshLayout.setRefreshing(true);
        makeRequest(requestBody, new WeResponseCallback() {
            @Override
            public void onResponse(ApiResponse commonPojo) throws Exception {
                mView.hidePorgress();
                mView.onDishesLoaded(commonPojo.getDishes());
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
