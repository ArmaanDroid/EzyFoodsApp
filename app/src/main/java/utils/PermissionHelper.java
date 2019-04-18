package utils;

import android.Manifest;
import android.app.Activity;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;

public class PermissionHelper {

    public static String[] permissionLocation = new String[]{Manifest.permission.ACCESS_FINE_LOCATION};
    public static  String[] permissionPicture = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};

    public static void checkPermissions(final Activity activity, final String[] permissions, final PermissionCallbacks callback) {
        try {
            Dexter.withActivity(activity)
                    .withPermissions(
                            permissions
                    )
                    .withListener(new MultiplePermissionsListener() {
                        @Override
                        public void onPermissionsChecked(MultiplePermissionsReport report) {
                            if (callback == null) {
                                return;
                            }
                            if (report.areAllPermissionsGranted()) {
                                callback.allOk();
                            } else if (report.isAnyPermissionPermanentlyDenied()) {
                                callback.permissionDenied();
                            } else {
                                callback.permissionNeeded(permissions);
                            }
                        }

                        @Override
                        public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions,
                                                                       PermissionToken token) {
                            token.continuePermissionRequest();
                        }
                    })
                    .check();
        } catch (Exception ex) {
            MyLog.e(ex);
            callback.error();
        }
    }

    public interface PermissionCallbacks {
        void allOk();

        void permissionNeeded(String[] permissions);

        void permissionDenied();

        void error();
    }

}
