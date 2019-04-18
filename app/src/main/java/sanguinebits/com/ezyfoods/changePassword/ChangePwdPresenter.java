package sanguinebits.com.ezyfoods.changePassword;

import android.app.Activity;

import apis.WebRequestBody;
import base.BasePresenter;
import model.ApiResponse;

public class ChangePwdPresenter extends BasePresenter {
    private ChangePwdView mView;
    private Activity activity;

    public ChangePwdPresenter(Activity activity, ChangePwdView mView) {
        super(activity);
        this.activity=activity;
        this.mView=mView;
    }


    public void changePassword(WebRequestBody detailToServer) {
        mView.showProgress("Please wait...");
        makeRequest(detailToServer, new WeResponseCallback() {
            @Override
            public void onResponse(ApiResponse commonPojo) throws Exception {
                mView.hidePorgress();
                mView.showToast("Password changed successfully.");
                activity.finish();
            }

            @Override
            public void failure(Exception e, String message) {
                mView.hidePorgress();

            }
        });


    }
}
