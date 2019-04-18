package base;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import apis.RetrofitClient;
import apis.WebRequestBody;
import model.ApiResponse;
import utils.LocationHelper;
import utils.MiscUtils;

public class BaseActivity extends AppCompatActivity implements BaseView,
        LocationHelper.LocationHelperPermissionInterface {
    private static final String TAG = "baseActivity";
    private static final int REQUEST_CHECK_SETTINGS = 233;
    protected DateFormat FormatDateMonthNamwYear = new SimpleDateFormat("dd/MM/yyyy");
    private ProgressDialog progressBar;
    private View view;
    private boolean isLocationFetching;
    private ProgressDialog progressDialog;
    protected DateFormat dateFormatApi = new SimpleDateFormat("dd/MM/yyyy", Locale.CANADA);

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progressBar = new ProgressDialog(this);
        progressBar.setCancelable(false);
        progressBar.setProgressStyle(android.R.attr.progressBarStyle);
    }

    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        view=parent;
        return super.onCreateView(parent, name, context, attrs);
    }

    @Override
    public void showProgress(String message) {
        progressBar.setMessage(message);
        progressBar.show();
    }

    @Override
    public void hidePorgress() {
        progressBar.dismiss();
    }

    @Override
    public void onStop() {
        super.onStop();
        progressBar.dismiss();
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }




    @Override
    public void allOk() {

        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);

        SettingsClient client = LocationServices.getSettingsClient(this);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());
        task.addOnSuccessListener(new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                Log.d(TAG, "onSuccess() called with: locationSettingsResponse = [" + locationSettingsResponse + "]");

                if (!isLocationFetching) {
                    LocationHelper.getCurrentLocation(BaseActivity.this,
                            view, locationHelperLocationUpdateInterface);
                }

            }
        });
        task.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure() called with: e = [" + e + "]");
                if (e instanceof ResolvableApiException) {
                    try {
                        // Show the dialog by calling startResolutionForResult(),
                        // and check the result in onActivityResult().
                        ResolvableApiException resolvable = (ResolvableApiException) e;
                        resolvable.startResolutionForResult(BaseActivity.this,
                                REQUEST_CHECK_SETTINGS);
                    } catch (IntentSender.SendIntentException sendEx) {
                        // Ignore the error.
                    }
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CHECK_SETTINGS && resultCode == Activity.RESULT_OK) {
            if (!isLocationFetching) {
                LocationHelper.getCurrentLocation(this, view,
                        locationHelperLocationUpdateInterface);
            }
        }
    }

    @Override
    public void permissionNeeded() {
        Snackbar.make(view,
                "Please grant location permission",
                Snackbar.LENGTH_LONG)
                .setAction("Settings", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MiscUtils.openAppSettings(BaseActivity.this);
                    }
                })
                .show();
    }

    @Override
    public void permissionDenied() {
        Snackbar.make(view,
                "Please go to settings and grant location permission",
                Snackbar.LENGTH_LONG)
                .setAction("Settings", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MiscUtils.openAppSettings(BaseActivity.this);
                    }
                })
                .show();
    }

    @Override
    public void error() {
        Snackbar.make(view,
                "Error occurred while fetching location! Please restart again.",
                Snackbar.LENGTH_SHORT)
                .show();
    }


    LocationHelper.LocationHelperLocationUpdateInterface locationHelperLocationUpdateInterface = new LocationHelper.LocationHelperLocationUpdateInterface() {
        @Override
        public void fetchingLocation() {
            try {
                Log.d(TAG, "fetchingLocation() called");
                progressDialog = new ProgressDialog(BaseActivity.this);
                progressDialog.setTitle("Please wait");
                progressDialog.setMessage("Fetching location...");
                progressDialog.setCancelable(false);
                progressDialog.show();
                isLocationFetching = true;

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void locationUpdateDone() {
            Log.d(TAG, "locationUpdateDone() called");
            progressDialog.dismiss();

            isLocationFetching = false;
        }

        @Override
        public void needPermission() {
            Log.d(TAG, "needPermission() called");
            isLocationFetching = false;

            Snackbar.make(view,
                    "Please go to settings and grant location permission",
                    Snackbar.LENGTH_LONG)
                    .setAction("Settings", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            MiscUtils.openAppSettings(BaseActivity.this);
                        }
                    })
                    .show();
        }
    };

    public interface WeResponseCallback {
        void onResponse(ApiResponse commonPojo) throws Exception;
        void failure(Exception e, String message);
    }
}
