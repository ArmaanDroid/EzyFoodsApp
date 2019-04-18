package utils;

import android.os.Environment;

public class AppConst {
    public final static String APP_NAME = "Ezy Foods";

    public static final String PACKAGE = "sanguinebits.com.ezyfoods";

    public static final String KEY_STRIPE_ID=PACKAGE+"key_stripe_id";
    public static final String ACCESS_TOKEN =PACKAGE+ "access_token";
    public static final String IS_LOGINED = PACKAGE+"is_logined";
    public static final String USER_ID = PACKAGE + "userID";
    public final static int GALLERY_INTENT = 2;

    public static final String JPEG_FILE_PREFIX = "IMG_";
    public static final String JPEG_FILE_SUFFIX = ".jpg";
    public static final String LOCAL_FILE_PREFIX = "file://";

    public static final String LOCAL_STORAGE_BASE_PATH_FOR_MEDIA = Environment
            .getExternalStorageDirectory() + "/" + APP_NAME;

    public static final String LOCAL_STORAGE_BASE_PATH_FOR_USER_PHOTOS =
            LOCAL_STORAGE_BASE_PATH_FOR_MEDIA + "/Photos/";

    public static final String BASE_URL = "http://34.198.249.175/MidoFood/";
    public static final String DISH_IMAGE_BASE_URL = BASE_URL+"/dishPic/";
    public static final String USER_IMAGE_BASE_URL = BASE_URL+"/userPics/";

    public static final String TAG_USER_LATITUDE = PACKAGE + "latitude";
    public static final String TAG_USER_LONGITUDE = PACKAGE + "longitude";
    public static final int REQUEST_PERMISSION_SETTING = 198;
    public static final String KEY_STRIPE_URL = PACKAGE+"stripe_url";
}
