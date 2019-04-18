package sanguinebits.com.ezyfoods.home.fragments.uploadDish;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.Gson;

import java.io.File;
import java.util.Date;
import java.util.UUID;

import apis.RequestEndPoints;
import apis.WebRequestBody;
import base.BasePresenter;
import model.ApiResponse;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import sanguinebits.com.ezyfoods.R;
import utils.Validations;

public class PostDishPresenter extends BasePresenter {
    private final FirebaseAnalytics mFirebaseAnalytics;
    private PostDishView mView;
    private Activity activity;
    private File mfile;


    public PostDishPresenter(Activity activity, PostDishView mView) {
        super(activity);
        this.activity = activity;
        this.mView = mView;

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(activity);

    }


    public void postDish(String title, String availableOn, String orderBy, String spiceLevel
            , String cuisineType, String pickUp, String delivery, String quantity, String zipCode
            , String description, String price, String weight, Uri pickupedImagePath, Date availableOnDate, Date orderByDate) {
        if (Validations.isFieldEmpty(title)) {
            mView.showToast("Please enter title.");
            return;
        }
        if (Validations.isFieldEmpty(availableOn)) {
            mView.showToast("Please enter Available On.");
            return;
        }
        if (Validations.isFieldEmpty(orderBy)) {
            mView.showToast("Please enter Order By.");
            return;
        }
        if (Validations.isFieldEmpty(spiceLevel)) {
            mView.showToast("Please enter Spice Level.");
            return;
        }
        if (Validations.isFieldEmpty(cuisineType)) {
            mView.showToast("Please enter Cuisine Type.");
            return;
        }
        if (Validations.isFieldEmpty(pickUp)) {
            mView.showToast("Please enter Pick Up.");
            return;
        }
        if (Validations.isFieldEmpty(delivery)) {
            mView.showToast("Please enter weight.");
            return;
        }
        if (Validations.isFieldEmpty(quantity)) {
            mView.showToast("Please enter quantity.");
            return;
        }
        if (Validations.isFieldEmpty(zipCode)) {
            mView.showToast("Please enter Zip Code.");
            return;
        }
        if (Validations.isFieldEmpty(description)) {
            mView.showToast("Please enter description.");
            return;
        }
        if (Validations.isFieldEmpty(price)) {
            mView.showToast("Please enter price.");
            return;
        }
        if (Validations.isFieldEmpty(weight)) {
            mView.showToast("Please enter weight.");
            return;
        }

        if (pickupedImagePath != null) {
            try {
                mfile = new File(pickupedImagePath.getPath());
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            mView.showToast("Please select image of dish.");
            return;
        }

        WebRequestBody requestBody = new WebRequestBody();
        requestBody.setTitle(title);
        requestBody.setAvailableOn(String.valueOf(availableOnDate.getTime()));
        requestBody.setOrderBy(String.valueOf(orderByDate.getTime()));
        requestBody.setSpiceLevel(spiceLevel);
        requestBody.setCuisineType(cuisineType);
        requestBody.setPickUp(pickUp);
        requestBody.setWeight(weight);
        requestBody.setDelivery(delivery);
        requestBody.setQuantity(quantity);
        requestBody.setZipcode(zipCode);
        requestBody.setPrice(price);
        requestBody.setDescription(description);
//        requestBody.setLatitude(SharedPrefs.getInstance(activity).getString(AppConst.TAG_USER_LATITUDE));
//        requestBody.setLongitude(SharedPrefs.getInstance(activity).getString(AppConst.TAG_USER_LONGITUDE));
        requestBody.setLatitude("36.778259");
        requestBody.setLongitude("-119.417931");
        requestBody.setRequestName(RequestEndPoints.add_dish);

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

        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, String.valueOf(UUID.randomUUID()));
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "postDish");
        bundle.putString(FirebaseAnalytics.Param.CONTENT, new Gson().toJson(requestBody));
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);

        makeRequestWithData(file, body, new WeResponseCallback() {
            @Override
            public void onResponse(ApiResponse commonPojo) throws Exception {
                mView.hidePorgress();
                mView.showToast("Your dish posted successfully.");
                mView.onDishPosted(commonPojo);


                Bundle bundle = new Bundle();
                bundle.putString(FirebaseAnalytics.Param.ITEM_ID, String.valueOf(UUID.randomUUID()));
                bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "postDishResult");
                bundle.putString(FirebaseAnalytics.Param.CONTENT, new Gson().toJson(commonPojo));
                mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);

            }

            @Override
            public void failure(Exception e, String message) {
                mView.hidePorgress();
                mView.showToast(message);

                Bundle bundle = new Bundle();
                bundle.putString(FirebaseAnalytics.Param.ITEM_ID, String.valueOf(UUID.randomUUID()));
                bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "postDishResult");
                bundle.putString(FirebaseAnalytics.Param.CONTENT, message);
                mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
            }
        });

    }
}
