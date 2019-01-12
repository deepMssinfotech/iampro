package com.mssinfotech.iampro.co.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validate {

    private static final String EMAIL_PATTERN = "^[a-zA-Z0-9#_~!$&'()*+,;=:.\"(),:;<>@\\[\\]\\\\]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*$";
    private static Pattern pattern = Pattern.compile(EMAIL_PATTERN);
    private static Matcher matcher;

    public static boolean Email(String email) {
        matcher = pattern.matcher(email);
        return matcher.matches();
    }
    public static boolean Password(String password) {
        return password.length() > 5;
    }
    public static boolean isNull(String text) {
        return text.isEmpty();
    }
    /*public static boolean validatePassword(data,tilPassword) {
        //final String data = etPassword.getText().toString();
        // Check if username is entered
        if (data.length() == 0) {
            if (!tilPassword.isErrorEnabled()) {
                tilPassword.setErrorEnabled(true);
            }
            tilPassword.setError("Password Required");
            return false;
        } else {
            if (tilPassword.isErrorEnabled()) {
                tilPassword.setErrorEnabled(false);
            }
            return true;
        }
    }*/
}
