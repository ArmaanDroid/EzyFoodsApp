package sanguinebits.com.ezyfoods.profile;

import android.app.Activity;

import apis.RequestEndPoints;
import apis.WebRequestBody;
import base.BasePresenter;
import model.ApiResponse;
import model.user.User;
import model.user.UserRepository;
import utils.AppUserType;
import utils.SharedPrefs;

public class ProfilePresenter extends BasePresenter {
    Activity activity;
    ProfileView  mView;

    public ProfilePresenter(Activity activity,ProfileView  mView) {
        super(activity);
        this.activity=activity;
        this.mView=mView;
    }


    public void updateStripeID(String stripeAccID) {
        WebRequestBody requestBody = new WebRequestBody();
        requestBody.setStripeAccountId(stripeAccID);

        requestBody.setRequestName(RequestEndPoints.user_update);
        mView.showProgress("Please wait...");

        makeRequest(requestBody, new WeResponseCallback() {
            @Override
            public void onResponse(ApiResponse commonPojo) throws Exception {
                mView.hidePorgress();
                User user = commonPojo.getUser();
                user.setUserType(AppUserType.chef);
                UserRepository.setUser(SharedPrefs.getInstance(activity), user);
                mView.stipeAccountCreated();
            }

            @Override
            public void failure(Exception e, String message) {
                mView.hidePorgress();
                mView.showToast(message);
            }
        });
    }
}
