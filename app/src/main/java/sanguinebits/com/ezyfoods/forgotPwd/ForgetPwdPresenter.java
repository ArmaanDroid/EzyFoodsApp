package sanguinebits.com.ezyfoods.forgotPwd;

import android.app.Activity;

import apis.RequestEndPoints;
import apis.WebRequestBody;
import base.BasePresenter;
import model.ApiResponse;
import utils.Validations;

public class ForgetPwdPresenter extends BasePresenter {
    private ForgotPwdView mView;
    private Activity activity;
    public ForgetPwdPresenter(Activity activity,ForgotPwdView mView) {
        super(activity);
        this.activity=activity;
        this.mView=mView;
    }

    void makeForgetPwdRequest(String email){
        if(Validations.isWrongEmail(email)){
            mView.showToast("Please enter correct email.");
        }

        WebRequestBody requestBody=new WebRequestBody();
        requestBody.setEmail(email);
        requestBody.setRequestName(RequestEndPoints.forgot_password);

        mView.showProgress("Please wait...");
        makeRequest(requestBody, new WeResponseCallback() {
            @Override
            public void onResponse(ApiResponse commonPojo) throws Exception {
                mView.hidePorgress();
                mView.showToast(commonPojo.getMessage());
                activity.finish();
            }

            @Override
            public void failure(Exception e, String message) {
                mView.hidePorgress();

            }
        });
    }
}
