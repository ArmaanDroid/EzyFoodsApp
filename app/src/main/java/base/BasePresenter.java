package base;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;


import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.Objects;

import apis.RetrofitClient;
import apis.WebRequestBody;
import model.ApiResponse;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sanguinebits.com.ezyfoods.R;
import utils.AppConst;
import utils.SharedPrefs;
//import services.OnlineStatusJob;

public class BasePresenter {
    public Activity activity;

    public BasePresenter(Activity activity) {
        this.activity = activity;
    }

    protected   void makeRequest(final WebRequestBody payload,
                            final WeResponseCallback webResponseCallback) {
        Log.v("requesttttt", new Gson().toJson(payload));
        Call<ApiResponse> call = RetrofitClient.getRestClient().makeRequestAuthentication("token=" + SharedPrefs.getInstance(activity).getString(AppConst.ACCESS_TOKEN), payload, "requests.php");
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                try {
                    if (response.body() == null) {
                        webResponseCallback.failure(null, "Something went wrong");
                        return;
                    }
                    if (response.isSuccessful() && response.body().getSuccess())
                        webResponseCallback.onResponse(response.body());
                    else if (response.body() != null)
                        webResponseCallback.failure(null, response.body().getMessage());
                    else
                        webResponseCallback.failure(null, "Something went wrong");

                    if (response.body().getMessage().equalsIgnoreCase("unauthorised")) {
                        logoutUser();
                    }
                } catch (Exception e) {
                    webResponseCallback.failure(e, "");
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                try {
                    webResponseCallback.failure((Exception) t, "Something went wrong");
                    t.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    protected void makeRequestWithData(MultipartBody.Part file, RequestBody requestBody, final WeResponseCallback webResponseCallback) {
        Call<ApiResponse> call = RetrofitClient.getRestClient().requestWithData("token="
                        + SharedPrefs.getInstance(activity).getString(AppConst.ACCESS_TOKEN)
                , "requests.php", file, requestBody);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                try {
                    if (response.body().getSuccess())
                        webResponseCallback.onResponse(response.body());
                    else
                        webResponseCallback.failure(null, response.body().getMessage());
                    if (response.body().getMessage().equalsIgnoreCase("unauthorised")) {
                        logoutUser();
                    }
                } catch (Exception e) {
                    webResponseCallback.failure(e, activity.getString(R.string.error_making_request));
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                try {
                    webResponseCallback.failure((Exception) t, activity.getString(R.string.error_making_request));
                    t.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    protected void makeRequestWithMultipleData(MultipartBody.Part[] file, RequestBody requestBody, final WeResponseCallback webResponseCallback) {
        Call<ApiResponse> call = RetrofitClient.getRestClient().requestWithMultipleData("token="
                        + SharedPrefs.getInstance(activity).getString(AppConst.ACCESS_TOKEN)
                , "requests.php", file, requestBody);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                try {
                    if (response.body().getSuccess())
                        webResponseCallback.onResponse(response.body());
                    else
                        webResponseCallback.failure(null, response.body().getMessage());

                    if (response.body().getMessage().equalsIgnoreCase("unauthorised")) {
                        logoutUser();
                    }
                } catch (Exception e) {
                    webResponseCallback.failure(e, activity.getString(R.string.error_making_request));
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                try {
                    webResponseCallback.failure((Exception) t, activity.getString(R.string.error_making_request));
                    t.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void logoutUser() {
        Toast.makeText(activity, "Unauthorised please login again.", Toast.LENGTH_LONG).show();
        SharedPrefs.getInstance(activity).clear();

        try {
            FirebaseInstanceId.getInstance().deleteInstanceId();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Objects.requireNonNull(activity).recreate();
        
//        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(activity));
//        Job myJob = dispatcher.newJobBuilder()
//                .setService(OnlineStatusJob.class)
//                .setTrigger(Trigger.executionWindow(0, 60))// the JobService that will be called
//                .setTag("my-unique-tag")        // uniquely identifies the job
//                .build();
//
//        dispatcher.mustSchedule(myJob);

//        Intent intent = new Intent(activity, OnlineStatusUpdateService.class);
//        activity.startService(intent);

//        activity.startActivity(new Intent(activity, WelcomeActivity.class));
//        activity.finish();
    }

    public interface WeResponseCallback {
        void onResponse(ApiResponse commonPojo) throws Exception;
        void failure(Exception e, String message);
    }

}
