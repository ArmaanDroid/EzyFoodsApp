package services;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.gson.Gson;

import apis.RetrofitClient;
import apis.WebRequestBody;
import base.BasePresenter;
import model.ApiResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sanguinebits.com.ezyfoods.EzyFoodApplication;
import utils.AppConst;
import utils.SharedPrefs;

/**
 * Created by Sahil on 8/31/2016.
 */
public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    public static String refreshedToken;

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        refreshedToken = FirebaseInstanceId.getInstance().getToken();
        EzyFoodApplication.firebaseToken=refreshedToken;
        Log.e("refreshed token",refreshedToken);
        updateFirebaseToken(refreshedToken);
    }

    private void updateFirebaseToken(String refreshedToken) {
        WebRequestBody requestBody=new WebRequestBody();
//        requestBody.setRequestName(RequestEndPoints.updateFirebaseToken);
        requestBody.setDeviceId(EzyFoodApplication.deviceID);
        requestBody.setFirebaseToken(refreshedToken);
        makeRequest(requestBody, new BasePresenter.WeResponseCallback() {
            @Override
            public void onResponse(ApiResponse commonPojo) throws Exception {

            }

            @Override
            public void failure(Exception e, String message) {

            }
        });
    }

    public  void makeRequest(final WebRequestBody payload,
                                   final BasePresenter.WeResponseCallback webResponseCallback) {
        Log.v("requesttttt", new Gson().toJson(payload));
        Call<ApiResponse> call = RetrofitClient.getRestClient().makeRequestAuthentication("token=" + SharedPrefs.getInstance(this).getString(AppConst.ACCESS_TOKEN), payload, "request.php");
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                try {
                    if (response.body().getSuccess())
                        webResponseCallback.onResponse(response.body());
                    else
                        webResponseCallback.failure(null, response.body().getMessage());
                } catch (Exception e) {
//                    webResponseCallback.failure(e,"Something went wrong" );
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                try {
                    webResponseCallback.failure((Exception) t,"Something went wrong");
                    t.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
