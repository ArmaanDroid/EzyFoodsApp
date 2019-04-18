package sanguinebits.com.ezyfoods.home.fragments.loginSignup;

import android.app.Activity;
import android.net.Uri;

import com.google.gson.Gson;

import java.io.File;

import apis.RequestEndPoints;
import apis.WebRequestBody;
import base.BasePresenter;
import model.ApiResponse;
import model.user.UserRepository;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import sanguinebits.com.ezyfoods.EzyFoodApplication;
import sanguinebits.com.ezyfoods.R;
import utils.AppConst;
import utils.AppUserType;
import utils.SharedPrefs;
import utils.Validations;

class LoginSignPresenter extends BasePresenter {
    private LoginSignupView mView;
    private Activity activity;
    private File mfile;

    LoginSignPresenter(Activity activity, LoginSignupView loginSignupView) {
        super(activity);
        this.activity = activity;
        this.mView = loginSignupView;
    }

    void login(String email, String password) {
        if (Validations.isWrongEmail(email)) {
            mView.showToast("Please enter correct email.");
            return;
        }
        if (Validations.isFieldEmpty(password)) {
            mView.showToast("Please enter password.");
            return;
        }

        WebRequestBody requestBody = new WebRequestBody();
        requestBody.setEmail(email);
        requestBody.setPassword(password);
        requestBody.setDeviceId(EzyFoodApplication.deviceID);
        requestBody.setFirebaseToken(EzyFoodApplication.firebaseToken);

        requestBody.setRequestName(RequestEndPoints.login);

        mView.showProgress(activity.getString(R.string.please_wait));
        makeRequest(requestBody, new WeResponseCallback() {
            @Override
            public void onResponse(ApiResponse commonPojo) throws Exception {
                mView.hidePorgress();

                if (commonPojo.getUser() != null) {
                    SharedPrefs.getInstance(activity).setString(AppConst.ACCESS_TOKEN, commonPojo.getUser().getAccessToken());
                    SharedPrefs.getInstance(activity).setString(AppConst.KEY_STRIPE_URL, commonPojo.getStripeUrl());
                    //check if stripe account is available
                    if (commonPojo.getUser().getUserType().equalsIgnoreCase(AppUserType.chef))
                        if (commonPojo.getUser().getStripeAccountId() == null || commonPojo.getUser().getStripeAccountId().isEmpty()) {
                            mView.openStripeActivity(commonPojo.getStripeUrl());
                            return;
                        }
                    UserRepository.setUser(SharedPrefs.getInstance(activity), commonPojo.getUser());
                    mView.onLoginSuccses();
                } else
                    mView.showToast(commonPojo.getMessage());
            }

            @Override
            public void failure(Exception e, String message) {
                mView.hidePorgress();
                mView.showToast(message);
            }
        });
    }

    void signup(String name, String zipCode, String email, String password, String phone, String address, Uri pickupedImagePath, String referedBY, final String userType) {
        if (Validations.isFieldEmpty(name)) {
            mView.showToast("Please enter your Full Name.");
            return;
        }
        if (Validations.isFieldEmpty(zipCode)) {
            mView.showToast("Please enter ZipCode.");
            return;
        }
        if (Validations.isWrongEmail(email)) {
            mView.showToast("Please enter correct email.");
            return;
        }
        if (Validations.isFieldEmpty(password)) {
            mView.showToast("Please enter password.");
            return;
        }
        if (Validations.isFieldEmpty(phone)) {
            mView.showToast("Please enter phone number.");
            return;
        }
        if (!Validations.isValidMobile(phone)) {
            mView.showToast("Please enter correct phone number.");
            return;
        }
        if (Validations.isFieldEmpty(address)) {
            mView.showToast("Please enter address.");
            return;
        }
        if (Validations.isFieldEmpty(userType)) {
            mView.showToast("Please select user type.");
            return;
        }

        if (pickupedImagePath != null) {
            try {
                mfile = new File(pickupedImagePath.getPath());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        WebRequestBody requestBody = new WebRequestBody();
        requestBody.setName(name);
        requestBody.setZipcode(zipCode);
        requestBody.setEmail(email);
        requestBody.setPassword(password);
        requestBody.setPhone(phone);
        requestBody.setAddress(address);
        requestBody.setDeviceId(EzyFoodApplication.deviceID);
        requestBody.setFirebaseToken(EzyFoodApplication.firebaseToken);
        requestBody.setRefered_by(referedBY);
        requestBody.setLatitude(AppConst.TAG_USER_LATITUDE);
        requestBody.setLongitude(AppConst.TAG_USER_LONGITUDE);
        requestBody.setUserType(userType);
        requestBody.setRequestName(RequestEndPoints.signup);

        RequestBody body =
                RequestBody.create(
                        okhttp3.MultipartBody.FORM, new Gson().toJson(requestBody));

        MultipartBody.Part file = null;

        if (mfile != null) {
            try {
                File imageFile = new File(mfile.getPath());

                RequestBody requestFile =
                        RequestBody.create(MediaType.parse("multipart/form-data"), imageFile);

                file = MultipartBody.Part.createFormData("image", imageFile.getName(), requestFile);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        mView.showProgress(activity.getString(R.string.please_wait));

        makeRequestWithData(file, body, new WeResponseCallback() {
            @Override
            public void onResponse(ApiResponse commonPojo) throws Exception {
                mView.hidePorgress();

                if (commonPojo.getUser() != null) {
                    SharedPrefs.getInstance(activity).setString(AppConst.ACCESS_TOKEN, commonPojo.getUser().getAccessToken());
                    SharedPrefs.getInstance(activity).setString(AppConst.KEY_STRIPE_URL, commonPojo.getStripeUrl());

                    if (userType.equalsIgnoreCase(AppUserType.chef))
                        //check if stripe account is available
                        if (commonPojo.getUser().getStripeAccountId() == null || commonPojo.getUser().getStripeAccountId().isEmpty()) {
                            mView.openStripeActivity(commonPojo.getStripeUrl());
                            return;
                        }
                    UserRepository.setUser(SharedPrefs.getInstance(activity), commonPojo.getUser());
                    mView.onLoginSuccses();
                }
            }

            @Override
            public void failure(Exception e, String message) {
                mView.hidePorgress();
                mView.showToast(message);
            }
        });
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
                UserRepository.setUser(SharedPrefs.getInstance(activity), commonPojo.getUser());
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
