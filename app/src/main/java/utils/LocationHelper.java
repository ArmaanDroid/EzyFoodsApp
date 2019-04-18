package utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;

/**
 * Created by vivek on 06/05/18.
 */

public class LocationHelper {
    private static final String TAG = LocationHelper.class.getSimpleName();

    public static void checkLocationPermission(final Activity activity, final LocationHelperPermissionInterface callback){
        try{
            Dexter.withActivity(activity)
                    .withPermissions(
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                    .withListener(new MultiplePermissionsListener() {
                        @Override
                        public void onPermissionsChecked(MultiplePermissionsReport report) {
                            if (report.areAllPermissionsGranted()){
                                if (callback!=null){
                                    callback.allOk();
                                }
                            }else if (report.isAnyPermissionPermanentlyDenied()){
                                callback.permissionDenied();
                            }else {
                                if (callback!=null){
                                    callback.permissionNeeded();
                                }
                            }
                        }

                        @Override
                        public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions,
                                                                       PermissionToken token) {
                            token.continuePermissionRequest();
                        }
                    })
                    .check();
        }catch (Exception ex){
            MyLog.e(ex);
            callback.error();
        }
    }

    public static void getCurrentLocation(final Activity activity, final View rootView,
                                          final LocationHelperLocationUpdateInterface updateInterface){
        updateInterface.fetchingLocation();
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED){
            FusedLocationProviderClient mFusedLocationClient = LocationServices
                    .getFusedLocationProviderClient(activity);
            Task<Location> task = mFusedLocationClient.getLastLocation();
            task.addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    updateInterface.locationUpdateDone();
                    if (location != null){
                        updateUserLocation(activity, location.getLatitude(), location.getLongitude());
                    }
                }
            });

            task.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    updateInterface.locationUpdateDone();

                    Snackbar.make(rootView, "Failed to fetch location",
                            Snackbar.LENGTH_SHORT)
                            .show();
                }
            });
        }
    }


    private static void updateUserLocation(Activity activity, double latitude, double longitude) {
        Log.d(TAG, "updateUserLocation() called with: latitude = [" + latitude + "], longitude = [" + longitude + "]");
        try{
            SharedPrefs.getInstance(activity.getApplicationContext()).setString(AppConst.TAG_USER_LATITUDE,
                    String.valueOf(latitude));
            SharedPrefs.getInstance(activity.getApplicationContext()).setString(AppConst.TAG_USER_LONGITUDE,
                    String.valueOf(longitude));
        }catch (Exception ex){
            MyLog.e(ex);
            SharedPrefs.getInstance(activity.getApplicationContext()).setString(AppConst.TAG_USER_LATITUDE,
                    "");
            SharedPrefs.getInstance(activity.getApplicationContext()).setString(AppConst.TAG_USER_LONGITUDE,
                    "");
        }


    }


    public static boolean isLocationAvailable(Context context){

//        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
//        boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
//        if (!isGPSEnabled){
//            return false;
//        }

        String lattitude = SharedPrefs.getInstance(context).getString(AppConst.TAG_USER_LATITUDE);
        String longitude = SharedPrefs.getInstance(context).getString(AppConst.TAG_USER_LONGITUDE);

        if (lattitude == null || longitude == null || lattitude.isEmpty() || longitude.isEmpty()){
            return false;
        }

        return true;
    }


    public interface LocationHelperPermissionInterface{
        void allOk();
        void permissionNeeded();
        void permissionDenied();
        void error();
    }

    public interface LocationHelperLocationUpdateInterface{
        void fetchingLocation();
        void locationUpdateDone();
        void needPermission();
    }

}
