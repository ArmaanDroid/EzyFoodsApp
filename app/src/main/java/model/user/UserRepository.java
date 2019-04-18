package model.user;
import utils.SharedPrefs;

public class UserRepository {
    private static User user;
    public static String KEY_USER_ID = "user_id";
    private static String KEY_NAME = "name";
    public static String KEY_EMAIL = "email";
    private static String KEY_DOB = "dob";
    private static String KEY_PROFILE_PIC = "profile_pic";
    private static String KEY_LATITUDE = "latitude";
    private static String KEY_LONGITUDE = "longitude";
    private static String KEY_ADDRESS = "address";
    private static String KEY_PHONE= "phone";
    private static String KEY_ZIP_CODE= "zipCode";
    public static String KEY_USER_TYPE= "userType";

    public static User getUser(SharedPrefs sharedPref) {
        user = new User();
        user.setUserId(sharedPref.getString(KEY_USER_ID));
        user.setName(sharedPref.getString(KEY_NAME));
        user.setEmail(sharedPref.getString(KEY_EMAIL));
        user.setDob(sharedPref.getString(KEY_DOB));
        user.setProfilePic(sharedPref.getString(KEY_PROFILE_PIC));
        user.setLatitude(sharedPref.getString(KEY_LATITUDE));
        user.setLongitude(sharedPref.getString(KEY_LONGITUDE));
        user.setAddress(sharedPref.getString(KEY_ADDRESS));
        user.setPhone(sharedPref.getString(KEY_PHONE));
        user.setUserType(sharedPref.getString(KEY_USER_TYPE));
        user.setZipcode(sharedPref.getString(KEY_ZIP_CODE));
        return user;
    }

    public static void setUser(SharedPrefs sharedPref, User user) {
        sharedPref.setString(KEY_USER_ID, user.getUserId());
        sharedPref.setString(KEY_NAME, user.getName());
        sharedPref.setString(KEY_EMAIL, user.getEmail());
        sharedPref.setString(KEY_DOB, user.getDob());
        sharedPref.setString(KEY_PROFILE_PIC, user.getProfilePic());
        sharedPref.setString(KEY_ADDRESS, user.getAddress());
        sharedPref.setString(KEY_PHONE, user.getPhone());
        sharedPref.setString(KEY_USER_TYPE, user.getUserType());
        sharedPref.setString(KEY_ZIP_CODE, user.getZipcode());
    }


}
