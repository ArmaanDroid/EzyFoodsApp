package utils;

import android.text.TextUtils;
import android.util.Patterns;

/**
 * Created by Mobile on 9/15/2017.
 */

public class Validations {
    public static boolean isFieldEmpty(String field) {
        return TextUtils.isEmpty(field);
    }

    public static boolean isNotValidPassword(String password) {
        if ((password.length() >= 6)) {
            return false;
        } else {
            return true;
        }
    }

    public static boolean isWrongEmail(String email) {
        return (!Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }

    public static boolean passwordDoesNotMatch(String password, String confirmPassword) {
        if (password.equalsIgnoreCase(confirmPassword)) {
            return false;
        } else {
            return true;
        }
    }

    public static boolean isValidMobile(String phone) {
        return android.util.Patterns.PHONE.matcher(phone).matches();
    }
}
