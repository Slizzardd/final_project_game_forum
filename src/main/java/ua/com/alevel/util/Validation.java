package ua.com.alevel.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Validation {

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    private Validation() {
        throw new IllegalStateException("Utility class.");
    }

    public static boolean emailValidate(String email) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(email);
        return matcher.find();
    }
}
