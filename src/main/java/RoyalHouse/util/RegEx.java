package RoyalHouse.util;

import java.util.regex.Pattern;

public class RegEx {
    public static final Pattern USERNAME_REGEX = Pattern.compile("^[a-zA-Z0-9_]{5,32}$");

    public static final Pattern EMAIL_REGEX = Pattern.compile("^(?=.{1,64}@)[a-zA-Z0-9]+(?:\\.[a-zA-Z0-9]+)*@[a-zA-Z0-9]+(?:\\.[a-zA-Z0-9.]+)*\\.[a-zA-Z]{2,}$");

    public static final Pattern PHONE_REGEX = Pattern.compile("^(380-(\\d{2})-(\\d{3})-(\\d{2})-(\\d{2}))$");

    public static final Pattern PASSWORD_REGEX = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$");

    public static final Pattern TELEGRAM_REGEX = Pattern.compile("^@[a-zA-Z0-9_]{5,32}$");

    public static final Pattern INSTAGRAM_REGEX = Pattern.compile("^https://www\\.instagram\\.com/[a-zA-Z0-9_.]{1,30}/?$");

    public static final Pattern FACEBOOK_REGEX = Pattern.compile("^https://www\\.facebook\\.com/[a-zA-Z0-9.]{5,50}/?$");
}
